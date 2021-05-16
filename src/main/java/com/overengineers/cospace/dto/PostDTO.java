package com.overengineers.cospace.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PostDTO {

    public final Long id;

    public final LocalDateTime created;

    public final String author;

    public final String title;

    public final String content;

    public final String parentName;

    public final long voting;

}
