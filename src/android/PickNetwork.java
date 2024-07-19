package com.orangekloud.plugins.pick_network;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.cainiao.cntech.blebox.CNTechLeHelper;
import com.cainiao.cntech.blebox.bluetooth.LeGattConnectCallback;
import com.cainiao.cntech.blebox.callback.LeWriteCallback;
import com.cainiao.cntech.blebox.exception.LeException;
import com.cainiao.cntech.blebox.scan.LeDevice;
import com.cainiao.cntech.blebox.scan.LeScanningCallback;
import com.cainiao.cntech.blebox.utils.HexUtil;
import com.google.gson.Gson;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

public class PickNetwork extends CordovaPlugin {

    final String[] BLE_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
    };
    private CallbackContext callbackContext;
    private JSONArray args;
    Gson gson = new Gson();
    private LeDevice leDevice;
    /**
     * Executes the request.
     */
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        this.callbackContext = callbackContext;
        this.args = args;
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
        if(checkSelfPermissions()){
            switch (action){
                case "startScan":startScan();break;
                case "connect":connect();break;
                case "disconnect":disconnect();break;
                // Return true : connected and false : not connected
                case "isConnected":return isConnected();
                // Return characteristic of device through leNotify
                case "getDetail":getDetail();break;
                case "setCabinet":setCabinet();break;
                case "queryCabinetInfo":queryCabinetInfo();break;
                case "queryGroup":queryGroup();break;
                case "openDoor":openDoor();break;
                case "getConnectedDevices":getConnectedDevices();
            }
        }
        //  Stop Scan
//            CNTechLeHelper.cancelScan();

        // Destroy
        CNTechLeHelper.destroy();
        return true;
//        }
//        return false;
    }
    private void startScan()throws JSONException{
        System.out.print("Starting Scan");
        int timeout = args.getInt(0);
        String mac = args.getString(1);
        String names = args.getString(2);
        String[] nameArr =  TextUtils.isEmpty(names) ? null : names.split(",");
        CNTechLeHelper.startScan(timeout, mac, nameArr,
                new LeScanningCallback() {
                    @Override
                    public void onScanStarted(boolean b) {

                    }

                    @Override
                    public void onLeScan(LeDevice leDevice) {

                    }

                    @Override
                    public void onScanning(LeDevice leDevice) {

                    }

                    @Override
                    public void onScanFinished(List<LeDevice> list) {
                        String result = gson.toJson(list);
                        callbackContext.success(result);
                    }
                });
    }
    private void connect()throws JSONException{
        System.out.println("Connect");
        LeDevice leDevice = (LeDevice) args.get(0);

        CNTechLeHelper.connect(leDevice, new LeGattConnectCallback() {
            @Override
            public void onStartConnect() {

            }

            @Override
            public void onConnectFail(LeDevice bleDevice, LeException exception) {
                // call fail callback
            }

            @Override
            public void onDisConnected(boolean b, LeDevice leDevice, BluetoothGatt bluetoothGatt, int i) {

            }

            @Override
            public void onConnectSuccess(LeDevice bleDevice, BluetoothGatt gatt, int status) {
                // return the connect status
                callbackContext.success(status);
            }
        });
    }
    private void disconnect()throws JSONException{
        System.out.print("Disconnect");
        LeDevice leDevice = (LeDevice) args.get(0);
        CNTechLeHelper.disconnect(leDevice);
    }
    private boolean isConnected()throws JSONException{
        System.out.print("isConnected");
        LeDevice leDevice = (LeDevice) args.get(0);
        return CNTechLeHelper.isConnected(leDevice);
    }
    // Return characteristic of device through leNotify
    private void getDetail()throws JSONException{
        System.out.print("Get Detail");
        LeDevice leDevice = (LeDevice) args.get(0);
        CNTechLeHelper.registerLeNotify(leDevice, (frameType, maps) -> {
            maps.put("frameType",Integer.toString(frameType));
            callbackContext.success(gson.toJson(maps));
        });
    }
    private void getConnectedDevices(){
        System.out.println("get connected devices");
        List<LeDevice> devices = CNTechLeHelper.getAllConnectedDevice();
        if(devices.size()>0){
            callbackContext.error("No devices found.");
        }
        callbackContext.success(gson.toJson(devices));
    }
    private void setCabinet()throws JSONException{
        System.out.print("Set Cabinet");
        String query = args.getString(0);
        this.leDevice = (LeDevice) args.get(1);
        leWrite(CNTechLeHelper.setCabinet(query));
    }
    private void queryCabinetInfo() throws JSONException{
        System.out.print("Query Cabinet");
        this.leDevice = (LeDevice) args.get(1);
        leWrite(CNTechLeHelper.queryCabinetInfo());
    }
    private void queryGroup()throws JSONException{
        System.out.print("Query Group");
        String query = args.getString(0);
        this.leDevice = (LeDevice) args.get(1);
        leWrite(CNTechLeHelper.queryGroup(query));
    }
    private void openDoor()throws JSONException{
        System.out.print("Open door");
        String query = args.getString(0);
        this.leDevice = (LeDevice) args.get(1);
        leWrite(CNTechLeHelper.openDoor(query));
    }
    private void leWrite(byte[] buffer){
        CNTechLeHelper.gattLeWrite(leDevice, buffer, new LeWriteCallback() {
            @Override
            public void onWriteSuccess(int current, int total, byte[] justWrite) {
                // to pass data to success call back
                String statusString = "write success, current: " + current
                        + " total: " + total
                        + " justWrite: " + HexUtil.formatHexString(justWrite, true);
                callbackContext.success(statusString);
            }

            @Override
            public void onWriteFailure(byte[] justWrite, LeException exception) {
                // to pass data and exception to error callback
                String errorString =  "write error,--->" + exception.toString();
                callbackContext.error(errorString);
            }
        });
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