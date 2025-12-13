package com.FourAM.Auth_Nexus.DTO;

import com.FourAM.Auth_Nexus.Enums.Provider;
import com.FourAM.Auth_Nexus.Model.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private UUID id;
    private String email;
    private String username;
    private String password;
    private String image;
    private boolean enabled = true;
    private Instant createdAt;
    private Instant updatedAt;
    private Provider provider = Provider.LOCAL;
    private Set<RoleDto> roles = new HashSet<>();
}
