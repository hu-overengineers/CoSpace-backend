package com.overengineers.cospace.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class UtilService {

    public static Pageable fixPageableSort(Pageable pageable, String sortParameter, boolean isAscending) {
        Sort sorting;
        if (!pageable.getSort().isUnsorted()) {
            sorting = pageable.getSort();
        } else if (isAscending) {
            sorting = Sort.by(sortParameter).ascending();
        } else {
            sorting = Sort.by(sortParameter).descending();
        }
        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sorting);
    }

    public static Date calculateDate(int day){
        LocalDate date= LocalDate.now();
        if(day < 0){
            date = date.minusDays(day);
        }
        else if(day > 0){
            date = date.plusDays(day);
        }

        Instant desiredTime = date
                .atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant();

        return Date.from(desiredTime);
    }

    public static Date now(){
        return new java.util.Date();
    }

}