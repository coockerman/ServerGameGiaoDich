package ServerNew.Model;

public class AssetData {
    private float countMoney;
    private float countFood;
    private float countIron;
    private float countGold;
    private float countMelee;
    private float countArrow;
    private float countCavalry;
    private float countCitizen;
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
    public AssetData(float countMoney, float countFood, float countIron, float countGold,
                     float countMelee, float countArrow, float countCavalry, float countCitizen) {
        this.countMoney = countMoney;
        this.countFood = countFood;
        this.countIron = countIron;
        this.countGold = countGold;
        this.countMelee = countMelee;
        this.countArrow = countArrow;
        this.countCavalry = countCavalry;
        this.countCitizen = countCitizen;
    }
}
