package com.wanderlust.bf_groupproject_1.task;

import com.wanderlust.bf_groupproject_1.entity.User;
import com.wanderlust.bf_groupproject_1.repository.PasswordResetTokenRepository;
import com.wanderlust.bf_groupproject_1.repository.UserRepository;
import com.wanderlust.bf_groupproject_1.repository.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserCleanupTask {

    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;

    // Run every day at 3 AM (or simply fixedRate if preferred. Let's use a fixed rate for simplicity)
    @Scheduled(fixedRateString = "3600000") // Run every hour
    @Transactional
    public void cleanupUnverifiedAccounts() {
        log.info("Starting cleanup of unverified accounts...");
        // Delete accounts older than 24 hours that are not enabled
        LocalDateTime cutoff = LocalDateTime.now().minusHours(24);
        List<User> unverifiedUsers = userRepository.findByEnabledFalseAndCreatedAtBefore(cutoff);
        
        int count = 0;
        for (User user : unverifiedUsers) {
            verificationTokenRepository.deleteByUser(user);
            passwordResetTokenRepository.deleteByUser(user);
            userRepository.delete(user);
            count++;
        }
        log.info("Finished cleanup. Deleted {} unverified accounts.", count);
    }
}
