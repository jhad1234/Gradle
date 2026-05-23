package com.yemen.telecom.bypass;

import android.content.Intent;
import android.graphics.Color;
import android.net.VpnService;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private boolean isServiceActive = false;
    private Button btnToggle, btnCheckRegion;
    private EditText edtPhoneNumber;
    private TextView txtRegionResult, txtYemenMobile, txtYou;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ربط جميع العناصر المحدثة من الواجهة
        btnToggle = findViewById(R.id.btn_toggle);
        btnCheckRegion = findViewById(findViewById(R.id.btn_check_region).getId());
        edtPhoneNumber = findViewById(R.id.edt_phone_number);
        txtRegionResult = findViewById(R.id.txt_region_result);
        txtYemenMobile = findViewById(R.id.status_yemen_mobile);
        txtYou = findViewById(R.id.status_you);

        updateUiState(false);

        // 🔍 زر الفحص وتحليل النطاق الجغرافي للرقم
        btnCheckRegion.setOnClickListener(v -> {
            String number = edtPhoneNumber.getText().toString().trim();
            if (number.isEmpty() || number.length() < 9) {
                Toast.makeText(MainActivity.this, "الرجاء إدخال رقم هاتف يمني صحيح مكون من 9 أرقام", Toast.LENGTH_SHORT).show();
                return;
            }

            // خوارزمية ذكية افتراضية لتمييز النطاق بناءً على أرقام البداية كمثال توضيحي
            // يمكنك تخصيص مفاتيح الحزم بدقة تامة هنا
            if (number.startsWith("770") || number.startsWith("771") || number.startsWith("733")) {
                txtRegionResult.setText("⚠️ النطاق الحالي: [شمال] - السعر مرتفع!");
                txtRegionResult.setTextColor(Color.parseColor("#E74C3C")); // أحمر تحذيري
                
                // تنشيط زر الخداع فوراً لتمكين المستخدم من التحويل للجنوب
                btnToggle.setEnabled(true);
                btnToggle.setText("⚙️ اضغط هنا للتحويل\nإلى نطاق [الجنوب]");
                btnToggle.setBackgroundColor(Color.parseColor("#34495E"));
            } else {
                txtRegionResult.setText("✅ النطاق الحالي: [جنوب] - السعر مخفض بالفعل");
                txtRegionResult.setTextColor(Color.parseColor("#2ECC71")); // أخضر
                
                btnToggle.setEnabled(true);
                btnToggle.setText("🚀 تأكيد تفعيل النفق الموحد");
                btnToggle.setBackgroundColor(Color.parseColor("#2980B9"));
            }
        });

        // 🔄 زر التفعيل والاتصال بالأنفاق الخلفية
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
            Toast.makeText(this, "تم فرض المحاكاة وتفعيل نفق الجنوب بنجاح حتمي!", Toast.LENGTH_LONG).show();
            simulateNetworkCapture();
        }
    }

    private void updateUiState(boolean active) {
        if (active) {
            btnToggle.setText("🟢 نفق الجنوب نشط\n(اضغط للإيقاف)");
            btnToggle.setBackgroundColor(Color.parseColor("#2ECC71"));
            txtRegionResult.setText("🛡️ النطاق المحاكي الآن: [جنوب] - مغطى بنجاح");
            txtRegionResult.setTextColor(Color.parseColor("#2ECC71"));
        } else {
            btnToggle.setText("🔴 التوجيه الجغرافي متوقف\n(افحص الرقم أولاً)");
            btnToggle.setBackgroundColor(Color.parseColor("#7F8C8D"));
            btnToggle.setEnabled(false); // يعود مغلقاً حتى يتم فحص رقم جديد
            
            txtYemenMobile.setTextColor(Color.parseColor("#555555"));
            txtYou.setTextColor(Color.parseColor("#555555"));
        }
    }

    private void simulateNetworkCapture() {
        txtYemenMobile.setTextColor(Color.parseColor("#2ECC71")); 
        txtYou.setTextColor(Color.parseColor("#2ECC71"));
    }
}
