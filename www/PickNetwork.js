// Simplify some cordova functions to be used
var exec = require("cordova/exec");
var argscheck = require("cordova/argscheck");
var getValue = argscheck.getValue;
/**
 * Contains the functions to be exported
 */
let pickNetworkExport = {};

pickNetworkExport.startScan = function (successCallback, options) {
  options = options || {};
  var args = [];

  args.push(getValue(options.timeout, 3000));
  args.push(getValue(options.mac, ""));
  args.push(getValue(options.name, ""));
  exec(successCallback, null, "PickNetwork", "startScan", args);
};

pickNetworkExport.connect = function (successCallback, errorCallback, options) {
  options = options || {};
  var args = [];

  args.push(getValue(options.leDevice, null));
  exec(successCallback, errorCallback, "PickNetwork", "connect", args);
};

pickNetworkExport.disconnect = function (options) {
  options = options || {};
  var args = [];

  args.push(getValue(options.leDevice, null));
  exec(null, null, "PickNetwork", "disconnect", args);
};

pickNetworkExport.isConnected = function (options) {
  options = options || {};
  var args = [];
  args.push(getValue(options.leDevice, null));
  exec(null, null, "PickNetwork", "isConnected", args);
};
pickNetworkExport.getDetail = function (options) {
  options = options || {};
  var args = [];
  args.push(getValue(options.leDevice, null));
  exec(null, null, "PickNetwork", "getDetail", args);
};
pickNetworkExport.setCabinet = function (
  successCallback,
  errorCallback,
  options
) {
  options = options || {};
  var args = [];
  args.push(getValue(options.query, null));
  args.push(getValue(options.leDevice, null));
  exec(successCallback, errorCallback, "PickNetwork", "setCabinet", args);
};
pickNetworkExport.queryCabinetInfo = function (
  successCallback,
  errorCallback,
  options
) {
  options = options || {};
  var args = [];
  args.push(getValue(options.leDevice, null));
  exec(successCallback, errorCallback, "PickNetwork", "queryCabinetInfo", args);
};
pickNetworkExport.queryGroup = function (
  successCallback,
  errorCallback,
  options
) {
  options = options || {};
  var args = [];
  args.push(getValue(options.query, null));
  args.push(getValue(options.leDevice, null));
  exec(successCallback, errorCallback, "PickNetwork", "queryGroup", args);
};
pickNetworkExport.openDoor = function (
  successCallback,
  errorCallback,
  options
) {
  options = options || {};
  var args = [];
  args.push(getValue(options.query, null));
  args.push(getValue(options.leDevice, null));
  exec(successCallback, errorCallback, "PickNetwork", "openDoor", args);
};
pickNetworkExport.getAllConnectedDevices = function (
  successCallback,
  errorCallback
) {
  var args = [];
  exec(
    successCallback,
    errorCallback,
    "PickNetwork",
    "getConnectedDevices",
    args
  );
};

// Export the functions
module.exports = pickNetworkExport;
