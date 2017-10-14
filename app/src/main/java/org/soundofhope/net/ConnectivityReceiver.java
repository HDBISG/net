package org.soundofhope.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * https://stackoverflow.com/questions/3461967/get-vpn-connection-status-on-android
 * not working.
 */
public class ConnectivityReceiver extends BroadcastReceiver
{
    public void onReceive(Context c, Intent intent)
    {
        String state = intent.getSerializableExtra("connection_state").toString();

        Log.d("连接状态： ", state.toString());

        if (state.equals("CONNECTING")) {
            // Do what needs to be done
        }
        else if (state.equals("CONNECTED")) {
            // Do what needs to be done
        }
        else if (state.equals("IDLE")) {
            int errorCode = intent.getIntExtra("err", 0);
            if (errorCode != 0) {
                // Do what needs to be done to report a failure
            }
            else {
                // Normal disconnect
            }
        }
        else if (state.equals("DISCONNECTING")) {
            // Usually not very interesting
        }
    }
}