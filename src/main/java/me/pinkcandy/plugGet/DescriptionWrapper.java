package me.pinkcandy.plugGet;

import java.util.ArrayList;
import java.util.List;

public class DescriptionWrapper {

    public static List<String> wrap(String text, int maxWidth) {
        text = text.replace("\r", " ").replace("\n", " ");

        List<String> result = new ArrayList<>();
        String[] words = text.split(" ");

        StringBuilder line = new StringBuilder();

        for (String word : words) {
            if (word.isEmpty()) continue;

            if (line.length() + word.length() + 1 > maxWidth) {
                result.add(line.toString());
                line = new StringBuilder(word);
            } else {
                if (line.length() == 0) line = new StringBuilder(word);
                else line.append(" ").append(word);
            }
        }

        if (line.length() > 0) result.add(line.toString());
        return result;
    }
}
