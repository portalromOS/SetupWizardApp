package com.portalrom.setupwizard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowInsetsController;

import com.portalrom.setupwizard.Utils.SetupWizardUtils;

public class NetworkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);

        final WindowInsetsController insetsController = getWindow().getInsetsController();
        SetupWizardUtils.hideTaskBar(insetsController);
    }
}