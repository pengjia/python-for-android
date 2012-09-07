package org.renpy.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ActivityNotFoundException;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.view.MotionEvent;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import android.util.DisplayMetrics;
import android.os.Debug;

import java.io.InputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.File;
import java.io.IOException;

import java.util.zip.GZIPInputStream;

public class PythonActivity extends Activity implements Runnable {

    // The audio thread for streaming audio...
    private static AudioThread mAudioThread = null;

    public static PythonActivity mActivity = null;

    // Did we launch our thread?
    private boolean mLaunchedThread = false;

    private ResourceManager resourceManager;

    // The path to the directory contaning our external storage.
    private File externalStorage;

    // The path to the directory containing the game.
    private File mPath = null;

    boolean _isPaused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Hardware.context = this;
        Action.context = this;
        this.mActivity = this;

        getWindowManager().getDefaultDisplay().getMetrics(Hardware.metrics);

        resourceManager = new ResourceManager(this);
        externalStorage = new File(Environment.getExternalStorageDirectory(), getPackageName());

        if (resourceManager.getString("public_version") != null) {
            mPath = externalStorage;
        } else {
            mPath = getFilesDir();
        }

//        // go to fullscreen mode
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                             WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
//        // Start showing an SDLSurfaceView.
//        mView = new SDLSurfaceView(
//            this,
//            mPath.getAbsolutePath());
//
//        Hardware.view = mView;
//        setContentView(mView);

        LinearLayout mainLyn = new LinearLayout(this);
        Button startBtn = new Button(this);
        startBtn.setText("START SERVICE");
        mainLyn.addView(startBtn);
//        Button stopBtn = new Button(this);
//        stopBtn.setText("STOP SERVICE");
//        mainLyn.addView(stopBtn);
        setContentView(mainLyn);

        startBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                PythonActivity.this.startService(new Intent(
                        PythonActivity.this, PythonService.class));
            }
        });
//        stopBtn.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                PythonActivity.this.stopService(new Intent(
//                        PythonActivity.this, SDLSurfaceView.class));
//            }
//        });
    }

    public void run() {

//        if ( mAudioThread == null ) {
//            Log.i("python", "starting audio thread");
//            mAudioThread = new AudioThread(this);
//        }
//
//        runOnUiThread(new Runnable () {
//                public void run() {
//                    mView.start();
//                }
//            });
    }

    @Override
    protected void onPause() {
        super.onPause();
        _isPaused = true;

//        if (mView != null) {
//            mView.onPause();
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        _isPaused = false;

        if (!mLaunchedThread) {
            mLaunchedThread = true;
            new Thread(this).start();
        }

//        if (mView != null) {
//            mView.onResume();
//        }
    }

    public boolean isPaused() {
        return _isPaused;
    }

//    @Override
//    public boolean onKeyDown(int keyCode, final KeyEvent event) {
//        //Log.i("python", "key2 " + mView + " " + mView.mStarted);
//        if (mView != null && mView.mStarted && SDLSurfaceView.nativeKey(keyCode, 1, event.getUnicodeChar())) {
//            return true;
//        } else {
//            return super.onKeyDown(keyCode, event);
//        }
//    }
//
//    @Override
//    public boolean onKeyUp(int keyCode, final KeyEvent event) {
//        //Log.i("python", "key up " + mView + " " + mView.mStarted);
//        if (mView != null && mView.mStarted && SDLSurfaceView.nativeKey(keyCode, 0, event.getUnicodeChar())) {
//            return true;
//        } else {
//            return super.onKeyUp(keyCode, event);
//        }
//    }
//
//    @Override
//    public boolean dispatchTouchEvent(final MotionEvent ev) {
//
//        if (mView != null){
//            mView.onTouchEvent(ev);
//            return true;
//        } else {
//            return super.dispatchTouchEvent(ev);
//        }
//    }

    protected void onDestroy() {
//        if (mView != null) {
//            mView.onDestroy();
//        }
        //Log.i(TAG, "on destroy (exit1)");
        System.exit(0);
    }
}

