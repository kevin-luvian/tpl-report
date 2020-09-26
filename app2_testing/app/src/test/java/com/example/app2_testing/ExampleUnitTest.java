package com.example.app2_testing;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void mainConcat_isWorking() {
        String inp1 = "Hello";
        String inp2 = "World";
        String expected = "Hello - World";
        String concated = MainActivity.concatString(inp1, inp2);
        System.out.println("Concated string: " + concated);
        assertEquals(expected, concated);
    }
}