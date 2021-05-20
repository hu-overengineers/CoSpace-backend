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
@SequenceGenerator(name = "idgen9", sequenceName = "PRIVATEMSG_SEQ", allocationSize = 1)
@AllArgsConstructor
@NoArgsConstructor
public class PrivateMessage extends BaseEntity {

    @Column(name = "AUTHOR")
    private String author;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "CONTENT")
    private String content;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinTable(name = "member_privateMessage",
            joinColumns = {@JoinColumn(name = "fk_privateMsg")},
            inverseJoinColumns = {@JoinColumn(name = "fk_member")}
    )
    private Member targetMember =new Member();
}
