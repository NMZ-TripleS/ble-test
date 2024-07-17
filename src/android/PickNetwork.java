package com.orangekloud.plugins.pick_network;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.cainiao.cntech.blebox.CNTechLeHelper;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.json.JSONArray;

public class PickNetwork extends CordovaPlugin {

    final String[] BLE_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
    };

    /**
     * Executes the request.
     */
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {
//        if (action.equals("..")) {
//            checkSelfPermissions();
            // Initialize
            CNTechLeHelper.initLe(this.cordova.getActivity(), false);

            // Start Scan
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (!bluetoothAdapter.isEnabled()) {
                // Bluetooth is disabled
                Toast.makeText(this.cordova.getActivity(), "Disabled bluetooth", Toast.LENGTH_LONG).show();
                return true;
            }

            // Stop Scan
//            CNTechLeHelper.cancelScan();

            // Destroy
            CNTechLeHelper.destroy();
            return true;
//        }
//        return false;
    }

    /**
     * Check if permission is already enabled,
     * if not initiate a request to the user.
     */
    private boolean checkSelfPermissions() {
        boolean result = true;
        try {
            Activity activity = this.cordova.getActivity();
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, BLE_PERMISSIONS, 0);
                result = false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            result = false;
        }
        return result;
    }

}