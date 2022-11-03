package com.suriya.util;

import org.junit.jupiter.api.Test;

public class RandomStringGeneratorTest {

    private static RandomStringGenerator randomStringGenerator = null;

    @Test
    public void passwordGeneratorTest() {
        randomStringGenerator = new RandomStringGenerator.Builder()
                .build();
        System.out.println(randomStringGenerator.generate(16));
    }

    @Test
    public void passwordGeneratorTest_withCustom() {
        randomStringGenerator = new RandomStringGenerator.Builder()
                .custom("1991", 4)
                .custom("Suriya", 12)
                .lower()
                .build();
        System.out.println(randomStringGenerator.generate(10));
    }

    @Test
    public void passwordGeneratorTest_forName() {
        randomStringGenerator = new RandomStringGenerator.Builder()
                .lower()
                .build();
        System.out.println(randomStringGenerator.generate(10));
    }
    
    

}
