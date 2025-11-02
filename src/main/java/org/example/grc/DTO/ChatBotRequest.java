package org.example.grc.DTO;

public class ChatBotRequest {
    private String description;
    private String type;

    public ChatBotRequest() {}

    public ChatBotRequest(String description, String type) {
        this.description = description;
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

