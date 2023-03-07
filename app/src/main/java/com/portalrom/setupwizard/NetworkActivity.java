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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.portalrom.setupwizard.Utils.Adapters.WifiCustomAdapter;
import com.portalrom.setupwizard.Utils.DataModel.WifiDataModel;
import com.portalrom.setupwizard.Utils.NetworkUtils;
import com.portalrom.setupwizard.Utils.SetupWizardUtils;

import java.util.ArrayList;
import java.util.List;

public class NetworkActivity extends AppCompatActivity {

    private SwitchCompat onOffSwitch;
    private boolean isWifiOn=false;
    private WifiManager wifiManager;
    private List<WifiDataModel> wifiAvailable = new ArrayList<WifiDataModel>(){};
    private TextView searchTag;

    private ImageView imgReload;
    private BroadcastReceiver wifiScanReceiver;
    private static WifiCustomAdapter adapter;
    private WifiDataModel selectedWifi;
    private TextView selectedStateText;
    private Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);

        final WindowInsetsController insetsController = getWindow().getInsetsController();
        SetupWizardUtils.hideTaskBar(insetsController);


        searchTag = (TextView) findViewById(R.id.searchLabel);
        next = (Button) findViewById(R.id.buttonNextN);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(this.WIFI_SERVICE);

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
            //imgReload.setVisibility(View.VISIBLE);
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
        {
            try {
                unregisterReceiver(wifiScanReceiver);

            } catch(IllegalArgumentException e) {

            }
        }

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
        int level=0;

        for (ScanResult result : results) {

            WifiDataModel dataModel = new WifiDataModel(result.SSID,
                    R.string.disconnected, result.level
                    , NetworkUtils.getScanResultSecurity(result), result);
            wifiAvailable.add(dataModel);
        }


        setListView(wifiAvailable);
        imgReload.setVisibility(View.VISIBLE);
        setSearchLabel(true,false);

        if(wifiScanReceiver != null)
            unregisterReceiver(wifiScanReceiver);

    }

    private void scanFailure() {

        imgReload.setVisibility(View.VISIBLE);

        // handle failure: new scan did NOT succeed
        // consider using old scan results: these are the OLD results!
        @SuppressLint("MissingPermission")
        List<ScanResult> results = wifiManager.getScanResults();

        if(wifiScanReceiver != null)
            unregisterReceiver(wifiScanReceiver);
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

    private void setListView(List<WifiDataModel> array){


        adapter= new WifiCustomAdapter((ArrayList<WifiDataModel>) array
                ,getApplicationContext());

        ListView listView = (ListView) findViewById(R.id.listWifi);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                selectedWifi = wifiAvailable.get(i);
                selectedStateText = view.findViewById(R.id.wifiConnectionState);
                selectedStateText.setText(R.string.connecting);

                if(array.get(i).getLock())
                    openWifiConnect();
                else
                    connectToWifi(null);
            }
        });
    }

    private void openWifiConnect() {

        WifiConnectFragment connect = new WifiConnectFragment(selectedWifi.getName());
        connect.show(getSupportFragmentManager(), "Connect");

    }


    public void connectToWifi(String insertedPw){

        String networkPass = insertedPw;
        boolean isConnected = NetworkUtils.connectToNetwork(this,
                selectedWifi,insertedPw);

        if(isConnected){
            selectedStateText.setText(R.string.connected);
            next.setVisibility(View.VISIBLE);
        }

        else
            selectedStateText.setText(R.string.error);
    }

    public void goToSync(View view) {

        Intent intent = new Intent(this, SyncRegister.class);
        startActivity(intent);

    }
}