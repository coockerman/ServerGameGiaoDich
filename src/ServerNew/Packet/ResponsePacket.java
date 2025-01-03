package ServerNew.Packet;

import ServerNew.Model.ChatMessage;
import ServerNew.Model.MongoModel.PlayerInfo;
import ServerNew.Model.UpdateStore.UpdateStoreData;

import java.util.List;

public class ResponsePacket {
    private String typeResponse;
    private String callbackResult;
    private PlayerInfo playerInfo;
    private UpdateStoreData updateStoreData;
    private ChatMessage chatMessage;
    private List<PlayerInfo> titleAllPlayer;

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

    public ResponsePacket(String typeResponse, ChatMessage chatMessage) {
        this.typeResponse = typeResponse;
        this.chatMessage = chatMessage;
    }

    public ResponsePacket(String typeResponse, List<PlayerInfo> titleAllPlayer) {
        this.typeResponse = typeResponse;
        this.titleAllPlayer = titleAllPlayer;
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

    public ChatMessage getChatMessage() {
        return chatMessage;
    }

    public void setChatMessage(ChatMessage chatMessage) {
        this.chatMessage = chatMessage;
    }

    public List<PlayerInfo> getTitleAllPlayer() {
        return titleAllPlayer;
    }

    public void setTitleAllPlayer(List<PlayerInfo> titleAllPlayer) {
        this.titleAllPlayer = titleAllPlayer;
    }
}
