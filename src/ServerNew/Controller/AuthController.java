package ServerNew.Controller;

import ServerNew.Model.MongoModel.AssetData;
import ServerNew.Model.MongoModel.AuthData;
import ServerNew.Model.MongoModel.BuildData;
import ServerNew.Model.MongoModel.PlayerInfo;
import ServerNew.Model.PasswordReset;
import ServerNew.Packet.ResponsePacket;
import ServerNew.Packet.ManagerType.TypeResponse;
import ServerNew.Utils.CryptoUtil;
import ServerNew.Utils.EmailUtil;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.java_websocket.WebSocket;

import java.util.ArrayList;
import java.util.List;

import static ServerNew.Utils.CryptoUtil.secretKey;

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
        try {
            if (newAuthData == null || newAuthData.getUserName().isEmpty() || newAuthData.getPassword().isEmpty()) {
                return new ResponsePacket(TypeResponse.RESPONSE_REGISTER_FALSE, "Dữ liệu không hợp lệ!");
            }

            Document query = new Document("username", newAuthData.getUserName());
            if (collectionAuth.find(query).first() != null) {
                return new ResponsePacket(TypeResponse.RESPONSE_REGISTER_FALSE, "Tên người dùng đã tồn tại!");
            }

            // Mã hóa mật khẩu trước khi lưu
            String encryptedPassword = CryptoUtil.encrypt(secretKey, newAuthData.getPassword());

            Document newPlayer = new Document("username", newAuthData.getUserName())
                    .append("password", encryptedPassword)
                    .append("status", true);
            collectionAuth.insertOne(newPlayer);

            PlayerInfo playerInfoInit = new PlayerInfo(newAuthData.getUserName(),
                    newAuthData.getSocket() != null ? newAuthData.getSocket().getRemoteSocketAddress().toString() : "Unknown",
                    newAuthData.getUserName(),
                    0,
                    new AssetData(),
                    new BuildData()
            );
            // Khởi tạo thông tin người chơi mới
            Document newInfoPlayer = PlayerInfo.ToDocument(playerInfoInit);
            collectionPlayerInfo.insertOne(newInfoPlayer);

            playerInfoMap.add(newAuthData);
            return new ResponsePacket(TypeResponse.RESPONSE_REGISTER_TRUE, "Đăng kí thành công");
        } catch (Exception e) {
            System.out.println("Lỗi xử lý mã hóa: " + e.getMessage());
            return new ResponsePacket(TypeResponse.RESPONSE_REGISTER_FALSE, "Đăng ký thất bại do lỗi hệ thống!");
        }
    }

    public ResponsePacket LoginPlayer(AuthData loginAuthData) {
        if (loginAuthData == null || loginAuthData.getUserName().isEmpty() || loginAuthData.getPassword().isEmpty()) {
            System.out.println("Dữ liệu không hợp lệ");
            return new ResponsePacket(TypeResponse.RESPONSE_LOGIN_FALSE, "Dữ liệu không hợp lệ!");
        }

        Document query = new Document("username", loginAuthData.getUserName());
        Document result = collectionAuth.find(query).first();

        if (result == null) {
            System.out.println("Sai tên người dùng hoặc mật khẩu!");
            return new ResponsePacket(TypeResponse.RESPONSE_LOGIN_FALSE, "Sai tên người dùng hoặc mật khẩu!");
        }

        try {
            // Giải mã mật khẩu
            String decryptedPassword = CryptoUtil.decrypt(secretKey, result.getString("password"));

            if (!decryptedPassword.equals(loginAuthData.getPassword())) {
                System.out.println("Sai tên người dùng hoặc mật khẩu!");
                return new ResponsePacket(TypeResponse.RESPONSE_LOGIN_FALSE, "Sai tên người dùng hoặc mật khẩu!");
            }

            boolean isOnline = result.getBoolean("status", false);
            if (isOnline) {
                System.out.println("Người dùng này đã online!");
                return new ResponsePacket(TypeResponse.RESPONSE_LOGIN_FALSE, "Người dùng này đã online!");
            }

            updateIpPlayer(loginAuthData.getUserName(), loginAuthData.getSocket().getRemoteSocketAddress().toString());
            updatePlayerStatus(loginAuthData.getUserName(), true);
            playerInfoMap.add(loginAuthData);
            System.out.println("Đăng nhập thành công!");
            return new ResponsePacket(TypeResponse.RESPONSE_LOGIN_TRUE, "Đăng nhập thành công!");
        } catch (Exception e) {
            System.out.println("Lỗi xử lý mã hóa: " + e.getMessage());
            return new ResponsePacket(TypeResponse.RESPONSE_LOGIN_FALSE, "Lỗi xử lý dữ liệu!");
        }
    }

    public ResponsePacket PasswordReset(PasswordReset passwordResetData) {
        if (passwordResetData == null || passwordResetData.getUsername().isEmpty() || passwordResetData.getPasswordOld().isEmpty()) {
            System.out.println("Dữ liệu không hợp lệ");
            return new ResponsePacket(TypeResponse.RESPONSE_PASSWORD_RESET_FALSE, "Dữ liệu không hợp lệ!");
        }

        Document query = new Document("username", passwordResetData.getUsername());
        Document result = collectionAuth.find(query).first();

        if (result == null) {
            System.out.println("Sai tên người dùng hoặc mật khẩu!");
            return new ResponsePacket(TypeResponse.RESPONSE_PASSWORD_RESET_FALSE, "Lỗi");
        }

        try {
            // Giải mã mật khẩu
            String decryptedPassword = CryptoUtil.decrypt(secretKey, result.getString("password"));

            if (!decryptedPassword.equals(passwordResetData.getPasswordOld())) {
                System.out.println("Sai mật khẩu!");
                return new ResponsePacket(TypeResponse.RESPONSE_PASSWORD_RESET_FALSE, "Sai mật khẩu");
            }

            String encryptedPassword = CryptoUtil.encrypt(secretKey, passwordResetData.getPasswordNew());
            Document update = new Document("$set", new Document("password", encryptedPassword));

            collectionAuth.updateOne(query, update);

            System.out.println("Đổi mật khẩu thành công");
            return new ResponsePacket(TypeResponse.RESPONSE_PASSWORD_RESET_TRUE, "Đổi mật khẩu thành công");
        } catch (Exception e) {
            System.out.println("Lỗi xử lý mã hóa: " + e.getMessage());
            return new ResponsePacket(TypeResponse.RESPONSE_LOGIN_FALSE, "Lỗi xử lý dữ liệu!");
        }
    }

    private void updatePlayerStatus(String username, boolean status) {
        Document query = new Document("username", username);
        Document update = new Document("$set", new Document("status", status));
        collectionAuth.updateOne(query, update);
    }
    private void updateIpPlayer(String username, String ip) {
        Document query = new Document("username", username);
        Document update = new Document("$set", new Document("ipPlayer", ip));
        collectionPlayerInfo.updateOne(query, update);
    }

    public ResponsePacket LogoutPlayer(AuthData logoutAuthData) {
        if (logoutAuthData == null || logoutAuthData.getUserName() == null) {
            System.out.println("Dữ liệu ko hợp lệ");
            return new ResponsePacket(TypeResponse.RESPONSE_LOGOUT_FALSE, "Dữ liệu bị trống!");
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
    public void HandleSendEmailForgetPassword(AuthData authDataForgetPass) {
        if (authDataForgetPass == null || authDataForgetPass.getUserName().isEmpty()) {
            System.out.println("Dữ liệu không hợp lệ");
            return;
        }

        Document query = new Document("username", authDataForgetPass.getUserName());
        Document result = collectionAuth.find(query).first();

        if (result == null) {
            System.out.println("Ko tồn tại tài khoản!");
            return;
        }

        try {
            // Giải mã mật khẩu
            String decryptedPassword = CryptoUtil.decrypt(secretKey, result.getString("password"));
            EmailUtil.sendPasswordEmail(authDataForgetPass.getUserName(), decryptedPassword);
        } catch (Exception e) {
            System.out.println("Lỗi xử lý mã hóa: " + e.getMessage());
        }
    }

    public List<AuthData> getPlayerInfoMap() {
        return playerInfoMap;
    }

    public void setPlayerInfoMap(List<AuthData> playerInfoMap) {
        this.playerInfoMap = playerInfoMap;
    }
}

