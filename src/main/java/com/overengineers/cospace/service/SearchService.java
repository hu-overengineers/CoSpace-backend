package com.overengineers.cospace.service;

import com.overengineers.cospace.dto.SearchResultsDTO;
import com.overengineers.cospace.entity.Club;
import com.overengineers.cospace.entity.Member;
import com.overengineers.cospace.entity.Post;
import com.overengineers.cospace.entity.SubClub;
import com.overengineers.cospace.repository.ClubRepository;
import com.overengineers.cospace.repository.MemberRepository;
import com.overengineers.cospace.repository.PostRepository;
import com.overengineers.cospace.repository.SubClubRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {

    private final ClubRepository clubRepository;
    private final SubClubRepository subClubRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    public Page<Club> searchClubs(String query, Pageable pageable) {
        return clubRepository.findByNameIgnoreCaseContaining(query, pageable);
    }

    public Page<SubClub> searchSubClubs(String query, Pageable pageable) {
        return subClubRepository.findByNameIgnoreCaseContaining(query, pageable);
    }

    public Page<Member> searchMembers(String query, Pageable pageable) {
        return memberRepository.findByUsernameIgnoreCaseContaining(query, pageable);
    }

    public Page<Post> searchPosts(String query, Pageable pageable) {
        return postRepository.findByContentIgnoreCaseContaining(query, pageable);
    }
}
