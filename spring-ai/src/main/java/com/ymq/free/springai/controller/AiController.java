package com.ymq.free.springai.controller;

import lombok.AllArgsConstructor;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Map;

/**
 * @author yinmengqi
 */
@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class AiController {
    private final OllamaChatClient chatClient;

    @GetMapping("/ai/generate")
    public Map generate(@RequestParam(value = "message", defaultValue = "给我讲个笑话") String message) {
        return Map.of("generation", chatClient.call(message));
    }

    @GetMapping(value = "/ai/generateStream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ChatResponse> generateStream(@RequestParam(value = "message", defaultValue = "给我讲个笑话") String message) {
        Prompt prompt = new Prompt(new UserMessage(message));
        return chatClient.stream(prompt);
    }
}
