package com.overengineers.cospace.mapper;

import com.overengineers.cospace.dto.QuestionDTO;
import com.overengineers.cospace.entity.Question;
import com.overengineers.cospace.entity.SubClub;
import com.overengineers.cospace.service.SubClubService;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface QuestionMapper {

    @Mapping(source = "question", target = "parentName", qualifiedByName = "parentName")
    QuestionDTO mapToDto(Question question);

    @Named("parentName")
    default String parentToParentName(Question question){
        if(question.getParent() != null)
            return question.getParent().getName();
        else
            return "";
    }

    Question mapToEntity(QuestionDTO questionDTO);

    List<QuestionDTO> mapToDto(List<Question> questionList);

    List<Question> mapToEntity(List<QuestionDTO> questionDTOList);
    
}
