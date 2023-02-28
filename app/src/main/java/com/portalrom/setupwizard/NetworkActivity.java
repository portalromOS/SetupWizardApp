package com.portalrom.setupwizard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsetsController;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;

import com.portalrom.setupwizard.Utils.SetupWizardUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class NetworkActivity extends AppCompatActivity {
    SwitchCompat onOffSwitch;
    boolean isWifiOn=false;
    private ListView wifiList;
    private WifiManager wifiManager;
    List<String> wifiAvailable = new ArrayList<String>(){};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);

        final WindowInsetsController insetsController = getWindow().getInsetsController();
        SetupWizardUtils.hideTaskBar(insetsController);

        wifiManager = (WifiManager) getApplicationContext().getSystemService(this.WIFI_SERVICE);
        wifiList = findViewById(R.id.listWifi);
        setSwitch();
    }

    private void setSwitch(){
        onOffSwitch = (SwitchCompat)  findViewById(R.id.wifiSwitch);

        boolean val = wifiManager.isWifiEnabled();
        onOffSwitch.setChecked(val);

        if(val)
            lookForNetworks();

        onOffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                isWifiOn=isChecked;
                wifiManager.setWifiEnabled(isWifiOn);

                if(isWifiOn)
                {
                    lookForNetworks();
                }
                else
                    clearNetworks();
            }
        });
    }


    private void lookForNetworks() {
        WifiManager wifiManager = (WifiManager)
                this.getSystemService(Context.WIFI_SERVICE);

        BroadcastReceiver wifiScanReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context c, Intent intent) {
                boolean success = intent.getBooleanExtra(
                        WifiManager.EXTRA_RESULTS_UPDATED, false);
                if (success) {
                    scanSuccess();
                } else {
                    scanFailure();
                }
            }
        };

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        this.registerReceiver(wifiScanReceiver, intentFilter);

        boolean success = wifiManager.startScan();
        if (!success) {
            scanFailure();
        }

    }

    private void scanSuccess() {
        @SuppressLint("MissingPermission")
        List<ScanResult> results = wifiManager.getScanResults();

        for (ScanResult result : results) {
            wifiAvailable.add((String) result.venueName);
        }

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview, wifiAvailable);

        ListView listView = (ListView) findViewById(R.id.listWifi);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //MISSING CODE
            }
        });
    }

    private void scanFailure() {
        // handle failure: new scan did NOT succeed
        // consider using old scan results: these are the OLD results!
        @SuppressLint("MissingPermission")
        List<ScanResult> results = wifiManager.getScanResults();

        //MISSING CODE
    }

    private void clearNetworks() {
//MISSING CODE

    }
}