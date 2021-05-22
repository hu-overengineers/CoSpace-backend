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
@SequenceGenerator(name = "idgen4", sequenceName = "POST_SEQ", allocationSize = 1)
@AllArgsConstructor
@NoArgsConstructor
public class Post extends BaseEntity {
    @Column(name = "AUTHOR")
    private String author;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "voting")
    private long voting;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private Set<Report> reports = new HashSet<>();

    @ManyToOne(cascade = {CascadeType.MERGE})
    @JoinTable(name = "subClub_post",
            joinColumns = {@JoinColumn(name = "fk_post")},
            inverseJoinColumns = {@JoinColumn(name = "fk_subClub")}
    )
    private SubClub parent = new SubClub();
}
