package com.wanderlust.bf_groupproject_1.service;

import com.wanderlust.bf_groupproject_1.dto.RegisterDTO;
import com.wanderlust.bf_groupproject_1.entity.PasswordResetToken;
import com.wanderlust.bf_groupproject_1.entity.User;
import com.wanderlust.bf_groupproject_1.entity.VerificationToken;
import com.wanderlust.bf_groupproject_1.enums.ErrorCode;
import com.wanderlust.bf_groupproject_1.enums.Role;
import com.wanderlust.bf_groupproject_1.exception.BusinessException;
import com.wanderlust.bf_groupproject_1.repository.PasswordResetTokenRepository;
import com.wanderlust.bf_groupproject_1.repository.UserRepository;
import com.wanderlust.bf_groupproject_1.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final VerificationTokenRepository tokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Value("${app.base-url}")
    private String baseUrl;

    @Transactional
    public void registerUser(RegisterDTO dto) {
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new BusinessException(ErrorCode.AUTH_PASSWORD_MISMATCH);
        }

        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new BusinessException(ErrorCode.AUTH_USERNAME_EXISTS);
        }

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new BusinessException(ErrorCode.AUTH_EMAIL_EXISTS);
        }

        User user = User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .role(Role.ROLE_USER)
                .enabled(false)
                .emailVerified(false)
                .build();

        userRepository.save(user);

        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = VerificationToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusHours(24))
                .build();

        tokenRepository.save(verificationToken);

        emailService.sendVerificationEmail(user.getEmail(), token, baseUrl);
    }

    @Transactional
    public void verifyEmail(String token) {
        VerificationToken verificationToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new BusinessException(ErrorCode.AUTH_TOKEN_INVALID));

        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new BusinessException(ErrorCode.AUTH_TOKEN_EXPIRED);
        }

        User user = verificationToken.getUser();
        user.setEmailVerified(true);
        user.setEnabled(true);
        userRepository.save(user);

        // Optional: delete token after verification
        tokenRepository.delete(verificationToken);
    }

    @Transactional
    public void forgotPassword(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            // Don't reveal whether the email exists - silently return
            return;
        }

        User user = userOpt.get();

        // Delete any existing reset tokens for this user
        passwordResetTokenRepository.deleteByUser(user);

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusHours(1))
                .build();

        passwordResetTokenRepository.save(resetToken);

        emailService.sendPasswordResetEmail(user.getEmail(), token, baseUrl);
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new BusinessException(ErrorCode.AUTH_TOKEN_INVALID));

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            passwordResetTokenRepository.delete(resetToken);
            throw new BusinessException(ErrorCode.AUTH_TOKEN_EXPIRED);
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        passwordResetTokenRepository.delete(resetToken);
    }
}

