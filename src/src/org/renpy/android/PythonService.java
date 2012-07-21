package org.renpy.android;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;


public class PythonService extends Service {

    private IBinder mBinder = new Binder() {
    };

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SDLSurfaceView core = new SDLSurfaceView(this);
        new Thread(core).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

}
