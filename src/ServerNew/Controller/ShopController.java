package ServerNew.Controller;

import ServerNew.Model.Resource;
import ServerNew.Model.Shop;
import ServerNew.Packet.RequestPacket;


public class ShopController {
    private Shop shop;

    public ShopController() {
        shop = new Shop();
    }



    private Resource getResourceByType(int itemType) {
        switch (itemType) {
            case 0: return shop.getGold();
            case 1: return shop.getIron();
            case 2: return shop.getFood();
            default: return null;
        }
    }
}

