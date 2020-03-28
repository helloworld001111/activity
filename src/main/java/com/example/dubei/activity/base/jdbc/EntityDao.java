package com.example.dubei.activity.base.jdbc;


import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 
* 类描述： 数据查询基类方法
* 创建人：姚林刚   
* 创建时间：2015年4月15日 下午2:26:59   
* 修改人：Administrator   
* 修改时间：2015年4月15日 下午2:26:59   
* 修改备注：   
* @version
 */
public interface EntityDao<E,PK extends Serializable>{

	/**通过主键id查询对象**/
	public Object getById(PK id);
	/**通过id删除主键**/
	public void deleteById(PK id);
	
	/** 插入数据 */
	public void save(E entity);
	
	/** 更新数据 */
	public void update(E entity);

	/** 根据id检查是否插入或是更新数据 */
	public void saveOrUpdate(E entity);

	public boolean isUnique(E entity, String uniquePropertyNames);
	
	/** 用于hibernate.flush() 有些dao实现不需要实现此类  */
	public void flush();
	/**得到所有数据方法**/
	public List<E> findAll();
	/**分页查询方法
	 * @return**/
	public Page pageQuery(int startNumber, int pageIndex, int pageSize, String sortColumn);
	/**带参数的分页查询方法**/
	public Page pageQuery(int startNumber, int pageIndex, int pageSize, String sortColumn, Map map);
	/**
	 * 对一个查询语句的结果进行分页方法，只查询当前页记录
	 * @param statement 查询语句
	 * @param pageIndex 查询页码
	 * @param pageSize 分页大小
	 * @param sortColumn 排序字段
	 * @param object 查询对象
	 * @return
	 */
	public List pageList(String statement, int pageIndex, int pageSize, String sortColumn, Object object);
	
}
