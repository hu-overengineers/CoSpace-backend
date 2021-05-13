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
@SequenceGenerator(name = "idgen5", sequenceName = "REPORT_SEQ", allocationSize = 1)
@AllArgsConstructor
@NoArgsConstructor
public class Report extends BaseEntity {

    @Column(name = "REPORT_AUTHOR")
    private String reportAuthor;

    @Column(name = "REPORT_MESSAGE")
    private String reportMessage;

    @Column(name = "REPORTED_POST_ID")
    private String reportedPostId;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinTable(name = "post_report",
            joinColumns = {@JoinColumn(name = "fk_report")},
            inverseJoinColumns = {@JoinColumn(name = "fk_post")}
    )
    private Post reportedPost = new Post();
}
