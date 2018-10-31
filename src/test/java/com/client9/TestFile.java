package com.client9;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Scanner;


public class TestFile {

    private static final Logger logger = LoggerFactory.getLogger(TestFile.class);

    public long testfile(String inputfile, String outputfile, boolean decode, boolean time) {
        long startTime;
        long endTime;
        long sum = 0;
        int count = 0;

        Scanner in = null;
        PrintWriter out = null;

        try {
            in = new Scanner(new FileReader(inputfile));
            out = new PrintWriter(outputfile, "UTF-8");
            Libinjection libinjection = Libinjection.getInstance();

            while (in.hasNextLine()) {
                String line = in.nextLine();

                /*
                 * urldecode
                 */
                if (decode) {
                    try {
                        line = URLDecoder.decode(line, "UTF-8");
                    } catch (UnsupportedEncodingException ex) {
                        ex.printStackTrace();
                    }
                }

                /*
                 *  test and add result to outputfile
                 */
                if (time) {
                    startTime = System.currentTimeMillis();
                    libinjection.libinjection_sqli(line);
                    endTime = System.currentTimeMillis();
                    sum += (endTime - startTime);
                    count++;
                } else {
                    libinjection.libinjection_sqli(line);
                }

                out.println(libinjection.getOutput());
            }
            if (time) {
                logger.info("Total: " + sum + " Average: " + sum / count);
            }
            return sum;

        } catch (Exception ex) {
            logger.error("file not found or unsupported encoding", ex);
        } finally {
            if (in != null) {
                try {
                    in.close();
                    in = null;
                } catch (Exception ex3) {
                    ex3.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                    out = null;
                } catch (Exception ex3) {
                    ex3.printStackTrace();
                }
            }

        }
        return Integer.MIN_VALUE;
    }

}
