package com.portalrom.setupwizard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.WindowInsetsController;
import android.widget.Spinner;

import com.portalrom.setupwizard.Utils.SetupWizardUtils;

public class LocaleActivity extends AppCompatActivity {

    Spinner localeSpinner;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locale);


        final WindowInsetsController insetsController = getWindow().getInsetsController();
        SetupWizardUtils.hideTaskBar(insetsController);
    }
}