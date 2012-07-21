package org.renpy.android;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class PythonBootReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent startServiceIntent = new Intent(context, PythonService.class);
        context.startService(startServiceIntent);
    }

}
