package com.hardi.ai.messageroles.controller;

import com.hardi.ai.messageroles.advisors.TokenUsageAuditAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AdvisorsController {

    private final ChatClient chatClient;

    public AdvisorsController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder
                .defaultSystem("""
                        You are a geography assistant specialized in capital cities.
                        
                        Your task is to answer questions about the capitals of countries accurately and concisely.
                        
                        Rules:
                        - If the user provides a country, return its capital city only unless more detail is requested.
                        - If the user provides a capital city, identify the country.
                        - Handle alternative country names and common misspellings.
                        - If a country has multiple capitals, clearly explain the roles of each capital.
                        - If the input is unclear or ambiguous, ask a short clarification question.
                        - Never invent information.
                        - Keep responses short, factual, and easy to read.
                        
                        Examples:
                        User: France
                        Assistant: Paris
                        
                        User: What is the capital of Japan?
                        Assistant: Tokyo
                        
                        User: Canberra
                        Assistant: Canberra is the capital of Australia.
                        """)
                .build();
    }

    @GetMapping("/chat/advisors")
    public String chat(@RequestParam("message") String message) {
        return chatClient
                .prompt()
                .advisors(new SimpleLoggerAdvisor()) //Provides logs on DEBUG level
                .user(message)
                .call()
                .content();
    }

    @GetMapping("/chat/custom-advisors")
    public String auditChat(@RequestParam("message") String message) {
        return chatClient
                .prompt()
                .advisors(new TokenUsageAuditAdvisor()) //Custom advisor
                .user(message)
                .call()
                .content();
    }

}
