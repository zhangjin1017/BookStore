package com.zj.bookstore.order.dao;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.zj.bookstore.user.domain.User;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.zj.bookstore.book.domain.Book;
import com.zj.bookstore.order.domain.Order;
import com.zj.bookstore.order.domain.OrderItem;
import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;

public class OrderDao {
	private QueryRunner qr = new TxQueryRunner();
	
	/**
	 * 添加订单
	 * @param order
	 */
	public void addOrder(Order order) {
		try {
			// 给出SQL模板
			String sql = "INSERT INTO orders VALUES (?,?,?,?,?,?)";
			/*
			 * 注意：
			 * 	> 其一：order.getOrdertime()得到的时间类型是java.util.Date类
			 * 	而此处是要将数据存储到数据库中，数据库中对应的时间类型为java.sql包下的类（Date、Time、Timestamp）
			 * 	java.util.Date是java.sql包下几个类的父类，即父类转为子类，所以需要处理。
			 * 	> 其二：java.sql包下三个类格式：Date仅有日期、Time仅有时间、Timestamp时间戳：既有日期也有时间
			 * 	由于是下单时间，所以需要详细，即我们选择有日期和时间的类：java.sql.Timestamp类
			 * 	> 即：处理java.util.Date类型，转换成java.sql.Timestamp类型
			 */
			// 得到下单时间
			Date date = order.getOrdertime();
			// 将java.util.Date类型转换成java.sql.Timestamp类型
			Timestamp timestamp = new Timestamp(date.getTime());
			
			// 给SQL模板的参数赋值
			Object[] params = { order.getOid(), timestamp, order.getTotal(),
					order.getState(), order.getOwner().getUid(),
					order.getAddress() };
			// 在数据库中执行SQL语句
			qr.update(sql, params);
			
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 插入订单条目
	 * @param orderItemList
	 */
	public void addOrderItemList(List<OrderItem> orderItemList) {
		/**
		 * QueryRunner类的batch(String sql, Object[][] params)
		 * 其中params是多个一维数组！
		 * 每个一维数组都与sql在一起执行一次，多个一维数组就执行多次！
		 */
		try {
			String sql = "INSERT INTO orderitem VALUES(?,?,?,?,?)";
			/*
			 * 把orderItemList转换成二维数组
			 * 	> 一个OrderItem对象对应转换成一个一维数组
			 * 	> 所以二维数组中一维数组的个数等于List集合中OrderItem对象的个数
			 */
			Object[][] params = new Object[orderItemList.size()][];
			// 循环遍历orderItemList，使用每一个orderItem对象为params中每个一维数组赋值
			for(int i = 0; i < orderItemList.size(); i++) {
				OrderItem item = orderItemList.get(i);
				params[i] = new Object[] { item.getIid(), item.getCount(), 
						item.getSubtotal(), item.getOrder().getOid(),
						item.getBook().getBid() };
			}
			// 执行批处理
			qr.batch(sql, params);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 我的订单：按uid查询订单
	 * @param uid
	 * @return
	 */
	public List<Order> findByUid(String uid) {
		try {
			/*
			 * 思路：
			 * 1，通过uid查询出当前用户的所有Order：List<Order>
			 * 2，循环遍历每个Order，加载得到其每一个OrderItem
			 */
			
			/*
			 * 1，得到当前用户的所有订单
			 */
			String sql = "SELECT * FROM orders WHERE uid = ?";
			List<Order> orderList = qr.query(sql, new BeanListHandler<Order>(Order.class), uid);
			
			/*
			 * 2，循环遍历每个Order，加载得到其每一个OrderItem
			 */
			for(Order order : orderList) {
				loadOrderItems(order); // 为order对象加载其所有订单条目
			}
			
			
			/*
			 * 3，返回订单列表
			 */
			return orderList;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 加载指定订单的所有订单条目
	 * @param order
	 */
	private void loadOrderItems(Order order) {
		try {
			/*
			 * 查询单表，可以查询出指定订单的所有订单条目
			 * 但条目中只有bid，却没有图书信息，而在页面显示必须需要图书信息
			 * 所以此处需要多表查询！
			 * 多表查询的情况下，执行SQL语句为了避免数据丢失，所以用MapListHandler()
			 */
			/*
			 * 多表查询。两张表：orderitem和book
			 */
			String sql = "SELECT * FROM orderitem i, book b WHERE i.bid = b.bid and oid = ?";
			/*
			 * 因为一行结果集对应的不再是一个javabean，所以不能再使用BeanListHandler，而是MapListHandler
			 */
			List<Map<String, Object>> mapList = qr.query(sql, new MapListHandler(), order.getOid());
			/*
			 * mapList是多个map，每个map对应一行结果集！
			 * （此处的一行结果集是orderitem和book两表连接查询得到的结果，既有orderitem也有book的数据）
			 * 所以我们希望使用每一个map（即每一行结果集）来生成两个对象：OrderItem和Book
			 * 然后再来建立两个对象的关系，即把得到的每一个book对象保存到orderItem对象中
			 */
			/*
			 * 循环遍历每个Map，使用每个map生成两个对象，然后建立关系。
			 * （得到的最终结果是一个OrderItem对象）
			 * 然后再把每一个orderItem对象用集合保存起来
			 * 最后把集合保存到order中即可
			 */
			List<OrderItem> orderItemList = toOrderItemList(mapList);
			order.setOrderItemList(orderItemList);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 把mapList中每一个map转换成两个对象，并建立关系。
	 */
	private List<OrderItem> toOrderItemList(List<Map<String, Object>> mapList) {
		List<OrderItem> orderItemList = new ArrayList<OrderItem>();
		for(Map<String, Object> map : mapList) {
			OrderItem item = toOrderItem(map);
			orderItemList.add(item);
		}
		return orderItemList;
	}

	/**
	 * 把一个map转换成一个OrderItem对象
	 */
	private OrderItem toOrderItem(Map<String, Object> map) {
		OrderItem orderItem = CommonUtils.toBean(map, OrderItem.class);
		Book book = CommonUtils.toBean(map, Book.class);
		orderItem.setBook(book);
		return orderItem;
	}

	
	/**
	 * 加载订单
	 * @param oid
	 * @return
	 */
	public Order load(String oid) {
		try {
			/*
			 * 1，得到当前用户的所有订单
			 */
			String sql = "SELECT * FROM orders WHERE oid = ?";
			Order order = qr.query(sql, new BeanHandler<Order>(Order.class), oid);
			/*
			 * 2，为order加载其所有的订单条目
			 */
			loadOrderItems(order);
			/*
			 * 3，返回订单列表
			 */
			return order;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 通过oid查询订单状态
	 * @param oid
	 * @return
	 */
	public int getStateByOid(String oid) {
		try {
			String sql = "SELECT state FROM orders WHERE oid = ?";
			
			// 常规做法
			/*Number num = (Number) qr.query(sql, new ScalarHandler<>(), oid);
			int state = num.intValue();
			return state;*/
			
			/*
			 * 由于查询的数据表中的state列，是真实存在的，而且他的类型为int
			 * 所以可以直接强转为Integer
			 * 如果查询的是：SELECT COUNT(*) FROM orders;
			 * 由于查询出来的结果是我们通过聚合函数得出来的，并不是已有的列
			 * 则没办法直接Integer强转，应该先转为Number，在调用intValue()等方法获得值
			 */
			return (Integer) qr.query(sql, new ScalarHandler<>(), oid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 修改订单状态
	 * @param oid
	 * @param state
	 */
	public void updateState(String oid, int state) {
		try {
			String sql = "UPDATE orders SET state = ? WHERE oid = ?";
			qr.update(sql, state, oid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	
	/**
	 * 查询所有的订单
	 * @return
	 */
	public List<Order> findAllOrder() {
		try {
			String sql = "SELECT * FROM orders";
			/*
			 * 显示订单信息的时候，需要显示user信息，所以还必须要有uid信息
			 * 由于order表中有uid，可以用作查询用户
			 * 为了防止该uid在封装到order对象中时丢失
			 * 所以打算封装到map中
			 */
			List<Map<String, Object>> mapList = qr.query(sql, new MapListHandler());
			
			/*
			 * 把mapList集合转换成orderList
			 */
			List<Order> orderList = toOrderList(mapList);
			
			/*
			 * 由于每个order对象中都必须要有订单条目
			 * 所以还需要给每个order对象加载orderItemList
			 */
			for(Order order : orderList) {
				loadOrderItems(order);
			}
			
			return orderList;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 把mapList集合转为orderList集合
	 * @param mapList
	 * @return
	 */
	private List<Order> toOrderList(List<Map<String, Object>> mapList) {
		// 创建集合用于存储转换出来的每个order
		List<Order> orderList = new ArrayList<Order>();
		// 循环遍历mapList，得到每一个map
		for(Map<String, Object> map : mapList) {
			// 映射到Order对象中
			Order order = CommonUtils.toBean(map, Order.class);
			// 映射到user对象中，该对象仅有uid数据
			User user = CommonUtils.toBean(map, User.class);
			
			// 用uid查询出完整的user
			user = findUserByUid(user.getUid());
			// 把user保存到每个order对象中
			order.setOwner(user);
			
			// 把每个order对象保存orderList集合中
			orderList.add(order);
		}
		return orderList;
	}

	/**
	 * 按uid查询user
	 * @param uid
	 * @return
	 */
	private User findUserByUid(String uid) {
		try {
			String sql = "SELECT * FROM user WHERE uid = ?";
			return qr.query(sql, new BeanHandler<User>(User.class), uid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	
	/**
	 * 按订单状态查询
	 * @param state
	 * @return
	 */
	public List<Order> findByState(int state) {
		try {
			String sql = "SELECT * FROM orders WHERE state = ?";
			// 执行SQL模板得到多条记录，每条记录都是一个map
			List<Map<String, Object>> mapList = qr.query(sql, new MapListHandler(), state);
			// 把mapList转换成orderList
			List<Order> orderList = toOrderList(mapList);
			/*
			 * 循环遍历orderList
			 * 为每个order对象加载其订单条目集合
			 */
			for(Order order : orderList) {
				loadOrderItems(order);
			}
			
			return orderList;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 发货
	 * @param oid
	 */
	public void send(String oid, int state) {
		try {
			String sql = "UPDATE orders SET state = ? WHERE oid = ?";
			qr.update(sql, state, oid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
