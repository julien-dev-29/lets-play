package com.letsplay.dto;

import com.letsplay.model.Role;
import java.util.List;

public class UserResponse {

    private String id;
    private String username;
    private String email;
    private List<Role> roles;

    public UserResponse() {}

    public UserResponse(String id, String username, String email, List<Role> roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public List<Role> getRoles() { return roles; }
    public void setRoles(List<Role> roles) { this.roles = roles; }
}