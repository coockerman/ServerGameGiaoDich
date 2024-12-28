package ServerNew.Model;

import org.java_websocket.WebSocket;

public class AuthData {
    private WebSocket socket;

    public AuthData() {

    }

    public WebSocket getSocket() {
        return socket;
    }

    public void setSocket(WebSocket socket) {
        this.socket = socket;
    }
}
