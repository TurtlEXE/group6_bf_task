package com.wanderlust.bf_groupproject_1.exception;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public String handleBusinessException(BusinessException ex, HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        String redirectUrl = referer != null ? referer : "/";
        
        // Remove existing error/success parameters to avoid appending multiple times
        if (redirectUrl.contains("?error=")) {
            redirectUrl = redirectUrl.substring(0, redirectUrl.indexOf("?error="));
        } else if (redirectUrl.contains("&error=")) {
            redirectUrl = redirectUrl.substring(0, redirectUrl.indexOf("&error="));
        }
        
        if (redirectUrl.contains("?")) {
            return "redirect:" + redirectUrl + "&error=" + ex.getErrorCode().getCode();
        }
        return "redirect:" + redirectUrl + "?error=" + ex.getErrorCode().getCode();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        String redirectUrl = referer != null ? referer : "/";
        if (redirectUrl.contains("?error=")) {
            redirectUrl = redirectUrl.substring(0, redirectUrl.indexOf("?error="));
        }
        if (redirectUrl.contains("?")) {
            return "redirect:" + redirectUrl + "&error=GEN_001";
        }
        return "redirect:" + redirectUrl + "?error=GEN_001";
    }

    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDeniedException(AccessDeniedException ex) {
        return "redirect:/login?error=GEN_003";
    }
    
    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception ex, HttpServletRequest request) {
        // Log the exception in real app
        ex.printStackTrace();
        String referer = request.getHeader("Referer");
        String redirectUrl = referer != null ? referer : "/";
        if (redirectUrl.contains("?error=")) {
            redirectUrl = redirectUrl.substring(0, redirectUrl.indexOf("?error="));
        }
        if (redirectUrl.contains("?")) {
            return "redirect:" + redirectUrl + "&error=GEN_002";
        }
        return "redirect:" + redirectUrl + "?error=GEN_002";
    }
}
