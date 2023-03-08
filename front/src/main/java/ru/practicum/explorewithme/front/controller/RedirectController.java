package ru.practicum.explorewithme.front.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class RedirectController {

    @GetMapping("/")
    public String index() {
        return "index";
    }
}
