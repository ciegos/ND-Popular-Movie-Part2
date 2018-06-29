package com.mycodingdesk.popularmovies.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Cesar Egoavil on 5/10/2018.
 */
public class NetworkUtil {

    /**
     *
     * @param context
     * @return
     */
    public static boolean isConnectionAvailable(Context context) {
        int[] networkTypes = {ConnectivityManager.TYPE_MOBILE,
                ConnectivityManager.TYPE_WIFI};
        try {
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            for (int networkType : networkTypes) {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo != null && activeNetworkInfo.getType() == networkType
                        && activeNetworkInfo.isAvailable() && activeNetworkInfo.isConnected())
                {
                    return true;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

}
