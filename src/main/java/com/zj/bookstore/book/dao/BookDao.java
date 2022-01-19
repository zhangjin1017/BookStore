package com.zj.bookstore.book.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.zj.bookstore.book.domain.Book;
import com.zj.bookstore.category.domain.Category;
import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;

public class BookDao {
	private QueryRunner qr = new TxQueryRunner();

	/**
	 * 查询所有图书
	 * @return
	 */
	public List<Book> findAll() {
		try {
			String sql = "SELECT * FROM book WHERE del = false";
			
			return qr.query(sql, new BeanListHandler<Book>(Book.class));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 按分类查询图书
	 * @param cid
	 * @return
	 */
	public List<Book> findByCategory(String cid) {
		try {
			String sql = "SELECT * FROM book WHERE cid = ? AND del = false";
			return qr.query(sql, new BeanListHandler<Book>(Book.class), cid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 加载图书
	 * @param bid
	 * @return
	 */
	public Book load(String bid) {
		try {
			String sql = "SELECT * FROM book WHERE bid = ?";
			/*
			 * Book对象中对应分类的成员属性是Category对象
			 * 而查询数据库表book得到的是cid
			 * 所以我们直接映射到Book对象中的时候，会导致cid这个数据丢失
			 * 
			 * 为了不让cid值丢失，所以不用new BeanHandler<Book>(Book.class)来直接映射到Book对象中
			 * 而是用new MapHandler()把查询出来的一整行记录映射成一个map集合
			 * 然后用CommonUtils工具类的toBean()方法
			 * 	> 把属于Book类型的数据映射到Book对象中
			 * 	> 把属于Category类型的数据映射到Category对象中
			 * 最后建立关系：即把category对象保存到Book对象中
			 */
			Map<String, Object> map = qr.query(sql, new MapHandler(), bid);
			
			Book book = CommonUtils.toBean(map, Book.class);
			Category category = CommonUtils.toBean(map, Category.class);
			
			book.setCategory(category);
			
			return book;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 查询指定分类下的图书数量
	 * @param cid
	 * @return
	 */
	public int getCountByCid(String cid) {
		try {
			String sql = "SELECT COUNT(*) FROM book WHERE cid = ?";
			Number cnt = (Number)qr.query(sql, new ScalarHandler<>(), cid);
			return cnt.intValue();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}


	/**
	 * 添加图书
	 * @param book
	 */
	public void add(Book book) {
		try {
			String sql = "INSERT INTO book VALUES (?,?,?,?,?,?,?)";
			Object[] params = { book.getBid(), book.getBname(), book.getPrice(),
					book.getAuthor(), book.getImage(), book.getCategory().getCid(),
					book.isDel() };
			qr.update(sql, params);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	
	/**
	 * 删除图书
	 * 	> 实质上是修改图书对应的del字段为true，即假删除
	 * @param bid
	 */
	public void delete(String bid) {
		try {
			String sql = "UPDATE book SET del = true WHERE bid = ?";
			qr.update(sql, bid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	
	/**
	 * 编辑图书
	 * @param book
	 */
	public void edit(Book book) {
		try {
			String sql = "UPDATE book SET bname = ?, price = ?, author = ?, image = ?, cid = ? WHERE bid = ?";
			Object[] params = { book.getBname(), book.getPrice(), book.getAuthor(), book.getImage(),
					book.getCategory().getCid(), book.getBid() };
			qr.update(sql, params);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
