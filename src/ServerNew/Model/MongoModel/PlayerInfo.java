package ServerNew.Model.MongoModel;

import org.bson.Document;

public class PlayerInfo {

    private String userName;
    private String ipPlayer;
    private String namePlayer;
    private int dayPlayer;
    private AssetData assetData;
    private BuildData buildData;

    public PlayerInfo() {
    }

    public PlayerInfo(String namePlayer, String userName) {
        this.namePlayer = namePlayer;
        this.userName = userName;
    }

    public PlayerInfo(String userName, String ipPlayer, String namePlayer, int dayPlayer, AssetData assetData, BuildData buildData) {
        this.userName = userName;
        this.ipPlayer = ipPlayer;
        this.namePlayer = namePlayer;
        this.dayPlayer = dayPlayer;
        this.assetData = assetData;
        this.buildData = buildData;
    }

    public String getNamePlayer() {
        return namePlayer;
    }

    public void setNamePlayer(String namePlayer) {
        this.namePlayer = namePlayer;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getIpPlayer() {
        return ipPlayer;
    }

    public void setIpPlayer(String ipPlayer) {
        this.ipPlayer = ipPlayer;
    }

    public int getDayPlayer() {
        return dayPlayer;
    }

    public void setDayPlayer(int dayPlayer) {
        this.dayPlayer = dayPlayer;
    }

    public AssetData getAssetData() {
        return assetData;
    }

    public void setAssetData(AssetData assetData) {
        this.assetData = assetData;
    }

    public BuildData getBuildData() {
        return buildData;
    }

    public void setBuildData(BuildData buildData) {
        this.buildData = buildData;
    }

    public static Document ToDocument(PlayerInfo playerInfo) {
        return new Document("username", playerInfo.getUserName())
                .append("ipPlayer", playerInfo.getIpPlayer())
                .append("namePlayer", playerInfo.getNamePlayer())
                .append("dayPlayer", playerInfo.getDayPlayer())
                .append("assetData", AssetData.ToDocument(playerInfo.getAssetData()))
                .append("buildData", BuildData.ToDocument(playerInfo.getBuildData()))
                .append("listInfoPK", null);
    }
    public static PlayerInfo fromDocument(Document document) {
        String userName = document.getString("username");
        String ipPlayer = document.getString("ipPlayer");
        String namePlayer = document.getString("namePlayer");
        int dayPlayer = document.getInteger("dayPlayer", 0); // Default to 0 if not present
        AssetData assetData = AssetData.fromDocument((Document) document.get("assetData"));
        BuildData buildData = BuildData.FromDocument((Document) document.get("buildData"));

        // Returning a new PlayerInfo instance with the extracted values
        return new PlayerInfo(userName, ipPlayer, namePlayer, dayPlayer, assetData, buildData);
    }
}
