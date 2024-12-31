package ServerNew.Model;

public class BuildData {
    private float countBuildFood;
    private float countBuildIron;
    private float countBuildGold;
    private float countBuildMelee;
    private float countBuildArrow;
    private float countBuildCavalry;
    private float countBuildCitizen;

    public BuildData() {
        this.countBuildFood = 0;
        this.countBuildIron = 0;
        this.countBuildGold = 0;
        this.countBuildMelee = 0;
        this.countBuildArrow = 0;
        this.countBuildCavalry = 0;
        this.countBuildCitizen = 0;
    }

    public BuildData(float countBuildFood, float countBuildIron, float countBuildGold,
                     float countBuildMelee, float countBuildArrow, float countBuildCavalry, float countBuildCitizen) {
        this.countBuildFood = countBuildFood;
        this.countBuildIron = countBuildIron;
        this.countBuildGold = countBuildGold;
        this.countBuildMelee = countBuildMelee;
        this.countBuildArrow = countBuildArrow;
        this.countBuildCavalry = countBuildCavalry;
        this.countBuildCitizen = countBuildCitizen;
    }
}
