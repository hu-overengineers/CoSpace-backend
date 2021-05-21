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
@SequenceGenerator(name = "idgen11", sequenceName = "QUESTION_SEQ", allocationSize = 1)
@AllArgsConstructor
@NoArgsConstructor
public class Question extends BaseEntity {
    @Column(name = "CONTENT")
    private String content;

    @Column(name = "ANSWER1")
    private String answer1;

    @Column(name = "ANSWER2")
    private String answer2;

    @Column(name = "ANSWER3")
    private String answer3;

    @Column(name = "ANSWER4")
    private String answer4;

    @Column(name = "GROUND_TRUTH")
    private String groundTruth;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinTable(name = "subClub_question",
            joinColumns = {@JoinColumn(name = "fk_question")},
            inverseJoinColumns = {@JoinColumn(name = "fk_subClub")}
    )
    private SubClub parent = new SubClub();
}
