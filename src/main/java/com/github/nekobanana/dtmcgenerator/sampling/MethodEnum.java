package com.github.nekobanana.dtmcgenerator.sampling;

public enum MethodEnum {
    PERFECT_SAMPLING("text1"),
    FORWARD_SAMPLING("text2"),
    FORWARD_COUPLING("text3");

    private final String text;

    MethodEnum(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public static MethodEnum fromString(String text) {
        for (MethodEnum m : MethodEnum.values()) {
            if (m.text.equalsIgnoreCase(text)) {
                return m;
            }
        }
        return PERFECT_SAMPLING;
    }
}
