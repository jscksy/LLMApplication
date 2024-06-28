package FortuneAI.dto;

import lombok.Data;

import java.util.List;

@Data
public class SearchResult {
    private String errorMessage;
    private List<SearchResultItem> items;
}
