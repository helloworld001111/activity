package com.example.dubei.activity.base.jdbc;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * DaoMyBatisConfig配置类。每一个数据源对应一个SqlSessionFactory，相应的对应一个DaoMybatis。DaoMybatis中封装了增删改查一系列方法
 */
//相当于把该类作为spring的xml配置文件中的<beans>。
//被注解的类内部包含有一个或多个被@Bean注解的方法，这些方法将会被AnnotationConfigApplicationContext或AnnotationConfigWebApplicationContext类进行扫描，并用于构建bean定义
@Configuration
public class DaoMyBatisConfig {
//	@Bean 用在方法上，告诉Spring容器，你可以从下面这个方法中拿到一个Bean
	@Bean(name="daoMybatis")
//	用@Primary 告诉spring 在@Autowired犹豫的时候优先选择哪一个具体的实现
	@Primary
	//@Qualifier 作用：可以与@Autowired配合使用实现按照名称注入Bean，效果等价于@Resource。也可以在方法参数中使用，等价于从Spring容器中获取该参数的值
    public DaoMyBatis createMybatisDao(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory){
		return new DaoMyBatis(sqlSessionFactory);
	}
	
//	@Bean(name="dataDaoMybatis")
//    public DaoMyBatis createxcloudDao(@Qualifier("dataSqlSessionFactory") SqlSessionFactory sqlSessionFactory){
//		return new DaoMyBatis(sqlSessionFactory);
//	}
}
