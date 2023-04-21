package com.microservice.authservice.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
public class UserConfig implements AuditorAware<String> {

	@Override
	public Optional<String> getCurrentAuditor() {
		String name = "User001";
		if (Objects.nonNull(SecurityContextHolder.getContext())
				&& Objects.nonNull(SecurityContextHolder.getContext().getAuthentication())
				&& SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
			name = SecurityContextHolder.getContext().getAuthentication().getName();
		}
		return Optional.ofNullable(name);
	}

	public String getUserName() {
		Optional<String> currentAuditor = getCurrentAuditor();
		if (!currentAuditor.isEmpty() && currentAuditor.isPresent()) {
			return currentAuditor.get();
		} else {
			return "";
		}
	}

}