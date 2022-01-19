package com.zj.bookstore.user.dao;

import java.sql.SQLException;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import com.zj.bookstore.user.domain.User;
import cn.itcast.jdbc.TxQueryRunner;

/**
 * User持久层
 * @author GoHome
 *
 */
public class UserDao {
	private QueryRunner qr = new TxQueryRunner();

	/**
	 * 根据用户名查询用户
	 * @param username
	 * @return
	 */
	public User findByUsername(String username) {
		try {
			// 给出SQL模板
			String sql = "SELECT * FROM user WHERE username = ?";
			// 给SQL模板的参数赋值
			Object param = username;
			// 调用QueryRunner类对象的query()方法执行查询语句
			User user = qr.query(sql, new BeanHandler<User>(User.class), param);
			// 返回user对象
			return user;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 根据邮箱查询用户
	 * @param email
	 * @return
	 */
	public User findByEmail(String email) {
		try {
			// 给出SQL模板
			String sql = "SELECT * FROM user WHERE email = ?";
			// 给SQL模板的参数赋值
			Object param = email;
			// 执行SQL语句
			User user = qr.query(sql, new BeanHandler<User>(User.class), param);
			// 返回user对象
			return user;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 添加用户
	 * @param user
	 */
	public void add(User user) {
		try {
			// 给出SQL模板：用于添加用户到数据表中
			String sql = "INSERT INTO user VALUES (?,?,?,?,?,?)";
			// 设置form中state属性的值为false，表示未激活账号
			user.setState(false);
			
			// 给SQL模板的参数赋值
			Object[] params = { user.getUid(), user.getUsername(), user.getPassword(), 
					user.getEmail(), user.getCode(), user.isState() };
			
			qr.update(sql, params);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 根据激活码查询用户
	 * @param code
	 * @return
	 */
	public User findByCode(String code) {
		try {
			// 给出SQL模板
			String sql = "SELECT * FROM user WHERE code = ?";
			// 执行SQL语句进行查询
			User user = qr.query(sql, new BeanHandler<User>(User.class), code);
			// 返回user
			return user;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 更新用户的激活状态
	 * @param uid
	 * @param state
	 */
	public void updateState(String uid, boolean state) {
		try {
			// 给出SQL模板
			String sql = "UPDATE user SET state = ? WHERE uid = ?";
			// 给SQL模板的参数赋值
			Object[] params = { state, uid };
			// 执行SQL语句
			qr.update(sql, params);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	
}
