package com.namegenerator;

import com.namegenerator.constants.FilePathConstants;
import com.namegenerator.generator.NameGenerator;
import com.namegenerator.markov.Markov;

public class ApplicationRunner {
    /**
     * Command line arguments are length of each words and number of words and relearn flag
     *
     * @param args first parameter is length of each words and second parameter is count of words and third parameter is relearn flag
     */
    public static void main(String[] args) {
        int length = 1;
        long count = 1l;
        boolean usePreviousKnowledgeBase = false;
        if (args.length == 3) {
            length = Integer.parseInt(args[0]);
            count = Long.parseLong(args[1]);
            usePreviousKnowledgeBase = Boolean.getBoolean(args[2]);
        }

        Markov markov = new Markov();
        markov.learn(FilePathConstants.DATA_FILE_PATH, usePreviousKnowledgeBase);

        NameGenerator.init(length);
        System.out.println("=".repeat(25));
        System.out.println("\tGenerated Names");
        System.out.println("=".repeat(25));
        for (long i = 0l; i < count; i++) {
            System.out.println(NameGenerator.generate());
        }
    }
}
