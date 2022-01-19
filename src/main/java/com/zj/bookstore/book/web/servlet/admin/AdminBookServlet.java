package com.zj.bookstore.book.web.servlet.admin;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zj.bookstore.book.domain.Book;
import com.zj.bookstore.book.service.BookService;
import com.zj.bookstore.category.domain.Category;
import com.zj.bookstore.category.service.CategoryService;
import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;

public class AdminBookServlet extends BaseServlet {
	private BookService bookService = new BookService();
	private CategoryService categoryService = new CategoryService();
	
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
		
		return "f:/adminjsps/admin/book/list.jsp";
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
		/*
		 * 1，获取参数bid，通过bid调用service方法得到Book对象，把book对象保存到request域中
		 * 2，获取到所有分类，保存到request域中
		 * 3，转发到/adminjsps/admin/book/desc.jsp
		 */
		Book book = bookService.load(request.getParameter("bid"));
		request.setAttribute("book", book);
		
		List<Category> categoryList = categoryService.findAll();
		request.setAttribute("categoryList", categoryList);
		
		return "f:/adminjsps/admin/book/desc.jsp";
		
	}
	
	/**
	 * 添加图书之前的查询
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String addPre(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		
		List<Category> categoryList = categoryService.findAll();
		
		request.setAttribute("categoryList", categoryList);
		
		return "f:/adminjsps/admin/book/add.jsp";
	}

	/**
	 * 删除图书：假删除
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
		
		String bid = request.getParameter("bid");
		bookService.delete(bid);
		
		String path = findAll(request, response);
		
		return path;
	}
	
	
	/**
	 * 编辑图书
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String edit(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		
		Book book = CommonUtils.toBean(request.getParameterMap(), Book.class);
		Category category = CommonUtils.toBean(request.getParameterMap(), Category.class);
		book.setCategory(category);
		
		bookService.edit(book);
		
		String path = findAll(request, response);
		
		return path;
	}
}
