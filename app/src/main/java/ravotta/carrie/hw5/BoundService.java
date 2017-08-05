package ravotta.carrie.hw5;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

public class BoundService extends Service {
	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("StartedService", "onCreate");
	}

	private volatile int i = 1;
	private class CounterThread extends Thread {
		@Override public void run() {
			for(i = 1; !isInterrupted() && i <=100; i++) {
				Log.d("StartedService", "count = " + i);
				Intent intent = new Intent("ravotta.carrie.hw5.count");
				intent.putExtra("count", i);
				sendBroadcast(intent);
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

	@Override
	public void onDestroy() {
		super.onDestroy();
		//counterThread.interrupt();
		Log.d("StartedService", "onDestroy");
	}

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
//		if (counterThread == null) {
//			counterThread = new CounterThread();
//			counterThread.start();
//		}
	}

	@Override
	public boolean onUnbind(Intent intent) {
		Log.d("StartedService", "onUnbind");
		return super.onUnbind(intent);
	}
}
