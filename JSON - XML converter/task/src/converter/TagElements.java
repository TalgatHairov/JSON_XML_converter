package converter;

public enum TagElements {
    ELEMENT_NAME ("element"),
    ELEMENT_CONTENT ("content"),
    ATTRIBUTE_NAME ("attribute_name"),
    ATTRIBUTE_VALUE ("attribute_value");

    private String element;

    TagElements(String name) {
        this.element = name;
    }

    @Override
    public String toString() {
        return element;
    }


}
