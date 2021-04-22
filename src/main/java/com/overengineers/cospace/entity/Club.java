package com.overengineers.cospace.entity;

import com.overengineers.cospace.entity.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@SequenceGenerator(name = "idgen2", sequenceName = "CLUB_SEQ")
@AllArgsConstructor
@NoArgsConstructor
public class Club extends BaseEntity {
    @Column(name = "CLUB_NAME")
    private String clubName;

    @Column(name = "DETAILS")
    private String details;

    // Club - Member Relation
    @ManyToMany(mappedBy = "clubs")
    private Set<Member> members = new HashSet<>();

    // Club - SubClub Relation
    @OneToMany(mappedBy = "upperClub")
    private Set<SubClub> subs = new HashSet<>();

}
