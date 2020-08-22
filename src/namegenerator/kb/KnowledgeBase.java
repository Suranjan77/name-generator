package namegenerator.kb;

import java.io.Serializable;
import java.util.Map;

public class KnowledgeBase implements Serializable {
    private static final long serialVersionUID = 43502L;

    private Map<Character, Map<Character, Double>> markovChain;
    private int totalCharacters;
    private int totalWords;

    public int getTotalCharacters() {
        return totalCharacters;
    }

    public void setTotalCharacters(int totalCharacters) {
        this.totalCharacters = totalCharacters;
    }

    public int getTotalWords() {
        return totalWords;
    }

    public void setTotalWords(int totalWords) {
        this.totalWords = totalWords;
    }

    public Map<Character, Map<Character, Double>> getMarkovChain() {
        return markovChain;
    }

    public void setMarkovChain(Map<Character, Map<Character, Double>> markovChain) {
        this.markovChain = markovChain;
    }
}
