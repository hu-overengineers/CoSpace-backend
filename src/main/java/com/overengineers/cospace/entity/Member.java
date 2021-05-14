package com.overengineers.cospace.entity;

import com.overengineers.cospace.entity.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


@Entity
@Setter
@Getter
@SequenceGenerator(name = "idgen1", sequenceName = "MEMBER_SEQ", allocationSize = 1)
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseEntity implements UserDetails {

    @Column(name = "USERNAME", unique = true)
    private String username;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "EMAIL", unique = true)
    @Email
    private String email;

    @ManyToMany
    @JoinTable(name = "member_club",
            joinColumns = {@JoinColumn(name = "fk_member")},
            inverseJoinColumns = {@JoinColumn(name = "fk_club")}
    )
    private Set<Club> clubs = new HashSet<>();

    public Set<Club> getClubs() {
        return this.clubs;
    }

    public boolean isNull() {
        return this.clubs == null;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "MEMBER_AUTHORITIES",
            joinColumns = @JoinColumn(name = "member_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id")
    )
    private Set<Authority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
