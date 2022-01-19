package com.zj.bookstore.order.service;

import java.util.List;

import com.zj.bookstore.order.dao.OrderDao;
import com.zj.bookstore.order.domain.Order;
import cn.itcast.jdbc.JdbcUtils;

public class OrderService {
	private OrderDao orderDao = new OrderDao();
	
	
	/**
	 * 添加订单
	 * 需要处理事务
	 * @param order
	 */
	public void add(Order order) {
		try {
			// 开启事务
			JdbcUtils.beginTransaction();
			
			/*
			 * 将添加订单和往订单中插入条目两步用事务绑定为一步
			 * 任何一步出问题，都会导致回滚事务，不做修改！
			 */
			orderDao.addOrder(order); // 添加一个订单
			orderDao.addOrderItemList(order.getOrderItemList()); // 插入订单中所有的条目
			
			// 提交事务
			JdbcUtils.commitTransaction();
		} catch (Exception e) {
			// 回滚事务
			JdbcUtils.rollbackTransaction();
			/*
			 * 即使回滚了事务，没做修改，也必须把异常抛出
			 * 否则没抛出异常，以为执行成功了，结果数据没做任何修改就摸不着北了
			 */
			throw new RuntimeException(e);
		}
	}

	/**
	 * 我的订单
	 * @param uid
	 * @return
	 */
	public List<Order> myOrders(String uid) {
		return orderDao.findByUid(uid);
	}

	/**
	 * 加载订单
	 * @param oid
	 * @return
	 */
	public Order load(String oid) {
		return orderDao.load(oid);
	}

	
	/**
	 * 确认收货
	 * @param oid
	 */
	public void confirm(String oid) throws OrderException {
		/*
		 * 1，校验订单状态
		 * 如果不是3，抛出异常
		 */
		int state = orderDao.getStateByOid(oid);
		
		if(state != 3) {
			throw new OrderException("确认收货失败！你不是好人！！！");
		}
		
		state = 4;
		orderDao.updateState(oid, state);
	}
	
	
	/**
	 * 支付方法
	 * @param oid
	 */
	public void zhiFu(String oid) {
		/*
		 * 1，获取订单状态
		 * 	* 如果状态为1，那么执行下面代码
		 * 	* 如果状态不为1，那么本方法什么都不做
		 */
		int state = orderDao.getStateByOid(oid);
		if(state == 1) {
			// 修改订单状态为2
			orderDao.updateState(oid, 2);
		}
	}

	
	/**
	 * 查询所有订单
	 * @return
	 */
	public List<Order> findAllOrder() {
		return orderDao.findAllOrder();
	}

	/**
	 * 按订单状态查询
	 * @param state
	 * @return
	 */
	public List<Order> findByState(int state) {
		return orderDao.findByState(state);
	}

	/**
	 * 发货
	 * @param oid
	 */
	public void send(String oid) {
		int state = 3;
		orderDao.send(oid, state);
	}
}
