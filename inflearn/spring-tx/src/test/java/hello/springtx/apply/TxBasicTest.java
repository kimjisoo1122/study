package hello.springtx.apply;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
public class TxBasicTest {

    @Autowired
    BasicService basicService;

    @Test
    void proxyCheck() {
        log.info("aop class = {}", basicService.getClass());
        boolean aopProxy = AopUtils.isAopProxy(basicService);
        assertThat(aopProxy).isTrue();
    }

    @Test
    void txCheck() {
        basicService.tx();
        basicService.noxTx();
    }


    @TestConfiguration
    static class TxApplyBasicConfig {
        @Bean
        public BasicService basicService() {
            return new BasicService();
        }
    }

    @Slf4j
    @Transactional
    static class BasicService {


        public void tx() {
            log.info("call tx");
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("txActive = {}", txActive);
        }

        public void noxTx() {
            log.info("call non tx");
            boolean txActive = TransactionSynchronizationManager.isActualTransactionActive();
            log.info("txActive = {}", txActive);
        }

    }
}

