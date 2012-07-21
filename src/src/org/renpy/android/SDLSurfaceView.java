package org.renpy.android;

import java.io.File;

import android.content.Context;
import android.os.Environment;


public class SDLSurfaceView implements Runnable {

    private Context context;

    // Did we launch our thread?
    private boolean mLaunchedThread = false;

    // The name of the directory where the context stores its files.
    String mFilesDirectory = null;

    // The name of the directory where the context stores its scripts.
    String mScriptsDirectory = null;

    // The value of the argument passed in.
    String mArgument = null;

    // The resource manager we use.
    ResourceManager mResourceManager;


    public SDLSurfaceView(Context context) {
        mResourceManager = new ResourceManager(context);
        mFilesDirectory = context.getFilesDir().getAbsolutePath();
        mArgument = new File(context.getFilesDir(), "main").getAbsolutePath();
        //mArgument = new File(Environment.getExternalStorageDirectory(), getPackageName()).getAbsolutePath();
    }

    public void run() {
        if (!mLaunchedThread) {
            mLaunchedThread = true;
            nativeInitJavaCallbacks();
            nativeSetEnv("ANDROID_PRIVATE", mFilesDirectory);
            nativeSetEnv("ANDROID_ARGUMENT", mArgument);
            nativeSetEnv("PYTHONOPTIMIZE", "2");
            nativeSetEnv("PYTHONHOME", mFilesDirectory);
            nativeSetEnv("PYTHONPATH", mArgument + ":" + mFilesDirectory + "/lib");

            nativeInit();
        }

        //Log.i(TAG, "End of native init, stop everything (exit0)");
        //System.exit(0);
    }

    public int checkPause() {
        return 0;
    }

    public int swapBuffers() {
        return 0;
    }

    public void waitForResume() {
    }

    // Native part

    public static native void nativeSetEnv(String name, String value);
    public static native void nativeInit();

    public static native void nativeMouse( int x, int y, int action, int pointerId, int pressure, int radius );
    public static native boolean nativeKey(int keyCode, int down, int unicode);
    public static native void nativeSetMouseUsed();
    public static native void nativeSetMultitouchUsed();

    public native void nativeResize(int width, int height);
    public native void nativeInitJavaCallbacks();


}
