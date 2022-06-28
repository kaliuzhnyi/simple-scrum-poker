package com.simplescrumpoker.controller;

import com.simplescrumpoker.dto.room.RoomEnterDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/")
public class MainController {
    public static final String VIEW_INDEX = "index";

    @GetMapping
    public String getMain(Model model,
                          RedirectAttributes redirectAttributes,
                          @ModelAttribute("roomEnter") RoomEnterDto roomEnterDto) {
        return VIEW_INDEX;
    }

}
