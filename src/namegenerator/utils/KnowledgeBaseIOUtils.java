package namegenerator.utils;

import namegenerator.kb.KnowledgeBase;

import java.io.*;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class KnowledgeBaseIOUtils {

    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public static Optional<KnowledgeBase> getKnowledgeBase(String path) {
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

    public static void saveKnowledgeBase(KnowledgeBase kb, String path) {
        try (FileOutputStream fos = new FileOutputStream(path)) {
            try (ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                LOGGER.log(Level.INFO, "Writing Knowledge base to file ... ");
                oos.writeObject(kb);
            }
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Could not write knowledge-base to file ... ");
        }
    }

    private KnowledgeBaseIOUtils() {
    }
}
