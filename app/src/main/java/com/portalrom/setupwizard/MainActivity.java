package com.portalrom.setupwizard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.widget.Button;

import com.portalrom.setupwizard.Utils.SetupWizardUtils;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        final WindowInsetsController insetsController = getWindow().getInsetsController();
        SetupWizardUtils.hideTaskBar(insetsController);
    }

    public void goToLocalePicker(View view) {
        Intent intent = new Intent(this, LocaleActivity.class);
        startActivity(intent);

    }
}