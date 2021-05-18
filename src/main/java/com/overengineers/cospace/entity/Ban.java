package com.overengineers.cospace.entity;

import com.overengineers.cospace.entity.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;


@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Ban extends BaseEntity {
    @Column(name = "REASON")
    private String reason;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "END_DATE")
    private Date endDate;

    @Column(name = "COUNT")
    private int count;

    @Column(name = "MOD_BAN")
    private boolean modBan; // SubClub admin ban, block to be a admin of any subclub

    @Column(name = "SUB_CLUB_NAME")
    private String subClubName;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinTable(name = "member_ban",
            joinColumns = {@JoinColumn(name = "fk_ban")},
            inverseJoinColumns = {@JoinColumn(name = "fk_member")}
    )
    private Member member = new Member();









}
