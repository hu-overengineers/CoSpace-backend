package com.overengineers.cospace.service;

import com.overengineers.cospace.entity.Authority;
import com.overengineers.cospace.entity.Member;
import com.overengineers.cospace.mapper.MemberMapper;
import com.overengineers.cospace.repository.AuthorityRepository;
import com.overengineers.cospace.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsManager implements UserDetailsManager {
    private final MemberRepository memberRepository;
    private final AuthorityRepository authorityRepository;

    @Override
    public void createUser(UserDetails member) {
        Member currentMember = (Member) member;
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        currentMember.setPassword(passwordEncoder.encode(currentMember.getPassword()));
        currentMember.setAuthorities(Set.of(authorityRepository.findByAuthority("USER")));
        memberRepository.save(currentMember);
    }

    @Override
    public void updateUser(UserDetails member) {
        Member oldMember = (Member) loadUserByUsername(member.getUsername());
        Member newMember = (Member) member;
        newMember.setId(oldMember.getId());
        memberRepository.save(newMember);
    }

    @Override
    public void deleteUser(String username) {
        memberRepository.deleteByUsername(username);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = (String) authentication.getPrincipal();
        Member member = (Member) loadUserByUsername(username);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if(passwordEncoder.matches(oldPassword, member.getPassword())){
            member.setPassword(passwordEncoder.encode(newPassword));
            memberRepository.save(member);
        }
        else {
            throw new BadCredentialsException("Old password is wrong!");
        }
    }

    @Override
    public boolean userExists(String username) {
        return memberRepository.existsByUsername(username);
    }

    public boolean userExistsByEmail(String email){
        return memberRepository.existsByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findByUsername(username);
    }
}
