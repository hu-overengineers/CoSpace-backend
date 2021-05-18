package com.overengineers.cospace.mapper;

import com.overengineers.cospace.dto.PostDTO;
import com.overengineers.cospace.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(source = "post", target = "parentName", qualifiedByName = "parentName")
    PostDTO mapToDto(Post post);

    @Named("parentName")
    default String parentToParentName(Post post){
        if(post.getParent() != null)
            return post.getParent().getName();
        else
            return "";
    }

    Post mapToEntity(PostDTO postDTO);

    List<PostDTO> mapToDto(List<Post> postList);

    List<Post> mapToEntity(List<PostDTO> postDTOList);

}