package com.zj.bookstore.order.web.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zj.bookstore.cart.domain.Cart;
import com.zj.bookstore.cart.domain.CartItem;
import com.zj.bookstore.order.domain.Order;
import com.zj.bookstore.order.domain.OrderItem;
import com.zj.bookstore.order.service.OrderException;
import com.zj.bookstore.order.service.OrderService;
import com.zj.bookstore.user.domain.User;
import cn.itcast.commons.CommonUtils;
import cn.itcast.payment.PaymentUtil;
import cn.itcast.servlet.BaseServlet;

public class OrderServlet extends BaseServlet {
	private OrderService orderService = new OrderService();
	
	/**
	 * 添加订单
	 * 把session中的cart用来生成order对象
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String add(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		
		/*
		 * 1，从session中得到cart
		 * 2，使用cart生成Order对象，使用cartItem对象生成orderItem对象
		 * 	> cart与order对应
		 * 	> cartItem与orderItem对应
		 * 3，调用service方法完成添加订单
		 * 4，保存order到request域中，转发到/jsps/order/desc.jsp
		 * 5，清空购物车
		 */
		// 1，从session中获取cart
		Cart cart = (Cart) request.getSession().getAttribute("cart");
		
		
		// 2，使用cart生成Order对象，使用cartItem对象生成orderItem对象
		/*
		 * 创建Order对象，并设置属性
		 * 	> cart与order对应
		 */
		Order order = new Order();
		order.setOid(CommonUtils.uuid()); // 设置订单编号
		order.setOrdertime(new Date()); // 设置下单时间
		order.setTotal(cart.getTotal()); // 设置订单的合计，从cart中获取合计
		order.setState(1); // 设置订单状态为1，表示未付款
		User user = (User) request.getSession().getAttribute("user");
		order.setOwner(user); // 设置订单所有者
		
		/*
		 * 创建订单条目集合
		 * 	> cartItemList与orderItemList对应
		 * 		* 即先获得cart中所存储的cartItem对象的集合cartItemList
		 * 		* 再创建一个用于存储OrderItem对象的集合orderItemList
		 * 		* 循环遍历cartItemList集合
		 * 			> 得到每一个cartItem对象，对应每一个orderItem对象，设置属性
		 * 			> 把每一个orderItem对象保存到orderItemList集合中
		 */
		// 得到cart对象中的cartItemList集合
		Collection<CartItem> cartItemList = cart.getCartItems();
		
		// 创建OrderItem集合
		List<OrderItem> orderItemList = new ArrayList<OrderItem>();
		
		// 循环遍历cartItemList集合
		for(CartItem cartItem : cartItemList) {
			// 创建一个OrderItem对象
			OrderItem orderItem = new OrderItem();
			// 设置订单条目编号
			orderItem.setIid(CommonUtils.uuid());
			// 设置订单条目中商品的数量
			orderItem.setCount(cartItem.getCount());
			// 设置订单条目中商品的小计
			orderItem.setSubtotal(cartItem.getSubtotal());
			// 设置订单条目所属的订单
			orderItem.setOrder(order);
			// 设置订单条目所购买的图书
			orderItem.setBook(cartItem.getBook());
			
			// 把orderItem对象保存到orderItemList集合中
			orderItemList.add(orderItem);
		}
		
		// 把订单条目集合保存到订单对象中
		order.setOrderItemList(orderItemList);
		
		
		// 3，调用orderService中的方法完成添加订单
		orderService.add(order);
		
		// 4，保存order到request域中，转发到/jsps/order/desc.jsp
		request.setAttribute("order", order);
		
		// 5，清空购物车
		cart.clear();
		
		return "f:/jsps/order/desc.jsp";
	}
	
	
	/**
	 * 我的订单
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String myOrders(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		/*
		 * 1，从session中得到当前用户user，再获取其uid
		 * 2，使用uid调用orderService#myOrders(uid)得到该用户的所有订单List<Order>
		 * 3，把订单列表保存到request域中，转发到/jsps/order/list.jsp
		 */
		User user = (User) request.getSession().getAttribute("user");
		
		List<Order> orderList = orderService.myOrders(user.getUid());
		
		request.setAttribute("orderList", orderList);
		
		return "f:/jsps/order/list.jsp";
	}
	
	/**
	 * 加载订单
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String load(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		
		/*
		 * 1，得到oid参数
		 * 2，使用oid调用service方法得到Order对象
		 * 3，保存order到request域中并转发到/jsps/order/desc.jsp
		 */
		String oid = request.getParameter("oid");
		
		Order order = orderService.load(oid);
		
		request.setAttribute("order", order);
		
		return "f:/jsps/order/desc.jsp";
	}
	
	
	/**
	 * 确认收货
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String confirm(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		/*
		 * 1，获取oid参数
		 * 2，调用service方法
		 * 	> 如果抛出异常，保存异常信息，转发到msg.jsp
		 * 3，保存成功信息，转发到msg.jsp
		 */
		String oid = request.getParameter("oid");
		
		try {
			orderService.confirm(oid);
		} catch (OrderException e) {
			request.setAttribute("msg", e.getMessage());
			return "f:/jsps/msg.jsp";
		}
		
		request.setAttribute("msg", "确认收货成功，交易完成！");
		
		return "f:/jsps/msg.jsp";
	}
	
	
	/**
	 * 支付之去银行
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String zhiFu(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		/*
		 * 读取配置文件中的测试账号和value
		 */
		Properties props = new Properties();
		InputStream input = this.getClass().getResourceAsStream("/merchantInfo.properties");
		props.load(input);
		
		/*
		 * 准备13参数
		 */
		String p0_Cmd = "Buy";
		String p1_MerId = props.getProperty("p1_MerId");
		String p2_Order = request.getParameter("oid");
		String p3_Amt = "0.01";
		String p4_Cur = "CNY";
		String p5_Pid = "";
		String p6_Pcat = "";
		String p7_Pdesc = "";
		String p8_Url = props.getProperty("p8_Url");
		String p9_SAF = "";
		String pa_MP = "";
		String pd_FrpId = request.getParameter("pd_FrpId");
		String pr_NeedResponse = "1";
		
		/*
		 * 计算hmac
		 */
		String keyValue = props.getProperty("keyValue");
		String hmac = PaymentUtil.buildHmac(p0_Cmd, p1_MerId, p2_Order, p3_Amt, 
				p4_Cur, p5_Pid, p6_Pcat, p7_Pdesc, p8_Url, p9_SAF, pa_MP, pd_FrpId, 
				pr_NeedResponse, keyValue);
		
		/*
		 * 把易宝的网址和13+1个参数连接成一个url
		 */
		StringBuilder url = new StringBuilder(props.getProperty("url"));
		url.append("?p0_Cmd=").append(p0_Cmd);
		url.append("&p1_MerId=").append(p1_MerId);
		url.append("&p2_Order=").append(p2_Order);
		url.append("&p3_Amt=").append(p3_Amt);
		url.append("&p4_Cur=").append(p4_Cur);
		url.append("&p5_Pid=").append(p5_Pid);
		url.append("&p6_Pcat=").append(p6_Pcat);
		url.append("&p7_Pdesc=").append(p7_Pdesc);
		url.append("&p8_Url=").append(p8_Url);
		url.append("&p9_SAF=").append(p9_SAF);
		url.append("&pa_MP=").append(pa_MP);
		url.append("&pd_FrpId=").append(pd_FrpId);
		url.append("&pr_NeedResponse=").append(pr_NeedResponse);
		url.append("&hmac=").append(hmac);
		
		System.out.println(url);
		/*
		 * 重定向
		 */
		response.sendRedirect(url.toString());
		
		return null;
	}
	
	
	/**
	 * 支付之银行回馈
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String back(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		/*
		 * 1，获取11+1个参数
		 */
		String p1_MerId = request.getParameter("p1_MerId");
		String r0_Cmd = request.getParameter("r0_Cmd");
		String r1_Code = request.getParameter("r1_Code");
		String r2_TrxId = request.getParameter("r2_TrxId");
		String r3_Amt = request.getParameter("r3_Amt");
		String r4_Cur = request.getParameter("r4_Cur");
		String r5_Pid = request.getParameter("r5_Pid");
		String r6_Order = request.getParameter("r6_Order");
		String r7_Uid = request.getParameter("r7_Uid");
		String r8_MP = request.getParameter("r8_MP");
		String r9_BType = request.getParameter("r9_BType");
		
		String hmac = request.getParameter("hmac");
		
		/*
		 * 2，校验访问者是不是易宝！
		 */
		Properties props = new Properties();
		InputStream input = this.getClass().getResourceAsStream("/merchantInfo.properties");
		props.load(input);
		
		String keyValue = props.getProperty("keyValue");
		
		boolean bool = PaymentUtil.verifyCallback(hmac, p1_MerId, r0_Cmd, r1_Code, 
				r2_TrxId, r3_Amt, r4_Cur, r5_Pid, r6_Order, r7_Uid, r8_MP, r9_BType, 
				keyValue);
		
		if(!bool) { // 如果校验之后两个hmac不一致，返回的是false，则不继续往下！
			request.setAttribute("msg", "您不是什么好东西！");
			return "f:/jsps/msg.jsp";
		}
		
		/*
		 * 3，获取订单状态，确定是否要修改订单状态，以及添加积分等业务操作。
		 * 切记业务需要在service中操作
		 */
		orderService.zhiFu(r6_Order); // 该方法可能对数据库进行操作，也可能不操作！
		
		/*
		 * 4，判断当前回调方式
		 * 	* 如果为点对点方式，那么需要回馈SUCCESS开头的字符串
		 */
		if(r9_BType.equals("2")) {
			response.getWriter().print("SUCCESS");
		}
		
		/*
		 * 5，保存成功信息转发到msg.jsp
		 */
		request.setAttribute("msg", "支付成功！等待卖家发货！");
		
		return "f:/jsps/msg.jsp";
	}
}
