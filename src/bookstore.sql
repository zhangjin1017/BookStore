/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MySQL
 Source Server Version : 80027
 Source Host           : localhost:3306
 Source Schema         : bookstore

 Target Server Type    : MySQL
 Target Server Version : 80027
 File Encoding         : 65001

 Date: 19/01/2022 16:17:40
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for book
-- ----------------------------
DROP TABLE IF EXISTS `book`;
CREATE TABLE `book`  (
  `bid` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '图书编号',
  `bname` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '书名',
  `price` decimal(10, 2) NOT NULL COMMENT '单价',
  `author` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '作者',
  `image` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图书封面',
  `cid` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图书对应的分类编号',
  `del` tinyint(1) NULL DEFAULT NULL COMMENT '是否删除图书',
  PRIMARY KEY (`bid`) USING BTREE,
  INDEX `book_category_cid`(`cid`) USING BTREE,
  CONSTRAINT `book_category_cid` FOREIGN KEY (`cid`) REFERENCES `category` (`cid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of book
-- ----------------------------
INSERT INTO `book` VALUES ('1', '测试1', 12.30, 'zzz', 'book_img/cat.jpg', '1', 0);
INSERT INTO `book` VALUES ('2', '测试2', 22.50, 'ccc', 'book_img/cat.jpg', '1', 0);
INSERT INTO `book` VALUES ('3', '测试3', 54.20, 'xxx', 'book_img/cat.jpg', '2', 0);
INSERT INTO `book` VALUES ('4', '测试4', 55.20, 'vvv', 'book_img/cat.jpg', '2', 0);
INSERT INTO `book` VALUES ('5', '测试5', 46.00, 'bbb', 'book_img/cat.jpg', '3', 0);
INSERT INTO `book` VALUES ('6', '测试6', 88.00, 'nnn', 'book_img/cat.jpg', '3', 0);

-- ----------------------------
-- Table structure for cart
-- ----------------------------
DROP TABLE IF EXISTS `cart`;
CREATE TABLE `cart`  (
  `cartid` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '购物车编号',
  `totalprice` decimal(10, 2) NOT NULL COMMENT '购物车总金额',
  `uid` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '购物车所属用户的编号',
  PRIMARY KEY (`cartid`) USING BTREE,
  INDEX `cart_user_uid`(`uid`) USING BTREE,
  CONSTRAINT `cart_user_uid` FOREIGN KEY (`uid`) REFERENCES `user` (`uid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cart
-- ----------------------------

-- ----------------------------
-- Table structure for cartitem
-- ----------------------------
DROP TABLE IF EXISTS `cartitem`;
CREATE TABLE `cartitem`  (
  `cartitemid` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '购物车条目编号',
  `subtotal` decimal(10, 2) NOT NULL COMMENT '购物车条目小计',
  `amount` int(0) NOT NULL COMMENT '购买图书的数量',
  `bid` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '购买图书的编号',
  `cartid` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '所属购物车的编号',
  PRIMARY KEY (`cartitemid`) USING BTREE,
  INDEX `cartitem_book_bid`(`bid`) USING BTREE,
  INDEX `cartitem_cart_cartid`(`cartid`) USING BTREE,
  CONSTRAINT `cartitem_book_bid` FOREIGN KEY (`bid`) REFERENCES `book` (`bid`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `cartitem_cart_cartid` FOREIGN KEY (`cartid`) REFERENCES `cart` (`cartid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of cartitem
-- ----------------------------

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`  (
  `cid` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '分类编号',
  `cname` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '分类名称',
  PRIMARY KEY (`cid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of category
-- ----------------------------
INSERT INTO `category` VALUES ('1', 'JavaSE');
INSERT INTO `category` VALUES ('2', 'JavaEE');
INSERT INTO `category` VALUES ('3', 'JavaScript');

-- ----------------------------
-- Table structure for orderitem
-- ----------------------------
DROP TABLE IF EXISTS `orderitem`;
CREATE TABLE `orderitem`  (
  `iid` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单条目编号',
  `count` int(0) NOT NULL COMMENT '商品数量',
  `subtotal` decimal(10, 2) NOT NULL COMMENT '小计',
  `oid` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单条目所属订单的编号',
  `bid` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '所购买的图书的编号',
  PRIMARY KEY (`iid`) USING BTREE,
  INDEX `orderitem_orders_oid`(`oid`) USING BTREE,
  INDEX `orderitem_book_bid`(`bid`) USING BTREE,
  CONSTRAINT `orderitem_book_bid` FOREIGN KEY (`bid`) REFERENCES `book` (`bid`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `orderitem_orders_oid` FOREIGN KEY (`oid`) REFERENCES `orders` (`oid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of orderitem
-- ----------------------------

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`  (
  `oid` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单编号',
  `ordertime` datetime(0) NOT NULL COMMENT '下单时间',
  `totalprice` decimal(10, 0) NOT NULL COMMENT '订单总金额',
  `state` int(0) NOT NULL COMMENT '订单状态：0表下单但未付款，1表已付款但未发货，2表已发货但未确认收货，3表确认收货交易完成',
  `uid` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '订单所属的用户编号',
  `address` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '收货地址',
  PRIMARY KEY (`oid`) USING BTREE,
  INDEX `orders_user_uid`(`uid`) USING BTREE,
  CONSTRAINT `orders_user_uid` FOREIGN KEY (`uid`) REFERENCES `user` (`uid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of orders
-- ----------------------------

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `uid` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户编号',
  `username` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `email` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '邮箱',
  `code` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '激活码',
  `state` tinyint(1) NOT NULL COMMENT '账户激活状态：0表未激活，1表激活',
  PRIMARY KEY (`uid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('90A053CC87D3465396B4A96B33B01C8B', 'zhangjin', 'zhangjin', '3514138423@qq.com', 'B3C9C1DE0FB4494EB858897CB781E938C0DD42FE43064C419E7BBED4ED724C91', 1);

SET FOREIGN_KEY_CHECKS = 1;
