package hello.springtx.apply;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@SpringBootTest
public class TxLevelTest {

    @Autowired
    LevelService service;

    @Test
    void orderTest() {
        service.write();
    }

    @TestConfiguration
    static class TxApplyLevelConfig {
        @Bean
        LevelService levelService() {
            return new LevelService();
        }
        @Bean
        LevelServiceV2 levelServiceV2() {
            return new LevelServiceV2();
        }
    }

    @Transactional(readOnly = true)
    static class LevelServiceV2 {

        public void write() {
            log.info("call V2 write");
            printTxInfo();
        }

        private static void printTxInfo() {
            boolean txActive =
                    TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx V2 active={}", txActive);
            boolean readOnly =
                    TransactionSynchronizationManager.isCurrentTransactionReadOnly();
            log.info("tx V2 readOnly={}", readOnly);
        }

    }
    @Service
    @Transactional(readOnly = true)
    static class LevelService {

        @Autowired private LevelServiceV2 levelServiceV2;

        @Transactional(readOnly = false)
        public void write() {
            log.info("call write");
            printTxInfo();
            levelServiceV2.write();
        }
        public void read() {
            log.info("call read");
            printTxInfo();
        }
        private void printTxInfo() {
            boolean txActive =
                    TransactionSynchronizationManager.isActualTransactionActive();
            log.info("tx active={}", txActive);
            boolean readOnly =
                    TransactionSynchronizationManager.isCurrentTransactionReadOnly();
            log.info("tx readOnly={}", readOnly);
        }
    }
}
