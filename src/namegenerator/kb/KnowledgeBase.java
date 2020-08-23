package namegenerator.kb;

import java.io.Serializable;
import java.util.Map;

public class KnowledgeBase implements Serializable {
    private static final long serialVersionUID = 43502L;

    private Map<Character, Map<Character, Double>> markovChain;
    private long totalCharacters;
    private long totalWords;

    public long getTotalCharacters() {
        return totalCharacters;
    }

    public void setTotalCharacters(long totalCharacters) {
        this.totalCharacters = totalCharacters;
    }

    public long getTotalWords() {
        return totalWords;
    }

    public void setTotalWords(long totalWords) {
        this.totalWords = totalWords;
    }

    public Map<Character, Map<Character, Double>> getMarkovChain() {
        return markovChain;
    }

    public void setMarkovChain(Map<Character, Map<Character, Double>> markovChain) {
        this.markovChain = markovChain;
    }

    @Override
    public String toString() {
        return "KnowledgeBase{" +
                "\nmarkovChain=[\n" + toMarkovChainString() + "]" +
                "\n, totalCharacters=" + totalCharacters +
                "\n, totalWords=" + totalWords +
                "\n}";
    }

    private String toMarkovChainString() {
        StringBuilder sb = new StringBuilder();
        markovChain.forEach((k, v) -> {
            sb.append("\t").append(k).append(": {");
            v.forEach((key, val) -> {
                sb.append(key).append(" : ").append(val).append(", ");
            });
            sb.deleteCharAt(sb.length() - 1);
            sb.deleteCharAt(sb.length() - 1);
            sb.append(" }\n");
        });
        return sb.toString();
    }
}
