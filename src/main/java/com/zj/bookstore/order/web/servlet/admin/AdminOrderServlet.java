package com.zj.bookstore.order.web.servlet.admin;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zj.bookstore.order.domain.Order;
import com.zj.bookstore.order.service.OrderService;
import cn.itcast.servlet.BaseServlet;

public class AdminOrderServlet extends BaseServlet {
	private OrderService orderService = new OrderService();

	/**
	 * 查询全部订单
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findAllOrder(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		/*
		 * 1，调用service方法得到全部的订单
		 * 2，保存订单到request域中
		 * 3，转发到list.jsp
		 */
		List<Order> orderList = orderService.findAllOrder();
		
		request.setAttribute("orderList", orderList);
		
		return "f:/adminjsps/admin/order/list.jsp";
	}
	
	
	/**
	 * 按订单状态查询订单
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findByState(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		/*
		 * 1，得到state参数
		 * 2，调用service方法传递state参数过去查询得到订单
		 * 3，把订单集合存储到request域中
		 * 4，转发到list.jsp
		 */
		int state = Integer.parseInt(request.getParameter("state"));
		
		List<Order> orderList = orderService.findByState(state);
		
		request.setAttribute("orderList", orderList);
		
		return "f:/adminjsps/admin/order/list.jsp";
	}
	
	
	/**
	 * 发货
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String send(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		/*
		 * 1，得到oid参数
		 * 2，调用service方法完成确认发货功能
		 * 3，调用findAllOrder()完成转发到list.jsp
		 */
		String oid = request.getParameter("oid");
		
		orderService.send(oid);
		
		return findAllOrder(request, response);
	}
	
}
