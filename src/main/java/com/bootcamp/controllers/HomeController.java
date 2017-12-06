/**
 * Created by darextossa on 5/4/17.
 */

package com.bootcamp.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@CrossOrigin(origins = "*")
public class HomeController {

    @RequestMapping("/")
    public String index() {
        return "index";
    }
}