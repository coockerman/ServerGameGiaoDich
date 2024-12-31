package ServerNew.Controller;

import ServerNew.Model.AssetData;
import ServerNew.Model.AuthData;
import ServerNew.Model.BuildData;
import ServerNew.Model.PlayerInfo;
import ServerNew.Packet.ResponsePacket;
import ServerNew.Packet.Trade.TypeResponse;
import ServerNew.Utils.JsonUtils;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.java_websocket.WebSocket;
import serverGameGiaoDich.Trade.InfoPlayer;

import java.util.ArrayList;
import java.util.List;

public class AuthController {
    private List<AuthData> playerInfoMap;
    private MongoCollection<Document> collectionAuth;
    private MongoCollection<Document> collectionPlayerInfo;

    public AuthController(MongoClient mongoClient) {
        this.playerInfoMap = new ArrayList<>();
        MongoDatabase database = mongoClient.getDatabase("GameDatabase");
        this.collectionAuth = database.getCollection("authCollection");
        this.collectionPlayerInfo = database.getCollection("playerInfo");
    }

    public ResponsePacket RegisterNewPlayer(AuthData newAuthData) {
        if (newAuthData == null || newAuthData.getUserName().isEmpty() || newAuthData.getPassword().isEmpty()) {
            return new ResponsePacket(TypeResponse.RESPONSE_REGISTER_FALSE, "Dữ liệu không hợp lệ!");
        }

        Document query = new Document("username", newAuthData.getUserName());
        if (collectionAuth.find(query).first() != null) {
            return new ResponsePacket(TypeResponse.RESPONSE_REGISTER_FALSE, "Tên người dùng đã tồn tại!");
        }

        Document newPlayer = new Document("username", newAuthData.getUserName())
                .append("password", newAuthData.getPassword())
                .append("status", true);
        collectionAuth.insertOne(newPlayer);

        PlayerInfo playerInfoInit = new PlayerInfo(newAuthData.getUserName(),
                newAuthData.getSocket().getRemoteSocketAddress().toString(),
                "default",
                0,
                new AssetData(),
                new BuildData()
        );
        //Init asset new player
        Document newInfoPlayer = PlayerInfo.ToDocument(playerInfoInit);
        collectionPlayerInfo.insertOne(newInfoPlayer);

        playerInfoMap.add(newAuthData);
        return new ResponsePacket(TypeResponse.RESPONSE_REGISTER_TRUE, "Đăng kí thành công");
    }

    public ResponsePacket LoginPlayer(AuthData loginAuthData) {
        if (loginAuthData == null || loginAuthData.getUserName().isEmpty() || loginAuthData.getPassword().isEmpty()) {
            System.out.println("Dữ liệu không hợp lệ");
            return new ResponsePacket(TypeResponse.RESPONSE_LOGIN_FALSE, "Dữ liệu không hợp lệ!");
        }

        Document query = new Document("username", loginAuthData.getUserName())
                .append("password", loginAuthData.getPassword());
        Document result = collectionAuth.find(query).first();

        if (result == null) {
            System.out.println("Sai tên người dùng hoặc mật khẩu!");
            return new ResponsePacket(TypeResponse.RESPONSE_LOGIN_FALSE, "Sai tên người dùng hoặc mật khẩu!");
        }

        boolean isOnline = result.getBoolean("status", false);
        if (isOnline) {
            System.out.println("Người dùng này đã online!");
            return new ResponsePacket(TypeResponse.RESPONSE_LOGIN_FALSE, "Người dùng này đã online!");
        }

        updatePlayerStatus(loginAuthData.getUserName(), true);
        playerInfoMap.add(loginAuthData);
        System.out.println("Đăng nhập thành công!");
        return new ResponsePacket(TypeResponse.RESPONSE_LOGIN_TRUE, "Đăng nhập thành công!");
    }

    private void updatePlayerStatus(String username, boolean status) {
        Document query = new Document("username", username);
        Document update = new Document("$set", new Document("status", status));
        collectionAuth.updateOne(query, update);
    }

    public ResponsePacket LogoutPlayer(AuthData logoutAuthData) {
        if (logoutAuthData == null || logoutAuthData.getUserName() == null) {
            System.out.println("Dữ liệu ko hợp lệ");
            return new ResponsePacket(TypeResponse.RESPONSE_LOGOUT_FALSE, "Dữ liệu không hợp lệ!");
        }

        Document query = new Document("username", logoutAuthData.getUserName());
        Document result = collectionAuth.find(query).first();

        if (result == null || !result.getBoolean("status", false)) {
            System.out.println("Người dùng không tồn tại hoặc chưa đăng nhập!");
            return new ResponsePacket(TypeResponse.RESPONSE_LOGOUT_FALSE, "Người dùng không tồn tại hoặc chưa đăng nhập!");
        }

        updatePlayerStatus(logoutAuthData.getUserName(), false);
        playerInfoMap.removeIf(data -> data.getUserName().equals(logoutAuthData.getUserName()));
        System.out.println(logoutAuthData.getUserName() + "Đăng xuất thành công");
        return new ResponsePacket(TypeResponse.RESPONSE_LOGOUT_TRUE, "Đăng xuất thành công!");
    }

    public void ForceLogout(WebSocket socket) {
        if (socket == null || socket.getRemoteSocketAddress() == null) {
            System.out.println("Dữ liệu ko hợp lệ");
            return;
        }

        String clientIp = socket.getRemoteSocketAddress().getAddress().getHostAddress();
        AuthData logoutAuthData = null;

        // Tìm kiếm người dùng trong danh sách playerInfoMap
        for (AuthData data : playerInfoMap) {
            if (data.getSocket() != null && data.getSocket().equals(socket)) {
                logoutAuthData = data;
                break;
            }
        }

        // Kiểm tra người dùng không tồn tại hoặc chưa đăng nhập
        if (logoutAuthData == null) {
            System.out.println("Người dùng không tồn tại hoặc chưa đăng nhập!");
            return;
        }

        // Cập nhật trạng thái offline trong cơ sở dữ liệu
        updatePlayerStatus(logoutAuthData.getUserName(), false);

        // Xóa người dùng khỏi danh sách playerInfoMap
        playerInfoMap.removeIf(data -> data.getSocket().equals(socket));

        // Ghi log thông tin người dùng đã bị đăng xuất
        System.out.println("Buộc đăng xuất người dùng: " + logoutAuthData.getUserName());
    }


    public List<AuthData> getPlayerInfoMap() {
        return playerInfoMap;
    }

    public void setPlayerInfoMap(List<AuthData> playerInfoMap) {
        this.playerInfoMap = playerInfoMap;
    }
}

