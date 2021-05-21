package com.overengineers.cospace.entity;

import com.overengineers.cospace.entity.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

@Entity
@Setter
@Getter
@SequenceGenerator(name = "idgen10", sequenceName = "ENROLLMENT_SEQ", allocationSize = 1)
@AllArgsConstructor
@NoArgsConstructor
public class Enrollment extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "member_id")
    Member member;

    @ManyToOne
    @JoinColumn(name = "subClub_id")
    SubClub subClub;

    float interestRate;

    boolean isEnrolled;
    
}
