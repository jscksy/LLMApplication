package FortuneAI.searchengine;


import FortuneAI.Interfaces.AbstractSearchEngine;
import FortuneAI.constant.AdiConstant;
import FortuneAI.dto.GoogleSearchResp;
import FortuneAI.dto.SearchResult;
import FortuneAI.dto.SearchResultItem;
import FortuneAI.utils.MPPageUtil;
import FortuneAI.vo.GoogleSetting;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;


@Slf4j
public class GoogleSearchEngine extends AbstractSearchEngine<GoogleSetting> {

    public GoogleSearchEngine() {
        super(AdiConstant.SearchEngineName.GOOGLE, AdiConstant.SysConfigKey.GOOGLE_SETTING, GoogleSetting.class);
    }

    @Override
    public boolean isEnabled() {
        return StringUtils.isNoneBlank(setting.getKey(), setting.getCx());
    }

    @Override
    public SearchResult search(String searchTxt) {
        SearchResult result = new SearchResult();
        List<SearchResultItem> items = new ArrayList<>();
        try {
            ResponseEntity<GoogleSearchResp> resp = getRestTemplate().getForEntity(MessageFormat.format("{0}?key={1}&cx={2}&q={3}", setting.getUrl(), setting.getKey(), setting.getCx(), searchTxt), GoogleSearchResp.class);
            if (null != resp && HttpStatus.OK.isSameCodeAs(resp.getStatusCode())) {
                GoogleSearchResp googleSearchResp = resp.getBody();
                if (null != googleSearchResp.getError()) {
                    log.error("google search error,code:{},message:{}", googleSearchResp.getError().getCode(), googleSearchResp.getError().getMessage());
                    result.setErrorMessage(googleSearchResp.getError().getMessage());
                } else {
                    log.info("google response:{}", resp);
                    items = MPPageUtil.convertToList(googleSearchResp.getItems(), SearchResultItem.class);
                }
            }
        } catch (Exception e) {
            log.error("google search error", e);
        }
        result.setItems(items);
        return result;
    }

}
