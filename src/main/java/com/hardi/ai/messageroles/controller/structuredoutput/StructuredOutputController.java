package com.hardi.ai.messageroles.controller.structuredoutput;

import com.hardi.ai.messageroles.model.CountryCities;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.converter.ListOutputConverter;
import org.springframework.ai.converter.MapOutputConverter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class StructuredOutputController {

    private final ChatClient chatClient;

    public StructuredOutputController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/chat/country-cities")
    public ResponseEntity<CountryCities> chat(@RequestParam("message") String message) {
        var countryCities = chatClient
                .prompt()
                .advisors(new SimpleLoggerAdvisor()) //Check context provided in logs!
                .user(message)
                .call()
                .entity(CountryCities.class);

        return ResponseEntity.ok().body(countryCities);
    }

    @GetMapping("/chat/cities")
    public ResponseEntity<List<String>> chatList(@RequestParam("message") String message) {
        var countryCities = chatClient
                .prompt()
                .advisors(new SimpleLoggerAdvisor()) //Check context provided in logs!
                .user(message)
                .call()
                .entity(new ListOutputConverter()); //Check getFormat() method inside this class

        return ResponseEntity.ok().body(countryCities);
    }

    //Timeout can happen when using Local LLM
    @GetMapping("/chat/cities-with-details")
    public ResponseEntity<Map<String, Object>> chatMap(@RequestParam("message") String message) {
        var countryCities = chatClient
                .prompt()
                .advisors(new SimpleLoggerAdvisor()) //Check context provided in logs!
                .user(message)
                .call()
                .entity(new MapOutputConverter()); //Check getFormat() method inside this class

        return ResponseEntity.ok().body(countryCities);
    }

    @GetMapping("/chat/country-cities-bean-list")
    public ResponseEntity<List<CountryCities>> chatBeanList(@RequestParam("message") String message) {
        var countryCities = chatClient
                .prompt()
                .advisors(new SimpleLoggerAdvisor()) //Check context provided in logs!
                .user(message)
                .call()
                .entity(new ParameterizedTypeReference<List<CountryCities>>() {});

        return ResponseEntity.ok().body(countryCities);
    }
}
