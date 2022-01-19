package com.zj.bookstore.order.utils;

public class PaymentGateway {
	/**
	 * 得到支付网关及13+1个参数组成的支付链接
	 * @param order 订单编号
	 * @param amount 支付金额
	 * @param url 回调地址
	 * @param channel 支付通道
	 * @return
	 */
	public static String getUrl(String order, String amount, String url, String channel) {
		// 用于存储支付网关及13+1个参数组成的链接字符串
		StringBuilder sb = new StringBuilder();
		
		// 得到hmac
		String hmac = PaymentUtil.buildHmac("Buy", "10001126856", order, 
				amount, "CNY", "", "", "", url, "", "", channel, "1", 
				"69cl522AV6q613Ii4W6u8K6XuW8vM1N6bFgyv769220IuYe9u37N4y7rI4Pl");
		
		// 支付网关
		sb.append("https://www.yeepay.com/app-merchant-proxy/node?");
		sb.append("p0_Cmd=Buy&");
		sb.append("p1_MerId=10001126856&");
		sb.append("p2_Order=" + order + "&");
		sb.append("p3_Amt=" + amount + "&");
		sb.append("p4_Cur=CNY&");
		sb.append("p5_Pid=&");
		sb.append("p6_Pcat=&");
		sb.append("p7_Pdesc=&");
		sb.append("p8_Url=" + url + "&");
		sb.append("p9_SAF=&");
		sb.append("pa_MP=&");
		sb.append("pd_FrpId=" + channel + "&");
		sb.append("pr_NeedResponse=1&");
		sb.append("hmac=" + hmac);
		
		return sb.toString();
	}
	
}
