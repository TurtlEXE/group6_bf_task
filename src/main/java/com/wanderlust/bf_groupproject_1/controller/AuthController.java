package com.wanderlust.bf_groupproject_1.controller;

import com.wanderlust.bf_groupproject_1.dto.RegisterDTO;
import com.wanderlust.bf_groupproject_1.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/login")
    public String showLoginForm() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("registerDTO", new RegisterDTO());
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid RegisterDTO registerDTO, org.springframework.validation.BindingResult bindingResult, Model model) {
        if (!registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "error.registerDTO", "Passwords do not match");
        }
        try {
            if (bindingResult.hasErrors()) {
                model.addAttribute("registerDTO", registerDTO);
                return "auth/register";
            }
            authService.registerUser(registerDTO);
            return "redirect:/login?success=registered";
        } catch (com.wanderlust.bf_groupproject_1.exception.BusinessException e) {
            if (e.getErrorCode() == com.wanderlust.bf_groupproject_1.enums.ErrorCode.AUTH_USERNAME_EXISTS) {
                bindingResult.rejectValue("username", "error.registerDTO", "Username is already taken");
            } else if (e.getErrorCode() == com.wanderlust.bf_groupproject_1.enums.ErrorCode.AUTH_EMAIL_EXISTS) {
                bindingResult.rejectValue("email", "error.registerDTO", "Email is already registered");
            } else {
                model.addAttribute("errorMessage", e.getMessage());
            }
            model.addAttribute("registerDTO", registerDTO);
            return "auth/register";
        }
    }

    @GetMapping("/verify")
    public String verifyEmail(@RequestParam String token) {
        authService.verifyEmail(token);
        return "redirect:/login?success=verified";
    }

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "auth/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam String email, RedirectAttributes redirectAttributes) {
        authService.forgotPassword(email);
        redirectAttributes.addFlashAttribute("successMessage", "If an account with that email exists, a password reset link has been sent.");
        return "redirect:/forgot-password";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam String token, Model model) {
        model.addAttribute("token", token);
        return "auth/reset-password";
    }

    @PostMapping("/reset-password")
    public String processResetPassword(@RequestParam String token,
                                       @RequestParam String password,
                                       @RequestParam String confirmPassword,
                                       Model model) {
        if (!password.equals(confirmPassword)) {
            model.addAttribute("token", token);
            model.addAttribute("errorMessage", "Passwords do not match");
            return "auth/reset-password";
        }
        if (password.length() < 6 || password.length() > 50) {
            model.addAttribute("token", token);
            model.addAttribute("errorMessage", "Password must be between 6 and 50 characters");
            return "auth/reset-password";
        }
        try {
            authService.resetPassword(token, password);
            return "redirect:/login?success=password_reset";
        } catch (com.wanderlust.bf_groupproject_1.exception.BusinessException e) {
            model.addAttribute("token", token);
            model.addAttribute("errorMessage", "This reset link is invalid or has expired. Please request a new one.");
            return "auth/reset-password";
        }
    }
}
