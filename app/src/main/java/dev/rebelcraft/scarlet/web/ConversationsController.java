package dev.rebelcraft.scarlet.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ConversationsController {

    @GetMapping("/conversations")
    public String conversations() {
        return "conversationsPage";
    }

    @GetMapping("/conversation")
    public String conversation() {
        return "conversationPage";
    }

}
