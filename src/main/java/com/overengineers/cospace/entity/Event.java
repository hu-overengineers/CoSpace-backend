package com.overengineers.cospace.entity;

import com.overengineers.cospace.entity.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@SequenceGenerator(name = "idgen7", sequenceName = "EVENT_SEQ", allocationSize = 1)
@AllArgsConstructor
@NoArgsConstructor
public class Event extends BaseEntity {

    @Column(name = "TITLE")
    private String title;

    @Column(name = "DETAILS")
    private String details;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATE")
    private Date date;

    @Column(name = "IS_ONLINE")
    private Boolean isOnline;

    @Column(name = "LOCATION")
    private String location; // if isOnline, location will be url of google meet, zoom, etc. Otherwise, physical loc.

    @Column(name = "UTIL_LINK")
    private String utilLink; // like when2meet

    @ManyToMany(mappedBy = "attendedEvents")
    private Set<Member> participants = new HashSet<>();

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinTable(name = "subClub_event",
            joinColumns = {@JoinColumn(name = "fk_event")},
            inverseJoinColumns = {@JoinColumn(name = "fk_subClub")}
    )
    private SubClub parent = new SubClub();

}
