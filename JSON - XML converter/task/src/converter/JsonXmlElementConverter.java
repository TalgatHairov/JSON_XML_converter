package converter;

class JsonXmlElementConverter {

    //old solution for stage #1
    /*
        // in main() of old solution
        final Scanner sc = new Scanner(System.in);
        String input = sc.nextLine().trim();
        String output;

        if (input.charAt(0) == '<') {
            output = parseXmlToJson(input);
        } else {
            output = parseJsonToXml(input);
        }

        System.out.println(output);
     */

    public static String parseXmlToJson(String in) {
        String out;

        String tagName = in.substring(1, in.indexOf('>'));
        String tagElementContent;

        if (tagName.contains("/")) {
            tagElementContent = "null";
            tagName = tagName.replace("/", "");
            out = "{\"" + tagName + "\" : " + tagElementContent + "}";
        } else {
            tagElementContent = in.substring(tagName.length() + 2, in.lastIndexOf('<'));
            out = "{\"" + tagName + "\" : \"" + tagElementContent + "\"}";
        }

        return out;
    }

    public static String parseJsonToXml(String in) {
        String out;

        String tagName = in.substring(in.indexOf('"') + 1, in.indexOf('"', in.indexOf('"') + 1));

        String tagElementContent;

        if (in.contains("null")) {
            tagElementContent = "null";
        } else {
            tagElementContent = in.substring(in.indexOf('"' , in.indexOf(":")) + 1, in.lastIndexOf('"'));
        }

        if (tagElementContent.equals("null")) {
            out = "<" + tagName + "/>";
        } else {
            out = "<" + tagName + ">" + tagElementContent + "</" + tagName + ">";
        }

        return out;
    }

}
