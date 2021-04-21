package com.overengineers.cospace.Mapper;

import com.overengineers.cospace.Dto.PostDTO;
import com.overengineers.cospace.Entity.Post;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostMapper {

    PostDTO mapToDto(Post post);

    Post mapToEntity(PostDTO postDTO);

    List<PostDTO> mapToDto(List<Post> postList);

    List<Post> mapToEntity(List<PostDTO> postDTOList);
}