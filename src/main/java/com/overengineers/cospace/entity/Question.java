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
@AllArgsConstructor
@NoArgsConstructor
public class Question extends BaseEntity {
    @Column(name = "CONTENT")
    private String content;

    @Column(name = "ANSWER")
    private String answer;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinTable(name = "subClub_question",
            joinColumns = {@JoinColumn(name = "fk_question")},
            inverseJoinColumns = {@JoinColumn(name = "fk_subClub")}
    )
    private SubClub parent = new SubClub();
}
