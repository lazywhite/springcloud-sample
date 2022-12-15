package com.example.user.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("blog")
public interface BlogClient {
    @RequestMapping(value = "/query", method = RequestMethod.GET)
    String getBlogs(@RequestParam String username);
}
