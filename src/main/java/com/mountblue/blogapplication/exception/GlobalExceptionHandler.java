//package com.mountblue.blogapplication.exception;
//
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.http.HttpStatus;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//
//import java.time.LocalDateTime;
//
//@ControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(Exception.class)
//    public String handleException(Exception ex, HttpServletRequest request, Model model) {
//        ErrorResponse errorResponse = new ErrorResponse(
//                LocalDateTime.now(),
//                HttpStatus.INTERNAL_SERVER_ERROR.value(),
//                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
//                ex.getMessage(),
//                request.getRequestURI()
//        );
//
//        model.addAttribute("error", errorResponse);
//        return "error-page";
//    }
//
//    // You can also handle specific exceptions separately
//    @ExceptionHandler(RuntimeException.class)
//    public String handleRuntimeException(RuntimeException ex, HttpServletRequest request, Model model) {
//        ErrorResponse errorResponse = new ErrorResponse(
//                LocalDateTime.now(),
//                HttpStatus.BAD_REQUEST.value(),
//                "Bad Request",
//                ex.getMessage(),
//                request.getRequestURI()
//        );
//
//        model.addAttribute("error", errorResponse);
//        return "error-page";
//    }
//}
