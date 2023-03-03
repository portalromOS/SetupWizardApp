package com.portalrom.setupwizard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.os.ConfigurationCompat;
import androidx.core.os.LocaleListCompat;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.WindowInsetsController;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;


import com.portalrom.setupwizard.Utils.SetupWizardUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class LocaleActivity extends AppCompatActivity {
    int selectedLocale = 0;
    int defaultLocale =0;
    List<Locale> systemLocales = new ArrayList<Locale>(){};
    List<String> systemLanguages = new ArrayList<String>(){};
    private Locale mCurrentLocale;
    Button next;
    View previousSelectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locale);

        final WindowInsetsController insetsController = getWindow().getInsetsController();
        SetupWizardUtils.hideTaskBar(insetsController);

        setLocales();

    }

    protected void setLocales(){

        loadLanguages();

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview, systemLanguages);

        ListView listView = (ListView) findViewById(R.id.listLocales);
        listView.setAdapter(adapter);

        listView.setSelection(defaultLocale-2);
        listView.setSelected(true);
        //listView.setItemChecked(defaultLocale,true);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                selectedLocale = i;
                next = (Button)findViewById(R.id.buttonNextL);
                next.setVisibility(View.VISIBLE);
            }
        });
    }

    private void loadLanguages() {
        Locale mCurrentLocale = Locale.getDefault();

        for (Locale locale : Locale.getAvailableLocales()) {
            if (locale.getLanguage().length() == 2) {
                if (!isLanguageInList(systemLanguages, locale)) {
                    systemLocales.add(locale);
                    systemLanguages.add(locale.getDisplayLanguage());

                    if(locale.getLanguage() == mCurrentLocale.getLanguage())
                    {
                        defaultLocale = systemLanguages.size()-1;
                    }
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


    public void goToNetwork(View view) {


        updateLocale();

        Intent intent = new Intent(this, NetworkActivity.class);
        startActivity(intent);

    }
}