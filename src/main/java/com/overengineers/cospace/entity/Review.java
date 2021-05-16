package com.overengineers.cospace.entity;


import com.overengineers.cospace.entity.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@SequenceGenerator(name = "idgen6", sequenceName = "REVIEW_SEQ", allocationSize = 1)
@AllArgsConstructor
@NoArgsConstructor
public class Review extends BaseEntity {
    @Column(name = "AUTHOR")
    private String author;

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "RATING")
    private int rating;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinTable(name = "subClub_review",
            joinColumns = {@JoinColumn(name = "fk_review")},
            inverseJoinColumns = {@JoinColumn(name = "fk_subClub")}
    )

    private SubClub parent = new SubClub();
}
