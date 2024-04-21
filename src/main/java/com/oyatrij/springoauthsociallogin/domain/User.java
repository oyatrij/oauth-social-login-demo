package com.oyatrij.springoauthsociallogin.domain;

import com.oyatrij.springoauthsociallogin.dto.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User implements Serializable {

    @Id
    private Long id;

    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
}