package rgo.cloud.docs.boot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;

import javax.sql.DataSource;

import static rgo.cloud.common.spring.util.TestCommonUtil.runScript;

@TestConfiguration
public class CommonTest {

    @Autowired
    private DataSource h2;

    public void truncateTables() {
        runScript("h2/truncate.sql", h2);
    }
}
