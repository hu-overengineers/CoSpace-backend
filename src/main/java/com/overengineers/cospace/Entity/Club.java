package com.overengineers.cospace.Entity;

import com.overengineers.cospace.Entity.Common.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@SequenceGenerator(name = "idgen", sequenceName = "CLUB_SEQ")
@NoArgsConstructor
@AllArgsConstructor
public class Club extends BaseEntity {
    @Column(name = "CLUB_NAME")
    private String clubName;

    @Column(name = "DETAILS")
    private String details;

    @ManyToMany(mappedBy = "clubs")
    private Set<Club> members = new HashSet<>();
    
}
