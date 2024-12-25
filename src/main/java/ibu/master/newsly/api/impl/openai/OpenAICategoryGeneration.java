package ibu.master.newsly.api.impl.openai;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;
import ibu.master.newsly.core.api.generateCategory.CategoryGenerator;

public class OpenAICategoryGeneration implements CategoryGenerator {
    private final OpenAiService openAiService;

    public OpenAICategoryGeneration(OpenAiService openAiService) {
        this.openAiService = openAiService;
    }

    @Override
    public String generateCategory(String category) {
        String prompt = "Suggest a category for the following News content: " + category;
        CompletionRequest completionRequest = CompletionRequest.builder()
                .prompt(prompt)
                .model("gpt-3.5-turbo-instruct")
                .maxTokens(10)
                .build();
        return openAiService.createCompletion(completionRequest).getChoices().get(0).getText().trim();
    }
}