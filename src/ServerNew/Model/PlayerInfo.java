package ServerNew.Model;

import org.java_websocket.WebSocket;

public class PlayerInfo {

    private String userName;
    private String namePlayer;

    public PlayerInfo() {
    }

    public PlayerInfo(String namePlayer, String userName) {
        this.namePlayer = namePlayer;
        this.userName = userName;
    }

    public String getNamePlayer() {
        return namePlayer;
    }

    public void setNamePlayer(String namePlayer) {
        this.namePlayer = namePlayer;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
