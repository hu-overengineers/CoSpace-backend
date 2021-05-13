package com.overengineers.cospace.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.overengineers.cospace.entity.Post;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReportDTO {
    private String reportAuthor;

    private String reportMessage;

    private String reportedPostId;

    @JsonIgnore
    public Post reportedPost;

}
