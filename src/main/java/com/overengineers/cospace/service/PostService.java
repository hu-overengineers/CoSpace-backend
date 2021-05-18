package com.overengineers.cospace.service;

import com.overengineers.cospace.dto.PostDTO;
import com.overengineers.cospace.dto.ReportDTO;
import com.overengineers.cospace.entity.Club;
import com.overengineers.cospace.entity.Post;
import com.overengineers.cospace.entity.Report;
import com.overengineers.cospace.entity.SubClub;
import com.overengineers.cospace.mapper.PostMapper;
import com.overengineers.cospace.mapper.ReportMapper;
import com.overengineers.cospace.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final SubClubRepository subClubRepository;

    private final ReportRepository reportRepository;
    private final ReportMapper reportMapper;

    private final PostRepository postRepository;
    private final PostMapper postMapper;

    private final SecurityService securityService;

    @Transactional
    public PostDTO createPost(PostDTO postDTO) {

        if(!securityService.isAuthorizedToSubClub(postDTO.getParentName()))
            return null;

        SubClub subClub  = subClubRepository.findByName(postDTO.getParentName()).get();

        Post newPost = postMapper.mapToEntity(postDTO);
        newPost.setAuthor(securityService.getAuthorizedUsername());
        newPost.setParent(subClub);
        newPost.setVoting(0);
        Post savedPost = postRepository.save(newPost);
        return postMapper.mapToDto(savedPost);

    }

    public List<PostDTO> getSubClubPosts(String subClubName) {
        return postMapper.mapToDto(postRepository.findByParentName(subClubName));
    }

    public PostDTO getPostDTOById(Long postID){
        return postMapper.mapToDto(postRepository.findById(postID).get());
    }

    public List<PostDTO> getTrends(Pageable pageable) {
        Pageable sortedByName = UtilService.fixPageableSort(pageable, "voting", false); // false: Descending
        Page<Post> allProducts = postRepository.findAll(sortedByName);
        return postMapper.mapToDto(allProducts.getContent());
    }

    @Transactional
    public ReportDTO reportPost(ReportDTO reportDTO) {
        Optional<Post> reportedPost = postRepository.findById(Long.parseLong(reportDTO.getPostId()));
        if(reportedPost.isPresent()){
            Report newReport = reportMapper.mapToEntity(reportDTO);
            newReport.setPost(reportedPost.get());
            newReport.setAuthor(securityService.getAuthorizedUsername());
            Report savedReport = reportRepository.save(newReport);
            return reportMapper.mapToDto(savedReport);
        }

        return null;
    }

    @Transactional
    public PostDTO votePost(Long postId) {
        Optional<Post> optionalCurrentPost = postRepository.findById(postId);

        if(!optionalCurrentPost.isPresent()){
            return null;
        }
        Post currentPost = optionalCurrentPost.get();

        if(!securityService.isAuthorizedToSubClub(currentPost.getParent().getName()))
            return null; // Not authorized

        Long currentVoting = currentPost.getVoting();
        currentPost.setVoting(currentVoting + 1);
        Post newPost = postRepository.save(currentPost);
        return postMapper.mapToDto(newPost);

    }

}
