package org.soundofhope.net;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.Collections;

import static org.apache.http.params.CoreConnectionPNames.CONNECTION_TIMEOUT;

/**
 * Created by ok on 14/10/17.
 */

public class NetManager {

    private static NetManager netManager = null;

    boolean connectionAvailable;
    boolean vpnAvailable;
    boolean serverReachable;

    Context context;

    String ipOrHostname = "";
    int port = 80;

    public static NetManager getInstance(Context context, String ipOrHostname, int port ) {
        if( netManager == null ) {
            netManager = new NetManager(  context,   ipOrHostname,   port );
        }
        return netManager;
    }

    private NetManager(Context context, String ipOrHostname, int port ) {

        this.context = context;
        this.ipOrHostname = ipOrHostname;
        this.port = port;

        checkStatusThread();

    }

    public void checkStatusThread() {

        new AsyncTask<String, Integer, String>() {

            @Override
            protected String doInBackground(String... params) {

                checkStatus();

                return null;
            };
        }.execute();
    }

    public void checkStatus() {

        connectionAvailable = isConnectionAvailable();

        serverReachable = isServerReachable();

        vpnAvailable = isVpnAvailable();

        Log.d("连接状态： ", connectionAvailable + "" );
        Log.d("VPN： ",  vpnAvailable + "" );
        Log.d("serverReachable： ", serverReachable + "");
    }

    public boolean isConnectionAvailable( ) {


        //Log.d("连接状态： ", state.toString());

        //检测API是不是小于21，因为到了API21之后getNetworkInfo(int networkType)方法被弃用
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.LOLLIPOP) {

            //获得ConnectivityManager对象
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            //获取ConnectivityManager对象对应的NetworkInfo对象
            //获取WIFI连接的信息
            NetworkInfo wifiNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            //获取移动数据连接的信息
            NetworkInfo dataNetworkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if (wifiNetworkInfo.isConnected() || dataNetworkInfo.isConnected()) {
                return true;
            }

        }else {
            //这里的就不写了，前面有写，大同小异
            System.out.println("API level 大于21");
            //获得ConnectivityManager对象
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            //获取所有网络连接的信息
            Network[] networks = connMgr.getAllNetworks();
            //用于存放网络连接信息
            //StringBuilder sb = new StringBuilder();
            //通过循环将网络信息逐个取出来
            /*for (int i=0; i < networks.length; i++){
                //获取ConnectivityManager对象对应的NetworkInfo对象
                NetworkInfo networkInfo = connMgr.getNetworkInfo(networks[i]);
                sb.append(networkInfo.getTypeName() + " connect is " + networkInfo.isConnected());
            }
            Toast.makeText(context, sb.toString(), Toast.LENGTH_SHORT).show();*/
            if( networks != null && networks.length > 0 ) {
                return true;
            }
         }
        return false;
    }
    /**
     * 检测是否能够连接到远程主机的端口号；
     */
    public boolean isServerReachable() {

        try {
            SocketAddress sockaddr = new InetSocketAddress(ipOrHostname, port);
            // Create an unbound socket
            Socket sock = new Socket();

            // This method will block no more than timeoutMs.
            // If the timeout occurs, SocketTimeoutException is thrown.
            int timeoutMs = 2000;   // 2 seconds
            sock.connect(sockaddr, timeoutMs);
            return true;
        } catch(IOException e) {
            // Handle exception
            System.out.println( e.getMessage() ) ;
            return false;
        }
    }

    /**
     * 检测是否使用VPN连接
     * @return
     */
    public boolean isVpnAvailable() {
        try {
            for( NetworkInterface intf : Collections.list(NetworkInterface.getNetworkInterfaces())) {

                // Pass over dormant interfaces
                if(!intf.isUp() || intf.getInterfaceAddresses().size() == 0)
                    continue;

                if ("tun0".equals(intf.getName()) || "ppp0".equals(intf.getName()) ){
                    // The VPN is up
                    //break;
                    return true;
                }
            }
        } catch (Exception e ) {
            e.printStackTrace();
        }
        return false;
    }
}
