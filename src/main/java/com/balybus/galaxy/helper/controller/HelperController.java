package com.balybus.galaxy.helper.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelperController {

    @PostMapping("/work-location")
    public String workLocation() {
        return "";
    }

    @PostMapping("/work-time")
    public String workTime() {
        return "";
    }

    @PostMapping("/helper-exp")
    public String helperExp() {
        return "";
    }
}
