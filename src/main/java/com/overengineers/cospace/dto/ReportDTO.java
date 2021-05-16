package com.overengineers.cospace.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReportDTO {

    private String author;

    private String content;

    private String postId;

}
