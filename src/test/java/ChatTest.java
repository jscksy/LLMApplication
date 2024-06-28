import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;

import java.util.Scanner;

public class ChatTest {

    public static ChatLanguageModel model = OpenAiChatModel.builder()
            .apiKey("sk-VZIOrXzVLF7NGZnm0Z3UpSbenrmyl0Hxsy5ZdWEIbJawu4Ju")
            .baseUrl("https://api.chatanywhere.tech/v1")
            .logRequests(true)
            .logResponses(true)
            .temperature(0.1)
            .build();

    public static void main(String[] args) {

        while(true){
            Scanner scanner = new Scanner(System.in);
            String question = scanner.nextLine();
            if(question.equals("quit")){
                break;
            }
            String res = model.generate(question);
            System.out.println(res);
        }

    }
}
