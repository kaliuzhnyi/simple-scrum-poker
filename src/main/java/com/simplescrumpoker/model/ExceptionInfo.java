package com.simplescrumpoker.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ExceptionInfo {

    Integer code;
    String title;
    String description;

}
