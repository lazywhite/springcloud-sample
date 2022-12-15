package com.example.blog.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BlogController {

    @Value("${server.port}")
    String port;

    @RequestMapping("/query")
    public String home(@RequestParam String username) {
        return "today is a great day  " + username + ", port:" + port;
    }
}
