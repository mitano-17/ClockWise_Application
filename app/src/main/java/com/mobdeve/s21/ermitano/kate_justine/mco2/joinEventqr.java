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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (intentResult != null) {
            if (intentResult.getContents() != null) {
                String scannedData = intentResult.getContents();
                Toast.makeText(this, "QR code Scanned.", Toast.LENGTH_SHORT).show();

                // Assume the scanned QR code contains the event name or event ID, depending on how it's encoded
                // For example, let's assume the QR code contains the event name:
                String eventName = scannedData;  // Adjust based on how your QR data is encoded

                // Pass the event name to the enterEventqr activity
                Intent intent = new Intent(joinEventqr.this, enterEventqr.class);
                intent.putExtra("eventName", eventName);  // Pass the event name
                startActivity(intent);
                finish();  // Optionally close the current activity
            } else {
                // Handle case when no QR code was scanned
                Toast.makeText(this, "No QR code scanned.", Toast.LENGTH_SHORT).show();
                finish();  // Close the activity if no QR code
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

}
