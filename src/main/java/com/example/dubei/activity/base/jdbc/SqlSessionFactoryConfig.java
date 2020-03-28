package com.example.dubei.activity.base.jdbc;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SqlSessionFactoryConfig {
//    @Primary
    //@Qualifier 作用：可以与@Autowired配合使用实现按照名称注入Bean，效果等价于@Resource。也可以在方法参数中使用，等价于从Spring容器中获取该参数的值
//    public SqlSessionFactory createSqlSessionFactory(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory){
//        return new SqlSessionFactory;
//    }

    @Value("${mybatis.mapper-locations}")
    private String mapperLocation;

//    @Value("${common-mybatis.mapper-locations}")
//    private String commonMapperLocation;

    @Value("${spring.datasource.druid.username}")
    private String username;

    @Value("${spring.datasource.druid.password}")
    private String password;

    @Value("${spring.datasource.druid.url}")
    private String dbUrl;

    @Value("${spring.datasource.druid.initial-size}")
    private int initialSize;

    @Value("${spring.datasource.druid.min-idle}")
    private int minIdle;

    @Value("${spring.datasource.druid.max-active}")
    private int maxActive;

    @Value("${spring.datasource.druid.max-wait}")
    private long maxWait;

    @Value("${spring.datasource.druid.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.druid.min-evictable-idle-time-millis}")
    private long minEvictableIdleTimeMillis;

    @Value("${spring.datasource.druid.time-between-eviction-runs-millis}")
    private long timeBetweenEvictionRunsMillis;

    @Value("${spring.datasource.druid.validation-query}")
    private String validationQuery;

    @Value("${spring.datasource.druid.test-while-idle}")
    private boolean testWhileIdle;

    @Value("${spring.datasource.druid.test-on-borrow}")
    private boolean testOnBorrow;

    @Value("${spring.datasource.druid.test-on-return}")
    private boolean testOnReturn;

//    @Value("${spring.datasource.druid.filter.stat.log-slow-sql}")
//    private boolean logSlowSql;
//
//    @Value("${spring.datasource.druid.filter.stat.slow-sql-millis}")
//    private long slowSqlMillis;

    @Bean(name = "mysqlDataSource")
    public DruidDataSource dataSource() {
        DruidDataSource druidDataSource = new DruidDataSource();
        try {
            druidDataSource.setUsername(username);
            druidDataSource.setPassword(password);
            druidDataSource.setUrl(dbUrl);
//            druidDataSource.setFilters("stat,wall");
            druidDataSource.setInitialSize(initialSize);
            druidDataSource.setMinIdle(minIdle);
            druidDataSource.setMaxActive(maxActive);
            druidDataSource.setMaxWait(maxWait);
            druidDataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
            druidDataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
//            druidDataSource.setUseGlobalDataSourceStat(true);
            druidDataSource.setDriverClassName(driverClassName);
            druidDataSource.setValidationQuery(validationQuery);
            druidDataSource.setTestWhileIdle(testWhileIdle);
            druidDataSource.setTestOnBorrow(testOnBorrow);
            druidDataSource.setTestOnReturn(testOnReturn);
            // 设置需要的过滤
//            List<Filter> statFilters =new ArrayList<>();
//            StatFilter statFilter = new StatFilter();
//            statFilter.setLogSlowSql(logSlowSql);
//            statFilter.setSlowSqlMillis(slowSqlMillis);
//            statFilters.add(statFilter);
            // 设置慢SQL
//            druidDataSource.setProxyFilters(statFilters);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return druidDataSource;
    }

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory mysqlSessionFactory(DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources1 = resolver.getResources(mapperLocation);
//        Resource[] resources2 = resolver.getResources(commonMapperLocation);
        Resource[] resources = new Resource[resources1.length];
        for (int i=0;i<resources1.length;i++) {
            resources[i] = resources1[i];
        }
//        int initSize = resources1.length;
//        for (int i=0;i<resources2.length;i++) {
//            resources[initSize+i] = resources2[i];
//        }
        sqlSessionFactoryBean.setMapperLocations(resources);
//        sqlSessionFactoryBean.setPlugins(new Interceptor[]{new CatMybatisInterceptor(dbUrl)});
        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
