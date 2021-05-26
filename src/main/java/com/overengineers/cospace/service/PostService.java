package com.overengineers.cospace.service;

import com.overengineers.cospace.dto.PostDTO;
import com.overengineers.cospace.dto.ReportDTO;
import com.overengineers.cospace.entity.Club;
import com.overengineers.cospace.entity.Post;
import com.overengineers.cospace.entity.Report;
import com.overengineers.cospace.entity.SubClub;
import com.overengineers.cospace.mapper.PostMapper;
import com.overengineers.cospace.mapper.ReportMapper;
import com.overengineers.cospace.repository.PostRepository;
import com.overengineers.cospace.repository.ReportRepository;
import com.overengineers.cospace.repository.SubClubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    public enum CustomFeed {
        POPULAR("Popular");

        private final String name;

        private CustomFeed(String s) {
            name = s;
        }

        public boolean equalsName(String otherName) {
            return name.equals(otherName);
        }

        public String toString() {
            return this.name;
        }
    }

    private final SubClubRepository subClubRepository;

    private final ReportRepository reportRepository;
    private final ReportMapper reportMapper;

    private final PostRepository postRepository;
    private final PostMapper postMapper;

    private final SecurityService securityService;
    private final SubClubService subClubService;
    private final ClubService clubService;

    @Transactional
    public PostDTO createPost(PostDTO postDTO) {

        if (!securityService.isAuthorizedToSubClub(postDTO.getParentName()))
            return null;

        SubClub subClub = subClubRepository.findByName(postDTO.getParentName()).get();

        Post newPost = postMapper.mapToEntity(postDTO);
        newPost.setAuthor(securityService.getAuthorizedUsername());
        newPost.setParent(subClub);
        newPost.setVoting(0);
        Post savedPost = postRepository.save(newPost);
        return postMapper.mapToDto(savedPost);

    }

    public List<Post> getPosts(String feedName, Date start, Date end, Pageable pageable) {
        if (feedName.equals(CustomFeed.POPULAR.name)) {
            return postRepository.findByCreatedBetween(start, end, pageable).toList();
        } else {
            SubClub subClub = subClubService.getByName(feedName);
            if (subClub != null) {
                return getSubClubPosts(subClub.getName(), start, end, pageable);
            } else {
                Club club = clubService.getByName(feedName);
                if (club == null) return null;
                return getClubPosts(club.getName(), start, end, pageable);
            }
        }
    }

    public List<Post> getSubClubPosts(String subClubName, Date start, Date end, Pageable pageable) {
        return postRepository.findByParentNameAndCreatedBetween(subClubName, start, end, pageable).toList();
    }

    public List<Post> getClubPosts(String clubName, Date start, Date end, Pageable pageable) {
        return postRepository.findByParent_Parent_NameAndCreatedBetween(clubName, start, end, pageable).toList();
    }

    public PostDTO getPostDTOById(Long postID) {
        return postMapper.mapToDto(postRepository.findById(postID).get());
    }

    public List<PostDTO> getPostsByAuthorAndSubClub(String username, String subClubName) {
        return postMapper.mapToDto(postRepository.findByAuthorAndParentName(username, subClubName));
    }

    public List<PostDTO> getPostsByAuthor(String username) {
        return postMapper.mapToDto(postRepository.findByAuthor(username));
    }

    public List<PostDTO> getTrends(Pageable pageable) {
        Pageable sortedByName = UtilService.fixPageableSort(pageable, "voting", false); // false: Descending
        Page<Post> allProducts = postRepository.findAll(sortedByName);
        return postMapper.mapToDto(allProducts.getContent());
    }

    @Transactional
    public ReportDTO reportPost(ReportDTO reportDTO) {
        Optional<Post> reportedPost = postRepository.findById(Long.parseLong(reportDTO.getPostId()));
        if (reportedPost.isPresent()) {
            Report newReport = reportMapper.mapToEntity(reportDTO);
            newReport.setPost(reportedPost.get());
            newReport.setAuthor(securityService.getAuthorizedUsername());
            Report savedReport = reportRepository.save(newReport);
            return reportMapper.mapToDto(savedReport);
        }

        return null;
    }

    @Transactional
    public PostDTO upvotePost(Long postId) {
        return votePost(postId, 1);
    }

    @Transactional
    public PostDTO downvotePost(Long postId) {
        return votePost(postId, -1);
    }

    private PostDTO votePost(Long postId, int vote) {
        Optional<Post> optionalCurrentPost = postRepository.findById(postId);

        if (!optionalCurrentPost.isPresent()) {
            return null;
        }
        Post currentPost = optionalCurrentPost.get();

        if (!securityService.isAuthorizedToSubClub(currentPost.getParent().getName()))
            return null; // Not authorized

        long currentVote = currentPost.getVoting();
        currentPost.setVoting(currentVote + vote);
        Post newPost = postRepository.save(currentPost);
        return postMapper.mapToDto(newPost);
    }

    @Transactional
    public boolean deleteOwnPostById(Long postId){

        Optional<Post> optionalCurrentPost = postRepository.findById(postId);
        if(!optionalCurrentPost.isPresent())
            return false;

        Post currentPost = optionalCurrentPost.get();

        if(!currentPost.getAuthor().equals(securityService.getAuthorizedUsername()))
            return false;

        postRepository.deleteById(postId);

        if(!postRepository.findById(postId).isPresent())
            return true; // Deleted successfully

        return false; // Still exists

    }

}
