package br.com.alura.screenmatch_spring_datajpa.service;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;

public class CallChatGPT {
    public static String translateToPortuguese (String text) {
        OpenAiService service = new OpenAiService("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");

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
