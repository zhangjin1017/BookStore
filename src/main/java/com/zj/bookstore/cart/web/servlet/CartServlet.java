package com.zj.bookstore.cart.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zj.bookstore.book.domain.Book;
import com.zj.bookstore.book.service.BookService;
import com.zj.bookstore.cart.domain.Cart;
import com.zj.bookstore.cart.domain.CartItem;
import cn.itcast.servlet.BaseServlet;

public class CartServlet extends BaseServlet {

	/**
	 * 添加购物条目
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
		
		/**
		 * 思路：
		 * 1，先得到购物车对象
		 * 	> 购物车从何而来？
		 * 		* 在用户登录成功的那一刻，就给用户创建一个购物车对象，并保存在session中以供使用
		 * 2，得到条目对象：CartItem
		 * 	> 需要先得到图书和数量
		 * 		* 得到图书
		 * 			> 页面是表单，传递过来的是bid，可调用BookService中的load(String bid)方法查询
		 * 		* 得到数量
		 * 			> 数量可直接从表单中获取
		 * 3，把条目对象保存到购物车中
		 */
		
		/*
		 * 1，得到购物车对象
		 */
		Cart cart = (Cart) request.getSession().getAttribute("cart");
		
		if(cart == null) {
			return "f:/jsps/cart/list.jsp";
		}
		
		/*
		 * 表单传递的只有bid和数量
		 * 2，得到条目
		 * 	> 得到图书和数量
		 * 		* 先得到图书的bid，然后我们需要通过bid查询数据库得到book
		 * 		* 数量表单中有
		 */
		// 得到bid
		String bid = request.getParameter("bid");
		// 通过bid查询数据库得到图书
		Book book = new BookService().load(bid);
		int count = Integer.parseInt(request.getParameter("count"));
		
		// 创建出条目类对象
		CartItem cartItem = new CartItem();
		cartItem.setBook(book);
		cartItem.setCount(count);
		
		/*
		 * 3，把条目对象保存到购物车对象中
		 */
		cart.add(cartItem);
		
		return "f:/jsps/cart/list.jsp";
	}
	
	/**
	 * 清空购物条目
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String clear(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		/**
		 * 1，得到购物车对象
		 * 2，调用购物车对象的clear()
		 */
		
		/*
		 * 得到购物车对象
		 */
		Cart cart = (Cart) request.getSession().getAttribute("cart");
		
		/*
		 * 清空购物车
		 */
		cart.clear();
		
		return "f:/jsps/cart/list.jsp";
	}
	
	/**
	 * 删除购物条目
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String delete(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		
		/**
		 * 1，得到购物车对象
		 * 2，得到bid
		 * 3，调用购物车对象的delete(String bid)方法
		 */
		
		/*
		 * 得到购物车对象
		 */
		Cart cart = (Cart) request.getSession().getAttribute("cart");
		
		/*
		 * 得到bid
		 */
		String bid = request.getParameter("bid");
		
		/*
		 * 删除条目
		 */
		cart.delete(bid);
		
		return "f:/jsps/cart/list.jsp";
	}
}
