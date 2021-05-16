package com.overengineers.cospace.entity;

import com.overengineers.cospace.entity.common.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@SequenceGenerator(name = "idgen3", sequenceName = "SUBCLUB_SEQ", allocationSize = 1)
@AllArgsConstructor
@NoArgsConstructor
public class SubClub extends BaseEntity {
    @Column(name = "NAME")
    private String name;

    @Column(name = "DETAILS")
    private String details;

    @Column(name = "RATING")
    private int rating;

    @OneToMany(mappedBy = "parent")
    private Set<Review> reviews = new HashSet<>();

    @OneToMany(mappedBy = "parent")
    private Set<Post> posts = new HashSet<>();

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinTable(name = "club_subClub",
            joinColumns = {@JoinColumn(name = "fk_subClub")},
            inverseJoinColumns = {@JoinColumn(name = "fk_club")}
    )
    private Club parent = new Club();
}