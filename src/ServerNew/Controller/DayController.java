package ServerNew.Controller;

import ServerNew.Model.MongoModel.AuthData;
import ServerNew.Packet.ResponsePacket;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
            }
        }, 0, 10000); // Lặp lại mỗi 10 giây (10000 ms)
    }

    private void handleGameDay() {
        System.out.println("Ngày mới đã đến! Xử lý các hành động của game...");

        upTimeAllPlayer();

        // In thông báo về quá trình xử lý
        System.out.println("Đã xử lý ngày mới trong game.");
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
