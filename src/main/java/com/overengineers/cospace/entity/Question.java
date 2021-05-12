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
    @Column(name = "QUESTION")
    private String question;

    @Column(name = "ANSWER")
    private String answer;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinTable(name = "club_question",
            joinColumns = {@JoinColumn(name = "fk_question")},
            inverseJoinColumns = {@JoinColumn(name = "fk_club")}
    )
    private Club questionClub = new Club();
}
