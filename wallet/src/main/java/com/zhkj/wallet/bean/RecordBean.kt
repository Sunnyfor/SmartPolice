package com.zhkj.wallet.bean

//钱包流水
data class RecordBean(
    var id: String,
    var isRecharge: String?,
    var type: String,
    var buyerLogonId: String,
    var buyerUserId: String,
    var wxOpenid: String,
    var param: String,
    var notifyUrlParam: String,
    var wechatPrePaymentOrderParam: String,
    var amount: String,
    var orderId: String,
    var tradeNo: String,
    var body: String,
    var refundAmount: String,
    var refundReason: String,
    var status: String,
    var createUserId: String,
    var createUserName: String,
    var createTime: String,
    var updateTime: String,
    var paySuccessTime: String,
    var payCloseTime: String,
    var payRevokeTime: String,
    var payRefundTime: String,
    var appid: String
)
//{
//    "id":60,
//    "isRecharge":1,
//    "type":1,
//    "buyerLogonId":"kyf***@sandbox.com",
//    "buyerUserId":"2088102180989794",
//    "wxOpenid":null,
//    "param":"",
//    "notifyUrlParam":"",
//    "wechatPrePaymentOrderParam":null,
//    "amount":"500",
//    "orderId":"1591785775000155",
//    "tradeNo":null,
//    "body":"11",
//    "refundAmount":null,
//    "refundReason":null,
//    "status":1,
//    "createUserId":1,
//    "createUserName":"admin",
//    "createTime":"2020-06-10 10:42:56",
//    "updateTime":"2020-06-10 10:45:04",
//    "paySuccessTime":"2020-06-10 10:43:41",
//    "payCloseTime":null,
//    "payRevokeTime":null,
//    "payRefundTime":null,
//    "appid":"2016102300743439"
//}