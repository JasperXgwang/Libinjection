package com.client9;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Keyword {

    private static final Logger logger = LoggerFactory.getLogger(Keyword.class);

    HashMap<String, Character> keywordMap = new HashMap<String, Character>();

    Keyword(String filename) {
        String word;
        char type;
        Pattern wordpattern, typepattern;
        Matcher matchedword, matchedtype;

        try {
            Scanner in = new Scanner(new FileReader(filename));
            String line;

            while (in.hasNextLine()) {
                line = in.nextLine();
                wordpattern = Pattern.compile("\\{\"(.*)\"");
                typepattern = Pattern.compile("\'(.*)\'");
                matchedword = wordpattern.matcher(line);
                matchedtype = typepattern.matcher(line);

                while (matchedword.find() && matchedtype.find()) {
                    word = matchedword.group(1);
                    type = matchedtype.group(1).charAt(0);
                    keywordMap.put(word, type);
                }
            }
            in.close();
        } catch (FileNotFoundException ex) {
            logger.error("file not found", ex);
        }
    }

    void printKeywordMap() {
        for (String keyword : keywordMap.keySet()) {
            String keytype = keywordMap.get(keyword).toString();
            logger.info("word: " + keyword + " type: " + keytype);
        }
        logger.info("table size: " + keywordMap.size());
    }
}
