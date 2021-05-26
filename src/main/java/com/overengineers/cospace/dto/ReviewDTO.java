package com.overengineers.cospace.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Getter
@Builder
public class ReviewDTO {

    private String author;

    private String content;

    private int rating;

    private String parentName;

    private Date created;
    
}
