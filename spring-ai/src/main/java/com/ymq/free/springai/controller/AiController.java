package com.ymq.free.springai.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.huggingface.HuggingfaceChatClient;
import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author yinmengqi
 */
@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
@Slf4j
public class AiController {
    private final OllamaChatClient chatClient;

    private final OllamaChatClient openAiChatClient;

    // 历史消息列表
    static List<Message> historyMessage = new ArrayList<>();
    // 历史消息列表的最大长度
    static int maxLen = 1000;

    @GetMapping("/ai/generate")
    public String generate(@RequestParam(value = "message", defaultValue = "给我讲个笑话") String message) {
        log.info("message:{},thread:{}", message, Thread.currentThread());
        // 用户输入的文本是UserMessage
        historyMessage.add(new UserMessage(message));
        // 发给AI前对历史消息对列的长度进行检查
        if (historyMessage.size() > maxLen) {
            historyMessage = historyMessage.subList(historyMessage.size() - maxLen - 1, historyMessage.size());
        }
        // 获取AssistantMessage
        ChatResponse chatResponse = chatClient.call(new Prompt(historyMessage));
        AssistantMessage assistantMessage = chatResponse.getResult().getOutput();
        // 将AI回复的消息放到历史消息列表中
        historyMessage.add(assistantMessage);
        return assistantMessage.getContent();
    }

    @GetMapping(value = "/ai/generateStream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> generateStream(@RequestParam(value = "message", defaultValue = "给我讲个笑话") String message) {
        // 用户输入的文本是UserMessage
        historyMessage.add(new UserMessage(message));
        // 发给AI前对历史消息对列的长度进行检查
        if (historyMessage.size() > maxLen) {
            historyMessage = historyMessage.subList(historyMessage.size() - maxLen - 1, historyMessage.size());
        }
        Prompt prompt = new Prompt(historyMessage);
        log.info("当前上下问historyMessage:{}", historyMessage);
        Flux<ChatResponse> chatResponseFlux = chatClient.stream(prompt);
        log.info("生成结束:正在返回");
        return chatResponseFlux.map(chatResponse -> {
            AssistantMessage assistantMessage = chatResponse.getResult().getOutput();
            // 将AI回复的消息放到历史消息列表中
            historyMessage.add(assistantMessage);
            //返回具体回答
            return assistantMessage.getContent().replace("data:", "");
        });
    }

    @GetMapping("hf/ai/generate")
    public String huggingGenerate(@RequestParam(value = "message", defaultValue = "给我讲个笑话") String message) {
        HuggingfaceChatClient client = new HuggingfaceChatClient("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", "https://api-inference.huggingface.co/models/cardiffnlp/twitter-roberta-base-sentiment-latest");
        Prompt prompt = new Prompt(message);
        ChatResponse response = client.call(prompt);
        return response.getResult().getOutput().getContent();
    }

    /***********************************************openAi*********************************************************/
    @GetMapping("/open/ai/generate")
    public Map openGenerate(@RequestParam(value = "message", defaultValue = "给我讲个笑话") String message) {
        return Map.of("generation", openAiChatClient.call(message));
    }

    @GetMapping("/open/ai/generateStream")
    public Flux<ChatResponse> openGenerateStream(@RequestParam(value = "message", defaultValue = "给我讲个笑话") String message) {
        Prompt prompt = new Prompt(new UserMessage(message));
        return openAiChatClient.stream(prompt);
    }
}
