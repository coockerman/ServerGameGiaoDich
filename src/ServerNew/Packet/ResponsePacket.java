package ServerNew.Packet;

import ServerNew.Model.PlayerInfo;
import ServerNew.Model.Shop;

public class ResponsePacket {
    private String typeResponse;
    private String callbackResult;
    private PlayerInfo playerInfo;
    private Shop shop;

    public ResponsePacket(String typeResponse, String callbackResult) {
        this.typeResponse = typeResponse;
        this.callbackResult = callbackResult;
    }

    public ResponsePacket(String typeResponse, String callbackResult, PlayerInfo playerInfo) {
        this.typeResponse = typeResponse;
        this.callbackResult = callbackResult;
        this.playerInfo = playerInfo;
    }

    public ResponsePacket(String typeResponse, String callbackResult, Shop shop) {
        this.typeResponse = typeResponse;
        this.callbackResult = callbackResult;
        this.shop = shop;
    }

    public String getTypeResponse() {
        return typeResponse;
    }

    public void setTypeResponse(String typeResponse) {
        this.typeResponse = typeResponse;
    }

    public String getCallbackResult() {
        return callbackResult;
    }

    public void setCallbackResult(String callbackResult) {
        this.callbackResult = callbackResult;
    }

    public PlayerInfo getPlayerInfo() {
        return playerInfo;
    }

    public void setPlayerInfo(PlayerInfo playerInfo) {
        this.playerInfo = playerInfo;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }
}
