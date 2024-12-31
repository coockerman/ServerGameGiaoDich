package ServerNew.Controller;

import ServerNew.Model.Resource;
import ServerNew.Model.Shop;
import ServerNew.Packet.RequestPacket;
import ServerNew.Packet.ResponsePacket;
import com.mongodb.client.MongoClient;


public class ShopController {
    private Shop shop;
    private MongoClient mongoClient;

    public ShopController(MongoClient mongoClient) {
        shop = new Shop();
        this.mongoClient = mongoClient;
    }

    private Resource getResourceByType(int itemType) {
        switch (itemType) {
            case 0: return shop.getGold();
            case 1: return shop.getIron();
            case 2: return shop.getFood();
            default: return null;
        }
    }
    public ResponsePacket getDataShop() {
        return shop.HandelUpdatePriceStore();
    }
    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }
}

