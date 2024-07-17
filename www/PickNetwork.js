/**
 * Contains the functions to be exported
 */
let pickNetworkExport = {};

/**
 * Payment Request
 */ 
// ccppExport.sendPaymentRequest = function (successCallback, errorCallback, options) {
//     options = options || {};

//     var args = [];

//     args.push(getValue(options.environment, "sandbox"));
//     args.push(getValue(options.merchantID, null));
//     args.push(getValue(options.paymentToken, null));

//     args.push(getValue(options.paymentType, "cc"));
//     args.push(getValue(options.agentCode, null));
//     args.push(getValue(options.agentChannelCode, null));

//     args.push(getValue(options.creditCardNo, null));
//     args.push(getValue(options.creditCardExpiryMonth, 0));
//     args.push(getValue(options.creditCardExpiryYear, 0));
//     args.push(getValue(options.creditCardSecurityCode, null));
    
//     exec(successCallback, errorCallback, 'CCPP', 'sendPaymentRequest', args);
// };

// Export the functions
module.exports = pickNetworkExport;