package com.overengineers.cospace.mapper;

import com.overengineers.cospace.dto.EventDTO;
import com.overengineers.cospace.entity.Event;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Mapper(componentModel = "spring")
public interface EventMapper {

    @Mapping(source = "event", target = "parentName", qualifiedByName = "parentName")
    @Mapping(source = "event", target = "isOnline", qualifiedByName = "isOnline")
    EventDTO mapToDto(Event event);

    @Named("parentName")
    default String parentToParentName(Event event){
        if(event.getParent() != null)
            return event.getParent().getName();
        else
            return "";
    }

    @Named("isOnline")
    default String isOnlineToIsOnline(Event event){
        if(event.isOnline())
            return "true";
        else
            return "false";
    }

    @Mapping(source = "eventDTO", target = "date", qualifiedByName = "date")
    Event mapToEntity(EventDTO eventDTO);

    @Named("date")
    default Date dateToDate(EventDTO eventDTO){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(eventDTO.date, formatter);
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
    List<EventDTO> mapToDto(List<Event> eventList);

    List<Event> mapToEntity(List<EventDTO> eventDTOList);

}
