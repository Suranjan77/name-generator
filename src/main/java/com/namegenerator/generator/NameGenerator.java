package com.namegenerator.generator;

import com.namegenerator.constants.FilePathConstants;
import com.namegenerator.knowledge.KnowledgeBase;
import com.namegenerator.utils.KnowledgeBaseIOUtils;

import java.util.Map;
import java.util.Random;

public class NameGenerator {

    private static Random randomUtil;
    private static KnowledgeBase knowledgeBase;
    private static volatile int wordLength;

    public static void init(int length) {
        wordLength = length;
        randomUtil = new Random();
        knowledgeBase = new KnowledgeBaseIOUtils().getKnowledgeBase(FilePathConstants.KNOWLEDGE_BASE_PATH).orElseThrow(() -> new RuntimeException("Failed to read knowledge base. Terminating ..."));
    }

    //Todo: Optimize
    public static String generate() {
        char randomChar = (char) ('a' + Math.abs(randomUtil.nextInt(26)));
        return buildCharSequence(randomChar);
    }

    private static String buildCharSequence(char randomChar) {
        StringBuilder sb = new StringBuilder();
        if (knowledgeBase.getMarkovChain() != null) {
            sb.append(randomChar);
            for (int i = 0; i < wordLength - 1; i++) {
                Map<Character, Double> charDistribution = knowledgeBase.getMarkovChain().get(randomChar);
                if (charDistribution != null) {
                    double randomProb = Math.abs(randomUtil.nextDouble());
                    double cumulativeProbability = 0.0;
                    for (Map.Entry<Character, Double> entry : charDistribution.entrySet()) {
                        cumulativeProbability += entry.getValue();
                        if (randomProb <= cumulativeProbability) {
                            sb.append(entry.getKey());
                            randomChar = entry.getKey();
                            break;
                        }
                    }
                }
            }
        }

        return sb.toString();
    }

    private NameGenerator() {
    }
}
