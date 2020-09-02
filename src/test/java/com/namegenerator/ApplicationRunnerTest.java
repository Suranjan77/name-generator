package com.namegenerator;

import org.junit.jupiter.api.Test;

public class ApplicationRunnerTest {
    @Test
    public void nameGeneratorTest() {
        String[] args = new String[]{
                "6",
                "20",
                "false"
        };
        ApplicationRunner.main(args);
    }
}
