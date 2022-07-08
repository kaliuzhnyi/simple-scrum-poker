package com.simplescrumpoker.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum VoteCard {
    VALUE_UNKNOWN("?"),
    VALUE_0("0"),
    VALUE_1("1"),
    VALUE_2("2"),
    VALUE_3("3"),
    VALUE_5("5"),
    VALUE_8("8"),
    VALUE_13("13"),
    VALUE_20("20"),
    VALUE_40("40"),
    VALUE_100("100");

    @Getter
    private final String text;

}
