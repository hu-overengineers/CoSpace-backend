package com.overengineers.cospace.Dto;

import com.overengineers.cospace.Entity.SubClub;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PostDTO {

    public final String postAuthor;

    public final String postTitle;

    public final String postContent;

    public final String postSubClubName;

    public SubClub postSubClub;
}
