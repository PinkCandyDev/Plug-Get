package me.pinkcandy.plugGet;

import java.util.ArrayList;
import java.util.List;

public class TextTools {

    public static List<String> wrapText(String text, int maxWidth) {
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

    public static int countLetters(List<String[]> texts, int ix){
        List<Integer> letters = new ArrayList<Integer>();
        for (String[] text : texts) {
            int count = 0;
            for (int i = 0; i < text[ix].length(); i++) {
                if (Character.isLetter(text[ix].charAt(i)))
                    count++;
            }
            letters.add(count);
        }

        int maxValue = 0;

        for (Integer integer : letters) {
            if (integer > maxValue)
                maxValue = integer;
        }

        return maxValue;
    }

    public static String makeSpaces(int count){
        StringBuilder spaces = new StringBuilder();
        for (int i = 0; i < count; i++) {
            spaces.append(" ");
        }
        return spaces.toString();
    }
}
