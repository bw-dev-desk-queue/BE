package com.lambdaschool.devdesk.queue.services;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserAuditing implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        String uName;
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth != null)
        {
            uName = auth.getName();
        }
        else
        {
            uName = "SYSTEM";
        }
        return Optional.of(uName);
    }
}
