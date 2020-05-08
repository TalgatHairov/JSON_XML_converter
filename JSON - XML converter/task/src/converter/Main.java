package converter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        StringBuilder output = new StringBuilder();
        File file = new File("test.txt");

        StringBuilder line = new StringBuilder();
        try (Scanner sc = new Scanner(file)){
            line = new StringBuilder(sc.nextLine().trim());

            boolean isXml = true;
            if (line.charAt(0) == '{') {
                isXml = false;
            }

            while (sc.hasNextLine()) {
                line.append(sc.nextLine().trim());
            }

            if (isXml) {
                output.append(xmlToJson(line));
            } else {
                output.append(jsonToXml(line));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println(output);
    }

    private static StringBuilder xmlToJson(StringBuilder line) {
        HashMap<String, String> tag = new HashMap<>();

        Matcher matcher = Pattern.compile("(?<=<)\\w+").matcher(line);

        if (matcher.find()) {
            tag.put(TagElements.ELEMENT_NAME.toString(), matcher.group());
        }

        if (!line.toString().contains("/>")) {
            matcher.usePattern(Pattern.compile("[\\w|\\s]+(?=<\\/)"));

            if (matcher.find()) {
                tag.put(TagElements.ELEMENT_CONTENT.toString(), matcher.group());
            }
        }

    matcher = Pattern.compile("\\w+(?= = )").matcher(line);

    Matcher valueMatcher = Pattern.compile("(?<=\")\\w+(?=\")").matcher(line);

        while (matcher.find() && valueMatcher.find()) {
            tag.put(matcher.group(), valueMatcher.group());
        }

        return hashMapToJsonString(tag);
    }

    private static StringBuilder hashMapToJsonString(HashMap<String, String> tag) {
        StringBuilder result = new StringBuilder("{\"");

        result.append(tag.get(TagElements.ELEMENT_NAME.toString()) + "\": {");

        for (Map.Entry<String, String> element : tag.entrySet()) {
            if (element.getKey().equals(TagElements.ELEMENT_NAME.toString()) || element.getKey().equals(TagElements.ELEMENT_CONTENT.toString())) {
                continue;
            } else {
        result.append("\"@" + element.getKey() + "\" : \"" + element.getValue() + "\" ,");
            }
        }

        if (tag.get(TagElements.ELEMENT_CONTENT.toString()) != null) {
            result.append("\"#" + tag.get(TagElements.ELEMENT_NAME.toString())
                    + "\" : \"" + tag.get(TagElements.ELEMENT_CONTENT.toString()) + "\"");
        } else {
            result.append("\"#" + tag.get(TagElements.ELEMENT_NAME.toString())
                    + "\" : " + tag.get(TagElements.ELEMENT_CONTENT.toString()));
        }

        result.append("} }");

        return result;
    }

    private static StringBuilder jsonToXml(StringBuilder line) {
        HashMap<String, String> tag = new HashMap<>();
        Matcher keyMatcher = Pattern.compile("(?<=\\{\")\\w+").matcher(line);

        if (keyMatcher.find()) {
            tag.put(TagElements.ELEMENT_NAME.toString(), keyMatcher.group());
        }

        keyMatcher = Pattern.compile("(?<= : )\"?[\\w\\s\\d]*\"?(?=})").matcher(line);

        if (keyMatcher.find()) {
            tag.put(TagElements.ELEMENT_CONTENT.toString(), keyMatcher.group());
        }

        // pattern for matching attribute
        keyMatcher = Pattern.compile("(?<=\"@)\\w+\\s?\\w*(?=\")").matcher(line);

        // pattern for getting attribute value
        Matcher valueMatcher = Pattern.compile("(?<=\" : )\"?\\w*\\s?\\w*\"?(?=,)").matcher(line);

        while (keyMatcher.find() && valueMatcher.find()) {
            tag.put(keyMatcher.group(), valueMatcher.group());
        }

        return hashMapToXmlString(tag);
    }


    private static StringBuilder hashMapToXmlString(HashMap<String, String> tag) {
        StringBuilder result = new StringBuilder("<");

        result.append(tag.get(TagElements.ELEMENT_NAME.toString()) + " ");
        for (Map.Entry<String, String> element : tag.entrySet()){
            if (element.getKey().equals(TagElements.ELEMENT_NAME.toString()) || element.getKey().equals(TagElements.ELEMENT_CONTENT.toString())) {
                continue;
            } else {
                result.append(element.getKey() + "=\"" + element.getValue().replaceAll("\"", "") + "\" ");
            }
        }

        if (tag.get(TagElements.ELEMENT_CONTENT.toString()).equals("null")) {
            result.append("/>");
        } else {
            result.append(">" + tag.get(TagElements.ELEMENT_CONTENT.toString()) + "</" + tag.get(TagElements.ELEMENT_NAME.toString()) + ">");
        }

        return result;
    }

}
