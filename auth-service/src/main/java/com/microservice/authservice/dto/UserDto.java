package com.microservice.authservice.dto;

import com.microservice.authservice.model.IdBasedEntity;
import com.microservice.authservice.model.Role;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
public class UserDto extends IdBasedEntity implements Serializable {
    private String username;
    private String email;
    private String password;
    private Set<Role> roles = new HashSet<>();

}
