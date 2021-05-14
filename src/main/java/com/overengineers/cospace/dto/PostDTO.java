package com.overengineers.cospace.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PostDTO {

    public final Long id;

    public final LocalDateTime created;

    public final String postAuthor;

    public final String postTitle;

    public final String postContent;

    public final String postSubClubName;

    public final long postVoting;

}
