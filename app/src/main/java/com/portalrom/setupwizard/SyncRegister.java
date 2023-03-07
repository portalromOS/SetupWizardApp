package com.portalrom.setupwizard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.WindowInsetsController;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.portalrom.setupwizard.Utils.SetupWizardUtils;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SyncRegister extends AppCompatActivity {

    Button connect;
    TextView info;
    //10.0.2.2 is the 127.0.0.1 to emulator
    private static String URL = "http://10.0.2.2:8080/serverapi/users.wsdl";
    private static String NAMESPACE = "http://www.mob.com/serverapi/users/base";
    private static String METHOD_NAME = "GetUserByIdRequest";
    private static String SOAP_ACTION = "http://www.mob.com/serverapi/users/base/GetUserByIdRequest";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_register);

        final WindowInsetsController insetsController = getWindow().getInsetsController();
        SetupWizardUtils.hideTaskBar(insetsController);

        connect = findViewById(R.id.connect);
        info = findViewById(R.id.result);
        connect.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                ExecutorService executor = Executors.newSingleThreadExecutor();
                Handler handler = new Handler(Looper.getMainLooper());

                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        //Background work here
                        String name = callWebService();

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                //UI Thread work here
                                info.setText(name);
                            }
                        });
                    }
                });

            }
        });

    }


    private String callWebService() {
        String result = "";

        SoapObject soapObject = new SoapObject(NAMESPACE, METHOD_NAME);


        PropertyInfo userId = new PropertyInfo();
        userId.setName("userId");
        userId.setValue("170738fd-07b4-42ee-9840-f0af507633b2");
        userId.setType(String.class);

        soapObject.addProperty(userId);

        SoapSerializationEnvelope envelope =  new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(soapObject);
        envelope.implicitTypes = true;
        envelope.setAddAdornments(false);
        envelope.dotNet = true;

        HttpTransportSE httpTransportSE = new HttpTransportSE(URL);
        httpTransportSE.debug = true;//ADD THIS

        try {
            httpTransportSE.call(SOAP_ACTION, envelope);
            //ADD THESE 2
            //Important Outputs to check how the request/Response looks like.. Check them in Logcat to find these outputs
            System.out.println("requestDump is :"+httpTransportSE.requestDump);//ADDED
            System.out.println("responseDump is :"+httpTransportSE.responseDump);//ADDED

            SoapObject resultsRequestSOAP = (SoapObject) envelope.getResponse();

            SoapPrimitive name = (SoapPrimitive) resultsRequestSOAP.getProperty("userName");
            result = name.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
