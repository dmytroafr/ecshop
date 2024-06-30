package com.echem.ecshop.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="users")
public class User implements UserDetails {
    private static final String SEQ_NAME = "user_seq";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_NAME)
    @SequenceGenerator(name = SEQ_NAME, sequenceName = SEQ_NAME, allocationSize = 1)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    private String firstName;
    private String lastName;

    @ToString.Exclude
    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    private String phone;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(name = "account_non_expired", nullable = false)
    private boolean accountNonExpired = true;
    @Column(name = "account_non_locked", nullable = false)
    private boolean accountNonLocked = true;
    @Column(name = "credentials_non_expired", nullable = false)
    private boolean credentialsNonExpired = true;
    @Column(name = "enabled", nullable = false)
    private boolean enabled = false;

    @CreationTimestamp
    private LocalDateTime created;

//    @ToString.Exclude
//    @OneToOne (mappedBy = "user",
//            cascade = CascadeType.ALL)
//    private Bucket bucket;
//
//    public void setBucket(Bucket bucket) {
//        bucket.setUser(this);
//        this.bucket = bucket;
//    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() { return username; }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public User(String username, String password, String email, Role role) {
        this.username = username;
        this.email = email;
        this.role = role;
        this.password = password;
    }
}
