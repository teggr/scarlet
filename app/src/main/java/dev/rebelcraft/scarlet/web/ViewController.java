package dev.rebelcraft.scarlet.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/conversations")
    public String conversationsListPage() {
        return "conversations";
    }

    @GetMapping("/conversations/{chatId}")
    public String conversationViewPage() {
        return "conversation";
    }
}
