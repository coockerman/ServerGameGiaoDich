package serverGameDeChe;

import serverGameDeChe.Trade.AbstractData;
import serverGameDeChe.Trade.Item;
import serverGameDeChe.Trade.RequestPacket;
import serverGameDeChe.Trade.UpdateStoreData;

public class ShopHandel {
    private int gold;
    private float priceGold;
    private int iron;
    private float priceIron;
    private int food;
    private float priceFood;

    public ShopHandel() {
        this.gold = 100;
        this.priceGold = 10;
        this.iron = 200;
        this.priceIron = 20;
        this.food = 400;
        this.priceFood = 30;

    }

    public RequestPacket HandelBuyByClient(AbstractData requestByClient) {
        if(requestByClient != null) {

            if(requestByClient.itemType == 0) {
                if(requestByClient.count < gold) {
                    AbstractData newData = new AbstractData(true, 0, requestByClient.count, priceGold);
                    gold -= requestByClient.count;
                    System.out.println("Xử lý dữ liệu xong, tài nguyên còn: gold: " + gold + " iron: " + iron + " food: " + food);
                    return new RequestPacket(2, newData);
                }else{
                    return new RequestPacket(2, new AbstractData(false));
                }

            }else if(requestByClient.itemType == 1) {
                if(requestByClient.count < iron) {
                    AbstractData newData = new AbstractData(true, 1, requestByClient.count, priceIron);
                    iron -= requestByClient.count;
                    return new RequestPacket(2, newData);
                }else{
                    return new RequestPacket(2, new AbstractData(false));
                }

            }else if(requestByClient.itemType == 2) {
                if(requestByClient.count < food) {
                    AbstractData newData = new AbstractData(true, 2, requestByClient.count, priceFood);
                    food -= requestByClient.count;
                    return new RequestPacket(2, newData);
                }else{
                    return new RequestPacket(2, new AbstractData(false));
                }
            }
        }
        return null;
    }
    public RequestPacket HandelSellByClient() {

        return null;
    }
    public RequestPacket HandelUpdatePriceStore() {
        Item itemGold = new Item(0, priceGold, priceGold, gold);
        Item itemIron = new Item(1, priceIron, priceIron, iron);
        Item itemFood = new Item(2, priceFood, priceFood, food);

        UpdateStoreData updateStoreData = new UpdateStoreData(itemGold, itemIron, itemFood);

        return new RequestPacket(5, updateStoreData);
    }
}
