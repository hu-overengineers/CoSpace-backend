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
    private final SearchService searchService;

    private final SubClubCreateRequestRepository subClubCreateRequestRepository;
    private final BanRepository banRepository;

    private final CustomUserDetailsManager customUserDetailsManager;

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
    public void kickMemberFromCoSpace(String username) {
        customUserDetailsManager.deleteUser(username);
    }

    public boolean deleteReport(long reportId) {

        Optional<Report> optionalReport = reportRepository.findById(reportId);
        if(!optionalReport.isPresent())
            return false; // Not found

        reportRepository.deleteById(reportId);

        return !reportRepository.findById(reportId).isPresent(); // Deleted successfully
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


    @Transactional
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
        UtilService.fixPageableSort(pageable, "username", true);
        return memberMapper.mapToDto(searchService.searchMembers(query, pageable).toList());
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
            if(currentSubClubList.size() >= 3){// Minimum 3 requests
                for(String currentSubName : currentSubClubList){
                    SubClubCreateResponseDTO resp = new SubClubCreateResponseDTO(key, currentSubName, currentSubClubList.size());
                    responseDTOList.add(resp);
                }
            }
        }
        return responseDTOList;
    }


    public Ban banModerator(String username, String reason) {
        Member member  = memberRepository.findByUsername(username);
        SubClub subClub = member.getModeratorSubClub();

        Optional<Ban> optionalBan = banRepository.findBySubClubNameAndMember_Username(subClub.getName(), username);
        Date expirationDate = UtilService.calculateDate(5);
        Ban newBan;

        if(!optionalBan.isPresent()){ // No previous ban
            Ban ban = new Ban(reason, expirationDate, 1, true, subClub.getName(), member);
            newBan = banRepository.save(ban);
        } else {
            Ban ban = optionalBan.get();
            ban.setCount(ban.getCount() + 1);
            ban.setEndDate(expirationDate);
            ban.setModBan(true);
            ban.setReason(reason);
            newBan = banRepository.save(ban);
        }
        member.setModeratorSubClub(null);
        SubClub subClubNew = subClubRepository.findByName(subClub.getName()).get();
        subClubNew.setModerator(null);
        subClubRepository.save(subClubNew);
        memberRepository.save(member);

        return newBan;
    }

    @Transactional
    public SubClubDTO updateSubClub(SubClubDTO subClubDTO) {
        // TODO: Review needed
        Optional<SubClub> optionalSubClub = subClubRepository.findById((long) subClubDTO.id);

        if (optionalSubClub.isPresent()) {
            SubClub subClub = optionalSubClub.get();

            subClub.setName(subClubDTO.getName());
            subClub.setDetails(subClubDTO.getDetails());

            List<Question> question = questionMapper.mapToEntity(subClubDTO.getQuestions());
            subClub.setQuestions(new HashSet<>(question));

            SubClub updatedSubClub = subClubRepository.save(subClub);

            return subClubMapper.mapToDto(updatedSubClub);
        }

        return null;
    }

    @Transactional
    public boolean deleteSubClub(long id) {

        Optional<SubClub> optionalSubClub= subClubRepository.findById(id);
        if(!optionalSubClub.isPresent())
            return false; // Not found

        Club club = optionalSubClub.get().getParent();
        long clubId = club.getId();
        int childCount = club.getChildren().size();
        subClubRepository.deleteById(id);

        if(subClubRepository.findById(id).isPresent())
            return false; // SubClub couldn't be deleted

        if(childCount == 1){
            clubRepository.deleteById(clubId);
            return !clubRepository.findById(clubId).isPresent(); // Both SubClub and Club are deleted successfully
        }

        return true; // SubClub is deleted, Club has more than one SubClub
    }

}
