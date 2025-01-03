package ServerNew.Controller;

import ServerNew.Model.MongoModel.*;
import ServerNew.Packet.ManagerType.TypeStatusGround;
import ServerNew.Packet.ResponsePacket;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.*;

public class DayController {
    private List<AuthData> playerInfoMap;
    private MongoCollection<Document> collectionAuth;
    private MongoCollection<Document> collectionPlayerInfo;
    private Timer gameDayTimer;

    public DayController(MongoClient mongoClient, Runnable onCompleteCallback) {
        this.playerInfoMap = new ArrayList<>();
        MongoDatabase database = mongoClient.getDatabase("GameDatabase");
        this.collectionAuth = database.getCollection("authCollection");
        this.collectionPlayerInfo = database.getCollection("playerInfo");

        // Tạo Timer để xử lý qua ngày mỗi 10 giây
        gameDayTimer = new Timer();
        gameDayTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                // Gọi phương thức xử lý các hành động khi qua ngày
                handleGameDay();
                createProductAllPlayer();
                onCompleteCallback.run();
            }
        }, 0, 10000); // Lặp lại mỗi 10 giây (10000 ms)
    }

    private void handleGameDay() {
        System.out.println("Ngày mới đã đến! Xử lý các hành động của game...");

        upTimeAllPlayer();

        // In thông báo về quá trình xử lý
        System.out.println("Đã xử lý ngày mới trong game.");
    }
    void createProductAllPlayer() {
        Bson statusFilter = Filters.eq("status", true);
        List<Document> activePlayers = collectionAuth.find(statusFilter).into(new ArrayList<>());

        for (Document player : activePlayers) {
            String username = player.getString("username");
            // Tìm người chơi trong collectionPlayerInfo bằng username
            Bson playerFilter = Filters.eq("username", username);
            Document playerInfo = collectionPlayerInfo.find(playerFilter).first();

            if (playerInfo != null) {
                // Lấy danh sách các công trình có trạng thái HAVE_BUILD
                Document buildData = (Document) playerInfo.get("buildData");
                Document assetData = (Document) playerInfo.get("assetData");
                BuildData getBuildData = BuildData.FromDocument(buildData);
                AssetData getAssetData = AssetData.FromDocument(assetData);

                for (ComboBuilder comboBuilder : getBuildData.getComboBuilders()) {
                    if(Objects.equals(comboBuilder.getStatusBuild(), TypeStatusGround.HAVE_BUILD)) {
                        for(ComboItem comboItem : getAssetData.getAssets()) {
                            if(Objects.equals(comboBuilder.getTypeBuild(), comboItem.getType())) {
                                int countGet = comboItem.getCount();
                                countGet += comboBuilder.getReward();
                                comboItem.setCount(countGet);
                            }
                        }
                    }
                }
                collectionPlayerInfo.updateOne(
                        Filters.eq("username", username),
                        Updates.set("assetData", AssetData.ToDocument(getAssetData))
                );
            }
        }
    }
    void upTimeAllPlayer() {
        Bson statusFilter = Filters.eq("status", true);
        List<Document> activePlayers = collectionAuth.find(statusFilter).into(new ArrayList<>());

        // Duyệt qua từng người chơi và tăng dayPlayer trong playerInfo
        for (Document player : activePlayers) {
            String username = player.getString("username");
            System.out.println("Đã xử lý upday 1 người chơi");
            // Tìm người chơi trong collectionPlayerInfo bằng username
            Bson playerFilter = Filters.eq("username", username);
            Document playerInfo = collectionPlayerInfo.find(playerFilter).first();

            if (playerInfo != null) {
                // Tăng dayPlayer lên 1
                int currentDayPlayer = playerInfo.getInteger("dayPlayer");
                int newCurrent = currentDayPlayer + 1;
                Document update = new Document("$set", new Document("dayPlayer", newCurrent));
                collectionPlayerInfo.updateOne(playerFilter, update);
            }
        }
    }
}
