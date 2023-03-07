package ru.practicum.explorewithme.main.server.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class KCRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private final String ROLES_BY_REALM_FIELD ="realm_access";

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Map<String, Object> realmAccess = (Map<String, Object>) jwt.getClaims().get(ROLES_BY_REALM_FIELD);

        if (realmAccess == null || realmAccess.isEmpty()) {
            return new ArrayList<>();
        }

        return ((List<String>) realmAccess.get("roles"))
                .stream().map(roleName -> "ROLE_" + roleName.toUpperCase())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
