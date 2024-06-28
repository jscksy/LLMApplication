package FortuneAI.service;

import FortuneAI.data.CustomInfo;
import FortuneAI.prompt.Prompts;
import FortuneAI.vo.CustInfo;
import FortuneAI.vo.Example;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.structured.StructuredPromptProcessor;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import static java.util.Arrays.asList;

@Service
@Slf4j
public class FortuneAiAssistant {

    public static ChatLanguageModel model = OpenAiChatModel.builder()
            .apiKey("sk-VZIOrXzVLF7NGZnm0Z3UpSbenrmyl0Hxsy5ZdWEIbJawu4Ju")
            .baseUrl("https://api.chatanywhere.tech/v1")
            .logRequests(true)
            .logResponses(true)
            .temperature(0.1)
            .build();

    public static HashMap<String,String> prompts = new HashMap<>();

    public void chat(){
        prompts.put("产品信息查询","根据客户需求，返回对应产品的信息");
        prompts.put("资产配置","回答资产配置相关的问题");

        // 构建langchain4j封装的AI助理12
        Assistant assistant = AiServices.builder(Assistant.class)
                .chatLanguageModel(model)
                .chatMemoryProvider(memoryId -> MessageWindowChatMemory.withMaxMessages(15))
                .build();

        Scanner scanner = new Scanner(System.in);

        System.out.println("请输入您的证件号作为唯一身份标识：");
        String id = scanner.nextLine();
        CustInfo origin = CustomInfo.customInfos.getOrDefault(id,new CustInfo());
        origin.setIdentityId(id);
        CustomInfo.customInfos.put(id,origin);

        mainProgram:while(true){
            System.out.println("请开始和大模型进行交互");
            // 模拟用户提问
            String question = scanner.nextLine();
            System.out.println("userMessage:" + question);

            // 利用langchian4j特性构造的提示词
//                FortuneAI.prompt.Prompts.PROMPTS sentiment = assistant.analyzeSentimentOf(1,question);
//                System.out.println(sentiment);
//                String identity = sentiment.toString();
            AiMessage aiMessage;
            Prompt prompt;
            // 用大模型分析用户提问，得到适合回答该问题的身份
            // 正常方式构造的提示词
            Prompts.IdentifyPrompt identifyPrompt = new Prompts.IdentifyPrompt();
            identifyPrompt.setIntents(asList("产品信息查询","资产配置","通用场景提问"));
            identifyPrompt.setMessage(question);
            identifyPrompt.setExamples(asList(new Example("如意宝年化收益率是多少？","产品信息查询"),
                    new Example("天天利有多少只产品？","产品信息查询"),
                    new Example("现在卖的最好的产品是哪只？","产品信息查询"),
                    new Example("闲钱怎么处理？","资产配置"),
                    new Example("我刚发工资了，想买些保险应对风险，有什么建议？","资产配置"),
                    new Example("我有30w存款，你有哪些投资理财建议吗？","资产配置"),
                    new Example("今天天气如何？","通用场景提问"),
                    new Example("怎么减肥？","通用场景提问")));

            prompt = StructuredPromptProcessor.toPrompt(identifyPrompt);
            String identity = assistant.chat(1,prompt.toUserMessage().singleText());
            System.out.println("场景："+identity);

            // 如果不属于任何一种身份，设置默认情况保证程序能够正常执行
            if(!prompts.containsKey(identity)){
                identity = "通用大模型";
                // 利用得到的身份信息构造回答原始问题的提示词
                Prompts.AnswerPrompt answerPromptPrompt = new Prompts.AnswerPrompt();
                answerPromptPrompt.setInput(question);
                answerPromptPrompt.setDescription("回答通用问题");
                answerPromptPrompt.setName(identity);
                prompt = StructuredPromptProcessor.toPrompt(answerPromptPrompt);
                // 利用大模型回答问题并输出结果
                String res  = assistant.chat(3,prompt.toUserMessage().singleText());
                System.out.println(res);
            }else if(identity.equals("资产配置")){
                //step1：获取客户信息
                List<String> res = checkAndReturnCustInfo(id,question);
                //客户信息完整
                if(res.get(0).equals("0")){
                    String result = assistant.chat(3,res.get(1));
                    System.out.println(result);
                }else{
                    String info;
                    int retryTimes = 0;
                    InfoCollection:while(res.get(0).equals("1")){
                        System.out.println("Propmt: " +res.get(1));
                        info = assistant.chat(2,res.get(1));
                        System.out.println("LLMMessage: " + info);
                        CustInfo custInfo = assistant.extractPersonFrom(2,scanner.nextLine());
                        System.out.println(custInfo.toString());
                        origin.update(custInfo);
                        CustomInfo.customInfos.put(origin.getIdentityId(),origin);
                        res = checkAndReturnCustInfo(id,question);
                        if(++retryTimes>6){
                            System.out.println("信息采集次数过多，请您确认是否还要继续该流程");
                            continue mainProgram;
                        }
                    }
                    System.out.println(res.get(1));
                    info = assistant.chat(3,res.get(1));
                    System.out.println("LLMMessage: " +info);

                }
            }else if(identity.equals("产品信息查询")){

                // 利用得到的身份信息构造回答原始问题的提示词
                Prompts.ProductQuery productQuery = new   Prompts.ProductQuery();
                productQuery.setInput(question);
                prompt = StructuredPromptProcessor.toPrompt(productQuery);
                // 利用大模型回答问题并输出结果
                String res  = assistant.prodInfoQuery(3,prompt.toUserMessage().singleText());
                System.out.println(res);
            }
        }

    }

    // 检查并返回客户信息
    // 如果客户画像完整,数组元素0位置返回客户画像提示词
    // 如果客户画像不完整，数组元素1位置返回客户信息收集话术
    public static List<String> checkAndReturnCustInfo(String id,String question){
        System.out.println("正在查询客户画像信息···");
        List<String> res = new ArrayList<>();
        CustInfo custInfo = CustomInfo.customInfos.getOrDefault(id, new CustInfo());
        List<String> leftInfo = new ArrayList<>();
        if(custInfo.getIdentityId()==null){
            leftInfo.add("客户身份证");
        }
        if(custInfo.getName()==null){
            leftInfo.add("客户姓名");
        }
        if(custInfo.getAge()==null){
            leftInfo.add("客户年龄");
        }
        if(custInfo.getJob()==null){
            leftInfo.add("客户工作");
        }
        if(custInfo.getBalance()==null){
            leftInfo.add("客户存款");
        }
        if(custInfo.getMarried()==null){
            leftInfo.add("客户婚姻状况");
        }
        if(leftInfo.isEmpty()){
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("请根据以下客户信息，客户姓名：");
            stringBuilder.append(custInfo.getName()+"\n");
            stringBuilder.append("今年：");
            stringBuilder.append(custInfo.getAge()+"岁\n");
            stringBuilder.append("职业情况：");
            stringBuilder.append(custInfo.getJob()+"\n");
            stringBuilder.append("资产情况：");
            stringBuilder.append(custInfo.getBalance()+"\n");
            stringBuilder.append(custInfo.getMarried()?"已婚\n":"未婚\n");
            stringBuilder.append("生成一段100至200字的文字，结合客户信息分条理具体回答客户问题："+ question);
            res.add("0");
            res.add(stringBuilder.toString());
        }else{
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("目前缺乏完整的客户画像，无法很好地回答客户的问题，请生成一段符合自然语言的话术来收集客户以下信息：");
            stringBuilder.append(leftInfo.get(0));
            res.add("1");
            res.add(stringBuilder.toString());
        }

        return res;
    }

}
