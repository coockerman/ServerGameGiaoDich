package ServerNew.Model;

public class ShopData {
    private int quantityFood;
    private int priceBuyFood;
    private int priceSellFood;
    private int quantityIron;
    private int priceBuyIron;
    private int priceSellIron;
    private int quantityGold;
    private int priceBuyGold;
    private int priceSellGold;
    public ShopData(Shop shop) {
        quantityFood = shop.getFood().getQuantity();
        priceBuyFood = (int)shop.getFood().getPriceBuy();
        priceSellFood = (int)shop.getFood().getPriceSell();

        quantityFood = shop.getFood().getQuantity();
        priceBuyFood = (int)shop.getFood().getPriceBuy();
        priceSellFood = (int)shop.getFood().getPriceSell();

        quantityFood = shop.getFood().getQuantity();
        priceBuyFood = (int)shop.getFood().getPriceBuy();
        priceSellFood = (int)shop.getFood().getPriceSell();
    }
}
