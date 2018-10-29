package com.client9;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class MainTest {

    private static final Logger logger = LoggerFactory.getLogger(MainTest.class);

    private Libinjection libinjection;

    @Before
    public void before() {
        libinjection = new Libinjection();
    }

    @Test
    public void testStr() {
        /* test a string */
        String input = "admin' OR 1=1--";
        input = "admin=1";
        input = "-1' and 1=1 union/* foo */select load_file('/etc/passwd')--";
        input = "'and 1=1"; //s&1
        input = "' UNION ALL SELECT NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL--";

        boolean issqli = libinjection.libinjection_sqli(input);

        logger.info(libinjection.getOutput());
    }

    @Test
    public void testFile() {
        /* test a file and output its results, with options to urldecode and time */
        TestFile t = new TestFile();
        t.testfile(getFilePath("/data/sqli.txt"), getFilePath("/data/sqli.txt.output"), true, false);
    }

    /**
     * 性能测试
     */
    @Test
    public void testPerformance() {
        String[] test = {
                "123 LIKE -1234.5678E+2;",
                "APPLE 19.123 'FOO' \"BAR\"",
                "/* BAR */ UNION ALL SELECT (2,3,4)",
                "1 || COS(+0X04) --FOOBAR",
                "dog apple @cat banana bar",
                "dog apple cat \"banana \'bar",
                "102 TABLE CLOTH",
                "(1001-'1') union select 1,2,3,4 from credit_cards"
        };

        /* print output for above inputs */
        for (int i = 0; i < test.length; i++) {
            libinjection.libinjection_sqli(test[i]);
            logger.info(libinjection.getOutput());
        }

        /* let jvm optimize for 100000 iterations */
        for (int c = 0; c < 100000; c++) {
            libinjection.libinjection_sqli(test[c % 8]);
        }

        /* time */
        double start = System.currentTimeMillis();
        for (int c = 0; c < 1000000; c++) {
            libinjection.libinjection_sqli(test[c % 8]);
        }
        double end = System.currentTimeMillis();

        double total = (end - start) / 1000.0;
        double tps = 1000000.0 / total;
        logger.info("iterations:{} total time:{} sec  tps:{}", 10000000, total, (int) tps);
    }

    private String getFilePath(String fileName) {
        String filePath = this.getClass().getResource(fileName).getPath();
        if (!new File(filePath).exists()) {
            throw new RuntimeException("file is not found");
        }
        return filePath;
    }

}
