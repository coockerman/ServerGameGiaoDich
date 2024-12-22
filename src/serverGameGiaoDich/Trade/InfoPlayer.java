package serverGameGiaoDich.Trade;

public class InfoPlayer {
    private String ipPlayer;
    private String namePlayer;
    private String dayPlayer;
    private SoldierData soldierData;

    public InfoPlayer() {}

    public InfoPlayer(String ipPlayer, String namePlayer, String dayPlayer, SoldierData soldierData)
    {
        this.ipPlayer = ipPlayer;
        this.namePlayer = namePlayer;
        this.dayPlayer = dayPlayer;
        this.soldierData = soldierData;
    }

    public String getIpPlayer() {
        return ipPlayer;
    }

    public void setIpPlayer(String ipPlayer) {
        this.ipPlayer = ipPlayer;
    }

    public String getNamePlayer() {
        return namePlayer;
    }

    public void setNamePlayer(String namePlayer) {
        this.namePlayer = namePlayer;
    }

    public String getDayPlayer() {
        return dayPlayer;
    }

    public void setDayPlayer(String dayPlayer) {
        this.dayPlayer = dayPlayer;
    }

    public SoldierData getSoldierData() {
        return soldierData;
    }

    public void setSoldierData(SoldierData soldierData) {
        this.soldierData = soldierData;
    }
}
