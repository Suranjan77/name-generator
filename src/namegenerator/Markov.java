package namegenerator;

import namegenerator.constants.FilePathConstants;
import namegenerator.kb.KnowledgeBase;
import namegenerator.kb.TwoCharSequence;

import java.io.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Markov {

    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private final String currentDir = System.getProperty("user.dir");
    private Set<String> names;

    private Markov() {
    }

    public static void main(String[] args) throws IOException {

    }

    public KnowledgeBase learn(final String dataPath) throws IOException {
        Optional<KnowledgeBase> knowledgeBase = getKnowledgeBase();
        if (knowledgeBase.isPresent()) {
            return knowledgeBase.get();
        }

        LOGGER.log(Level.INFO, "Learning from data set ...");

        initialize(dataPath);

        KnowledgeBase kb = new KnowledgeBase();
        Map<Character, Map<Character, Double>> knowledge = new HashMap<>();

        List<TwoCharSequence> twoCharSequences = new ArrayList<>();
        List<char[]> characters = names.stream().map(String::toCharArray).collect(Collectors.toList());
        characters.forEach(charArray -> {
            if (charArray.length % 2 == 0) {
                twoCharSequences.addAll(createCharPairs(charArray, charArray.length));
            } else {
                twoCharSequences.addAll(createCharPairs(charArray, charArray.length - 1));
            }
        });

        return kb;
    }

    private List<TwoCharSequence> createCharPairs(final char[] charArray, final int limit) {
        List<TwoCharSequence> twoCharSequences = new ArrayList<>();
        for (int i = 0; i < limit - 1; i = i + 2) {
            TwoCharSequence twoCharSequence = new TwoCharSequence(charArray[i], charArray[i + 1]);
            twoCharSequences.add(twoCharSequence);
        }
        return twoCharSequences;
    }

    private Optional<KnowledgeBase> getKnowledgeBase() {
        String path = currentDir + "/" + FilePathConstants.KNOWLEDGE_BASE_PATH;
        try (FileInputStream fis = new FileInputStream(new File(path))) {
            try (ObjectInputStream objectInputStream = new ObjectInputStream(fis)) {
                LOGGER.log(Level.INFO, "Reading Knowledge base from file ... ");
                KnowledgeBase kb = (KnowledgeBase) objectInputStream.readObject();
                return Optional.of(kb);
            }
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.log(Level.WARNING, "Could not read knowledge-base from file ... ");
            return Optional.empty();
        }
    }

    private void initialize(final String filePath) throws IOException {
        if (null == names || names.isEmpty()) {
            String path = currentDir + filePath;
            File file = new File(path);
            LOGGER.log(Level.INFO, "Initializing Data Set  from {0}", path);
            try (FileReader fileReader = new FileReader(file)) {
                try (BufferedReader bufferedReader = new BufferedReader(fileReader)) {
                    names = new HashSet<>();
                    String st;
                    while ((st = bufferedReader.readLine()) != null) {
                        names.add(st.toLowerCase());
                    }
                }
            }
        }
    }

}
