package com.overengineers.cospace.mapper;

import com.overengineers.cospace.dto.PostDTO;
import com.overengineers.cospace.entity.Post;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostMapper {

    PostDTO mapToDto(Post post);

    Post mapToEntity(PostDTO postDTO);

    List<PostDTO> mapToDto(List<Post> postList);

    List<Post> mapToEntity(List<PostDTO> postDTOList);
}