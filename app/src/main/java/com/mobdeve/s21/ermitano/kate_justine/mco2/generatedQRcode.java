package com.mobdeve.s21.ermitano.kate_justine.mco2;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generatedqrcode);

        ImageView qrCodeImageView = findViewById(R.id.QRcodeIv);
        Button downloadButton = findViewById(R.id.downloadBtn);

        // Generate a random QR code
        String randomData = "UniqueData-" + System.currentTimeMillis(); // Random unique data
        Bitmap qrBitmap = generateQRCode(randomData);
        if (qrBitmap != null) {
            qrCodeImageView.setImageBitmap(qrBitmap);
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
        File qrFile = new File(getExternalFilesDir(null), "GeneratedQRCode.png");

        try (FileOutputStream out = new FileOutputStream(qrFile)) {
            qrBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            Toast.makeText(this, "QR Code saved to: " + qrFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error saving QR code", Toast.LENGTH_SHORT).show();
        }
    }
}
