package fa.hust.utils;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
@Component
public class StringUtil {
    private static final String alpha = "abcdefghijklmnopqrstuvwxyz"; // a-z
    private static final String alphaUpperCase = alpha.toUpperCase(); // A-Z
    private static final String digits = "0123456789"; // 0-9
    private static final String ALPHA_NUMERIC = alpha + alphaUpperCase + digits;
    private static Random generator = new Random();



    public int randomNumber(int min, int max) {
        return generator.nextInt((max - min) + 1) + min;
    }

    public String randomPassword(int size) {
        List<String> result = new ArrayList<>();
        Consumer<String> appendChar = s -> {
            int number = randomNumber(0, s.length() - 1);
            result.add("" + s.charAt(number));
        };
        appendChar.accept(digits);
        while (result.size() < size) {
            appendChar.accept(ALPHA_NUMERIC);
        }
        Collections.shuffle(result, generator);
        return String.join("", result);
    }
    public String randomCode(int size) {
        List<String> result = new ArrayList<>();
        Consumer<String> appendChar = s -> {
            int number = randomNumber(0, s.length() - 1);
            result.add("" + s.charAt(number));
        };
        while (result.size() < size) {
            appendChar.accept(digits);
        }
        Collections.shuffle(result, generator);
        return String.join("", result);
    }
}
