package FortuneAI.data;

import FortuneAI.vo.CustInfo;

import java.util.HashMap;
import java.util.Map;

public class CustomInfo {
    public static Map<String, CustInfo> customInfos = new HashMap<>();

    static {
        customInfos.put("1",new CustInfo("小王","1",25,"程序员",
                "250000",false));
        customInfos.put("2",new CustInfo("小陈","2",30,null,
                null,true));
    }
}
