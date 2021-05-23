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

    @Column(name = "SENDER_USERNAME")
    private String senderUsername;

    @Column(name = "RECEIVER_USERNAME")
    private String receiverUsername;

    @Column(name = "CONTENT")
    private String content;

    // TODO: isSeen can be implemented

}
