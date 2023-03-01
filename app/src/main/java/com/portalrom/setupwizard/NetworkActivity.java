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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
    TextView searchTag;
    ListView listView;
    ImageView imgReload;
    BroadcastReceiver wifiScanReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);

        final WindowInsetsController insetsController = getWindow().getInsetsController();
        SetupWizardUtils.hideTaskBar(insetsController);

        listView = (ListView) findViewById(R.id.listWifi);
        searchTag = (TextView) findViewById(R.id.searchLabel);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(this.WIFI_SERVICE);
        wifiList = findViewById(R.id.listWifi);

        setReload();
        setSwitch();

    }

    private void setReload() {

        imgReload = (ImageView) findViewById(R.id.reload);
        imgReload.setClickable(true);
        imgReload.setFocusable(true);
        imgReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgReload.setVisibility(View.INVISIBLE);
                setSearchLabel(true,true);
                clearNetworks();
                lookForNetworks();
            }
        });
    }

    private void setSwitch(){
        onOffSwitch = (SwitchCompat)  findViewById(R.id.wifiSwitch);

        boolean val = wifiManager.isWifiEnabled();
        onOffSwitch.setChecked(val);
        isWifiOn=val;

        if(val){
            imgReload.setVisibility(View.VISIBLE);
            setSearchLabel(true,true);
            lookForNetworks();
        }

        onOffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                isWifiOn=isChecked;
                wifiManager.setWifiEnabled(isWifiOn);

                if(isWifiOn)
                {
                    setSearchLabel(true,true);
                    lookForNetworks();
                }
                else{
                    setSearchLabel(false,false);
                    imgReload.setVisibility(View.INVISIBLE);
                    clearNetworks();
                }

            }
        });
    }

    private void lookForNetworks() {

        if(wifiScanReceiver != null)
            unregisterReceiver(wifiScanReceiver);

        WifiManager wifiManager = (WifiManager)
                this.getSystemService(Context.WIFI_SERVICE);

        wifiScanReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context c, Intent intent) {
                boolean success = intent.getBooleanExtra(
                        WifiManager.EXTRA_RESULTS_UPDATED, false);

                if(isWifiOn){
                    if (success) {
                        scanSuccess();
                    } else {
                        scanFailure();
                    }
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(wifiScanReceiver, intentFilter);
        boolean success = wifiManager.startScan();
    }

    private void scanSuccess() {
        @SuppressLint("MissingPermission")
        List<ScanResult> results = wifiManager.getScanResults();

        for (ScanResult result : results) {
            wifiAvailable.add((String) result.SSID);
        }

        setListView(wifiAvailable);
        imgReload.setVisibility(View.VISIBLE);
        setSearchLabel(true,false);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //MISSING CODE
            }
        });
    }

    private void scanFailure() {

        imgReload.setVisibility(View.VISIBLE);


        // handle failure: new scan did NOT succeed
        // consider using old scan results: these are the OLD results!
        @SuppressLint("MissingPermission")
        List<ScanResult> results = wifiManager.getScanResults();

        //MISSING CODE
    }

    private void clearNetworks() {
        //MISSING CODE
        wifiAvailable.clear();
        setListView(wifiAvailable);
    }


    private void setSearchLabel(boolean isVisible, boolean isSearchOn){

        if(isSearchOn)
            searchTag.setText(R.string.searchLabelOn);
        else
            searchTag.setText(R.string.searchLabelOff);

        searchTag.invalidate();
        searchTag.requestLayout();

        if(isVisible)
            searchTag.setVisibility(View.VISIBLE);
        else
            searchTag.setVisibility(View.INVISIBLE);

        searchTag.invalidate();
        searchTag.requestLayout();
    }

    private void setListView(List<String> array){
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview, array);
        listView.setAdapter(adapter);
        listView.postInvalidate();



    }
}