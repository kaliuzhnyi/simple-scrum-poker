package com.simplescrumpoker.http.exception;

import com.simplescrumpoker.http.rest.PackageMarker;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackageClasses = PackageMarker.class)
public class RestExceptionController {
}
