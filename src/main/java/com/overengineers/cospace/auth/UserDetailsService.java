package com.overengineers.cospace.auth;

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
    private Map<String, String> users = new HashMap<>();

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostConstruct
    public void init(){
        String password = "123";
        String encodedPassword = passwordEncoder.encode(password);
        users.put("yusuf",encodedPassword);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        if (users.containsKey(username)){
            return new User(username, users.get(username), new ArrayList<>());
        }
        throw new UsernameNotFoundException(username);
    }
}
