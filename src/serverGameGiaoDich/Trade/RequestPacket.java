package serverGameGiaoDich.Trade;

import com.google.gson.Gson;

public class RequestPacket {
    private int packetType;
    private AbstractData abstractData;
    private UpdateStoreData updateStoreData;
    private String namePlayer;      // Tên người chơi
    private String messagePlayer;   // Tin nhắn
    private String dayPlay;    // Dùng cho PacketType 8
    private boolean isRegisterPlayer;

    public RequestPacket(int packetType) {
        this.packetType = packetType;
        this.abstractData = new AbstractData(true);
        this.updateStoreData = null;
    }

    public RequestPacket(int packetType, AbstractData abstractData) {
        this.packetType = packetType;
        this.abstractData = abstractData;
        this.updateStoreData = null;
    }

    public RequestPacket(int packetType, String namePlayer, boolean isRegisterPlayer) {
        this.packetType = packetType;
        this.namePlayer = namePlayer;
        this.isRegisterPlayer = isRegisterPlayer;
    }
    public RequestPacket(int packetType, UpdateStoreData updateStoreData) {
        this.packetType = packetType;
        this.abstractData = null;
        this.updateStoreData = updateStoreData;
    }

    // Constructor cho SendMessage và BroadcastMessage
    public RequestPacket(int packetType, String namePlayer, String messagePlayer) {
        this.packetType = packetType;
        this.namePlayer = namePlayer;
        this.messagePlayer = messagePlayer;
        this.abstractData = null;
        this.updateStoreData = null;
    }

    public static String toJson(RequestPacket requestPacket) {
        Gson gson = new Gson();
        return gson.toJson(requestPacket);
    }

    public static RequestPacket fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, RequestPacket.class);
    }

    public int getPacketType() {
        return packetType;
    }

    public void setPacketType(int packetType) {
        this.packetType = packetType;
    }

    public AbstractData getAbstractData() {
        return abstractData;
    }

    public void setAbstractData(AbstractData abstractData) {
        this.abstractData = abstractData;
    }

    public UpdateStoreData getUpdateStoreData() {
        return updateStoreData;
    }

    public void setUpdateStoreData(UpdateStoreData updateStoreData) {
        this.updateStoreData = updateStoreData;
    }

    public String getNamePlayer() {
        return namePlayer;
    }

    public void setNamePlayer(String namePlayer) {
        this.namePlayer = namePlayer;
    }

    public String getMessagePlayer() {
        return messagePlayer;
    }

    public void setMessagePlayer(String messagePlayer) {
        this.messagePlayer = messagePlayer;
    }
    
    public String getDayPlay() {
        return dayPlay;
    }

    public void setDayPlay(String dayPlay) {
        this.dayPlay = dayPlay;
    }

    public boolean isRegisterPlayer() {
        return isRegisterPlayer;
    }

    public void setRegisterPlayer(boolean registerPlayer) {
        isRegisterPlayer = registerPlayer;
    }
}
