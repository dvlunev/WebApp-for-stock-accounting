package me.lunev.coursework3.model;

public enum Size {

    SIZE_36(36),
    SIZE_36_5(36.5f),
    SIZE_37(37),
    SIZE_37_5(37.5f),
    SIZE_38(38f);

    private float numberSize;

    Size(float numberSize) {
        this.numberSize = numberSize;
    }

    public double getNumberSize() {
        return numberSize;
    }

    public static Size findByNumberSize(float numberSize) {
        for (Size size : values()) {
            if (size.getNumberSize() == numberSize) {
                return size;
            }
        }
        return null;
    }
}
