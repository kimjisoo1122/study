package hello.springtx.propagation;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.sql.DataSource;

@SpringBootTest
@Slf4j
public class BasicTxTest {

    @Autowired
    PlatformTransactionManager transactionManager;

    @Test
    void comit() {
        log.info("트랜잭션 시작");
        TransactionStatus transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
        log.info("트랜잭션 커밋 시작");
        transactionManager.commit(transactionStatus);
        log.info("트랜잭션 종료");
    }
    @Test
    void rollBack() {
        log.info("트랜잭션 시작");
        TransactionStatus transactionStatus = transactionManager.getTransaction(new DefaultTransactionDefinition());
        log.info("트랜잭션 롤백 시작");
        transactionManager.rollback(transactionStatus);
        log.info("트랜잭션 종료");
    }

    @Test
    void inner_commit() {
        log.info("외부 트랜잭션 시작");
        TransactionStatus outerTx = transactionManager.getTransaction(new DefaultTransactionDefinition());
        // isNewTransaction 이게 처음 생성한 트랜잭션이야??
        log.info("outerTx.isNewTransaction = {}", outerTx.isNewTransaction());

        // 내부 트랜잭션 시작
        innerTxLogic();

        log.info("외부 트랜잭션 커밋");
        transactionManager.commit(outerTx);
    }
    @Test
    void inner_rollback() {
        log.info("외부 트랜잭션 시작");
        TransactionStatus outerTx = transactionManager.getTransaction(new DefaultTransactionDefinition());
        // isNewTransaction 이게 처음 생성한 트랜잭션이야??
        log.info("outerTx.isNewTransaction = {}", outerTx.isNewTransaction());

        // 내부 트랜잭션 시작
        innerTxLogic();

        log.info("외부 트랜잭션 커밋");
        transactionManager.commit(outerTx);
    }
    @Test
    void inner_rollback_requires_new() {
        log.info("외부 트랜잭션 시작");
        TransactionStatus outerTx = transactionManager.getTransaction(new DefaultTransactionDefinition());
        // isNewTransaction 이게 처음 생성한 트랜잭션이야??
        log.info("outerTx.isNewTransaction = {}", outerTx.isNewTransaction());

        // 내부 트랜잭션 시작
        innerTxRequiresNewLogic();

        log.info("외부 트랜잭션 커밋");
        transactionManager.commit(outerTx);
    }

    private void innerTxLogic() {
        log.info("내부 트랜잭션 시작");
        TransactionStatus innerTx = transactionManager.getTransaction(new DefaultTransactionAttribute());
        log.info("innerTx.isNewTransaction = {}", innerTx.isNewTransaction());
        log.info("내부 트랜잭션 커밋");
        transactionManager.rollback(innerTx);
    }
    private void innerTxRequiresNewLogic() {
        DefaultTransactionAttribute definition = new DefaultTransactionAttribute();
        log.info("내부 트랜잭션 전파범위 PROPAGATION_REQUIRES_NEW");
        definition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        log.info("내부 트랜잭션 시작");
        TransactionStatus innerTx = transactionManager.getTransaction(definition);
        log.info("innerTx.isNewTransaction = {}", innerTx.isNewTransaction());
        log.info("내부 트랜잭션 커밋");
        transactionManager.rollback(innerTx);
    }


    @TestConfiguration
    static class Config {

        @Bean
        public PlatformTransactionManager transactionManager(DataSource dataSource) {
            return new DataSourceTransactionManager(dataSource);
        }
    }


}

