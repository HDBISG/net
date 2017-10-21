package org.soundofhope.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

/**
 * 可以监测网络变化；但检测不到VPN变化
 */
public class NetWorkStateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NetManager netManager = NetManager.getInstance( context, "www.google.com", 80 );
        netManager.checkStatusThread();
    }
}
