package com.overengineers.cospace.mapper;

import org.mapstruct.Mapper;
import com.overengineers.cospace.entity.Member;
import com.overengineers.cospace.dto.MemberDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MemberMapper {

    MemberDTO mapToDto(Member member);

    Member mapToEntity(MemberDTO memberDTO);

    List<MemberDTO> mapToDto(List<Member> memberList);

    List<Member> mapToEntity(List<MemberDTO> memberDTOList);
}
