package com.zj.bookstore.book.web.servlet.admin;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.ImageIcon;

import com.zj.bookstore.category.service.CategoryService;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.zj.bookstore.book.domain.Book;
import com.zj.bookstore.book.service.BookService;
import com.zj.bookstore.category.domain.Category;
import com.zj.bookstore.category.service.CategoryService;
import cn.itcast.commons.CommonUtils;

/**
 * 负责处理图书管理模块的上传请求
 * 
 * @author GoHome
 *
 */
public class AdminAddBookServlet extends HttpServlet {
	private BookService bookService = new BookService();
	private CategoryService categoryService = new CategoryService();
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		
		try {
			/*
			 * 上传三步：
			 * 	> 创建工厂
			 * 	> 创建解析器
			 * 	> 解析request
			 */
			DiskFileItemFactory factory = new DiskFileItemFactory(30 * 1024, new File("D:/haha/temp"));
			ServletFileUpload sfu = new ServletFileUpload(factory);
			
			/**
			 * 设置限制单个文件大小
			 */
			sfu.setFileSizeMax(30 * 1024); // 不得超过30KB
			
			List<FileItem> fileItemList = sfu.parseRequest(request);
			/*
			 * 把List集合中每个FileItem字段对象中的数据保存到Map集合中
			 * 	> 其中表单项名称做key，表单项的值做value
			 */
			Map<String, String> map = new HashMap<String, String>();
			for(FileItem fileItem : fileItemList) {
				// 如果是普通表单项则存储到map集合中
				if(fileItem.isFormField()) {
					map.put(fileItem.getFieldName(), fileItem.getString("utf-8"));
				}
			}
			
			/*
			 * 把map中的数据分别映射到对象中，并在对象间建立关系
			 */
			// 映射到Book对象中
			Book book = CommonUtils.toBean(map, Book.class);
			// 映射到Category对象中
			Category category = CommonUtils.toBean(map, Category.class);
			// 建立关系：保存category对象到book对象中
			book.setCategory(category);
			// 补全book对象中的bid
			book.setBid(CommonUtils.uuid());
			
			/*
			 * 由于FileItemList中有上传文件是无法直接保存到map中的
			 * 需要另外保存
			 */
			// 得到根目录
			String savePath = this.getServletContext().getRealPath("/book_img");
			// 得到上传文件的名称
			String fileName = fileItemList.get(1).getName();
			
			/**
			 * 校验上传文件后缀
			 */
			if(! fileName.toLowerCase().endsWith("jpg")) {
				request.setAttribute("msg", "您上传的图片不是JPG扩展名！");
				request.setAttribute("categoryList", categoryService.findAll()); // 保存所有分类
				request.getRequestDispatcher("/adminjsps/admin/book/add.jsp")
						.forward(request, response);
				return;
			}
			
			/*
			 * 处理文件名称是绝对路径的问题
			 */
			int index = fileName.lastIndexOf("\\");
			if(index != -1) {
				fileName = fileName.substring(index + 1);
			}
			
			/*
			 * 处理文件名称的同名问题
			 */
			fileName = CommonUtils.uuid() + "_" + fileName;
			// 保存文件的路径到book对象中
			book.setImage("book_img/" + fileName);
			
			// 得到保存文件的File对象
			File destFile = new File(savePath, fileName);
			
			// 保存文件
			fileItemList.get(1).write(destFile);
			
			/**
			 * 校验图片的尺寸
			 * 	> 必须保存到本地才可以校验
			 * 	> 必须先得到图片的Image对象才能得到宽高
			 * 		* Image是抽象类，不想写实现类的话，有另一个选择
			 * 			> 创建一个ImageIcon类的对象：new ImageIcon(String filePath);
			 * 			> 通过该对象的getImage()方法就可以获得图片的Image类对象
			 */
			// 首先需要得到ImageIcon类对象
			ImageIcon imageIcon = new ImageIcon(destFile.getAbsolutePath());
			// 得到对应的Image类对象
			Image image = imageIcon.getImage();
			// 得到宽
			int width = image.getWidth(null);
			// 得到高
			int height = image.getHeight(null);
			/*
			 * 校验
			 * 	> 不论宽高，只要任一超过200像素就保存错误信息并转发到add.jsp
			 */
			if(width > 200 || height > 200) {
				/*
				 * 由于校验需要先保存图片到本地
				 * 但如果不符合要求，那么该图片必须删除！
				 */
				destFile.delete(); // 删除这个文件
				request.setAttribute("msg", "您上传的图片尺寸超出了200 * 200！");
				request.setAttribute("categoryList", categoryService.findAll());
				request.getRequestDispatcher("/adminjsps/admin/book/add.jsp")
						.forward(request, response);
				return;
			}
			
			
			/*
			 * 调用service方法进行添加图书
			 */
			bookService.add(book);
			
			/*
			 * 现在想响应到/adminjsps/admin/book/list.jsp页面
			 * 要求页面有图书信息，但我们这里又不想做一遍查询所有图书的操作
			 * 要如何做？
			 * 答：去调用别的servlet中的方法直接获取到全部图书信息
			 * 	即跨Servlet的请求，用请求转发
			 * 请求转发到AdminBookServlet#findAll()
			 */
			request.getRequestDispatcher("/admin/AdminBookServlet?method=findAll")
					.forward(request, response);
			
		} catch (Exception e) {
			if(e instanceof FileUploadBase.FileSizeLimitExceededException) {
				request.setAttribute("msg", "上传文件大小不得超过30KB！");
				request.setAttribute("categoryList", categoryService.findAll());
				request.getRequestDispatcher("/adminjsps/admin/book/add.jsp")
						.forward(request, response);
			}
		}
	}
	
	
	/*
	// 自己思考做的
	public void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {

		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		
		 * 1，创建解析器工厂
		 * 2，通过解析器工厂得到解析器
		 * 3，解析request得到FileItem集合
		 * 4，循环遍历集合得到每个FileItem对象
		 * 5，判断是不是普通表单项
		 * 	> 如果是，获取到该普通表单项的表单项名称
		 * 		* 接着判断名称是否为bname，pirce，author，cid
		 * 		* 然后获取到对应的值保存到book对象中
		 *  > 如果不是，则表示是附件表单项
		 *  	* 保存附件
		 *  		> 得到保存文件的根目录
		 *  		> 得到附件的名称
		 *  		> 处理文件名称是绝对路径的问题
		 *  		> 处理保存文件同名的问题
		 *  			* uuid
		 *  			* 把得到的文件名称保存到book对象的image属性中
		 *  		> 目录打散
		 *  		> 创建出保存文件路径的File对象
		 *  		> 保存文件
		 * 6，调用bookService的方法进行添加图书操作
		 * 	> 如果抛出异常，保存异常信息、book对象到request域中
		 * 	> 调用categoryService中的findAll方法查询得到categoryList，并保存到request域中
		 * 	> 转发到/adminjsps/admin/book/add.jsp
		 * 7，保存成功信息到request域中，并转发到/adminjsps/admin/book/list.jsp
		 
		// 创建出解析器工厂
		DiskFileItemFactory factory = new DiskFileItemFactory();
		// 得到解析器
		ServletFileUpload sfu = new ServletFileUpload(factory);
		// 解析request
		List<FileItem> fileItemList;
		try {
			fileItemList = sfu.parseRequest(request);
		} catch (FileUploadException e) {
			throw new RuntimeException(e + "：：解析request的时候出错了！！");
		}

		// 创建Book对象用于封装客户端传递过来的参数
		Book book = new Book();
		// 补全bid
		book.setBid(CommonUtils.uuid());
		// 循环遍历FileItemList集合
		for (FileItem fileItem : fileItemList) {
			// 判断该fileItem对象是不是普通表单项
			boolean bool = fileItem.isFormField();
			if (bool) { // 如果是普通表单项
				// 得到当前表单项的名称，即name属性的值
				String name = fileItem.getFieldName();
				if ("bname".equals(name)) { // 如果该普通表单项的名称是bname
					book.setBname(fileItem.getString("utf-8"));
				} else if ("price".equals(name)) { // 如果该普通表单项的名称是pirce
					// 把String类型转为double类型
					double price = Double.parseDouble(fileItem.getString("utf-8"));
					book.setPrice(price);
				} else if ("author".equals(name)) { // 如果该普通表单项的名称是author
					book.setAuthor(fileItem.getString("utf-8"));
				} else if ("cid".equals(name)) { // 如果该普通表单项的名称是cid
					Category category = new Category();
					category.setCid(fileItem.getString("utf-8"));
					book.setCategory(category);
				}

			} else { // 否则就是文件表单项
				// 创建出根目录
				String root = request.getServletContext().getRealPath("/book_img");
				
				 * 处理上传文件名称是绝对路径的问题
				 
				// 得到上传文件的文件名称
				String fileName = fileItem.getName();
				// 获取到最后一个“/”符号的下标索引号
				int index = fileName.lastIndexOf("\\");
				// 如果获取到的“/”符号的下标索引号不等于-1，那么表示存在
				if (index != -1) {
					// 从“/”符号的下一个下标索引号开始获取子串
					fileName = fileName.substring(index + 1);
				}

				
				 * 处理保存文件名称同名的问题
				 
				fileName = CommonUtils.uuid() + "_" + fileName;
				// 顺便把该图片文件在项目下的路径保存到book的image中
				book.setImage("book_img/" + fileName);
				
				
				 * 目录打散之hash打散
				 

				// 得到fileName的hashCode值
				int hCode = fileName.hashCode();
				// 转为16进制值
				String hex = Integer.toHexString(hCode);
				// 得到0下标的字符
				char one = hex.charAt(0);
				// 得到1下标的字符
				char two =hex.charAt(1);
				// 得到打散之后的最终目录的File对象
				File dirFile = new File(root, one + "/" + two);
				// 如果目录不存在则创建
				if(! dirFile.exists()) { 
					dirFile.mkdirs(); 
				} 
				// 得到包括目录和文件名称在内的File对象
				File destFile = new File(dirFile, fileName); 
				// 保存文件
				fileItem.write(destFile);


				
				 * 得到保存文件的目录及文件名称组成的File对象
				 
				File destFile = new File(root, fileName);

				
				 * 保存文件
				 
				try {
					fileItem.write(destFile);
				} catch (Exception e) {
					throw new RuntimeException(e + "：：保存文件时出错了！");
				}
			}
		}
		
		 * 调用BookService方法进行添加图书
		 
		try {
			bookService.add(book);
		} catch (BookException e) {
			request.setAttribute("msg", e.getMessage());
			request.setAttribute("book", book);
			List<Category> categoryList = categoryService.findAll();
			request.setAttribute("categoryList", categoryList);
			request.getRequestDispatcher("/adminjsps/admin/book/add.jsp").forward(request, response);
		}
		
		// 转发到AdminAddServlet页面调用findAll()方法
		request.getRequestDispatcher("/admin/AdminBookServlet?method=findAll").forward(request, response);
	}
	*/
}
