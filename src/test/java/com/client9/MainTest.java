package com.client9;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
        List<String> list = new ArrayList<String>();
        list.add("admin=1");
        list.add("admin' OR 1=1--");
        list.add("-1' and 1=1 union/* foo */select load_file('/etc/passwd')--");
        list.add("'and 1=1");
        list.add("' UNION ALL SELECT NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL--");
        list.add("*/UNION SELECT password FROM users--");
        list.add("?id=sleep(9999)");
        list.add("http://testphp.vulnweb.com/main.php?SmallClass=' union select * from news where 1=2 and ''='");

        for (String input : list) {
            libinjection.libinjection_sqli(input);
            logger.info(libinjection.getOutput() + ">>>>>>" + input);
        }
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
            logger.info(test[i] + "---->" + libinjection.getOutput());
        }

        /* let jvm optimize for 100000 iterations */
        int iterations = 1000000;
        /* time */
        double start = System.currentTimeMillis();
        for (int c = 0; c < iterations; c++) {
            libinjection.libinjection_sqli(test[c % 8]);
        }
        double end = System.currentTimeMillis();

        double total = (end - start) / 1000.0;
        double tps = iterations / total;
        logger.info("iterations:{} total time:{} sec  tps:{}", iterations, total, (int) tps);
    }

    private String getFilePath(String fileName) {
        String filePath = this.getClass().getResource(fileName).getPath();
        if (!new File(filePath).exists()) {
            throw new RuntimeException("file is not found");
        }
        return filePath;
    }

}
