package ServerNew.Packet;

import ServerNew.Model.Build.BuildGround;
import ServerNew.Model.ChatMessage;
import ServerNew.Model.MongoModel.AuthData;
import ServerNew.Model.MongoModel.PlayerInfo;
import ServerNew.Model.PasswordReset;
import ServerNew.Model.Trade;

public class RequestPacket {
    private String typeRequest;
    private AuthData authData;
    private PlayerInfo playerInfo;
    private Trade trade;
    private BuildGround buildGround;
    private ChatMessage chatMessage;
    private PasswordReset passwordReset;

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

    public Trade getTrade() {
        return trade;
    }

    public void setTrade(Trade trade) {
        this.trade = trade;
    }

    public BuildGround getBuildGround() {
        return buildGround;
    }

    public void setBuildGround(BuildGround buildGround) {
        this.buildGround = buildGround;
    }

    public ChatMessage getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(ChatMessage chatMessage) {
        this.chatMessage = chatMessage;
    }

    public PasswordReset getPasswordReset() {
        return passwordReset;
    }

    public void setPasswordReset(PasswordReset passwordReset) {
        this.passwordReset = passwordReset;
    }
}
