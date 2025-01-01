package ServerNew.Model.Shop;

import ServerNew.Model.UpdateStore.Item;
import ServerNew.Model.UpdateStore.UpdateStoreData;
import ServerNew.Packet.ResponsePacket;
import ServerNew.Packet.ManagerType.TypeResponse;

public class Shop {
    private Resource gold;
    private Resource iron;
    private Resource food;

    public Shop() {
        this.gold = new Resource(500, 150, 120, 500, 0.1f);
        this.iron = new Resource(1000, 30, 24, 1000, 0.05f);
        this.food = new Resource(2000, 5, 4, 2000, 0.005f);
    }

    public ResponsePacket HandelUpdatePriceStore() {
        // Tạo các đối tượng Item để trả về thông tin cập nhật giá
        Item itemGold = new Item(0, gold.getPriceBuy(), gold.getPriceSell(), gold.getQuantity());
        Item itemIron = new Item(1, iron.getPriceBuy(), iron.getPriceSell(), iron.getQuantity());
        Item itemFood = new Item(2, food.getPriceBuy(), food.getPriceSell(), food.getQuantity());

        UpdateStoreData updateStoreData = new UpdateStoreData(itemGold, itemIron, itemFood);
        return new ResponsePacket(TypeResponse.RESPONSE_GET_DATA_SHOP,"Lấy dữ liệu thành công", updateStoreData);
    }
    public Resource getGold() {
        return gold;
    }

    public Resource getIron() {
        return iron;
    }

    public Resource getFood() {
        return food;
    }
}

