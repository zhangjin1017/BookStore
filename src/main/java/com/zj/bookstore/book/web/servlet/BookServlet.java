package com.zj.bookstore.book.web.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zj.bookstore.book.domain.Book;
import com.zj.bookstore.book.service.BookService;
import cn.itcast.servlet.BaseServlet;

public class BookServlet extends BaseServlet {
	private BookService bookService = new BookService();
	
	/**
	 * 查询所有图书
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findAll(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		List<Book> bookList = bookService.findAll();
		request.setAttribute("bookList", bookList);
		
		return "f:/jsps/book/list.jsp";
		
	}
	
	/**
	 * 按分类查询图书
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String findByCategory(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		String cid= request.getParameter("cid");
		List<Book> bookList = bookService.findByCategory(cid);
		request.setAttribute("bookList", bookList);
		
		return "f:/jsps/book/list.jsp";
	}
	
	
	/**
	 * 加载图书
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
		
		String bid = request.getParameter("bid");
		
		Book book = bookService.load(bid);
		
		request.setAttribute("book", book);
		
		return "f:/jsps/book/desc.jsp";
	}
}
