package com.portalrom.setupwizard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.os.ConfigurationCompat;
import androidx.core.os.LocaleListCompat;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.WindowInsetsController;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.portalrom.setupwizard.Utils.SetupWizardUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class LocaleActivity extends AppCompatActivity {
    int selectedLocale = 0;
    List<Locale> systemLocales = new ArrayList<Locale>(){};
    List<String> systemLanguages = new ArrayList<String>(){};
    private Locale mCurrentLocale;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locale);

        final WindowInsetsController insetsController = getWindow().getInsetsController();
        SetupWizardUtils.hideTaskBar(insetsController);

        setLocales();

    }

    protected void setLocales(){

        Locale mCurrentLocale = Locale.getDefault();

        loadLanguages();

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview, systemLanguages);

        ListView listView = (ListView) findViewById(R.id.listLocales);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                selectedLocale = i;
                updateLocale();
            }
        });
    }

    private void loadLanguages() {

        for (Locale locale : Locale.getAvailableLocales()) {
            if (locale.getLanguage().length() == 2) {
                if (!isLanguageInList(systemLanguages, locale)) {
                    systemLocales.add(locale);
                    systemLanguages.add(locale.getDisplayLanguage());
                }
            }
        }
    }

    private static boolean isLanguageInList(List<String> list, Locale locale) {
        if (list == null) {
            return false;
        }
        for (String item: list) {
            if (item.equalsIgnoreCase(locale.getDisplayLanguage())){
                return true;
            }
        }
        return false;
    }

    private void updateLocale(){

        Locale locale = systemLocales.get(selectedLocale);
        mCurrentLocale = locale;

        Configuration config = new Configuration();
        Configuration overrideConfiguration = getResources().getConfiguration();
        overrideConfiguration.setLocale(locale);
        Context context  = createConfigurationContext(overrideConfiguration);
        Resources resources = context.getResources();

        com.android.internal.app.LocalePicker.updateLocale(mCurrentLocale);
    }

    public void closeAplication(View view) {
        finishAffinity();
        System.exit(0);

    }
}