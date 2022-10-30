package com.suriya.util;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class PasswordGeneratorTest {

    private static PasswordGenerator passwordGenerator = null;

    @Test
    public void passwordGeneratorTest() {
        passwordGenerator = new PasswordGenerator.Builder()
                .build();
        System.out.println(passwordGenerator.generate(16));
    }

    @Test
    public void passwordGeneratorTest_withCustom() {
        passwordGenerator = new PasswordGenerator.Builder()
                .custom("1991", 4)
                .custom("Suriya", 12)
                .lower()
                .build();
        System.out.println(passwordGenerator.generate(10));
    }

    @Test
    public void passwordGeneratorTest_forName() {
        passwordGenerator = new PasswordGenerator.Builder()
                .lower()
                .build();
        System.out.println(passwordGenerator.generate(10));
    }
    
    

}
