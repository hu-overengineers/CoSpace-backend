package com.overengineers.cospace.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReportDTO {

    private String reportAuthor;

    private String reportMessage;

    private String reportedPostId;

}
