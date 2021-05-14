package com.overengineers.cospace.service;

import com.overengineers.cospace.dto.PostDTO;
import com.overengineers.cospace.dto.ReportDTO;
import com.overengineers.cospace.entity.Post;
import com.overengineers.cospace.entity.Report;
import com.overengineers.cospace.entity.SubClub;
import com.overengineers.cospace.mapper.PostMapper;
import com.overengineers.cospace.mapper.ReportMapper;
import com.overengineers.cospace.repository.PostRepository;
import com.overengineers.cospace.repository.ReportRepository;
import com.overengineers.cospace.repository.SubClubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public PostDTO savePost(PostDTO postDTO) {
        Optional<SubClub> postSubClub = subClubRepository.findBySubClubName(postDTO.postSubClubName);
        if(postSubClub.isPresent()){
            Post newPost = postMapper.mapToEntity(postDTO);
            newPost.setPostSubClub(postSubClub.get());
            newPost.setPostVoting(0);
            newPost.setCreated(LocalDateTime.now());
            Post savedPost = postRepository.save(newPost);
            return postMapper.mapToDto(savedPost);
        }
        return null;
    }

    public List<PostDTO> getSubClubPosts(String subClubName) {
        return postMapper.mapToDto(postRepository.findByPostSubClubName(subClubName));
    }

    public ReportDTO reportPost(ReportDTO reportDTO) {
        Optional<Post> reportedPost = postRepository.findById(Long.parseLong(reportDTO.getReportedPostId()));
        if(reportedPost.isPresent()){
            Report newReport = reportMapper.mapToEntity(reportDTO);
            newReport.setReportedPost(reportedPost.get());
            newReport.setCreated(LocalDateTime.now());
            Report savedReport = reportRepository.save(newReport);
            return reportMapper.mapToDto(savedReport);
        }

        return null;
    }
}
