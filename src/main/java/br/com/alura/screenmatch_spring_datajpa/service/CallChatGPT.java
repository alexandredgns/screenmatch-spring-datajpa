package br.com.alura.screenmatch_spring_datajpa.service;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;

public class CallChatGPT {
    public static String translateToPortuguese (String text) {
        OpenAiService service = new OpenAiService(System.getenv("OPENAI_KEY"));

        CompletionRequest request = CompletionRequest.builder()
                .model("gpt-3.5-turbo-instruct")
                .prompt("translate this text to portuguese: " + text)
                .maxTokens(1000)
                .temperature(0.7)
                .build();

        var response = service.createCompletion(request);
        return response.getChoices().get(0).getText();
    }
}
