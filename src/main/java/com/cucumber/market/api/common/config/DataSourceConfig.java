package com.cucumber.market.api.common.config;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean(name = "dataSource")
    @ConfigurationProperties("spring.datasource.hikari")
    public DataSource dataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    //sqlSessionFactory 생성
    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource, ApplicationContext applicationContext) throws Exception {
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();

        factoryBean.setDataSource(dataSource);
        factoryBean.setMapperLocations(applicationContext.getResources("classpath:/sqlmapper/mappers/*.xml"));
        return factoryBean.getObject();
    }

    //sqlSession 생성
    @Bean(name = "sqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }


}
