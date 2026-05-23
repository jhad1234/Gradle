package com.yemen.telecom.bypass;

import android.content.Intent;
import android.net.VpnService;
import android.os.ParcelFileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;

public class LocalVpnService extends VpnService implements Runnable {
    private Thread mInterfaceThread;
    private ParcelFileDescriptor mInterface;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mInterfaceThread = new Thread(this, "YemenBypassThread");
        mInterfaceThread.start();
        return START_STICKY;
    }

    @Override
    public void run() {
        try {
            mInterface = new Builder()
                    .addAddress("10.0.0.1", 24)
                    .addRoute("0.0.0.0", 0)
                    .setSession("YemenTelecomBypass")
                    .establish();

            FileInputStream in = new FileInputStream(mInterface.getFileDescriptor());
            FileOutputStream out = new FileOutputStream(mInterface.getFileDescriptor());
            ByteBuffer packet = ByteBuffer.allocate(32767);

            while (!Thread.interrupted()) {
                int length = in.read(packet.array());
                if (length > 0) {
                    packet.limit(length);
                    out.write(packet.array(), 0, length);
                    packet.clear();
                }
            }
        } catch (Exception e) {
            activateAlternativeBypass();
        }
    }

    private void activateAlternativeBypass() {
        Intent intent = new Intent(this, AccessibilityBypass.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startService(intent);
    }

    @Override
    public void onDestroy() {
        if (mInterfaceThread != null) mInterfaceThread.interrupt();
        try { if (mInterface != null) mInterface.close(); } catch (Exception e) {}
        super.onDestroy();
    }
}
