package com.base.sbc.config.enums;

/**
 * @author 24796
 */

public enum BasicNumber {
    ZERO("0"),
    ONE("1"),
    TWO("2"),
    THREE("3"),
    FOUR("4"),
    FIVE("5"),
    SIX("6"),
    SEVEN("7"),
    EIGHT("8"),
    NINE("9");

    private String number;

    BasicNumber(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }
}
