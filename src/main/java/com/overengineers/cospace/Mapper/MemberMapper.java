package com.overengineers.cospace.Mapper;

import org.mapstruct.Mapper;
import com.overengineers.cospace.Entity.Member;
import com.overengineers.cospace.Dto.MemberDTO;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MemberMapper {

    MemberDTO mapToDto(Member member);

    Member mapToEntity(MemberDTO memberDTO);

    List<MemberDTO> mapToDto(List<Member> memberList);

    List<Member> mapToEntity(List<MemberDTO> memberDTOList);
}
