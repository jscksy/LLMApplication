package FortuneAI.vo;

import FortuneAI.Interfaces.AbstractSearchEngine;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class SearchEngineInfo {
    private String name;
    private Boolean enable;
    @JsonIgnore
    private AbstractSearchEngine engine;
}
