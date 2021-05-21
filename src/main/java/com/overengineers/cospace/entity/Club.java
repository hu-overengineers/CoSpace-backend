package com.overengineers.cospace.entity;

import com.overengineers.cospace.entity.common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@SequenceGenerator(name = "idgen2", sequenceName = "CLUB_SEQ", allocationSize = 1)
@AllArgsConstructor
@NoArgsConstructor
public class Club extends BaseEntity {
    @Column(name = "NAME")
    private String name;

    @Column(name = "DETAILS")
    private String details;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.MERGE)
    private Set<SubClub> children = new HashSet<>();

}
