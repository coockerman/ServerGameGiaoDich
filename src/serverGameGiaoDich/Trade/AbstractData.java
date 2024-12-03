package serverGameGiaoDich.Trade;

import com.google.gson.Gson;

public class AbstractData {
    public boolean isStatus;
    public int itemType;
    public int count;
    public float price;

    // Constructor
    public AbstractData(boolean isStatus, int itemType, int count, float price) {
        this.isStatus = isStatus;
        this.itemType = itemType;
        this.count = count;
        this.price = price;
    }

    // Constructor chỉ với isStatus
    public AbstractData(boolean isStatus) {
        this.isStatus = isStatus;
    }

    // Chuyển đối tượng AbstractData sang JSON
    public static String toJson(AbstractData abstractData) {
        Gson gson = new Gson();
        return gson.toJson(abstractData);
    }

    // Chuyển JSON về đối tượng AbstractData
    public static AbstractData fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, AbstractData.class);
    }
}
