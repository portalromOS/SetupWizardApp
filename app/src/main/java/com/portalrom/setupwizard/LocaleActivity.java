package com.portalrom.setupwizard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.LocaleList;
import android.view.View;
import android.view.WindowInsetsController;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


import com.android.internal.app.LocalePicker;
import com.portalrom.setupwizard.Utils.SetupWizardUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LocaleActivity extends AppCompatActivity {
    String selectedLocale = "";
    List<String> systemLocales = new ArrayList<String>(){};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locale);

        final WindowInsetsController insetsController = getWindow().getInsetsController();
        SetupWizardUtils.hideTaskBar(insetsController);

        setLocales();

    }

    private void loadLanguages() {

        Locale[] mLocaleAdapter = Locale.getAvailableLocales();


        for (int i = 0; i < mLocaleAdapter.length; i++) {

            String l = mLocaleAdapter[i].getDisplayLanguage();

            if(!l.isEmpty())
                if(!systemLocales.contains(l))
                    systemLocales.add(l);
        }

        Collections.sort((systemLocales));

    }

    protected void setLocales(){

        Locale mCurrentLocale = Locale.getDefault();

        loadLanguages();

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_listview, systemLocales);

        ListView listView = (ListView) findViewById(R.id.listLocales);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                selectedLocale = listView.getItemAtPosition(i).toString();

            }
        });
    }


    private void updateLocale(){

        Locale locale = new Locale(selectedLocale);
        Locale.setDefault(locale);
        Configuration config = new Configuration();

        Configuration overrideConfiguration = getBaseContext().getResources().getConfiguration();
        overrideConfiguration.setLocale(locale);
        Context context  = createConfigurationContext(overrideConfiguration);
        Resources resources = context.getResources();

    }


}