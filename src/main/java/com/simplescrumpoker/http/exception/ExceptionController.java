package com.simplescrumpoker.http.exception;

import com.simplescrumpoker.http.controller.PackageMarker;
import com.simplescrumpoker.model.ExceptionInfo;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice(basePackageClasses = PackageMarker.class)
class ExceptionController {

//    @ExceptionHandler(RuntimeException.class)
//    public ModelAndView handelException(Exception e) {
//        var exceptionModel = ExceptionInfo.builder()
//                .title("Oops. Something is wrong.")
//                .build();
//
//        var mav = new ModelAndView("exception");
//        mav.addObject("exception", exceptionModel);
//
//        return mav;
//    }
//
//    @ExceptionHandler(NoHandlerFoundException.class)
//    public ModelAndView handelNoHandlerFound() {
//
//        var exceptionModel = ExceptionInfo.builder()
//                .code(404)
//                .title("The page you requested was not found.")
//                .build();
//
//        var mav = new ModelAndView("exception");
//        mav.addObject("exception", exceptionModel);
//
//        return mav;
//    }

}
