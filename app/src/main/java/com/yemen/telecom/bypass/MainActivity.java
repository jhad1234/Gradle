package com.yemen.telecom.bypass;

import android.content.Intent;
import android.graphics.Color;
import android.net.VpnService;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private boolean isServiceActive = false;
    private Button btnToggle;
    private TextView txtYemenMobile, txtYou, txtSabafon, txtYemenNet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnToggle = findViewById(R.id.btn_toggle);
        txtYemenMobile = findViewById(R.id.status_yemen_mobile);
        txtYou = findViewById(R.id.status_you);
        txtSabafon = findViewById(R.id.status_sabafon);
        txtYemenNet = findViewById(R.id.status_yemen_net);

        updateUiState(false);

        btnToggle.setOnClickListener(v -> {
            if (!isServiceActive) {
                Intent intent = VpnService.prepare(MainActivity.this);
                if (intent != null) {
                    startActivityForResult(intent, 0);
                } else {
                    onActivityResult(0, RESULT_OK, null);
                }
            } else {
                stopService(new Intent(MainActivity.this, LocalVpnService.class));
                updateUiState(false);
                isServiceActive = false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            startService(new Intent(this, LocalVpnService.class));
            updateUiState(true);
            isServiceActive = true;
            Toast.makeText(this, "المنظومة نشطة وجاهزة لاصطياد الحزم الموحدة", Toast.LENGTH_SHORT).show();
            simulateNetworkCapture();
        }
    }

    private void updateUiState(boolean active) {
        if (active) {
            btnToggle.setText("🟢 نظام الخداع نشط\n(اضغط للإيقاف)");
            btnToggle.setBackgroundColor(Color.parseColor("#2ECC71"));
        } else {
            btnToggle.setText("🔴 نظام الخداع متوقف\n(اضغط للتفعيل)");
            btnToggle.setBackgroundColor(Color.parseColor("#E74C3C"));
            
            txtYemenMobile.setTextColor(Color.parseColor("#555555"));
            txtYou.setTextColor(Color.parseColor("#555555"));
            txtSabafon.setTextColor(Color.parseColor("#555555"));
            txtYemenNet.setTextColor(Color.parseColor("#555555"));
        }
    }

    private void simulateNetworkCapture() {
        txtYemenMobile.setTextColor(Color.parseColor("#2ECC71")); 
        txtYou.setTextColor(Color.parseColor("#2ECC71"));
        txtSabafon.setTextColor(Color.parseColor("#2ECC71"));
        txtYemenNet.setTextColor(Color.parseColor("#2ECC71"));
    }
}
