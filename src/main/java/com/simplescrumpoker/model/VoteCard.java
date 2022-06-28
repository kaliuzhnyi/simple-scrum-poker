package com.simplescrumpoker.model;

public enum VoteCard {
    VALUE_UNKNOWN,
    VALUE_0,
    VALUE_1,
    VALUE_2,
    VALUE_3,
    VALUE_5,
    VALUE_8,
    VALUE_13,
    VALUE_20,
    VALUE_40,
    VALUE_100;

    public String textRepresentation() {
        return switch (this) {
            case VALUE_UNKNOWN -> "?";
            case VALUE_0 -> "0";
            case VALUE_1 -> "1";
            case VALUE_2 -> "2";
            case VALUE_3 -> "3";
            case VALUE_5 -> "5";
            case VALUE_8 -> "8";
            case VALUE_13 -> "13";
            case VALUE_20 -> "20";
            case VALUE_40 -> "40";
            case VALUE_100 -> "100";
        };
    }

}
