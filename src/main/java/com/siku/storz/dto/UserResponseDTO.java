package com.siku.storz.dto;

import com.siku.storz.model.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserResponseDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String username;
    private String email;
    List<Role> roles;
}
