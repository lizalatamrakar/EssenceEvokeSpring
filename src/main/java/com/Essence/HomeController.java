package com.Essence;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "index";  // loads index.html from templates/
    }

    @GetMapping("/sidebar")
    public String sidebar() {
        return "sidebar";  // loads index.html from templates/
    }
}
