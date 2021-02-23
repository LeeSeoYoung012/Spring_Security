package com.security.security1.controller;

import com.security.security1.Repository.UserRepository;
import com.security.security1.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller //View를 리턴하겠다.
public class indexController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping({"","/"})
    public String index(){
        //머스테치 기본폴더 src/main/resources
        return "index";
    }

    @GetMapping("/user")
    public String user(){
        return "user";
    }

    @GetMapping("/admin")
    public String admin(){
        return "admin";
    }

    @GetMapping("/manager")
    public String manager(){
        return "manager";
    }

    @GetMapping("/joinForm")
    public String joinForm(){
        return "joinForm";
    }


    //스프링 시큐리티가 해당 주소를 낚아챔 - Security Config 한 후에는 작동을 안함
    @GetMapping("/loginForm")
    public String loginForm(){
        return "loginForm";
    }

    @PostMapping("/join")
    public String join(User user){
        System.out.println(user);
        user.setRole("ROLE_USER");
        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword); //인코딩된 password로 설정
        userRepository.save(user);// 비밀번호 1234 일경우 시큐리티로 로그인할 수 없음
        return "redirect:/loginForm"; //리다이렉트는 함수 재사용이 가능하다.
    }


}

