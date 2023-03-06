package com.portalrom.setupwizard.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;

import com.portalrom.setupwizard.Utils.DataModel.WifiDataModel;

import java.util.List;

public class NetworkUtils {

    // Constants used for different security types
    private static final String WPA2 = "WPA2";
    private static final String WPA = "WPA";
    private static final String WEP = "WEP";
    private static final String OPEN = "Open";
    /* For EAP Enterprise fields */
    private static final String WPA_EAP = "WPA-EAP";
    private static final String IEEE8021X = "IEEE8021X";




    public static boolean getScanResultSecurity(ScanResult scanResult) {
        boolean isLocked = false;
        final String cap = scanResult.capabilities;
        final String[] securityModes = { WEP, WPA, WPA2, WPA_EAP, IEEE8021X };
        for (int i = securityModes.length - 1; i >= 0; i--) {
            if (cap.contains(securityModes[i])) {
                isLocked =true;
            }
        }
        return isLocked;
    }



    public static boolean connectToNetwork(Context context, WifiDataModel dataModel
            , String Password){

        String networkSSID = dataModel.getName();
        String networkPass = Password;

        final String cap = dataModel.getResult().capabilities;
        boolean isWEP = cap.contains(WEP);
        boolean isWAP = cap.contains(WPA) ||  cap.contains(WPA2) || cap.contains(WPA_EAP) ;
        boolean isEAP = cap.contains(IEEE8021X);
        boolean isOPEN = !isWEP && !isWAP && !isEAP;

        boolean connected = false;

        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = "\"" + networkSSID + "\"";   // Please note the quotes. String should contain ssid in quotes

        if(isWEP) {

            conf.wepKeys[0] = "\"" + networkPass + "\"";
            conf.wepTxKeyIndex = 0;
            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
        }

        if(isWAP){
            conf.preSharedKey = "\""+ networkPass +"\"";
        }

        if(isOPEN){
            conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        }

        WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        wifiManager.addNetwork(conf);


        @SuppressLint("MissingPermission")
        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();

        for( WifiConfiguration i : list ) {
            if(i.SSID != null && i.SSID.equals("\"" + networkSSID + "\"")) {
                wifiManager.disconnect();
                wifiManager.enableNetwork(i.networkId, true);
                connected = wifiManager.reconnect();
                break;
            }
        }

        return  connected;
    }





}
