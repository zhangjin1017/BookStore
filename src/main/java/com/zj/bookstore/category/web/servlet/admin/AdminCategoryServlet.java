package com.zj.bookstore.category.web.servlet.admin;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zj.bookstore.category.domain.Category;
import com.zj.bookstore.category.service.CategoryException;
import com.zj.bookstore.category.service.CategoryService;
import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;

public class AdminCategoryServlet extends BaseServlet {
	private CategoryService categoryService = new CategoryService();
	
	/**
	 * 查询所有分类
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
		
		List<Category> categoryList = categoryService.findAll();
		
		request.setAttribute("categoryList", categoryList);
		
		return "f:/adminjsps/admin/category/list.jsp";
	}
	
	
	/**
	 * 添加分类
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
		 * 1，封装表单数据
		 * 2，校验cname是否为空
		 * 	> 如果为空，抛出异常
		 * 3，补全：cid
		 * 4，调用service方法完成添加工作
		 * 5，调用fingAll()
		 */
		// 封装表单数据
		Category category = CommonUtils.toBean(request.getParameterMap(), Category.class);
		// 校验cname是否为空
		if(category.getCname() == null || category.getCname().trim().isEmpty()) {
			request.setAttribute("msg", "分类名称不能为空！");
			request.setAttribute("category", category);
			return "f:/adminjsps/admin/category/add.jsp";
		}
		// 补全cid
		category.setCid(CommonUtils.uuid());
		
		/*
		 * 调用service方法进行添加分类操作
		 * 	> 如果抛出异常，保存异常信息和category表单对象到request域中
		 * 	> 转发到add.jsp
		 */
		try {
			categoryService.add(category);
		} catch (CategoryException e) {
			request.setAttribute("msg", e.getMessage());
			request.setAttribute("category", category);
			return "f:/adminjsps/admin/category/add.jsp";
		}
		
		/*
		 * 调用findAll方法
		 * 	> 主要是为了给request中添加新查询出来的category对象的集合
		 * 	> 顺便得到转发到list.jsp页面的值
		 */
		String path = findAll(request, response);
		
		return path;
	}
	
	
	/**
	 * 删除分类
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
		/*
		 * 1，得到参数cid
		 * 2，调用service方法完成删除
		 * 	> 如果抛出异常，保存异常信息到request域并转发回msg.jsp显示
		 * 3，调用findAll()方法（最后会转发到/adminjsps/admin/category/list.jsp）
		 */
		String cid = request.getParameter("cid");
		
		try {
			categoryService.delete(cid);
		} catch (CategoryException e) {
			request.setAttribute("msg", e.getMessage());
			return "f:/adminjsps/msg.jsp";
		}
		
		String path = findAll(request, response);
		
		return path;
	}
	
	
	/**
	 * 编辑之前的查询
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String editPre(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		/*
		 * 1，获得cid参数
		 * 2，调用service方法得到一个Category对象
		 * 3，保存category对象到request域中并转发到mod.jsp
		 */
		String cid = request.getParameter("cid");
		
		Category category = categoryService.load(cid);
		
		request.setAttribute("category", category);
		
		return "f:/adminjsps/admin/category/mod.jsp";
	}
	
	
	/**
	 * 编辑分类
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
		/*
		 * 1，封装表单数据到Category对象中
		 * 2，校验cname是否为空
		 * 	> 如果为空，抛出异常：分类名称不能为空！
		 * 3，使用category对象调用service方法进行修改分类
		 * 	> 如果抛出异常，保存异常信息和category对象到request域中并转发到mod.jsp
		 * 4，调用findAll方法
		 * 	> 主要是通过该方法把新查询出来的category对象集合保存到request域中
		 * 	> 再得到返回值：转发到list.jsp
		 */
		Category category = CommonUtils.toBean(request.getParameterMap(), Category.class);
		
		if(category.getCname() == null || category.getCname().trim().isEmpty()) {
			request.setAttribute("msg", "分类名称不能为空！！");
			request.setAttribute("category", category);
			return "f:/adminjsps/admin/category/mod.jsp";
		}
		
		/*
		 * 调用service方法进行修改分类
		 * 	> 如果抛出异常，保存异常信息和category对象到request域中并转发到mod.jsp
		 */
		try {
			categoryService.edit(category);
		} catch (CategoryException e) {
			request.setAttribute("msg", e.getMessage());
			request.setAttribute("category", category);
			return "f:/adminjsps/admin/category/mod.jsp";
		}
		
		String path = findAll(request, response);
		
		return path;
	}
}
