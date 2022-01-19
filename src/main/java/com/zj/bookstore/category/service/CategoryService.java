package com.zj.bookstore.category.service;

import java.util.List;

import com.zj.bookstore.book.dao.BookDao;
import com.zj.bookstore.book.domain.Book;
import com.zj.bookstore.category.dao.CategoryDao;
import com.zj.bookstore.category.domain.Category;

public class CategoryService {
	private CategoryDao categoryDao = new CategoryDao();
	private BookDao bookDao = new BookDao();

	/**
	 * 查询所有分类
	 * @return
	 */
	public List<Category> findAll() {
		return categoryDao.findAll();
	}

	/**
	 * 添加分类
	 * @param category
	 * @throws CategoryException 
	 */
	public void add(Category category) throws CategoryException {
		/*
		 * 用cname查询数据库
		 * 	> 如果得到category对象不为null的话，抛出异常：分类名称已存在
		 */
		Category _category = categoryDao.findByCname(category.getCname());
		if(_category != null) {
			throw new CategoryException("分类名称：" + category.getCname() + "，已经存在！");
		}
		
		// 调用add()方法进行添加分类
		categoryDao.add(category);
	}

	/**
	 * 删除分类
	 * @param cid
	 */
	public void delete(String cid) throws CategoryException {
		// 获取该分类下图书的本书
		int count = bookDao.getCountByCid(cid);
		// 如果该分类下存在图书，不让删除，抛出异常
		if(count > 0) {
			throw new CategoryException("删除分类失败！该分类下有对应的图书，不能删除！");
		}
		// 删除该分类
		categoryDao.delete(cid);
	}

	/**
	 * 加载分类
	 * @param cid
	 * @return
	 */
	public Category load(String cid) {
		return categoryDao.load(cid);
	}

	/**
	 * 编辑分类
	 * @param category
	 * @throws CategoryException 
	 */
	public void edit(Category category) throws CategoryException {
		// 使用cname查询数据库得到Category对象
		Category _category = categoryDao.findByCname(category.getCname());
		if(_category != null) {
			throw new CategoryException("分类名称：" + category.getCname() + "，已经存在！");
		}
		// 调用edit方法进行修改分类
		categoryDao.edit(category);
	}

}
