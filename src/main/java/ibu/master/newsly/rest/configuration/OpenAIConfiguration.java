package ibu.master.newsly.rest.configuration;

import com.theokanning.openai.service.OpenAiService;
import ibu.master.newsly.api.impl.openai.OpenAICategoryGeneration;
import ibu.master.newsly.core.api.generateCategory.CategoryGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAIConfiguration {
    @Value("${openai.secret}")
    private String apiSecret;

    @Bean
    public CategoryGenerator generateCategory() {
        return new OpenAICategoryGeneration(this.openAiService());
    }

    @Bean
    public OpenAiService openAiService() {
        return new OpenAiService(this.apiSecret);
    }
}