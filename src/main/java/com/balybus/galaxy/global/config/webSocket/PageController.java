package com.balybus.galaxy.global.config.webSocket;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/page")
public class PageController {
    @GetMapping("/chatTestA")
    public String chatTestA(){
        return "/chat/chatTestA";
    }

    @GetMapping("/chatTestB")
    public String chatTestB(){
        return "/chat/chatTestB";
    }
}
