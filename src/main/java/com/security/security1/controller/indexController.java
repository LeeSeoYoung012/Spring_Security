package com.security.security1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller //View를 리턴하겠다.
public class indexController {

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

    //스프링 시큐리티가 해당 주소를 낚아챔 - Security Config 한 후에는 작동을 안함
    @GetMapping("/login")
    public String login(){
        return "loginForm";
    }

    @GetMapping("/join")
    public String join(){
        return "join";
    }

    @GetMapping("/joinProc")
    public String joinProc(){
        return "회원가입 완료됨!";
    }
}

