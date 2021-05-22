package com.overengineers.cospace.entity;

import com.overengineers.cospace.entity.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;

@Entity
@Setter
@Getter
@SequenceGenerator(name = "idgen12", sequenceName = "CREATEREQ_SEQ", allocationSize = 1)
@AllArgsConstructor
@NoArgsConstructor
public class SubClubCreateRequest extends BaseEntity {

    @Column(name = "SubClubName")
    private String subClubName;

    @Column(name = "clubName")
    private String clubName;

    @Column(name = "authorUsername")
    private String authorUsername;

}
