package com.mobdeve.s21.ermitano.kate_justine.mco2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class generatedQRcode extends AppCompatActivity {
    private Bitmap qrBitmap = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generatedqrcode);

        ImageView qrCodeImageView = findViewById(R.id.QRcodeIv);
        Button downloadButton = findViewById(R.id.downloadBtn);

        // Retrieve event details passed via Intent
        Intent intent = getIntent();
        String eventId = intent.getStringExtra("eventId");
        String userId = intent.getStringExtra("userId");

        if (eventId != null) {
            // Use the eventId to generate the QR code
            qrBitmap = generateQRCode(eventId);
            if (qrBitmap != null) {
                qrCodeImageView.setImageBitmap(qrBitmap);
            }
        }

        // Download the QR code as an image
        downloadButton.setOnClickListener(v -> {
            if (qrBitmap != null) {
                downloadQRCode(qrBitmap);
            } else {
                Toast.makeText(this, "QR Code not available", Toast.LENGTH_SHORT).show();
            }
        });

        // Back button logic
        ImageView backButton = findViewById(R.id.backImg4);
        backButton.setOnClickListener(v -> finish());
    }

    // Generate QR Code
    private Bitmap generateQRCode(String data) {
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            BitMatrix bitMatrix = barcodeEncoder.encode(data, BarcodeFormat.QR_CODE, 300, 300);
            Toast.makeText(this, "Generated QR code", Toast.LENGTH_SHORT).show();
            return barcodeEncoder.createBitmap(bitMatrix);
        } catch (WriterException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error generating QR code", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    // Download QR Code
    private void downloadQRCode(Bitmap qrBitmap) {

        try {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                // Get the path to the Downloads folder
                File downloadsFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                // Create the file in the Downloads folder
                File qrFile = new File(downloadsFolder, "GeneratedQRCode.png");
                // Save the bitmap to the file
                FileOutputStream outputStream = new FileOutputStream(qrFile);
                qrBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                outputStream.flush();
                outputStream.close();

                // Notify the user
                Toast.makeText(this, "QR Code saved to Downloads folder", Toast.LENGTH_SHORT).show();

                // Refresh Media Store to make the file immediately visible in Downloads
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                mediaScanIntent.setData(Uri.fromFile(qrFile));
                sendBroadcast(mediaScanIntent);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error saving QR code", Toast.LENGTH_SHORT).show();
        }
    }
}
