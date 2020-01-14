package com.progmasters.moovsmart.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AuthenticatedUser {

    private List<String> role;
    private Long userId;

    public AuthenticatedUser(MyUserDetails user) {
        this.role = getAllRoles(user.getAuthorities());
        this.userId = user.getUserId();
    }

    private List<String> getAllRoles(Collection<? extends GrantedAuthority> authorities) {
        List<String>role = new ArrayList<>();
        for(GrantedAuthority authority: authorities){
            role.add(authority.getAuthority());
        }
        return role;
    }

    public List<String> getRole() {
        return role;
    }

    public void setRole(List<String> role) {
        this.role = role;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
