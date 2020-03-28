package com.example.dubei.activity.base.jdbc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.dao.support.DaoSupport;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 
* 类描述： mybatis操作基类,封装基本操作方法   
* 创建人：姚林刚
* 创建时间：2015年3月6日 上午10:03:24   
* 修改人：Administrator   
* 修改时间：2015年3月6日 上午10:03:24   
* 修改备注：   
* @version
 */
public abstract class MyBatis<E,PK extends Serializable> extends DaoSupport implements EntityDao<E,PK> {
    protected final Log log = LogFactory.getLog(getClass());
    
    private SqlSessionFactory sqlSessionFactory;
    private SqlSessionTemplate sqlSessionTemplate;
    
    public MyBatis(SqlSessionFactory sqlSessionFactory){
    	this.sqlSessionFactory = sqlSessionFactory;
    	this.sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory);
    }
    
    public MyBatis(){
    }

    @Override
	protected void checkDaoConfig() throws IllegalArgumentException {
		Assert.notNull(sqlSessionFactory,"sqlSessionFactory must be not null");
	}

	public SqlSessionFactory getSqlSessionFactory() {
		return sqlSessionFactory;
	}

	/**
	 * 传递sqlSessionFactory，并初始化sqlSessionTemplate
	 * @param sqlSessionFactory
	 */
	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
		this.sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory);
	}
	

    public SqlSessionTemplate getSqlSessionTemplate() {
    	return sqlSessionTemplate;
    }
    
    /**
     * 通过id获得对象
     */
	@Override
    public Object getById(PK primaryKey) {
        Object object = getSqlSessionTemplate().selectOne(getFindByPrimaryKeyStatement(), primaryKey);
        return object;
    }
    
    /**
     * 通过id删除对象
     */
	@Override
	public void deleteById(PK id) {
		int affectCount = getSqlSessionTemplate().delete(getDeleteStatement(), id);
	}
	
	/**
	 * 保存对象
	 */
	@Override
    public void save(E entity) {
		prepareObjectForSaveOrUpdate(entity);
		int affectCount = getSqlSessionTemplate().insert(getInsertStatement(), entity);    	
    }
    
    /**
     * 修改对象
     */
	@Override
	public void update(E entity) {
		prepareObjectForSaveOrUpdate(entity);
		int affectCount = getSqlSessionTemplate().update(getUpdateStatement(), entity);
	}
	
	/**
	 * 用于子类覆盖,在insert,update之前调用
	 * @param o
	 */
    protected void prepareObjectForSaveOrUpdate(E o) {
    }
    

    /**
     * 返回命名空间方法，方便在增删查该方法中使用，子类具体实现，如果没有实现，抛出异常，无法执行基本增删查该方法
     * @return
     */
    public String getIbatisMapperNamesapce() {
        throw new RuntimeException("not yet implement");
    }
    
    /**
     * 拼装最基本的增删查改id
     * @return
     */
	@Override
    public void saveOrUpdate(E entity){
    	throw new RuntimeException("not yet implement");
    }
    
    public String getFindByPrimaryKeyStatement() {
    	return getIbatisMapperNamesapce()+".selectByPrimaryKey";
    }

    public String getInsertStatement() {
    	return getIbatisMapperNamesapce()+".insertSelective";
    }

    public String getUpdateStatement() {
    	return getIbatisMapperNamesapce()+".updateByPrimaryKeySelective";
    }

    public String getDeleteStatement() {
    	return getIbatisMapperNamesapce()+".deleteByPrimaryKey";
    }
    
    public String getPageStatement() {
    	return getIbatisMapperNamesapce()+".pageQuery";
    }
    
    public String getCountStatement() {
    	return getIbatisMapperNamesapce()+".count";
    }

    public String getCountStatementForPaging(String statementName) {
		return statementName +".count";
	}
    
    public org.apache.ibatis.session.Configuration getConfiguration(){
    	return this.sqlSessionFactory.getConfiguration();
    }
    /**
     * 得到语句
     * @param statement 语句id
     * @param object 声称语句参数对象
     * @return 查询语句
     */
    protected String getStatement(String statement,Object object){
    	return getConfiguration().getMappedStatement(statement).getSqlSource().getBoundSql(object).getSql();
    }
    
    protected BoundSql getBoundSql(String statement,Object object){
    	return getConfiguration().getMappedStatement(statement).getSqlSource().getBoundSql(object);
    }
    
    /**
     * 得到参数集
     * @param statement 查询语句主键
     * @return
     */
    protected ParameterMap getParametMap(String statement){
    	return getConfiguration().getMappedStatement(statement).getParameterMap();
    }
    
    /**
     * 新建一个查询语句
     * @param msId
     * @param sqlSource
     * @param resultType
     */
    protected void newSelectMappedStatement(String msId, SqlSource sqlSource, final Class<?> resultType,final MappedStatement ms) {
        MappedStatement newMs = null;
        MappedStatement.Builder builder = new MappedStatement.Builder(getConfiguration(), msId, sqlSource, SqlCommandType.SELECT);
        builder.resource(ms.getResource());  
        builder.fetchSize(ms.getFetchSize());  
        builder.statementType(ms.getStatementType());  
        builder.keyGenerator(ms.getKeyGenerator());  
        builder.timeout(ms.getTimeout());  
        builder.parameterMap(ms.getParameterMap());  
        if(resultType!=null){
        	builder.resultMaps(new ArrayList<ResultMap>() {{
            	add(new ResultMap.Builder(getConfiguration(),"defaultResultMap",resultType, new ArrayList<ResultMapping>(0)).build());
            }});
        }
        builder.resultMaps(ms.getResultMaps());
        builder.statementType(ms.getStatementType());
        builder.resultSetType(ms.getResultSetType());  
        builder.cache(ms.getCache());  
        builder.flushCacheRequired(ms.isFlushCacheRequired());  
        builder.useCache(ms.isUseCache());
        newMs = builder.build();
        //缓存
        getConfiguration().addMappedStatement(newMs);
    }
    
    /**
     * 查询总行数,只针对无参数方法
     * @param statement
     * @param object
     * @return
     */
    protected int getRowTotal(String statement,Object object){
    	MappedStatement oldmap = getConfiguration().getMappedStatement(statement);
    	String sql = this.getStatement(statement, object);
    	sql = "select count(1) from ("+sql+")  counttable ";
    	BoundSql boundSql = this.getBoundSql(statement, object);
//    	List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();    
//        //利用Configuration、查询记录数的Sql语句countSql、参数映射关系parameterMappings和参数对象page建立查询记录数对应的BoundSql对象。    
//        BoundSql countBoundSql = new BoundSql(getConfiguration(), sql, parameterMappings, boundSql.getParameterObject()); 
//        
//        for (ParameterMapping mapping : boundSql.getParameterMappings()) {  
//            String prop = mapping.getProperty();  
//            if (boundSql.hasAdditionalParameter(prop)) {  
//            	countBoundSql.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));  
//            }  
//        } 
//        
//    	if(!this.getConfiguration().hasStatement(statement+"_count")){
//    		newSelectMappedStatement(statement+"_count",new BoundSqlSqlSource(countBoundSql),Integer.class,oldmap);
//    	}
    	int totpage=0;  
    	Connection connection;
		try {
			connection = oldmap.getConfiguration().getEnvironment().getDataSource().getConnection();
			PreparedStatement countStmt = connection.prepareStatement(sql);    
	        BoundSql countBS = copyFromBoundSql(oldmap, boundSql, sql);  
	        DefaultParameterHandler parameterHandler = new DefaultParameterHandler(oldmap, object, countBS);  
	        parameterHandler.setParameters(countStmt);  
	        ResultSet rs = countStmt.executeQuery();  
	        if (rs.next()) {    
	          totpage = rs.getInt(1);    
	        }  
	        rs.close();    
	        countStmt.close();    
	        connection.close();  
		} catch (SQLException e) {
			e.printStackTrace();
		}            
    	return totpage;
    }
    
    /** 
     * 复制BoundSql对象 
     */  
    private BoundSql copyFromBoundSql(MappedStatement ms, BoundSql boundSql, String sql) {  
      BoundSql newBoundSql = new BoundSql(ms.getConfiguration(),sql, boundSql.getParameterMappings(), boundSql.getParameterObject());  
      for (ParameterMapping mapping : boundSql.getParameterMappings()) {  
          String prop = mapping.getProperty();  
          if (boundSql.hasAdditionalParameter(prop)) {  
              newBoundSql.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));  
          }  
      }  
      return newBoundSql;  
    }  
    
    public class BoundSqlSqlSource implements SqlSource {    
		BoundSql boundSql;

		public BoundSqlSqlSource(BoundSql boundSql) {
			this.boundSql = boundSql;
		}
		@Override
		public BoundSql getBoundSql(Object parameterObject) {
			return boundSql;
		}
	}
    
    /**
     * 查询分页方法，只查询当前分页信息
     * 姚林刚
     * @param statement
     * @param pageIndex
     * @param pageSize
     * @param sortColumn
     * @param object
     * @return
     */
	@Override
    public List pageList(String statement,int pageIndex,int pageSize,String sortColumn,Object object){
    	int start = (pageIndex-1)*pageSize;
    	int end = pageIndex*pageSize;
    	String sql = this.getStatement(statement, object);
    	sql = "select * from (select a.*, ROWNUM RN   from ("+sql+") a where rownum <= " + end +") where RN > " + start;
    	SqlSourceBuilder builder = new SqlSourceBuilder(getConfiguration());  
    	if(object==null){
    		object = new Object();
    	}
    	SqlSource sqlSource = builder.parse(sql, object.getClass(), null);  
    	if(!this.getConfiguration().hasStatement(statement+"_page")){
    		newSelectMappedStatement(statement+"_page",sqlSource,null,getConfiguration().getMappedStatement(statement));
    	}
    	return (List)this.getSqlSessionTemplate().selectList(statement+"_page", object);
    }
    
    /**
     * 查询分页方法
	 * @return
	 */
	@Override
    public Page pageQuery(int startNumber, int pageIndex, int pageSize, String sortColumn){
    	int totalCount = (Integer) sqlSessionTemplate.selectOne(getCountStatement(),null);
    	int endNumber = pageSize;
    	if(startNumber+pageSize>totalCount){
    		endNumber = totalCount;
    	}else{
    		endNumber = startNumber+pageSize;
    	}
    	Map paramMap = new HashMap();
    	paramMap.put("startNumber", startNumber);
    	paramMap.put("endNumber", endNumber);
    	paramMap.put("sortColumn", sortColumn);
    	return new Page(pageIndex, pageSize, totalCount, sqlSessionTemplate.selectList(getPageStatement(), paramMap));
    }

    /**
     * 带查询条件的查询分页方法
     */
	@Override
    public Page pageQuery(int startNumber, int pageIndex, int pageSize, String sortColumn, Map map){
    	int totalCount = (Integer) sqlSessionTemplate.selectOne(getCountStatement(),map);
    	//判读使用esayUI分页，即没有指定页时，开始页为1，开始记录为0
		startNumber=pageSize*(pageIndex-1);
    	int endNumber = pageSize;
    	if(startNumber+pageSize>totalCount){
    		endNumber = totalCount;
    	}else{
    		endNumber = startNumber+pageSize;
    	}
    	map.put("startNumber", startNumber);
    	map.put("endNumber", endNumber);
    	map.put("sortColumn", sortColumn);
    	return new Page(pageIndex, pageSize, totalCount, sqlSessionTemplate.selectList(getPageStatement(), map));
    }

	public List findAll() {
		throw new UnsupportedOperationException();
	}

	public boolean isUnique(E entity, String uniquePropertyNames) {
		throw new UnsupportedOperationException();
	}

	public void flush() {
		//ignore
	}

	/**
	 * 基本操作对象，用来具体执行传入的操作
	 * @author Administrator
	 *
	 */
	public static class SqlSessionTemplate {
		SqlSessionFactory sqlSessionFactory;

		public SqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
			this.sqlSessionFactory = sqlSessionFactory;
		}

		/**
		 * 执行方法，得到session执行操作
		 * @param action
		 * @return
		 */
		public Object execute(SqlSessionCallback action)  {
			SqlSession session = null;
			try {
				session = sqlSessionFactory.openSession();
				Object result = action.doInSession(session);
				return result;
			}finally {
				if(session != null){
					session.close();
				}
			}
		}

		/**
		 * 封装查询对象的基本操作
		 * @param statement
		 * @param parameter
		 * @return
		 */
		public Object selectOne(final String statement,final Object parameter) {
			return execute(new SqlSessionCallback() {
				public Object doInSession(SqlSession session) {
					return session.selectOne(statement, parameter);
				}
			});
		}

		/**
		 * 同上
		 * @param statement
		 * @param parameter
		 * @return
		 */
		public List selectList(final String statement,final Object parameter) {
			return (List)execute(new SqlSessionCallback() {
				public Object doInSession(SqlSession session) {
					return session.selectList(statement, parameter);
				}
			});
		}

		/**
		 * 封装查询数组的具体操作
		 * @param statement
		 * @param parameter
		 * @param offset
		 * @param limit
		 * @return
		 */
		public List selectPageList(final String statement,final Object parameter,final int offset,final int limit) {
			return (List)execute(new SqlSessionCallback() {
				public Object doInSession(SqlSession session) {
					return session.selectList(statement, parameter, new RowBounds(offset,limit));
				}
			});
		}

		/**
		 * 封装查询数组的具体操作
		 * @param statement
		 * @param parameter
		 * @return
		 */
		public Map selectMap(final String statement,final Object parameter,final String key) {
			return (Map)execute(new SqlSessionCallback() {
				public Object doInSession(SqlSession session) {
					return session.selectMap(statement, parameter,key);
				}
			});
		}

		/**
		 * 封装查询数组的具体操作
		 * @param statement
		 * @param parameter
		 * @param offset
		 * @param limit
		 * @return
		 */
		public Map selectPageMap(final String statement,final Object parameter,final String key,final int offset,final int limit) {
			return (Map)execute(new SqlSessionCallback() {
				public Object doInSession(SqlSession session) {
					return session.selectMap(statement, parameter,key, new RowBounds(offset,limit));
				}
			});
		}

		/**
		 * 封装删除操作
		 * @param statement
		 * @param parameter
		 * @return
		 */
		public int delete(final String statement,final Object parameter) {
			return (Integer)execute(new SqlSessionCallback() {
				public Object doInSession(SqlSession session) {
					return session.delete(statement, parameter);
				}
			});
		}

		/**
		 * 封装修改操作
		 * @param statement
		 * @param parameter
		 * @return
		 */
		public int update(final String statement,final Object parameter) {
			return (Integer)execute(new SqlSessionCallback() {
				public Object doInSession(SqlSession session) {
					return session.update(statement, parameter);
				}
			});
		}
		
		/**
		 * 封装插入操作
		 * @param statement
		 * @param parameter
		 * @return
		 */
		public int insert(final String statement,final Object parameter) {
			return (Integer)execute(new SqlSessionCallback() {
				public Object doInSession(SqlSession session) {
					return session.insert(statement, parameter);
				}
			});
		}

		/**
		 * 封装插入操作
		 * @param statement
		 * @param parameter
		 * @return
		 */
//		public int selectCount(final String statement,final Object parameter) {
//			return (Integer)execute(new SqlSessionCallback() {
//				public Object doInSession(SqlSession session) {
//					return session.insert(statement, parameter);
//				}
//			});
//		}
		
	} 
	
}
