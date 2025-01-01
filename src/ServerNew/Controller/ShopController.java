package ServerNew.Controller;

import ServerNew.Model.MongoModel.AssetData;
import ServerNew.Model.Shop.Resource;
import ServerNew.Model.Shop.Shop;
import ServerNew.Model.Trade;
import ServerNew.Packet.ResponsePacket;
import ServerNew.Packet.ManagerType.TypeObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;

import java.util.List;
import java.util.Objects;


public class ShopController {
    private Shop shop;
    private MongoClient mongoClient;
    private MongoCollection<Document> collectionPlayerInfo;

    public ShopController(MongoClient mongoClient) {
        shop = new Shop();
        this.mongoClient = mongoClient;
        MongoDatabase database = mongoClient.getDatabase("GameDatabase");
        this.collectionPlayerInfo = database.getCollection("playerInfo");
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

    public boolean CheckBuy(Trade trade) {
        Document query = new Document("username", trade.getUsername());
        System.out.println(trade.getUsername());
        Document result = collectionPlayerInfo.find(query).first();
        if(result == null) {
            System.out.println("ko tìm thấy trong cơ sở dữ liệu");
            return false;
        }
        Document resultAsset = (Document)result.get("assetData");
        AssetData assetData = AssetData.FromDocument(resultAsset);
        int countBuy = trade.getCount();
        int money = assetData.getCountMoney();

        // Lấy thông tin mặt hàng
        Resource resource = null;
        String type = trade.getTypeItem();
        if (Objects.equals(type, TypeObject.FOOD)) {
            resource = shop.getFood();
        } else if (Objects.equals(type, TypeObject.IRON)) {
            resource = shop.getIron();
        } else if (Objects.equals(type, TypeObject.GOLD)) {
            resource = shop.getGold();
        }

        // Kiểm tra mặt hàng hợp lệ
        if (resource == null || countBuy > resource.getQuantity()) {
            return false;
        }

        // Tính toán giá và số lượng
        int price = (int) resource.getPriceBuy();
        int needMoney = price * countBuy;
        int countHave = assetData.getAssetCountByType(type);

        if (money >= needMoney) {
            money -= needMoney;
            countHave += countBuy;
            resource.decreaseQuantity(countBuy);
            // Cập nhật dữ liệu trong database
            collectionPlayerInfo.updateMany(
                    Filters.eq("username", trade.getUsername()),
                    Updates.combine(
                            Updates.set("assetData.countMoney", money),
                            Updates.set("assetData.assets.$[elem].count", countHave)
                    ),
                    new com.mongodb.client.model.UpdateOptions().arrayFilters(
                            List.of(Filters.eq("elem.type", type))
                    )
            );
            return true;
        }
        return false;
    }

    public boolean CheckSell(Trade trade) {
        Document query = new Document("username", trade.getUsername());
        System.out.println(trade.getUsername());
        Document result = collectionPlayerInfo.find(query).first();
        if(result == null) {
            System.out.println("ko tìm thấy trong cơ sở dữ liệu");
            return false;
        }
        Document resultAsset = (Document)result.get("assetData");
        AssetData assetData = AssetData.FromDocument(resultAsset);
        int countSell = trade.getCount();

        // Lấy thông tin mặt hàng
        Resource resource = null;
        String type = trade.getTypeItem();
        if (Objects.equals(type, TypeObject.FOOD)) {
            resource = shop.getFood();
        } else if (Objects.equals(type, TypeObject.IRON)) {
            resource = shop.getIron();
        } else if (Objects.equals(type, TypeObject.GOLD)) {
            resource = shop.getGold();
        }

        // Kiểm tra mặt hàng hợp lệ
        int countHave = assetData.getAssetCountByType(type);
        if (resource == null || countSell > countHave) {
            return false;
        }

        // Tính toán giá và số lượng
        int price = (int) resource.getPriceSell();
        int earnMoney = price * countSell;
        int money = assetData.getCountMoney();
        money += earnMoney;
        countHave -= countSell;
        resource.increaseQuantity(countSell);

        // Cập nhật dữ liệu trong database
        collectionPlayerInfo.updateMany(
                Filters.eq("username", trade.getUsername()),
                Updates.combine(
                        Updates.set("assetData.countMoney", money),
                        Updates.set("assetData.assets.$[elem].count", countHave)
                ),
                new com.mongodb.client.model.UpdateOptions().arrayFilters(
                        List.of(Filters.eq("elem.type", type))
                )
        );
        return true;
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }
}
