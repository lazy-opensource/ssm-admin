/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50621
Source Host           : 127.0.0.1:3306
Source Database       : innovate

Target Server Type    : MYSQL
Target Server Version : 50621
File Encoding         : 65001

Date: 2017-03-27 22:07:12
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for demo
-- ----------------------------
DROP TABLE IF EXISTS `demo`;
CREATE TABLE `demo` (
  `uuid` varchar(100) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `createTime` date DEFAULT NULL,
  `updateTime` date DEFAULT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of demo
-- ----------------------------
INSERT INTO `demo` VALUES ('0', 'lzy', null, null);
INSERT INTO `demo` VALUES ('099db03d-be66-470a-a32c-fc98f3e7c81b', 'zhangsan3', null, null);
INSERT INTO `demo` VALUES ('1', 'lzy', null, null);
INSERT INTO `demo` VALUES ('12', 'lzy', null, null);
INSERT INTO `demo` VALUES ('822ad002-88d1-44f9-ba61-9e91848b60f5', 'zhangsan3', null, null);
INSERT INTO `demo` VALUES ('b7dd05e0-2a45-4b51-aca3-e9ff6cd3e604', 'zhangsan3', null, null);

-- ----------------------------
-- Table structure for sys_group
-- ----------------------------
DROP TABLE IF EXISTS `sys_group`;
CREATE TABLE `sys_group` (
  `uuid` varchar(100) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `status` varchar(2) DEFAULT NULL,
  `remark` varchar(50) DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `updateTime` datetime DEFAULT NULL,
  `oper` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_group
-- ----------------------------
INSERT INTO `sys_group` VALUES ('1', '超级用户组', '1', '超级管理员组', null, null, null);

-- ----------------------------
-- Table structure for sys_group_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_group_menu`;
CREATE TABLE `sys_group_menu` (
  `uuid` varchar(100) NOT NULL,
  `group_uuid` varchar(100) DEFAULT NULL,
  `menu_uuid` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_group_menu
-- ----------------------------
INSERT INTO `sys_group_menu` VALUES ('0', '1', '0');
INSERT INTO `sys_group_menu` VALUES ('1', '1', '1');
INSERT INTO `sys_group_menu` VALUES ('2', '1', '2');
INSERT INTO `sys_group_menu` VALUES ('3', '1', '3');
INSERT INTO `sys_group_menu` VALUES ('4', '1', '4');
INSERT INTO `sys_group_menu` VALUES ('5', '1', '5');
INSERT INTO `sys_group_menu` VALUES ('6', '1', '6');
INSERT INTO `sys_group_menu` VALUES ('7', '1', '7');

-- ----------------------------
-- Table structure for sys_group_oper
-- ----------------------------
DROP TABLE IF EXISTS `sys_group_oper`;
CREATE TABLE `sys_group_oper` (
  `uuid` varchar(100) NOT NULL,
  `group_uuid` varchar(100) DEFAULT NULL,
  `oper_uuid` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_group_oper
-- ----------------------------
INSERT INTO `sys_group_oper` VALUES ('1', '1', '1');
INSERT INTO `sys_group_oper` VALUES ('10', '1', '10');
INSERT INTO `sys_group_oper` VALUES ('11', '1', '11');
INSERT INTO `sys_group_oper` VALUES ('12', '1', '12');
INSERT INTO `sys_group_oper` VALUES ('13', '1', '13');
INSERT INTO `sys_group_oper` VALUES ('14', '1', '14');
INSERT INTO `sys_group_oper` VALUES ('15', '1', '15');
INSERT INTO `sys_group_oper` VALUES ('16', '1', '16');
INSERT INTO `sys_group_oper` VALUES ('17', '1', '17');
INSERT INTO `sys_group_oper` VALUES ('18', '1', '18');
INSERT INTO `sys_group_oper` VALUES ('19', '1', '19');
INSERT INTO `sys_group_oper` VALUES ('2', '1', '2');
INSERT INTO `sys_group_oper` VALUES ('20', '1', '20');
INSERT INTO `sys_group_oper` VALUES ('21', '1', '21');
INSERT INTO `sys_group_oper` VALUES ('22', '1', '22');
INSERT INTO `sys_group_oper` VALUES ('23', '1', '23');
INSERT INTO `sys_group_oper` VALUES ('24', '1', '24');
INSERT INTO `sys_group_oper` VALUES ('3', '1', '3');
INSERT INTO `sys_group_oper` VALUES ('4', '1', '4');
INSERT INTO `sys_group_oper` VALUES ('5', '1', '5');
INSERT INTO `sys_group_oper` VALUES ('6', '1', '6');
INSERT INTO `sys_group_oper` VALUES ('7', '1', '7');
INSERT INTO `sys_group_oper` VALUES ('8', '1', '8');
INSERT INTO `sys_group_oper` VALUES ('9', '1', '9');

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log` (
  `uuid` varchar(100) NOT NULL,
  `className` varchar(200) DEFAULT NULL,
  `methodName` varchar(200) DEFAULT NULL,
  `message` varchar(500) DEFAULT NULL,
  `exceptionType` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `loginName` varchar(50) DEFAULT NULL,
  `loginIp` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `uuid` varchar(100) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `url` varchar(100) DEFAULT NULL,
  `parentId` varchar(100) DEFAULT NULL,
  `sortCode` varchar(100) DEFAULT NULL,
  `status` varchar(2) DEFAULT NULL,
  `icon` varchar(50) DEFAULT NULL,
  `code` varchar(20) DEFAULT NULL,
  `remark` varchar(50) DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `updateTime` datetime DEFAULT NULL,
  `oper` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES ('0', '菜单列表', '', '-1', '0', '1', null, null, '顶级菜单', null, null, null);
INSERT INTO `sys_menu` VALUES ('1', '系统管理', '', '0', '1', '1', null, 'menu_sys:manager:0', '二级菜单', null, null, null);
INSERT INTO `sys_menu` VALUES ('2', '用户管理', 'user/toList', '1', '0', '1', null, 'menu_sys:user:1', '三级菜单', null, null, null);
INSERT INTO `sys_menu` VALUES ('3', '角色管理', 'role/toList', '1', '1', '1', null, 'menu_sys:role:2', '三级菜单', null, null, null);
INSERT INTO `sys_menu` VALUES ('4', '菜单管理', 'menu/toList', '1', '2', '1', null, 'menu_sys:menu:3', '三级菜单', null, null, null);
INSERT INTO `sys_menu` VALUES ('5', '操作管理', 'oper/toList', '1', '3', '1', null, 'menu_sys:oper:4', '三级菜单', null, null, null);
INSERT INTO `sys_menu` VALUES ('6', '用户组管理', 'group/toList', '1', '4', '1', null, 'menu_sys:group:5', '三级菜单', null, null, null);
INSERT INTO `sys_menu` VALUES ('7', '日志管理', 'log/toList', '1', '5', '1', null, 'menu_sys:log:6', '三级菜单', null, null, null);

-- ----------------------------
-- Table structure for sys_oper
-- ----------------------------
DROP TABLE IF EXISTS `sys_oper`;
CREATE TABLE `sys_oper` (
  `uuid` varchar(100) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `status` varchar(2) DEFAULT NULL,
  `sortCode` varchar(100) DEFAULT NULL,
  `url` varchar(100) DEFAULT NULL,
  `menuId` varchar(100) DEFAULT NULL,
  `code` varchar(50) DEFAULT NULL,
  `icon` varchar(50) DEFAULT NULL,
  `remark` varchar(50) DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `updateTime` datetime DEFAULT NULL,
  `oper` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_oper
-- ----------------------------
INSERT INTO `sys_oper` VALUES ('1', '新增用户', '1', '0', 'toAdd();', '2', 'oper_sys:user:add:2', null, '用户管理菜单按钮', null, null, 'sys');
INSERT INTO `sys_oper` VALUES ('10', '关联操作', '1', '4', 'toContactOper();', '3', 'oper_sys:role:toContactOper:3', null, '角色管理菜单按钮', null, null, 'sys');
INSERT INTO `sys_oper` VALUES ('11', '关联用户组', '1', '5', 'toContactGroup();', '3', 'oper_sys:role:toContactGroup:3', null, '角色管理菜单按钮', null, null, 'sys');
INSERT INTO `sys_oper` VALUES ('12', '新增菜单', '1', '0', 'toAdd();', '4', 'oper_sys:menu:add:4', null, '菜单管理按钮', null, null, 'sys');
INSERT INTO `sys_oper` VALUES ('13', '删除菜单', '1', '1', 'del();', '4', 'oper_sys:menu:del:4', null, '菜单管理按钮', null, null, 'sys');
INSERT INTO `sys_oper` VALUES ('14', '编辑菜单', '1', '2', 'toEdit();', '4', 'oper_sys:menu:edit:4', null, '菜单管理按钮', null, null, 'sys');
INSERT INTO `sys_oper` VALUES ('15', '关联父菜单', '1', '2', 'toContactParentMenu();', '4', 'oper_sys:menu:contactParentMenu:4', null, '菜单管理按钮', null, null, 'sys');
INSERT INTO `sys_oper` VALUES ('16', '新增操作', '1', '0', 'toAdd();', '5', 'oper_sys:oper:add:5', null, '操作管理按钮', null, null, 'sys');
INSERT INTO `sys_oper` VALUES ('17', '删除操作', '1', '1', 'del();', '5', 'oper_sys:oper:del:5', null, '操作管理按钮', null, null, 'sys');
INSERT INTO `sys_oper` VALUES ('18', '编辑操作', '1', '2', 'toEdit();', '5', 'oper_sys:oper:edit:5', null, '操作管理按钮', null, null, 'sys');
INSERT INTO `sys_oper` VALUES ('19', '关联菜单', '1', '3', 'toContactMenu();', '5', 'oper_sys:oper:toContactMenu:5', null, '操作管理按钮', null, null, 'sys');
INSERT INTO `sys_oper` VALUES ('2', '删除用户', '1', '1', 'del();', '2', 'oper_sys:user:del:2', null, '用户管理菜单按钮', null, null, 'sys');
INSERT INTO `sys_oper` VALUES ('20', '新增用户组', '1', '0', 'toAdd();', '6', 'oper_sys:group:add:6', null, '用户组管理按钮', null, null, 'sys');
INSERT INTO `sys_oper` VALUES ('21', '删除用户组', '1', '1', 'del();', '6', 'oper_sys:group:del:6', null, '用户组管理按钮', null, null, 'sys');
INSERT INTO `sys_oper` VALUES ('22', '编辑用户组', '1', '2', 'toEdit();', '6', 'oper_sys:group:edit:6', null, '用户组管理按钮', null, null, 'sys');
INSERT INTO `sys_oper` VALUES ('23', '关联菜单', '1', '3', 'toContactMenu();', '6', 'oper_sys:group:toContactMenu:6', null, '用户组管理按钮', null, null, 'sys');
INSERT INTO `sys_oper` VALUES ('24', '关联操作', '1', '4', 'toContactOper();', '6', 'oper_sys:group:toContactOper:6', null, '用户组管理按钮', null, null, 'sys');
INSERT INTO `sys_oper` VALUES ('3', '编辑用户', '1', '2', 'toEdit();', '2', 'oper_sys:user:edit:2', null, '用户管理菜单按钮', null, null, 'sys');
INSERT INTO `sys_oper` VALUES ('4', '关联角色', '1', '3', 'toContactRoles()', '2', 'oper_sys:user:toContactRoles:2', null, '用户管理菜单按钮', null, null, 'sys');
INSERT INTO `sys_oper` VALUES ('5', '关联用户组', '1', '4', 'toContactGroup();', '2', 'oper_sys:user:toContactGroup:2', null, '用户管理菜单按钮', null, null, 'sys');
INSERT INTO `sys_oper` VALUES ('6', '新增角色', '1', '0', 'toAdd();', '3', 'oper_sys:role:add:3', null, '角色管理菜单按钮', null, null, 'sys');
INSERT INTO `sys_oper` VALUES ('7', '删除角色', '1', '1', 'del();', '3', 'oper_sys:role:del:3', null, '角色管理菜单按钮', null, null, 'sys');
INSERT INTO `sys_oper` VALUES ('8', '编辑角色', '1', '2', 'toEdit();', '3', 'oper_sys:role:edit:3', null, '角色管理菜单按钮', null, null, 'sys');
INSERT INTO `sys_oper` VALUES ('9', '关联菜单', '1', '3', 'toContactMenu();', '3', 'oper_sys:role:toContactMenu:3', null, '角色管理菜单按钮', null, null, 'sys');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `uuid` varchar(100) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `status` varchar(2) DEFAULT NULL,
  `remark` varchar(100) DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `updateTime` datetime DEFAULT NULL,
  `oper` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('1', '超级管理员', '1', '拥有所有权限', null, null, null);

-- ----------------------------
-- Table structure for sys_role_group
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_group`;
CREATE TABLE `sys_role_group` (
  `uuid` varchar(100) NOT NULL,
  `role_uuid` varchar(100) DEFAULT NULL,
  `group_uuid` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_role_group
-- ----------------------------
INSERT INTO `sys_role_group` VALUES ('1', '1', '1');

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `uuid` varchar(100) NOT NULL,
  `role_uuid` varchar(100) DEFAULT NULL,
  `menu_uuid` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES ('0', '1', '0');
INSERT INTO `sys_role_menu` VALUES ('1', '1', '1');
INSERT INTO `sys_role_menu` VALUES ('2', '1', '2');
INSERT INTO `sys_role_menu` VALUES ('3', '1', '3');
INSERT INTO `sys_role_menu` VALUES ('4', '1', '4');
INSERT INTO `sys_role_menu` VALUES ('5', '1', '5');
INSERT INTO `sys_role_menu` VALUES ('6', '1', '6');
INSERT INTO `sys_role_menu` VALUES ('7', '1', '7');

-- ----------------------------
-- Table structure for sys_role_oper
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_oper`;
CREATE TABLE `sys_role_oper` (
  `uuid` varchar(100) NOT NULL,
  `role_uuid` varchar(100) DEFAULT NULL,
  `oper_uuid` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_role_oper
-- ----------------------------
INSERT INTO `sys_role_oper` VALUES ('1', '1', '1');
INSERT INTO `sys_role_oper` VALUES ('10', '1', '10');
INSERT INTO `sys_role_oper` VALUES ('11', '1', '11');
INSERT INTO `sys_role_oper` VALUES ('12', '1', '12');
INSERT INTO `sys_role_oper` VALUES ('13', '1', '13');
INSERT INTO `sys_role_oper` VALUES ('14', '1', '14');
INSERT INTO `sys_role_oper` VALUES ('15', '1', '15');
INSERT INTO `sys_role_oper` VALUES ('16', '1', '16');
INSERT INTO `sys_role_oper` VALUES ('17', '1', '17');
INSERT INTO `sys_role_oper` VALUES ('18', '1', '18');
INSERT INTO `sys_role_oper` VALUES ('19', '1', '19');
INSERT INTO `sys_role_oper` VALUES ('2', '1', '2');
INSERT INTO `sys_role_oper` VALUES ('20', '1', '20');
INSERT INTO `sys_role_oper` VALUES ('21', '1', '21');
INSERT INTO `sys_role_oper` VALUES ('22', '1', '22');
INSERT INTO `sys_role_oper` VALUES ('23', '1', '23');
INSERT INTO `sys_role_oper` VALUES ('24', '1', '24');
INSERT INTO `sys_role_oper` VALUES ('3', '1', '3');
INSERT INTO `sys_role_oper` VALUES ('4', '1', '4');
INSERT INTO `sys_role_oper` VALUES ('5', '1', '5');
INSERT INTO `sys_role_oper` VALUES ('6', '1', '6');
INSERT INTO `sys_role_oper` VALUES ('7', '1', '7');
INSERT INTO `sys_role_oper` VALUES ('8', '1', '8');
INSERT INTO `sys_role_oper` VALUES ('9', '1', '9');

-- ----------------------------
-- Table structure for sys_role_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_user`;
CREATE TABLE `sys_role_user` (
  `uuid` varchar(100) NOT NULL,
  `role_uuid` varchar(100) DEFAULT NULL,
  `user_uuid` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_role_user
-- ----------------------------
INSERT INTO `sys_role_user` VALUES ('1', '1', '1');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `uuid` varchar(100) NOT NULL,
  `loginName` varchar(50) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `tag` int(100) DEFAULT NULL,
  `status` varchar(2) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `groupId` varchar(100) DEFAULT NULL,
  `remark` varchar(50) DEFAULT NULL,
  `updateTime` datetime DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  `oper` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1', 'admin', '3ad1c0f3793e149e5bfb3014bfa53e06', '25684969', '1', null, '1', null, null, null, null);
