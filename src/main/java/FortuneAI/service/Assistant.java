package FortuneAI.service;

import FortuneAI.prompt.Prompts;
import FortuneAI.vo.CustInfo;
import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;

interface Assistant {

        @SystemMessage("你是一位智能问答机器人，请从网上或本地知识库搜寻相关知识，回答客户对于金融产品的问题。")
        String prodInfoQuery(@MemoryId int memoryId, @UserMessage String userMessage);

        String chat(@MemoryId int memoryId, @UserMessage String userMessage);

        String chat(@UserMessage String userMessage);

        @UserMessage("首先判断用户回答{{text}}是否包含上文准备采集的信息，如果不包含返回空对象。如果包含则根据用户的回答{{text}}，提取相应的信息。如果{{text}}中不包含某项信息，将该项属性置为空指针null。")
        CustInfo extractPersonFrom(@MemoryId int memoryId, @V("text") String text);

        @UserMessage("分辨用户问题所属的场景：{{text}}")
        Prompts.PROMPTS analyzeSentimentOf(@MemoryId int memory, @V("text") String text);
    }