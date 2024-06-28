package FortuneAI.Interfaces;

import FortuneAI.dto.SearchResult;
import FortuneAI.utils.JsonUtil;
import FortuneAI.utils.LocalCache;
import FortuneAI.utils.SpringUtil;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.net.Proxy;

public abstract class AbstractSearchEngine<T> {

    protected String engineName;

    protected Proxy proxy;

    public AbstractSearchEngine(String engineName, String settingName, Class<T> clazz) {
        this.engineName = engineName;
        String st = LocalCache.CONFIGS.get(settingName);
        setting = JsonUtil.fromJson(st, clazz);
    }


    protected T setting;

    public abstract boolean isEnabled();

    public abstract SearchResult search(String searchTxt);

    public AbstractSearchEngine setProxy(Proxy proxy) {
        this.proxy = proxy;
        return this;
    }

    protected RestTemplate getRestTemplate() {
        RestTemplate restTemplate = SpringUtil.getBean(RestTemplate.class);
        if (null != proxy) {
            SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
            requestFactory.setProxy(proxy);
            restTemplate.setRequestFactory(requestFactory);
        }
        return restTemplate;
    }
}
