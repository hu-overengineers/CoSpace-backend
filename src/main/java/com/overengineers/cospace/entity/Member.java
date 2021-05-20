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
    @JoinTable(name = "member_subClub",
            joinColumns = {@JoinColumn(name = "fk_member")},
            inverseJoinColumns = {@JoinColumn(name = "fk_subClub")}
    )
    private Set<SubClub> subClubs = new HashSet<>();

    @ManyToMany(mappedBy = "dismissibleMembers")
    private Set<SubClub> dismissibleSubClubs = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "member_event",
            joinColumns = {@JoinColumn(name = "fk_member")},
            inverseJoinColumns = {@JoinColumn(name = "fk_event")}
    )
    private Set<Event> joinedEvents = new HashSet<>();

    @OneToOne
    @JoinColumn(name = "ban_id", referencedColumnName = "id")
    private Ban ban;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(
            name = "MEMBER_AUTHORITIES",
            joinColumns = @JoinColumn(name = "member_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id")
    )
    private Set<Authority> authorities;

    @OneToOne(mappedBy = "moderator")
    private SubClub moderatorSubClub;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinTable(name = "subClub_modRequestMember",
            joinColumns = {@JoinColumn(name = "fk_member")},
            inverseJoinColumns = {@JoinColumn(name = "fk_subClub")}
    )
    private SubClub modRequestedSubClubs =new SubClub();

    // Functions

    public boolean isNull() {
        return getSubClubs() == null;
    }

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
