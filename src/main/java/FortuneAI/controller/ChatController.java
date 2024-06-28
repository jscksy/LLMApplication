package FortuneAI.controller;


import FortuneAI.service.FortuneAiAssistant;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;

@Controller
public class ChatController {

    @Resource
    private FortuneAiAssistant fortuneAiAssistant;

    @GetMapping("/chat")
    public void chat(){
        fortuneAiAssistant.chat();
    }

}
