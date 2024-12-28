package ServerNew.Model;

import org.java_websocket.WebSocket;

public class PlayerInfo {
    private WebSocket socket;

    public PlayerInfo() {
    }

    public WebSocket getSocket() {
        return socket;
    }

    public void setSocket(WebSocket socket) {
        this.socket = socket;
    }
}
