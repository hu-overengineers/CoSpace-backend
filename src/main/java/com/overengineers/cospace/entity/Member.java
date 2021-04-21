package com.overengineers.cospace.entity;

import com.overengineers.cospace.entity.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.HashSet;
import java.util.Set;


@Entity
@Setter
@Getter
@SequenceGenerator(name = "idgen", sequenceName = "MEMBER_SEQ")
@NoArgsConstructor
@AllArgsConstructor
public class Member extends BaseEntity {

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
}
