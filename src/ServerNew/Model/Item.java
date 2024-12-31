package ServerNew.Model;

public class Item {
    public int itemType;
    public float priceBuy;
    public float priceSell;
    public int countInStock;

    public Item() {

    }
    public Item(int itemType, float priceBuy, float priceSell, int countInStock)
    {
        this.itemType = itemType;
        this.priceBuy = priceBuy;
        this.priceSell = priceSell;
        this.countInStock = countInStock;
    }
    @Override
    public String toString() {
        return "Item{" +
                "itemType=" + itemType +
                ", priceBuy=" + priceBuy +
                ", priceSell=" + priceSell +
                ", countInStock=" + countInStock +
                '}';
    }

}
