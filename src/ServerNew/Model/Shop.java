package ServerNew.Model;

public class Shop {
    private Resource gold;
    private Resource iron;
    private Resource food;

    public Shop() {
        this.gold = new Resource(500, 150, 120, 500, 0.1f);
        this.iron = new Resource(1000, 30, 24, 1000, 0.05f);
        this.food = new Resource(2000, 5, 4, 2000, 0.005f);
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

