package com.overengineers.cospace.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.overengineers.cospace.Entity.Common.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@SequenceGenerator(name = "idgen3", sequenceName = "SUB_CLUB_SEQ")
@AllArgsConstructor
@NoArgsConstructor
public class SubClub extends BaseEntity {
    @Column(name = "SUB_CLUB_NAME")
    private String subClubName;

    @Column(name = "DETAILS")
    private String details;

    private String upperClubName;

    @ManyToMany(mappedBy = "subClubs")
    private Set<Member> members = new HashSet<>();

    @OneToMany(mappedBy = "postSubClub")
    private Set<Post> posts = new HashSet<>();

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinTable(name = "club_subClub",
            joinColumns = {@JoinColumn(name = "fk_subClub")},
            inverseJoinColumns = {@JoinColumn(name = "fk_club")}
    )
    private Club upperClub = new Club();
}