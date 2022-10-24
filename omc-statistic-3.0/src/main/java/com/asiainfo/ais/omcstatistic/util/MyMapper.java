package com.asiainfo.ais.omcstatistic.util;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.*;

/**
 * 继承自己的MyMapper
 * 用于自动生成mybatis 增删改查方法
 *
 * @author yangyc
 * @since 2018-05-05 16:08:44
 */
public interface MyMapper<T> extends Mapper<T>, MySqlMapper<T>{
  //TODO
  //FIXME 特别注意，该接口不能被扫描到，否则会出错
}