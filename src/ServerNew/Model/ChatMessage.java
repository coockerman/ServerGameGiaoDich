package ServerNew.Model;

public class ChatMessage {
    private String namePlayer;
    private String message;

    public ChatMessage() {}

    public ChatMessage(String namePlayer, String message)
    {
        this.namePlayer = namePlayer;
        this.message = message;
    }

    public String getNamePlayer() {
        return namePlayer;
    }

    public void setNamePlayer(String namePlayer) {
        this.namePlayer = namePlayer;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
