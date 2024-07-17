package com.orangekloud.plugins.pick_network;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.json.JSONArray;

public class PickNetwork extends CordovaPlugin {

    /**
     * Executes the request.
     */
	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) {
        // Send Payment Request
        if (action.equals("sendPaymentRequest")) { 
            // args.getString(0);
            return true;
        }

        // Context cordovaContext = this.cordova.getActivity().getApplicationContext();

        // callbackContext.error("Missing any of the following fields: Merchant ID and Payment Token");
        return false;
    }

}