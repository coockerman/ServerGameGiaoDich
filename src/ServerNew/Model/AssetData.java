package ServerNew.Model;

import org.bson.Document;

public class AssetData {
    private int countMoney;
    private int countFood;
    private int countIron;
    private int countGold;
    private int countMelee;
    private int countArrow;
    private int countCavalry;
    private int countCitizen;

    // Constructor mặc định
    public AssetData() {
        this.countMoney = 500;
        this.countFood = 100;
        this.countIron = 100;
        this.countGold = 100;
        this.countMelee = 0;
        this.countArrow = 0;
        this.countCavalry = 0;
        this.countCitizen = 0;
    }

    // Constructor tùy chỉnh
    public AssetData(int countMoney, int countFood, int countIron, int countGold,
                     int countMelee, int countArrow, int countCavalry, int countCitizen) {
        this.countMoney = countMoney;
        this.countFood = countFood;
        this.countIron = countIron;
        this.countGold = countGold;
        this.countMelee = countMelee;
        this.countArrow = countArrow;
        this.countCavalry = countCavalry;
        this.countCitizen = countCitizen;
    }

    // Getter và Setter
    public int getCountMoney() {
        return countMoney;
    }

    public void setCountMoney(int countMoney) {
        this.countMoney = countMoney;
    }

    public int getCountFood() {
        return countFood;
    }

    public void setCountFood(int countFood) {
        this.countFood = countFood;
    }

    public int getCountIron() {
        return countIron;
    }

    public void setCountIron(int countIron) {
        this.countIron = countIron;
    }

    public int getCountGold() {
        return countGold;
    }

    public void setCountGold(int countGold) {
        this.countGold = countGold;
    }

    public int getCountMelee() {
        return countMelee;
    }

    public void setCountMelee(int countMelee) {
        this.countMelee = countMelee;
    }

    public int getCountArrow() {
        return countArrow;
    }

    public void setCountArrow(int countArrow) {
        this.countArrow = countArrow;
    }

    public int getCountCavalry() {
        return countCavalry;
    }

    public void setCountCavalry(int countCavalry) {
        this.countCavalry = countCavalry;
    }

    public int getCountCitizen() {
        return countCitizen;
    }

    public void setCountCitizen(int countCitizen) {
        this.countCitizen = countCitizen;
    }

    // Chuyển đối tượng sang Document
    public static Document ToDocument(AssetData assetData) {
        return new Document("countMoney", assetData.getCountMoney())
                .append("countFood", assetData.getCountFood())
                .append("countIron", assetData.getCountIron())
                .append("countGold", assetData.getCountGold())
                .append("countMelee", assetData.getCountMelee())
                .append("countArrow", assetData.getCountArrow())
                .append("countCavalry", assetData.getCountCavalry())
                .append("countCitizen", assetData.getCountCitizen());
    }

    // Chuyển Document thành đối tượng
    public static AssetData fromDocument(Document document) {
        return new AssetData(
                document.getInteger("countMoney", 0),
                document.getInteger("countFood", 0),
                document.getInteger("countIron", 0),
                document.getInteger("countGold", 0),
                document.getInteger("countMelee", 0),
                document.getInteger("countArrow", 0),
                document.getInteger("countCavalry", 0),
                document.getInteger("countCitizen", 0)
        );
    }
}
