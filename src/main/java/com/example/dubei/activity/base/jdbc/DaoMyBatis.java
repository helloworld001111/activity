package com.example.dubei.activity.base.jdbc;


import org.apache.ibatis.session.SqlSessionFactory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
* 类描述：   数据库查询帮助类
 */
public class DaoMyBatis extends MyBatis<Object, Serializable> {
	
	public DaoMyBatis(SqlSessionFactory sqlSessionFactory){
		super(sqlSessionFactory);
	}
	
	public DaoMyBatis(){
	}
	/**
	 * 通过主键查询对象
	 * @param statement 查询语句id
	 * @param id 查询的id
	 * @return 返回查询出的对象
	 */
	public Object getById(String statement,Serializable id){
		return super.getSqlSessionTemplate().selectOne(statement, id);
	}
	
	/**
	 * 通过主键删除数据
	 * @param statement 查询语句id
	 * @param id 查询的id
	 * @return 返回mybatis删除返回码
	 */
	public int deleteById(String statement,Serializable id){
		return super.getSqlSessionTemplate().delete(statement, id);
	}
	
	/** 
	 * 添加一条数据记录
	 * @param statement 查询语句id
	 * @param object 要添加的对象
	 * @return 返回mybatis的返回码
	 */
	public int save(String statement,Object object){
		return super.getSqlSessionTemplate().insert(statement, object);
	}
	
	/**
	 * 更新一条语句
	 * @param statement 查询语句id
	 * @param object 要修改的对象
	 * @return 返回mybatis的返回码
	 */
	public int update(String statement,Object object){
		return super.getSqlSessionTemplate().update(statement, object);
	}
	
	/**
	 * 查询数据集
	 * @param statement 查询语句id
	 * @param paramObject 查询参数，类型对应查询语句配置的参数
	 * @return 查询出的数据结果集
	 */
	public List queryList(String statement,Object paramObject){
		return super.getSqlSessionTemplate().selectList(statement, paramObject);
	}
	
	/**
	 * 查询map结果集
	 * @param satement
	 * @param paramObject
	 * @param key map主键值列
	 * @return
	 */
	public Map queryMap(String satement,Object paramObject,String key){
		return super.getSqlSessionTemplate().selectMap(satement, paramObject, key);
	}
	/**
	 * 查询结果集并分页
	 * @param statement 查询语句id
	 * @param paramObject 查询参数，类型对应查询语句配置的参数
	 * @param pageIndex 分页页码
	 * @param pageSize 分页结果
	 * @return 查询出的结果集
	 */
	public List queryPageList(String statement,Object paramObject,int pageIndex,int pageSize){
		int start = (pageIndex-1)*pageSize;
		return super.getSqlSessionTemplate().selectPageList(statement, paramObject, start, pageSize);
	}
	
	/**
	 * 查询结果集并分页
	 * @param statement 查询语句id
	 * @param paramObject 查询参数，类型对应查询语句配置的参数
	 * @param start 开始行数
	 * @return 查询出的结果集
	 */
	public List queryDataTable(String statement,Object paramObject,int start,int pageSize){
		return super.getSqlSessionTemplate().selectPageList(statement, paramObject, start, pageSize);
	}
	
	/**
	 * 通过sql语句查询结果集并分页
	 * @param statement
	 * @return 查询出的结果集
	 */
	public List querySqlLimitList(String statement,Object paramObject,int start,int end){
		return super.pageList(statement,start,end,null,paramObject);
	}
	
	/**
	 * 查询结果集并分页
	 * @param statement 查询语句id
	 * @param paramObject 查询参数，类型对应查询语句配置的参数
	 * @param start 开始行数
	 * @param end 结束行数
	 * @return 查询出的结果集
	 */
	public List queryLimitList(String statement,Object paramObject,int start,int end){
		int pageSize = end - start;
		return super.getSqlSessionTemplate().selectPageList(statement, paramObject, start, pageSize);
	}
	
	/**
	 * 查询map结果集
	 * @param satement
	 * @param paramObject
	 * @param key map主键值列
	 * @return
	 */
	public Map queryPageMap(String satement,Object paramObject,String key,int pageIndex,int pageSize){
		int start = (pageIndex-1)*pageSize;
		return super.getSqlSessionTemplate().selectPageMap(satement, paramObject, key, start, pageSize);
	}
	
	/**
	 * 返回easyUI格式的分页数据
	 * @param statement
	 * @param paramObject
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	public Map getEasyuiPage(String statement,Object paramObject,int pageIndex,int pageSize){
		int totalCount = super.getRowTotal(statement, paramObject);
		List list = super.getSqlSessionTemplate().selectPageList(statement, paramObject, (pageIndex-1)*pageSize, pageSize);
		Map map = new HashMap();
		map.put("total", totalCount);
		map.put("rows", list);
		return map;
	}
	
	/**
	 * 查询一个对象
	 * @param satement 查询语句
	 * @param paramObject 查询参数对象
	 * @return 查询结果对象
	 */
	public Object queryObject(String satement,Object paramObject){
		return super.getSqlSessionTemplate().selectOne(satement, paramObject);
	}

	/**
	 * 根据参数删除数据
	 * @author Administrator
	 * @param statement 语句id
	 * @param paramObject 参数对象
	 * @return
	 */
	public int delete(String statement,Object paramObject){
		return super.getSqlSessionTemplate().delete(statement, paramObject);
	}
	
	/**
	 * 直接通过给定参数执行操作方法
	 @ author Administrator
	 * @param action 操作对象
	 */
	public void execute(SqlSessionCallback action){
		super.getSqlSessionTemplate().execute(action);
	}
}
