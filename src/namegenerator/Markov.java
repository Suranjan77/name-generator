package namegenerator;

import namegenerator.constants.FilePathConstants;
import namegenerator.kb.CharSequencePair;
import namegenerator.kb.KnowledgeBase;

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
        Markov markov = new Markov();
        KnowledgeBase knowledgeBase = markov.learn(FilePathConstants.DATA_FILE_PATH);
        if (knowledgeBase != null) {
            System.out.println(knowledgeBase.toString());
        }

    }

    //todo: Optimization
    public KnowledgeBase learn(final String dataPath) throws IOException {
        Optional<KnowledgeBase> knowledgeBase = getKnowledgeBase();
        if (knowledgeBase.isPresent()) {
            return knowledgeBase.get();
        }

        LOGGER.log(Level.INFO, "Learning from data set ...");

        initialize(dataPath);

        KnowledgeBase kb = new KnowledgeBase();

        List<CharSequencePair> charSequencePairs = new ArrayList<>();
        List<char[]> characters = names.stream().map(String::toCharArray).collect(Collectors.toList());
        characters.forEach(charArray -> {
            if (charArray.length % 2 == 0) {
                charSequencePairs.addAll(createCharPairs(charArray, charArray.length));
            } else {
                charSequencePairs.addAll(createCharPairs(charArray, charArray.length - 1));
            }
        });

        Map<Character, List<Character>> associatedChars = new HashMap<>();
        charSequencePairs.forEach(c -> {
            associatedChars.putIfAbsent(c.getX(), new ArrayList<>());
            associatedChars.computeIfPresent(c.getX(), (k, v) -> {
                v.add(c.getY());
                return v;
            });
        });

        Map<Character, Map<Character, Double>> knowledge = new HashMap<>();
        associatedChars.forEach((k, v) -> {
            knowledge.putIfAbsent(k, new HashMap<>());
            knowledge.computeIfPresent(
                    k,
                    (key, value) -> v.stream().collect(
                            Collectors.groupingBy(e -> e, Collectors.collectingAndThen(
                                    Collectors.counting(),
                                    count -> (double) count / (double) v.size())
                            )
                    )
            );
        });

        kb.setMarkovChain(knowledge);
        long charCount = 0l;
        for (char[] c : characters) {
            charCount += c.length;
        }
        kb.setTotalCharacters(charCount);
        kb.setTotalWords(names.size());
        return kb;
    }

    private List<CharSequencePair> createCharPairs(final char[] charArray, final int limit) {
        List<CharSequencePair> charSequencePairs = new ArrayList<>();
        for (int i = 0; i < limit - 1; i = i + 2) {
            CharSequencePair charSequencePair = new CharSequencePair(charArray[i], charArray[i + 1]);
            charSequencePairs.add(charSequencePair);
        }
        return charSequencePairs;
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
