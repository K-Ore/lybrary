package com.backend.lybrary.dto.admin;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class UserAdminResponse {

    private Long id;
    private String email;
    private Set<String> roles;
    private boolean blocked;
    private boolean deleted;
}
