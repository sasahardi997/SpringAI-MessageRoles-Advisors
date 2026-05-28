package com.hardi.ai.messageroles.advisors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.CallAdvisor;
import org.springframework.ai.chat.client.advisor.api.CallAdvisorChain;

public class TokenUsageAuditAdvisor implements CallAdvisor {

    private static final Logger log = LoggerFactory.getLogger(TokenUsageAuditAdvisor.class);

    @Override
    public ChatClientResponse adviseCall(ChatClientRequest chatClientRequest, CallAdvisorChain callAdvisorChain) {

        // nextCall() is actual call to LLM
        // 1. -> we can modify data before that call (pre-process advisor)
        // 2. -> we can modify data after that call (post-process advisor)
        var chatClientResponse = callAdvisorChain.nextCall(chatClientRequest);
        var chatResponse = chatClientResponse.chatResponse();

        if(chatResponse.getMetadata() != null) {
            var usage = chatResponse.getMetadata().getUsage();

            if(usage != null) {
                log.info("Token usage details: {}", usage);
            }
        }

        return chatClientResponse;
    }

    @Override
    public String getName() {
        return TokenUsageAuditAdvisor.class.getName();
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
