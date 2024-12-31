package ServerNew.Model;

import org.bson.Document;

public class BuildData {
    private int countBuildFood;
    private int countBuildIron;
    private int countBuildGold;
    private int countBuildMelee;
    private int countBuildArrow;
    private int countBuildCavalry;
    private int countBuildCitizen;

    public BuildData() {
        this.countBuildFood = 0;
        this.countBuildIron = 0;
        this.countBuildGold = 0;
        this.countBuildMelee = 0;
        this.countBuildArrow = 0;
        this.countBuildCavalry = 0;
        this.countBuildCitizen = 0;
    }

    public BuildData(int countBuildFood, int countBuildIron, int countBuildGold,
                     int countBuildMelee, int countBuildArrow, int countBuildCavalry, int countBuildCitizen) {
        this.countBuildFood = countBuildFood;
        this.countBuildIron = countBuildIron;
        this.countBuildGold = countBuildGold;
        this.countBuildMelee = countBuildMelee;
        this.countBuildArrow = countBuildArrow;
        this.countBuildCavalry = countBuildCavalry;
        this.countBuildCitizen = countBuildCitizen;
    }

    public int getCountBuildFood() {
        return countBuildFood;
    }

    public void setCountBuildFood(int countBuildFood) {
        this.countBuildFood = countBuildFood;
    }

    public int getCountBuildIron() {
        return countBuildIron;
    }

    public void setCountBuildIron(int countBuildIron) {
        this.countBuildIron = countBuildIron;
    }

    public int getCountBuildGold() {
        return countBuildGold;
    }

    public void setCountBuildGold(int countBuildGold) {
        this.countBuildGold = countBuildGold;
    }

    public int getCountBuildMelee() {
        return countBuildMelee;
    }

    public void setCountBuildMelee(int countBuildMelee) {
        this.countBuildMelee = countBuildMelee;
    }

    public int getCountBuildArrow() {
        return countBuildArrow;
    }

    public void setCountBuildArrow(int countBuildArrow) {
        this.countBuildArrow = countBuildArrow;
    }

    public int getCountBuildCavalry() {
        return countBuildCavalry;
    }

    public void setCountBuildCavalry(int countBuildCavalry) {
        this.countBuildCavalry = countBuildCavalry;
    }

    public int getCountBuildCitizen() {
        return countBuildCitizen;
    }

    public void setCountBuildCitizen(int countBuildCitizen) {
        this.countBuildCitizen = countBuildCitizen;
    }

    public static Document ToDocument(BuildData buildData) {
        return new Document("countBuildFood", buildData.getCountBuildFood())
                .append("countBuildIron", buildData.getCountBuildIron())
                .append("countBuildGold", buildData.getCountBuildGold())
                .append("countBuildMelee", buildData.getCountBuildMelee())
                .append("countBuildArrow", buildData.getCountBuildArrow())
                .append("countBuildCavalry", buildData.getCountBuildCavalry())
                .append("countBuildCitizen", buildData.getCountBuildCitizen());
    }

    public static BuildData FromDocument(Document document) {
        return new BuildData(
                document.getInteger("countBuildFood", 0),
                document.getInteger("countBuildIron", 0),
                document.getInteger("countBuildGold", 0),
                document.getInteger("countBuildMelee", 0),
                document.getInteger("countBuildArrow", 0),
                document.getInteger("countBuildCavalry", 0),
                document.getInteger("countBuildCitizen", 0)
        );
    }
}
