package fr.imie.sensair.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by mirouf on 15/05/17.
 */

public class AirQualityExceptionService extends Service {
    protected boolean isRunning = false;
    protected Thread thread;
    protected final IBinder binder = new AirQualityExceptionBinder();

    public class AirQualityExceptionBinder extends Binder {
        public AirQualityExceptionService getService() {
            return AirQualityExceptionService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Toast.makeText(this, "hello service", Toast.LENGTH_LONG).show();

        this.thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (AirQualityExceptionService.this.isRunning) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {

                    }
                }
            }
        });

        thread.setName("Main thread of Service");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int result = super.onStartCommand(intent, flags, startId);
        String bar = intent.getStringExtra("foo");

        if (!this.thread.isAlive()) {
            this.isRunning = true;
            this.thread.start();
        }

        return result;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        this.onStartCommand(intent, 0, 0);

        return this.binder;
    }

    public String test() {
        return "My test funciton !";
    }
}
