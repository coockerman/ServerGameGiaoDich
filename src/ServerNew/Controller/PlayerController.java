package ServerNew.Controller;

import ServerNew.Model.PlayerInfo;
import ServerNew.Packet.ResponsePacket;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class PlayerController {
    private MongoCollection<Document> collectionPlayerInfo;
    public PlayerController(MongoClient mongoClient) {
        MongoDatabase database = mongoClient.getDatabase("GameDatabase");
        this.collectionPlayerInfo = database.getCollection("playerInfo");
    }
    public ResponsePacket RegisterName(PlayerInfo playerInfo) {

        return null;
    }
}
