package com.javadude.maps;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
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

public class UFOService extends Service {
    UFOPositionAIDL.Stub binder = new UFOPositionAIDL.Stub() {
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

    private String server;

    public UFOService() {
        //server = "http://10.0.2.2:8080/restserver/todo";
        server = "http://javadude.com/aliens/";
    }

    private volatile int i = 1;
    private class CounterThread extends Thread {
        @Override public void run() {
            for(i = 1; !isInterrupted() && i <=100; i++) {
                Log.d("StartedService", "count = " + i);
                for(UFOPositionReporter reporter : reporters) {
                    try {
                        reporter.report(i);
                    } catch (RemoteException e) {
                        Log.e(getClass().getSimpleName(), "Could not send report", e);
                    }
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    interrupt();
                }
            }
            stopSelf();
        }
    }

    private CounterThread counterThread;

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

    public Cursor getUFOPosition(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        if (selection != null) {
            throw new IllegalArgumentException("Selection arguments not supported by this content provider");
        }

        if (getContext() == null) {
            throw new RuntimeException("No context available!");
        }

        String id = uri.getLastPathSegment();
        String uriString = server + "/" + id;
        try {
            Result result = httpRequest(Method.GET, uriString, null);
            if (result.getStatusCode() < 300) {
                JSONObject jsonObject = new JSONObject(result.getContent());
//                MatrixCursor matrixCursor = new MatrixCursor(PROJECTION_ALL);
//                matrixCursor.newRow()
//                        .add(jsonObject.optLong("id"))
//                        .add(jsonObject.optString("name"))
//                        .add(jsonObject.optString("description"))
//                        .add(jsonObject.optInt("priority"));
//                matrixCursor.setNotificationUri(getContext().getContentResolver(), uri);
//                return matrixCursor;
            } else {
                throw new RuntimeException("Could not access data: " + result.getStatusCode() + ": " + result.getStatusMessage());
            }
        } catch (Throwable e) {
            throw new RuntimeException("Could not access data", e);
        }
    }

    public Cursor getAllUFOPositions(Uri uri, String[] projection, String selection,
                             String[] selectionArgs, String sortOrder) {
        if (selection != null) {
            throw new IllegalArgumentException("Selection arguments not supported by this content provider");
        }

        if (getContext() == null) {
            throw new RuntimeException("No context available!");
        }

        String uriString = server;
        try {
            Result result = httpRequest(Method.GET, uriString, null);
            if (result.getStatusCode() < 300) {
                JSONArray jsonArray = new JSONArray(result.getContent());
                //MatrixCursor matrixCursor = new MatrixCursor(PROJECTION_ALL);
                for(int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.optJSONObject(i);
//                    matrixCursor.newRow()
//                            .add(jsonObject.optLong("id"))
//                            .add(jsonObject.optString("name"))
//                            .add(jsonObject.optString("description"))
//                            .add(jsonObject.optInt("priority"));
                }
                //matrixCursor.setNotificationUri(getContext().getContentResolver(), uri);
                //return matrixCursor;
            } else {
                throw new RuntimeException("Could not access data: " + result.getStatusCode() + ": " + result.getStatusMessage());
            }
        } catch (Throwable e) {
            throw new RuntimeException("Could not access data", e);
        }
    }

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadExecutor();
    private Result httpRequest(final Method method, final String uriString, final UFOPosition ufoPosition) {
        Callable<Result> callable = new Callable<Result>() {
            @Override
            public Result call() throws Exception {
                try {
                    URL url = new URL(uriString);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    connection.setRequestMethod(method.name());
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
