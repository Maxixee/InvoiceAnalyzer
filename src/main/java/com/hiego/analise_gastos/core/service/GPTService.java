package com.hiego.analise_gastos.core.service;

import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.stereotype.Service;
import org.springframework.ai.chat.model.ChatResponse;

@Service
public class GPTService {

    private final ChatModel chatModel;

    public GPTService(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @RateLimiter(name = "openai")
    public ChatResponse callingGPTapi(String prompt){

        try{
            return chatModel.call(
                    new Prompt(
                            prompt,
                            OpenAiChatOptions.builder()
                                    .model("gpt-4o")
                                    .temperature(0.4)
                                    .build()
                    ));
        } catch (RequestNotPermitted e) {
            throw new RuntimeException("Limite de chamadas por minuto atingido. Aguarde alguns instantes.... ", e);
        }

    }
}
