package ServerNew.Model;

import org.java_websocket.WebSocket;

public class AuthData {
    private WebSocket socket;
    private String userName;
    private String password;

    public AuthData() {

    }

    public WebSocket getSocket() {
        return socket;
    }

    public void setSocket(WebSocket socket) {
        this.socket = socket;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
