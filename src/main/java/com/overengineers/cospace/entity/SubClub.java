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
    @Column(name = "NAME", unique = true)
    private String name;

    @Column(name = "DETAILS", columnDefinition = "TEXT")
    private String details;

    @Column(name = "RATING")
    private int rating;

    @OneToMany(orphanRemoval = true, mappedBy = "subClub")
    Set<Enrollment> enrollments;

    @OneToMany(orphanRemoval = true, mappedBy = "parent")
    private Set<Post> posts = new HashSet<>();

    @OneToMany(orphanRemoval = true, mappedBy = "parent")
    private Set<Review> reviews = new HashSet<>();

    @OneToMany(orphanRemoval = true, mappedBy = "parent")
    private Set<Question> questions = new HashSet<>();

    @ManyToOne(cascade = {CascadeType.MERGE})
    @JoinTable(name = "club_subClub",
            joinColumns = {@JoinColumn(name = "fk_subClub")},
            inverseJoinColumns = {@JoinColumn(name = "fk_club")}
    )
    private Club parent = new Club();

    @OneToOne
    @JoinColumn(name = "moderator")
    private Member moderator;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "member_dismissibleSubClub",
            joinColumns = {@JoinColumn(name = "fk_subClub")},
            inverseJoinColumns = {@JoinColumn(name = "fk_member")}
    )
    private Set<Member> dismissibleMembers = new HashSet<>();

    @OneToMany(orphanRemoval = true, mappedBy = "parent")
    private Set<Event> events = new HashSet<>();

    @OneToMany(orphanRemoval = true, mappedBy = "modRequestedSubClubs")
    private Set<Member> modRequestedMembers = new HashSet<>();
}