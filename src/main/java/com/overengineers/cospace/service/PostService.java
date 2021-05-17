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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PostService {
    private final SubClubRepository subClubRepository;
    private final ClubRepository clubRepository;
    private final MemberRepository memberRepository;

    private final ReportRepository reportRepository;
    private final ReportMapper reportMapper;

    private final PostRepository postRepository;
    private final PostMapper postMapper;

    public boolean isAuthorizedForClub(String clubName){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        Set<Club> clubs = memberRepository.findByUsername(username).getClubs();
        if (!clubRepository.findByName(clubName).isPresent()) // Club is not existed
            return false;

        Club club = clubRepository.findByName(clubName).get();
        if (clubs.contains(club)){
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public PostDTO savePost(PostDTO postDTO) {
        Optional<SubClub> postSubClub = subClubRepository.findByName(postDTO.getParentName());
        if(postSubClub.isPresent()){
            if(postSubClub.get().getParent() == null || !isAuthorizedForClub(postSubClub.get().getParent().getName()))
                return null; // Not authorized or Club is not existed
            Post newPost = postMapper.mapToEntity(postDTO);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = (String) authentication.getPrincipal();
            newPost.setAuthor(username);
            newPost.setParent(postSubClub.get());
            newPost.setVoting(0);
            newPost.setCreated(LocalDateTime.now());
            Post savedPost = postRepository.save(newPost);
            return postMapper.mapToDto(savedPost);
        }
        return null;
    }

    public List<PostDTO> getSubClubPosts(String subClubName) {
        return postMapper.mapToDto(postRepository.findByParentName(subClubName));
    }

    public PostDTO getPostDTOById(Long postID){
        return postMapper.mapToDto(postRepository.findById(postID).get());
    }

    @Transactional
    public ReportDTO reportPost(ReportDTO reportDTO) {
        Optional<Post> reportedPost = postRepository.findById(Long.parseLong(reportDTO.getPostId()));
        if(reportedPost.isPresent()){
            Report newReport = reportMapper.mapToEntity(reportDTO);
            newReport.setPost(reportedPost.get());
            newReport.setCreated(LocalDateTime.now());
            Report savedReport = reportRepository.save(newReport);
            return reportMapper.mapToDto(savedReport);
        }

        return null;
    }

    public List<PostDTO> getTrends(Pageable pageable) {
        Sort sorting;
        if(!pageable.getSort().isUnsorted()) {
            sorting = pageable.getSort();
        }
        else {
            // Default Sorting
           sorting = Sort.by("voting").descending();
        }

        Pageable sortedByName =
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sorting);
        Page<Post> allProducts = postRepository.findAll(sortedByName);

        return postMapper.mapToDto(allProducts.getContent());
    }

    @Transactional
    public PostDTO votePost(Long postId) {
        Optional<Post> optionalCurrentPost = postRepository.findById(postId);

        if(!optionalCurrentPost.isPresent()){
            return null;
        }
        Post currentPost = optionalCurrentPost.get();

        if(currentPost.getParent() == null)
            return null; // Post SubClub is not existed
        if(currentPost.getParent().getParent() == null)
            return null; // Upper club of post subclub is not existed
        if(!isAuthorizedForClub(currentPost.getParent().getParent().getName()))
            return null; // Not authorized

        Long currentVoting = currentPost.getVoting();
        currentPost.setVoting(currentVoting + 1);
        postRepository.save(currentPost);
        return postMapper.mapToDto(currentPost);

    }

}
