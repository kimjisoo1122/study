package com.study.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.datasource.DataSourceFactory;

import javax.sql.DataSource;
import java.util.Properties;

public class HikariCPDataSourceFactory implements DataSourceFactory {
    private Properties properties;
    @Override
    public void setProperties(Properties props) {
        this.properties = props;
    }
    @Override
    public DataSource getDataSource() {
        return new HikariDataSource(new HikariConfig("/properties/hikari.properties"));
    }
}
