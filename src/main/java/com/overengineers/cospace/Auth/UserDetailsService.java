package com.overengineers.cospace.Auth;

import com.overengineers.cospace.Entity.Member;
import com.overengineers.cospace.Service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    @Autowired
    MemberService memberService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        Member currentMember = memberService.login(username);
        if (currentMember.isNull()){
            throw new UsernameNotFoundException(username);
        }else
        {
            return new User(currentMember.getUsername(), currentMember.getPassword(), new ArrayList<>());
        }
    }
}
