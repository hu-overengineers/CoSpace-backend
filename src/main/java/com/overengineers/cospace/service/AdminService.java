package com.overengineers.cospace.service;

import com.overengineers.cospace.dto.*;
import com.overengineers.cospace.entity.*;
import com.overengineers.cospace.mapper.*;
import com.overengineers.cospace.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import javax.transaction.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final ClubService clubService;
    private final ClubMapper clubMapper;
    private final ClubRepository clubRepository;

    private final SubClubMapper subClubMapper;
    private final SubClubRepository subClubRepository;

    private final ReportMapper reportMapper;
    private final ReportRepository reportRepository;

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;

    private final QuestionRepository questionRepository;
    private final QuestionMapper questionMapper;

    private final EnrollmentService enrollmentService;
    private final SecurityService securityService;

    private final SubClubCreateRequestRepository subClubCreateRequestRepository;
    private final SubClubCreateRequestMapper subClubCreateRequestMapper;

    @Transactional
    public ClubDTO createClub(ClubDTO clubDTO) {
        Club club = clubMapper.mapToEntity(clubDTO);
        Club newClub = clubService.saveNewClub(club);
        return clubMapper.mapToDto(newClub);
    }

    @Transactional
    public Question createQuestion(QuestionDTO questionDTO, String subClubName){
        Question question = questionMapper.mapToEntity(questionDTO);
        question.setParent(subClubRepository.findByName(subClubName).get());
        Question savedQuestion = questionRepository.save(question);
        return savedQuestion;
    }

    @Transactional
    public SubClubDTO createSubClub(SubClubDTO subClubDTO) {
        Optional<Club> parent = clubRepository.findByName(subClubDTO.getParentName());
        if(parent.isPresent()){
            SubClub subClub = subClubMapper.mapToEntity(subClubDTO);
            subClub.setParent(parent.get());
            subClub.setRating(0);
            SubClub newSubClub = subClubRepository.save(subClub);
            return subClubMapper.mapToDto(newSubClub);
        }
        return null;
    }

    public List<ReportDTO> getAllReports() {
        return reportMapper.mapToDto(reportRepository.findAll());
    }

    @Transactional
    public MemberDTO banMember(String username){
        Member member = memberRepository.findByUsername(username);
        // TODO: Change with banModerator, this func is unnecessary
        Member newMember = member;
        //memberRepository.save(newMember);
        return memberMapper.mapToDto(newMember);
    }

    public List<Member> getModeratorRequests(String subClubName) {
        Optional<SubClub> optionalSubClub = subClubRepository.findByName(subClubName);
        if (!optionalSubClub.isPresent())
            return null;

        SubClub subClub = optionalSubClub.get();
        return new ArrayList<>(subClub.getModRequestedMembers());
    }

    public List<MemberDTO> getModeratorRequestsDTO(String subClubName) {
        return memberMapper.mapToDto(getModeratorRequests(subClubName));
    }

    @Transactional
    public SubClubDTO makeModerator(String username, String subClubName){
        if(!securityService.isAuthorizedToSubClub(username, subClubName))
            return null; // Candidate is not authorized (banned or not enrolled)

        if(securityService.isModBanned(username))
            return null; // The candidate has banned when the member was a moderator.

        SubClub subClub = subClubRepository.findByName(subClubName).get();
        if(subClub.getModerator() != null)
            return null; // SubClub has already a moderator, first should make him/her regular member.

        Member member = memberRepository.findByUsername(username);
        subClub.setModerator(member);
        SubClub newSubClub = subClubRepository.save(subClub);
        return subClubMapper.mapToDto(newSubClub);
    }


    public SubClubDTO assignModeratorRandomly(String subClubName){
        List<Member> requestedMembers = getModeratorRequests(subClubName);
        if(requestedMembers == null)
            return null;

        if (requestedMembers.size() == 0)
            return null;

        Random rand = new Random();
        int luckyMemberIndex = rand.nextInt(requestedMembers.size());

        return makeModerator(requestedMembers.get(luckyMemberIndex).getUsername(), subClubName);
    }

    public List<MemberDTO> searchMember(String query, Pageable pageable) {
        // Sort by username, alphabetically
        UtilService.fixPageableSort(pageable, "username", true);
        return memberMapper.mapToDto(memberRepository.findByUsernameIgnoreCaseContaining(query, pageable));
    }

    public List<SubClubDTO> getEnrolledSubClubs(String username) {
        try{
            List<SubClub> subClubs = enrollmentService.getMemberSubClubs(username);
            return subClubMapper.mapToDto(subClubs);
        }
        catch (Exception e){
            e.printStackTrace();
            return new ArrayList<>();
        }

    }

    public List<SubClubCreateResponseDTO> getSubClubCreateRequests() {
        List<SubClubCreateRequest> requests = subClubCreateRequestRepository.findAll();
        List<SubClubCreateResponseDTO> responseDTOList = new ArrayList<>();
        HashMap<String, List<String>> responseMap = new HashMap<>();

        for(SubClubCreateRequest req : requests){
            String currentClubName = req.getClubName();
            String currentSubClubName = req.getSubClubName();
            if(!responseMap.containsKey(currentClubName)){
                List<String> subClubList = new ArrayList<>();
                subClubList.add(currentSubClubName);
                responseMap.put(currentClubName, subClubList);
            }
            else {
                responseMap.get(currentClubName).add(currentSubClubName);
            }
        }

        for(String key : responseMap.keySet()){
            List<String> currentSubClubList = responseMap.get(key);
            SubClubCreateResponseDTO resp = new SubClubCreateResponseDTO(key, currentSubClubList, currentSubClubList.size());
            responseDTOList.add(resp);
        }
        return responseDTOList;
    }



}
