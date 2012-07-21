package org.renpy.android;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;


public class PythonApplication extends Application {

    private ResourceManager resourceManager;

    // The path to the directory contaning our external storage.
    private File externalStorage;

    // The path to the directory containing the game.
    // private File mPath = null;

    @Override
    public void onCreate() {
        super.onCreate();
        resourceManager = new ResourceManager(this);
        //externalStorage = new File(Environment.getExternalStorageDirectory(), getPackageName());
        externalStorage = new File(getFilesDir(), "main");

        unpackData("private", getFilesDir());
        unpackData("public", externalStorage);

        System.loadLibrary("sdl");
        System.loadLibrary("sdl_image");
        System.loadLibrary("sdl_ttf");
        System.loadLibrary("sdl_mixer");
        System.loadLibrary("python2.7");
        System.loadLibrary("application");
        System.loadLibrary("sdl_main");

        System.load(getFilesDir() + "/lib/python2.7/lib-dynload/_io.so");
        System.load(getFilesDir() + "/lib/python2.7/lib-dynload/unicodedata.so");
        System.load(getFilesDir() + "/lib/python2.7/lib-dynload/_csv.so");

        try {
            System.loadLibrary("sqlite3");
            System.load(getFilesDir() + "/lib/python2.7/lib-dynload/_sqlite3.so");
        } catch(UnsatisfiedLinkError e) {
        }

        try {
            System.load(getFilesDir() + "/lib/python2.7/lib-dynload/_imaging.so");
            System.load(getFilesDir() + "/lib/python2.7/lib-dynload/_imagingft.so");
            System.load(getFilesDir() + "/lib/python2.7/lib-dynload/_imagingmath.so");
        } catch(UnsatisfiedLinkError e) {
        }
    }


    /**
     * Show an error using a toast. (Only makes sense from non-UI
     * threads.)
     */
    public void toastError(final String msg) {

        final Application thisActivity = this;

        /*
        runOnUiThread(new Runnable () {
                public void run() {
                    Toast.makeText(thisActivity, msg, Toast.LENGTH_LONG).show();
                }
            });
        */
        Toast.makeText(thisActivity, msg, Toast.LENGTH_LONG).show();

        // Wait to show the error.
        synchronized (this) {
            try {
                this.wait(1000);
            } catch (InterruptedException e) {
            }
        }
    }

    public void recursiveDelete(File f) {
        if (f.isDirectory()) {
            for (File r : f.listFiles()) {
                recursiveDelete(r);
            }
        }
        f.delete();
    }


    /**
     * This determines if unpacking one the zip files included in
     * the .apk is necessary. If it is, the zip file is unpacked.
     */
    public void unpackData(final String resource, File target) {

        // The version of data in memory and on disk.
        String data_version = resourceManager.getString(resource + "_version");
        String disk_version = null;

        // If no version, no unpacking is necessary.
        if (data_version == null) {
            return;
        }

        // Check the current disk version, if any.
        String filesDir = target.getAbsolutePath();
        String disk_version_fn = filesDir + "/" + resource + ".version";

        try {
            byte buf[] = new byte[64];
            InputStream is = new FileInputStream(disk_version_fn);
            int len = is.read(buf);
            disk_version = new String(buf, 0, len);
            is.close();
        } catch (Exception e) {
            disk_version = "";
        }

        // If the disk data is out of date, extract it and write the
        // version file.
        if (! data_version.equals(disk_version)) {
            Log.v("python", "Extracting " + resource + " assets.");

            recursiveDelete(target);
            target.mkdirs();

            AssetExtract ae = new AssetExtract(this);
            if (!ae.extractTar(resource + ".mp3", target.getAbsolutePath())) {
                toastError("Could not extract " + resource + " data.");
            }

            try {
                // Write .nomedia.
                new File(target, ".nomedia").createNewFile();

                // Write version file.
                FileOutputStream os = new FileOutputStream(disk_version_fn);
                os.write(data_version.getBytes());
                os.close();
            } catch (Exception e) {
                Log.w("python", e);
            }
        }

    }
}

