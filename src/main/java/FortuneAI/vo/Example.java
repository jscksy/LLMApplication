package FortuneAI.vo;

public class Example {
    public String message;
    public String intent;

    public Example(String message, String intent) {
        this.message = message;
        this.intent = intent;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }
}
