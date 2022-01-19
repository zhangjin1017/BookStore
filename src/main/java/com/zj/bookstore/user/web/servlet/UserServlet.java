package com.zj.bookstore.user.web.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import javax.mail.Session;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zj.bookstore.cart.domain.Cart;
import com.zj.bookstore.user.domain.User;
import com.zj.bookstore.user.service.UserException;
import com.zj.bookstore.user.service.UserService;
import cn.itcast.commons.CommonUtils;
import cn.itcast.mail.Mail;
import cn.itcast.mail.MailUtils;
import cn.itcast.servlet.BaseServlet;

/**
 * User表述层
 * @author GoHome
 *
 */
public class UserServlet extends BaseServlet {
	private UserService userService = new UserService();
	
	/**
	 * 注册功能
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	public String register(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// 处理请求编码和响应编码问题
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		
		/**
		 * 1，封装表单数据到form对象中
		 * 2，补全：uid、code
		 * 3，输入校验
		 * 	> 保存错误信息、form到request域中，转发到regist.jsp
		 * 4，调用service方法完成注册
		 * 	> 保存错误信息、form到request域中，转发到regist.jsp
		 * 5，发邮件
		 * 6，保存成功信息转发到msg.jsp
		 */
		/**
		 * 1，封装表单数据到User对象form中
		 */
		// 封装表单数据到User对象form中
		User form = CommonUtils.toBean(request.getParameterMap(), User.class);

		/**
		 * 2，补全：uid，code
		 */
		// 补全uid
		form.setUid(CommonUtils.uuid());
		// 补全激活码
		form.setCode(CommonUtils.uuid() + CommonUtils.uuid());
		
		/**
		 * 3，先校验用户名、密码、邮箱长度及格式是否符合要求
		 * 如果不符合要求，保存错误信息和表单数据到request域中并转发到regist.jsp
		 * 如果匹配，则进入下一步：注册
		 */
		Map<String, String> errors = new HashMap<String, String>();
		/*
		 * 校验用户名
		 */
		String username = form.getUsername();
		if(username == null || username.trim().isEmpty()) {
			errors.put("username", "用户名不能为空！");
		} else if(! username.matches("[a-zA-Z][a-zA-Z0-9]{5,15}")) { // 正则校验
			errors.put("username", "用户名格式不对！应该首字母开头、且长度为6~16位");
		}
		
		/*
		 * 校验密码
		 */
		String password = form.getPassword();
		if(password == null || password.trim().isEmpty()) {
			errors.put("password", "密码不能为空！");
		} else if(! password.matches("\\w{3,10}")) { // 正则校验
			errors.put("password", "密码长度必须在3~10之间！");
		}
		
		/*
		 * 校验邮箱
		 */
		String email = form.getEmail();
		if(email == null || email.trim().isEmpty()) {
			errors.put("email", "邮箱不能为空！");
		} else if(! email.matches("[a-zA-Z0-9_]+@[a-zA-Z0-9]+(\\.[a-zA-Z]{1,3})+")) { // 正则校验
			errors.put("email", "邮箱格式不对！应该为xxx@xxx.xxx");
		}
		
		/*
		 * 判断是否存在错误信息
		 */
		if(errors.size() > 0) {
			/**
			 * 1，保存错误信息
			 * 2，保存表单数据
			 * 3，转发到regist.jsp
			 */
			request.setAttribute("errors", errors);
			request.setAttribute("user", form);
			return "f:/jsps/user/regist.jsp";
		}

		
		/**
		 * 4，调用UserService中的regist(User form)方法进行注册
		 * 	> 如果抛出异常，保存异常信息和表单数据到request域中并转发到regist.jsp
		 * 	> 如果没有抛出异常，则说明注册成功，进入下一步：发送激活邮件
		 */
		try {
			userService.regist(form);
		} catch (UserException e) {
			// 保存异常信息到request域中
			request.setAttribute("msg", e.getMessage());
			// 保存封装有表单数据的User类对象form到request域中
			request.setAttribute("user", form);
			// 转发到regist.jsp
			return "f:/jsps/user/regist.jsp";
		}
		
		/**
		 * 5，发送激活邮件
		 * 	> 准备配置文件！
		 * 	> 从配置文件中读取出邮件服务器的参数信息
		 * 	    发件人、邮件标题、邮件正文等
		 */
		/*
		 * 获取邮件服务器的配置信息
		 */
		// 获取到配置文件的资源输入流
		InputStream input = this.getClass().getResourceAsStream("/email_template.properties");
		// 创建Properties集合用于存储配置文件信息
		Properties props = new Properties();
		// 把配置文件中的信息加载到props集合中
		props.load(new InputStreamReader(Objects.requireNonNull(MailUtils.class.getClassLoader().
				getResourceAsStream("/email_template.properties")), StandardCharsets.UTF_8));
		
		// 获取到邮件服务器的主机名
		String host = props.getProperty("host");
		// 获取到用户名
		String uname = props.getProperty("uname");
		// 获取到密码
		String pwd = props.getProperty("pwd");
		// 获取发件人
		String from = props.getProperty("from");
		// 获取收件人
		String to = form.getEmail();
		// 获取到邮件标题
		String subject = props.getProperty("subject");
		// 获取到邮件正文
		String content = props.getProperty("content");
		// 将content中的占位符替换，即把激活码添加到content中
		content = MessageFormat.format(content, form.getCode()); // 替换{0}，即第一个占位符
		
		/*
		 * 利用MailUtils工具类来便捷发送邮件
		 */
		// 得到Session类对象
		Session session = MailUtils.createSession(host, uname, pwd);

		// 创建Mail对象
		Mail mail = new Mail(from, to, subject, content);

		// 发送邮件
		MailUtils.send(session, mail);

		/**
		 * 6，保存注册成功信息到request域中并转发到msg.jsp
		 */
		request.setAttribute("msg", "恭喜，注册成功！请马上到邮箱激活！");
		return "f:/jsps/msg.jsp";
	}
	
	
	/**
	 * 激活功能
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String active(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		// 得到code请求参数：激活码
		String code = request.getParameter("code");
		try {
			// 调用Service层方法激活
			userService.active(code);
		} catch (UserException e) {
			// 保存异常信息到request域中
			request.setAttribute("msg", e.getMessage());
			// 转发到msg.jsp
			return "f:/jsps/msg.jsp";
		}
		
		request.setAttribute("msg", "恭喜，激活成功！");
		return "f:/jsps/msg.jsp";
	}
	
	
	/**
	 * 登录功能
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String login(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		
		// 封装表单数据到User对象中
		User form = CommonUtils.toBean(request.getParameterMap(), User.class);
		
		Map<String, String> errors = new HashMap<String, String>();
		
		// 校验用户名和密码是否为空
		String username = form.getUsername();
		if(username == null || username.trim().isEmpty()) {
			errors.put("username", "用户名不能为空！");
		}
		String password = form.getPassword();
		if(password == null || password.trim().isEmpty()) {
			errors.put("password", "密码不能为空！");
		}
		
		// 判断集合是否为空，不为空则表示有错误信息
		if(errors.size() > 0) {
			request.setAttribute("errors", errors);
			request.setAttribute("user", form);
			return "f:/jsps/user/login.jsp";
		}
		
		User user;
		
		try {
			// 调用Service层的login()方法判断用户是否存在或密码是否正确
			user = userService.login(form);
		} catch (UserException e) {
			// 保存异常信息到request域中
			request.setAttribute("msg", e.getMessage());
			// 把表单数据保存到request域中
			request.setAttribute("user", form);
			// 转发到login.jsp
			return "f:/jsps/user/login.jsp";
		}
		
		// 登录成功，把user保存当Session域中
		request.getSession().setAttribute("user", user);
		
		/*
		 * 登录成功之后，给用户添加一个购物车Cart类对象，并把该购物车保存到session域中
		 */
		request.getSession().setAttribute("cart", new Cart());
		
		if(user.getUsername().equalsIgnoreCase("Administrator")) {
			return "f:/adminjsps/admin/index.jsp";
		}
		
		// 转发到index.jsp
		return "r:/index.jsp";
	}
	
	/**
	 * 退出功能
	 * @param request
	 * @param response
	 * @return
	 * @throws ServletException
	 * @throws IOException
	 */
	public String quit(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		// 废止session
		request.getSession().invalidate();
		return "r:/index.jsp";
	}
	
}
