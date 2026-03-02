package me.pinkcandy.plugGet.Tools;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
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

    public static int countLetters(String text){
        int count = 0;
        for (int i = 0; i < text.length(); i++) {
            if (Character.isLetter(text.charAt(i)))
                count++;
        }
        return count;
    }

    public static String generateSpaces(int count){
        StringBuilder spaces = new StringBuilder();
        for (int i = 0; i < count; i++) {
            spaces.append(" ");
        }
        return spaces.toString();
    }

    public static String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;

        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static String formatDate(String isoDate) {
        if (isoDate == null) return "";

        LocalDate date = OffsetDateTime.parse(isoDate).toLocalDate();
        return date.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }
}
