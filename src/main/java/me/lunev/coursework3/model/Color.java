package me.lunev.coursework3.model;

public enum Color {

    BlACK("black"),
    WHITE("white"),
    RED("red"),
    YELLOW("yellow"),
    COLORFUL("colorful");

    private final String textColor;

    Color(String textColor) {
        this.textColor = textColor;
    }

    public String getTextColor() {
        return textColor;
    }

    public static Color findByTextColor(String textColor) {
        for (Color color : values()) {
            if (color.getTextColor().equals(textColor)) {
                return color;
            }
        }
        return null;
    }
}
