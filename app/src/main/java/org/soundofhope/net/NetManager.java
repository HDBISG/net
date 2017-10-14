package org.soundofhope.net;

import android.os.AsyncTask;

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

    static boolean netStatus;

    static String ipOrHostname = "www.google.com";
    static int port = 80;

    public NetManager(String ipOrHostname, int port ) {

        new AsyncTask<String, Integer, String>() {

            @Override
            protected String doInBackground(String... params) {

                testSocketAddress( );
                boolean isConnectVPN = isConnectVPN();
                System.out.println( "是否连接VPN =" + isConnectVPN );

                return null;
            };
        }.execute();
    }

    /**
     * 检测是否能够连接到远程主机的端口号；
     */
    public static void testSocketAddress() {

        try {
            SocketAddress sockaddr = new InetSocketAddress(ipOrHostname, port);
            // Create an unbound socket
            Socket sock = new Socket();

            // This method will block no more than timeoutMs.
            // If the timeout occurs, SocketTimeoutException is thrown.
            int timeoutMs = 2000;   // 2 seconds
            sock.connect(sockaddr, timeoutMs);
            netStatus = true;
        } catch(IOException e) {
            // Handle exception
            System.out.println( e.getMessage() ) ;
            netStatus = false;
        }
        System.out.println( "网络状态=" + netStatus );

    }

    /**
     * 检测是否使用VPN连接
     * @return
     */
    public static boolean isConnectVPN() {
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
