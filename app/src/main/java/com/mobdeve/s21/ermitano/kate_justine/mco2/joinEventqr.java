package com.mobdeve.s21.ermitano.kate_justine.mco2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import javax.annotation.Nullable;

public class joinEventqr extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        //launches the QR code scanner immediately using the camera
        QRscanner();

    }
    private void QRscanner(){
        IntentIntegrator intentIntegrator = new IntentIntegrator(joinEventqr.this);
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.setPrompt("Scan a QR code");
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        intentIntegrator.initiateScan();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(intentResult != null){
           if(intentResult.getContents() != null){
               String scannedData = intentResult.getContents();
               Toast.makeText(this, "QR code Scanned.", Toast.LENGTH_SHORT).show();
               //passes the scannedQr to enter the event
               Intent intent = new Intent(joinEventqr.this, enterEventqr.class);
               intent.putExtra("scannedData", scannedData);
               startActivity(intent);
               finish();
           }else{
               //redirects back to dashboard when no QR code is scanned and when the back button is clicked
               Toast.makeText(this, "No QR code scanned.", Toast.LENGTH_SHORT).show();
               finish();
           }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
