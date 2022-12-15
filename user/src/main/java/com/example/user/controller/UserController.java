package com.example.user.controller;

import com.example.user.clients.BlogClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/{name}")
public class UserController {

    @Value("${server.port}")
    String port;

    @Autowired private BlogClient blogClient;

    @RequestMapping("")
    public String home(@PathVariable String name) {
        return "hi " + name + ",i am from port:" + port;
    }

    @RequestMapping("/profile")
    public String profile(@PathVariable String name) {
        String blogs = this.blogClient.getBlogs(name);
        return "name: " + name + ", port:" + port + ", blogs: " + blogs;
    }
}
