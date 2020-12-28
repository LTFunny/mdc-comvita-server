package com.aquilaflycloud.mdc.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * MybatisPlusConfig
 * @author star
 * @date 2019-09-20
 */
@Configuration
@EnableTransactionManagement
@MapperScan("com.aquilaflycloud.**.mapper")
public class MybatisPlusConfiguration {

    /**
     * 使用阿里drds时开启2PC事务(推荐 RDS for MySQL 5.6 用户使用)
     * 参考: https://help.aliyun.com/document_detail/71230.html?spm=a2c4g.11186623.2.10.dd29792c46fvlA
     */
    /*@Bean
    public DataSourceTransactionManager drdsTransactionManager() {
        return new DataSourceTransactionManager() {
            @Override
            protected void prepareTransactionalConnection(Connection con, TransactionDefinition definition) throws SQLException {
                try (Statement stmt = con.createStatement()) {
                    stmt.executeUpdate("SET drds_transaction_policy = '2PC'"); // 以 FLEXIBLE 为例
                }
            }
        };
    }*/
}
