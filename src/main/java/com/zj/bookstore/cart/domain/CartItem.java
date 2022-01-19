package com.zj.bookstore.cart.domain;

import java.math.BigDecimal;

import com.zj.bookstore.book.domain.Book;

/**
 * 条目类
 * @author GoHome
 *
 */
public class CartItem {
	private Book book; // 商品
	private int count; // 数量
	
	/*
	 * 小计，商品的单价*数量。它没有对应的成员！
	 */
	public double getSubtotal() {
		/*
		 * 由于二进制的运算会出现误差
		 * 为了避免误差，必须将值转为BigDecimal类型
		 * 运算结束之后再转换成需要的类型！
		 */
		// 将 “商品的单价” double类型转成BigDecimal类型
		BigDecimal d1 = new BigDecimal(book.getPrice() + "");
		// 将条目中的“商品数量” int类型转成BigDecimal类型
		BigDecimal d2 = new BigDecimal(Integer.toString(count));
		// 将两个BigDecimal类型的进行运算得出结果
		BigDecimal d3 = d1.multiply(d2);
		// 把BigDecimal类型的结果转换成double类型
		double result = d3.doubleValue();
		return result;
	}
	
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	@Override
	public String toString() {
		return "CartItem [book=" + book + ", count=" + count + "]";
	}
	public CartItem(Book book, int count) {
		super();
		this.book = book;
		this.count = count;
	}
	public CartItem() {
		super();
	}
}
