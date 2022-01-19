package com.zj.bookstore.cart.domain;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 购物车类
 * @author GoHome
 *
 */
public class Cart {
	private Map<String, CartItem> map = new LinkedHashMap<String, CartItem>(); // bid为key
	
	/**
	 * 计算合计：合计等于所有条目的小计之和
	 * @return
	 */
	public double getTotal() {
		// 得到一个BigDecimal类型的总计
		BigDecimal total = new BigDecimal("0");
		// 循环遍历所有的条目
		for(CartItem cartItem : map.values()) {
			// 将每个条目的小计 从double类型转换成BigDecimal类型
			BigDecimal subTotal = BigDecimal.valueOf(cartItem.getSubtotal());
			// 执行加法运算
			total = total.add(subTotal);
		}
		// 将BigDecimal类型的结果转换成double类型
		double result = total.doubleValue();
		return result;
	}
	
	/**
	 * 添加条目到购物车中
	 * @param cartItem
	 */
	public void add(CartItem cartItem) {
		if(map.containsKey(cartItem.getBook().getBid())) { // 判断原来车中是否存在该条目
			CartItem _cartItem = map.get(cartItem.getBook().getBid()); // 得到存在的原条目
			_cartItem.setCount(_cartItem.getCount() + cartItem.getCount()); // 设置原条目的数量为：原条目数量+新条目的数量
			map.put(cartItem.getBook().getBid(), _cartItem);
		} else {
			map.put(cartItem.getBook().getBid(), cartItem);
		}
	}
	
	/**
	 * 清空所有条目
	 */
	public void clear() {
		map.clear();
	}
	
	/**
	 * 删除指定条目
	 * @param bid
	 */
	public void delete(String bid) {
		map.remove(bid);
	}
	
	/**
	 * 获取所有条目
	 * @return
	 */
	public Collection<CartItem> getCartItems() {
		return map.values();
	}
}
