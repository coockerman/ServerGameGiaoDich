package ServerNew.Packet;

import ServerNew.Model.MongoModel.PlayerInfo;
import ServerNew.Model.UpdateStore.UpdateStoreData;

public class ResponsePacket {
    private String typeResponse;
    private String callbackResult;
    private PlayerInfo playerInfo;
    private UpdateStoreData updateStoreData;

    public ResponsePacket(String typeResponse, String callbackResult) {
        this.typeResponse = typeResponse;
        this.callbackResult = callbackResult;
    }

    public ResponsePacket(String typeResponse, String callbackResult, PlayerInfo playerInfo) {
        this.typeResponse = typeResponse;
        this.callbackResult = callbackResult;
        this.playerInfo = playerInfo;
    }

    public ResponsePacket(String typeResponse, String callbackResult, UpdateStoreData updateStoreData) {
        this.typeResponse = typeResponse;
        this.callbackResult = callbackResult;
        this.updateStoreData = updateStoreData;
    }

    public ResponsePacket(String typeResponse, String callbackResult, PlayerInfo playerInfo, UpdateStoreData updateStoreData) {
        this.typeResponse = typeResponse;
        this.callbackResult = callbackResult;
        this.playerInfo = playerInfo;
        this.updateStoreData = updateStoreData;
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

    public UpdateStoreData getUpdateStoreData() {
        return updateStoreData;
    }

    public void setUpdateStoreData(UpdateStoreData updateStoreData) {
        this.updateStoreData = updateStoreData;
    }
}
