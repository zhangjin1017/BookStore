package com.zj.bookstore.user.service;

import com.zj.bookstore.user.dao.UserDao;
import com.zj.bookstore.user.domain.User;

public class UserService {
	private UserDao userDao = new UserDao();
	
	/**
	 * 注册用户
	 * @param form
	 */
	public void regist(User form) throws UserException {
		/*
		 * 检查用户名是否已经被注册
		 */
		User user = userDao.findByUsername(form.getUsername());
		if(user != null) {
			throw new UserException("用户名：" + form.getUsername() + "，已被注册！");
		}
		
		/*
		 * 检查邮箱是否已被注册
		 */
		user = userDao.findByEmail(form.getEmail());
		if(user != null) {
			throw new UserException("邮箱：" + form.getEmail() + "，已被注册！");
		}
		
		/*
		 * 添加用户
		 */
		userDao.add(form);
	}

	/**
	 * 账号激活
	 * @param code
	 * @return
	 */
	public void active(String code) throws UserException {
		/*
		 * 1，调用DAO层的方法：按激活码查询用户
		 */
		User user = userDao.findByCode(code);
		
		/*
		 * 2，如果user不存在，说明激活错误
		 */
		if(user == null) {
			throw new UserException("激活码无效！");
		}
		
		/*
		 * 3，用户存在的话，判断该用户的激活状态是否为true
		 */
		if(user.isState()) { // 如果为true，则抛出异常：用户已经激活过了
			throw new UserException("您已经激活过了，不要再激活了！");
		}
		
		/*
		 * 4，更新用户激活状态
		 */
		// 得到按code查询出来的user对象的uid
		String uid = user.getUid();
		// 定义激活状态
		boolean state = true;
		// 调用DAO中的方法进行激活
		userDao.updateState(uid, state);
	}

	/**
	 * 登录功能
	 * @param form
	 * @return
	 */
	public User login(User form) throws UserException {
		/**
		 * 1，使用username查询，得到User
		 * 2，如果user为null，抛出异常：用户名不存在
		 * 3，如果没抛出异常，比较form和user的密码，如果不同，抛出异常：密码错误
		 * 4，如果密码相同，查看用户的状态，若为false，表示未激活，抛出异常：账号未激活
		 * 5，成功执行到这一步，说明用户名存在，密码正确且账号已激活，则返回查询出来的user
		 */
		// 通过用户名进行查找
		User user = userDao.findByUsername(form.getUsername());
		// 如果获得的User对象为null，则表示用户不存在，抛出异常
		if(user == null) {
			throw new UserException("用户名：" + form.getUsername() + "，不存在！");
		}
		
		// 如果没抛出异常，说明用户存在，校对密码是否一致
		if(! user.getPassword().equals(form.getPassword())) {
			throw new UserException("你输入的密码错误！");
		}
		
		// 如果密码一致，判断状态是否激活，如果未激活，抛出异常
		if(! user.isState()) {
			throw new UserException("账号未激活！请先激活再尝试登录！");
		}
		
		return user;
	}
}
