package FortuneAI.prompt;

import FortuneAI.vo.Example;
import dev.langchain4j.model.input.structured.StructuredPrompt;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

import java.util.HashMap;
import java.util.List;

public class Prompts {

    public static String physics_template = "你是一位非常聪明的物理学家，非常擅长回答物理相关的问题,"
            +"并且会以一种简洁易懂的方式对问题做出讲解。"
            +"当你无法回答问题的时候，就会主动承认无法回答问题。"
            +"以下是具体问题:"
            +"{input}";

    public static String math_template = "你是一位非常棒的数学家，非常擅长回答数学相关的问题。"
            +"你之所以这么棒，是因为你能够将难题拆解成它们的组成部分,"
            +"对组成部分分别作答后，再将它们组合起来最终成功的回答出最初的原始问题。"
            +"以下是具体问题:"
            +"{input}";

    public static String insure_template = "你是一位非常棒的保险销售经理，非常擅长回答保险销售相关的问题。"
            +"你之所以这么棒，是因为你能够将难题拆解成它们的组成部分,"
            +"对组成部分分别作答后，再将它们组合起来最终成功的回答出最初的原始问题。"
            +"以下是具体问题:"
            +"{input}";

    public static String fund_template = "你是一位非常棒的资产配置专家，非常擅长回答资产配置相关的问题。"
            +"你之所以这么棒，是因为你能够将难题拆解成它们的组成部分,"
            +"对组成部分分别作答后，再将它们组合起来最终成功的回答出最初的原始问题。"
            +"以下是具体问题:"
            +"{{input}}";

    public static HashMap<String,String> prompts = new HashMap<>();
    static {
        prompts.put("产品信息查询","根据客户需求，返回对应产品的信息");
        prompts.put("资产配置","回答资产配置相关的问题");
    }

    public static String[] PROMPT = {"你是一位非常棒的{{name}}，非常擅长{{description}}。",
            "你之所以这么棒，是因为你能够将难题拆解成它们的组成部分,",
            "对组成部分分别作答后，再将它们组合起来最终成功的回答出最初的原始问题。",
            "以下是具体问题:",
            "{{input}}",
            "请按以下格式组织你的回答：",
            "name: ···",
            "answer:···"
            };

    public static enum PROMPTS {
        产品信息查询,
        资产配置,
        通用大模型
    }

    // 构建回答问题的提示词
    @StructuredPrompt({"你是一位非常棒的{{name}}，非常擅长{{description}}。",
            "你之所以这么棒，是因为你能够将难题拆解成它们的组成部分,",
            "对组成部分分别作答后，再将它们组合起来最终成功的回答出最初的原始问题。",
            "以下是具体问题:",
            "{{input}}",
            "请按以下格式组织你的回答：",
            "name: {{name}}",
            "answer:···"
    })
    public static class AnswerPrompt {

        private String name;
        private String description;
        private String input;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getInput() {
            return input;
        }

        public void setInput(String input) {
            this.input = input;
        }
    }

    // 构建回答问题的提示词
    @StructuredPrompt({"请回答以下问题:",
            "{{input}}"
    })
    public static class ProductQuery {

        private String input;

        public String getInput() {
            return input;
        }

        public void setInput(String input) {
            this.input = input;
        }
    }

    // 构建意图识别的提示词模板
    @StructuredPrompt({"使用意图来标记对话中的用户消息",
            "        意图必须是以下情况之一:\n" +
                    "        {% for intent in intents %}- intents:{{intents}}\n" +
                    "        {% endfor %}。\n"+
                    "        下面给出了一些示例，请以此为参考：\n"+
                    "        {% for example in examples %}- examples:{{examples}}\n" +
                    "        Message: {example.message}\n" +
                    "        Intent: {example.intent}\n" +
                    "        {% endfor %}\n" +
                    "        用户消息: {{message}}\n"+
                    "        请直接回复该intent，不要带任何前缀 \n "
    })
    public static class IdentifyPrompt {

        private String message;
        private List<String> intents;
        private List<Example> examples;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public List<String> getIntents() {
            return intents;
        }

        public void setIntents(List<String> intents) {
            this.intents = intents;
        }

        public List<Example> getExamples() {
            return examples;
        }

        public void setExamples(List<Example> examples) {
            this.examples = examples;
        }
    }

    // 利用langchain4j特性简化构建意图识别的提示词
    interface SentimentAnalyzer {
        @UserMessage("分辨用户问题所属的场景：{{text}}")
        Prompts.PROMPTS analyzeSentimentOf(@MemoryId int memory, @V("text") String text);
    }

}
