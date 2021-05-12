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
@SequenceGenerator(name = "idgen4", sequenceName = "POST_SEQ", allocationSize = 1)
@AllArgsConstructor
@NoArgsConstructor
public class Post extends BaseEntity {
    @Column(name = "POST_AUTHOR")
    private String postAuthor;

    @Column(name = "POST_TITLE")
    private String postTitle;

    @Column(name = "POST_CONTENT")
    private String postContent;

    @Column(name = "POST_SUB_CLUB_NAME")
    private String postSubClubName;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinTable(name = "subClub_post",
            joinColumns = {@JoinColumn(name = "fk_post")},
            inverseJoinColumns = {@JoinColumn(name = "fk_subClub")}
    )
    private SubClub postSubClub = new SubClub();
}
