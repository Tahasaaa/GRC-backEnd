package org.example.grc.Controllers;

import org.example.grc.DTO.ChatBotRequest;
import org.example.grc.DTO.ChatBotResponse;
import org.example.grc.Services.ChatBotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chatbot")
@CrossOrigin("*")
public class ChatBotController {

    private final ChatBotService chatBotService;

    @Autowired
    public ChatBotController(ChatBotService chatBotService) {
        this.chatBotService = chatBotService;
    }

    @PostMapping("/reformulate")
    public ResponseEntity<ChatBotResponse> reformulate(@RequestBody ChatBotRequest request) {
        ChatBotResponse response = chatBotService.generateStructuredReclamation(
                request.getDescription(),
                request.getType()
        );
        return ResponseEntity.ok(response);
    }
}
