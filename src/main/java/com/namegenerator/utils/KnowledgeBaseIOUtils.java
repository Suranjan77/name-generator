package com.namegenerator.utils;

import com.namegenerator.ApplicationRunner;
import com.namegenerator.knowledge.KnowledgeBase;

import java.io.*;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class KnowledgeBaseIOUtils {

    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public Optional<KnowledgeBase> getKnowledgeBase(String path) {
        try (FileInputStream fis = new FileInputStream(new File(Objects.requireNonNull(ApplicationRunner.class.getClassLoader().getResource(".")).getFile() + "/" + path))) {
            try (ObjectInputStream objectInputStream = new ObjectInputStream(fis)) {
                KnowledgeBase kb = (KnowledgeBase) objectInputStream.readObject();
                return Optional.of(kb);
            }
        } catch (IOException | ClassNotFoundException e) {
            LOGGER.log(Level.WARNING, "Could not read knowledge-base from file ... ");
            return Optional.empty();
        }
    }

    public void saveKnowledgeBase(KnowledgeBase kb, String path) {
        try (FileOutputStream fos = new FileOutputStream((Objects.requireNonNull(ApplicationRunner.class.getClassLoader().getResource(".")).getFile() + "/" + path))) {
            try (ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(kb);
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Could not write knowledge-base to file ... ");
        }
    }

}
