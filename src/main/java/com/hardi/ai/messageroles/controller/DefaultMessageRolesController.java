package com.hardi.ai.messageroles.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/defaults")
public class DefaultMessageRolesController {

    private final ChatClient chatClient;

    public DefaultMessageRolesController(ChatClient.Builder chatClientBuilder) {
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

    @GetMapping("/chat")
    public String chat(@RequestParam("message") String message) {
        return chatClient
                .prompt()
                .user(message)
                .call()
                .content();
    }

    @GetMapping("/v2/chat")
    public String chatOverridden(@RequestParam("message") String message) {
        return chatClient
                .prompt()
                .user(message)
                .system("You are HR assistant") //overridden default system role
                .call()
                .content();
    }
}
