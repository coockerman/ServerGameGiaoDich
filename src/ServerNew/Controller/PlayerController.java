package ServerNew.Controller;

import ServerNew.Model.PlayerInfo;
import ServerNew.Packet.ResponsePacket;
import ServerNew.Packet.Trade.TypeResponse;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;

public class PlayerController {
    private MongoCollection<Document> collectionPlayerInfo;
    public PlayerController(MongoClient mongoClient) {
        MongoDatabase database = mongoClient.getDatabase("GameDatabase");
        this.collectionPlayerInfo = database.getCollection("playerInfo");
    }
    public ResponsePacket RegisterName(PlayerInfo playerInfo) {
        try {
            // Tìm thông tin người chơi trong cơ sở dữ liệu
            Document existingPlayer = collectionPlayerInfo.find(Filters.eq("username", playerInfo.getUserName())).first();
            if (existingPlayer == null) {
                return new ResponsePacket(TypeResponse.RESPONSE_REGISTER_NAME_FALSE, "Người chơi không tồn tại");
            }

            // Kiểm tra xem tên người chơi mới đã tồn tại chưa
            Document nameConflict = collectionPlayerInfo.find(Filters.eq("namePlayer", playerInfo.getNamePlayer())).first();
            if (nameConflict != null && !nameConflict.getString("username").equals(playerInfo.getUserName())) {
                return new ResponsePacket(TypeResponse.RESPONSE_REGISTER_NAME_FALSE, "Tên người chơi đã tồn tại");
            }

            // Cập nhật thông tin người chơi
            collectionPlayerInfo.updateOne(
                    Filters.eq("username", playerInfo.getUserName()),
                    Updates.set("namePlayer", playerInfo.getNamePlayer())
            );

            return new ResponsePacket(TypeResponse.RESPONSE_REGISTER_NAME_TRUE, "Cập nhật tên thành công");
        } catch (Exception e) {
            return new ResponsePacket(TypeResponse.RESPONSE_REGISTER_NAME_FALSE, "Lỗi: " + e.getMessage());
        }
    }
}
