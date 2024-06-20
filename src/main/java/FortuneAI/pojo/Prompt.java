package FortuneAI.pojo;

public class Prompt {

    String name;
    String description;
    String prompt_template;

    public Prompt(String name, String description, String prompt_template) {
        this.name = name;
        this.description = description;
        this.prompt_template = prompt_template;
    }

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

    public String getPrompt_template() {
        return prompt_template;
    }

    public void setPrompt_template(String prompt_template) {
        this.prompt_template = prompt_template;
    }
}
