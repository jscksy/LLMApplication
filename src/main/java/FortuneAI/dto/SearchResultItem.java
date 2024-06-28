package FortuneAI.dto;

import lombok.Data;

@Data
public class SearchResultItem {
    private String title;
    private String link;
    private String snippet;
    private String content;
}
