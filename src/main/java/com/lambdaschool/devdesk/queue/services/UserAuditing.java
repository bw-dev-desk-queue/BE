package com.lambdaschool.devdesk.queue.services;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserAuditing implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        String uName = "SYSTEM";
        return Optional.of(uName);
    }
}
