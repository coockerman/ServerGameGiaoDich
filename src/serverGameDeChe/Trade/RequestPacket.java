package serverGameDeChe.Trade;

import com.google.gson.Gson;

public class RequestPacket {
    private int packetType;
    private AbstractData abstractData;
    private UpdateStoreData updateStoreData;

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

    public RequestPacket(int packetType, UpdateStoreData updateStoreData) {
        this.packetType = packetType;
        this.abstractData = null;
        this.updateStoreData = updateStoreData;
    }

    public static String toJson(RequestPacket requestPacket) {
        Gson gson = new Gson();
        return gson.toJson(requestPacket);
    }

    // Chuyển JSON về đối tượng RequestPacket
    public static RequestPacket fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, RequestPacket.class);
    }

    // Getters and Setters nếu cần
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
}
