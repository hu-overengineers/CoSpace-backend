package com.overengineers.cospace.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UtilService {

    Pageable fixPageableSort(Pageable pageable, String sortParameter, boolean isAscending) {
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


}