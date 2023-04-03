package com.base.sbc.config.enums;

/**
 * @author 卞康
 * @date 2023/4/3 11:03:37
 */
public enum BasicNumber {
    /*** 0 */
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
    /*** 值 */
    private final String number;

    BasicNumber(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }
}
