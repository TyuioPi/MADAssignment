package com.s3628594.controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.widget.Toast;

public class checkInternetConnection extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()){
            Toast.makeText(context,"Internet connected", Toast.LENGTH_LONG).show();
            RequestLocation.getSingletonInstance().getLocation(context);
        }else{
            Toast.makeText(context,"Internet not connected", Toast.LENGTH_LONG).show();
        }
    }
}
