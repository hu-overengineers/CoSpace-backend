package com.overengineers.cospace.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class PrivateMessageDTO {

    public final Long id;

    public final Date created;

    public final String senderUsername;

    public final String content;

    public final String receiverUsername;

}
