package com.overengineers.cospace.mapper;

import com.overengineers.cospace.dto.EventDTO;
import com.overengineers.cospace.entity.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EventMapper {

    @Mapping(source = "event", target = "parentName", qualifiedByName = "parentName")
    @Mapping(source = "event", target = "numberOfParticipants", qualifiedByName = "numberOfParticipants")
    EventDTO mapToDto(Event event);

    @Named("parentName")
    default String parentToParentName(Event event) {
        if (event.getParent() != null)
            return event.getParent().getName();
        else
            return "";
    }

    @Named("numberOfParticipants")
    default Integer participantsToNumberOfParticipants(Event event) {
        if (event.getParticipants() != null) {
            return event.getParticipants().size();
        } else return 0;
    }

    Event mapToEntity(EventDTO eventDTO);

    List<EventDTO> mapToDto(List<Event> eventList);

    List<Event> mapToEntity(List<EventDTO> eventDTOList);

}
