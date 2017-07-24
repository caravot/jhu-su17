package com.javadude.maps;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static java.security.AccessController.getContext;

public class UFOServiceImpl extends Service {
    UFOService.Stub binder = new UFOService.Stub() {
        public void reset() {
            i = 1;
        }
        public void add(UFOPositionReporter reporter) {
            reporters.add(reporter);
        }
        public void remove(UFOPositionReporter reporter) {
            reporters.remove(reporter);
        }
    };

    private List<UFOPositionReporter> reporters = new ArrayList<>();
    private ArrayList<UFOPosition> ufoPositions = new ArrayList<>();

    // the alien REST web server
    private String server = "http://javadude.com/aliens";

    // used to count our alien requests
    private volatile int i = 1;

    // number of seconds between thread counts
    private static int intervalSeconds = 1;

    // number of times to call for alien positions
    private static int alienCounter = 2;

    private CounterThread counterThread;

    private class CounterThread extends Thread {
        @Override public void run() {
            for(i = 1; !isInterrupted() && i <= alienCounter; i++) {
                Log.d("UFOServiceCounter", "count = " + i);

                try {
                    UFOPosition ufoPosition = getUFOPosition(i);
                    ufoPositions.add(ufoPosition);
                } catch (Exception e) {
                    Log.e(getClass().getSimpleName(), "Could not get UFO position");
                    Log.e(getClass().getSimpleName(), e.getMessage());
                    interrupt();
                    stopSelf();
                }

                for (UFOPositionReporter reporter : reporters) {
                    try {
                        reporter.report(ufoPositions);
                    } catch (RemoteException e) {
                        Log.e(getClass().getSimpleName(), "Could not send report", e);
                    }
                }

                try {
                    Thread.sleep(intervalSeconds * 1000);
                } catch (InterruptedException e) {
                    interrupt();
                }
            }
            stopSelf();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("StartedService", "onCreate");
    }

    @Nullable
    @Override
    public synchronized IBinder onBind(Intent intent) {
        if (counterThread == null) {
            counterThread = new CounterThread();
            counterThread.start();
        }
        return binder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        counterThread.interrupt();
        Log.d("StartedService", "onDestroy");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d("StartedService", "onUnbind");
        return super.onUnbind(intent);
    }

    // get an alien position from the REST web server
    public UFOPosition getUFOPosition(int i) {
        if (getContext() == null) {
            throw new RuntimeException("No context available!");
        }

        // build the GET request
        String uriString = server + "/" + i + ".json";

        try {
            Result result = httpRequest(uriString, null);

            // result is ok
            if (result.getStatusCode() < 300) {
                JSONArray jsonArray = new JSONArray(result.getContent());


                for (int j = 0 ; j < jsonArray.length(); j++) {
                    JSONObject obj = jsonArray.getJSONObject(j);

                    int shipNumber = obj.getInt("ship");
                    double lat = obj.getDouble("lat");
                    double lon = obj.getDouble("lon");

                    UFOPosition ufoPosition = new UFOPosition(shipNumber, lat, lon);
//                    System.out.println(ufoPosition.toString());
                    return ufoPosition;
                }

                // TODO use HttpStatus.SC_NOT_FOUND instead
            } else if (result.getStatusCode() == 404) {
                // TODO add throw for http status of 404
            }
            else {
                throw new RuntimeException("Could not access data: " + result.getStatusCode() + ": " + result.getStatusMessage());
            }
        } catch (Throwable e) {
            throw new RuntimeException("Could not access data", e);
        }

        return null;
    }

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadExecutor();
    private Result httpRequest(final String uriString, final UFOPosition ufoPosition) {
        Callable<Result> callable = new Callable<Result>() {
            @Override
            public Result call() throws Exception {
                try {
                    URL url = new URL(uriString);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    connection.setRequestMethod("GET");
                    connection.connect();

                    if (ufoPosition != null) {
                        OutputStream out = connection.getOutputStream();
                        BufferedOutputStream bos = new BufferedOutputStream(out);
                        bos.write(ufoPosition.toJsonString().getBytes());
                        bos.flush();
                    }

                    int responseCode = connection.getResponseCode();
                    String content = "";

                    if (responseCode < 300) {
                        InputStream in = connection.getInputStream();
                        InputStreamReader isr = new InputStreamReader(in);
                        BufferedReader br = new BufferedReader(isr);
                        String line;
                        while((line = br.readLine()) != null) {
                            content += line + "\n";
                        }
                    }
                    return new Result(responseCode, connection.getResponseMessage(), content);

                } catch (IOException | JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        Future<Result> future = EXECUTOR_SERVICE.submit(callable);

        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
