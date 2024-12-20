package serverGameGiaoDich;

import serverGameGiaoDich.Trade.AbstractData;
import serverGameGiaoDich.Trade.Item;
import serverGameGiaoDich.Trade.RequestPacket;
import serverGameGiaoDich.Trade.UpdateStoreData;

public class ShopHandel {
    private Resource gold;
    private Resource iron;
    private Resource food;

    public ShopHandel() {
        // Khởi tạo tài nguyên với số lượng, giá mua, giá bán, số lượng mặc định và tốc độ điều chỉnh giá
        this.gold = new Resource(200, 150, 120, 200, 0.3f);
        this.iron = new Resource(500, 30, 24, 500, 0.1f);
        this.food = new Resource(5000, 5, 4, 5000, 0.03f);
    }

    public RequestPacket HandelBuyByClient(AbstractData requestByClient) {
        if (requestByClient != null) {
            Resource resource = getResourceByType(requestByClient.itemType);
            if (resource != null && requestByClient.count <= resource.getQuantity()) {
                resource.decreaseQuantity(requestByClient.count); // Giảm số lượng và tự động cập nhật giá
                AbstractData newData = new AbstractData(true, requestByClient.itemType, requestByClient.count, resource.getPriceBuy());
                return new RequestPacket(2, newData);
            }
            return new RequestPacket(2, new AbstractData(false)); // Không đủ số lượng
        }
        return null;
    }

    public RequestPacket HandelSellByClient(AbstractData requestByClient) {
        if (requestByClient != null) {
            Resource resource = getResourceByType(requestByClient.itemType);
            if (resource != null) {
                resource.increaseQuantity(requestByClient.count); // Tăng số lượng và tự động cập nhật giá
                AbstractData newData = new AbstractData(true, requestByClient.itemType, requestByClient.count, resource.getPriceSell());
                return new RequestPacket(3, newData);
            }
        }
        return null;
    }

    public RequestPacket HandelUpdatePriceStore() {
        // Tạo các đối tượng Item để trả về thông tin cập nhật giá
        Item itemGold = new Item(0, gold.getPriceBuy(), gold.getPriceSell(), gold.getQuantity());
        Item itemIron = new Item(1, iron.getPriceBuy(), iron.getPriceSell(), iron.getQuantity());
        Item itemFood = new Item(2, food.getPriceBuy(), food.getPriceSell(), food.getQuantity());

        UpdateStoreData updateStoreData = new UpdateStoreData(itemGold, itemIron, itemFood);
        return new RequestPacket(5, updateStoreData);
    }

    private Resource getResourceByType(int itemType) {
        // Lấy tài nguyên tương ứng dựa trên loại
        switch (itemType) {
            case 0:
                return gold;
            case 1:
                return iron;
            case 2:
                return food;
            default:
                return null;
        }
    }
}
