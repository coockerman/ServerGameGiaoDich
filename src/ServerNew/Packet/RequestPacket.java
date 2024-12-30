package ServerNew.Packet;

import ServerNew.Model.AuthData;
import ServerNew.Model.PlayerInfo;

public class RequestPacket {
    private String typeRequest;
    private AuthData authData;
    private PlayerInfo playerInfo;

    public RequestPacket() {
    }


    public String getTypeRequest() {
        return typeRequest;
    }

    public void setTypeRequest(String typeRequest) {
        this.typeRequest = typeRequest;
    }

    public AuthData getAuthData() {
        return authData;
    }

    public void setAuthData(AuthData authData) {
        this.authData = authData;
    }

    public PlayerInfo getPlayerInfo() {
        return playerInfo;
    }

    public void setPlayerInfo(PlayerInfo playerInfo) {
        this.playerInfo = playerInfo;
    }
}
