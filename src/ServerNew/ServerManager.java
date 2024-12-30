package ServerNew;

import ServerNew.Controller.AuthController;
import ServerNew.Controller.ShopController;
import ServerNew.Model.AuthData;
import ServerNew.Model.PlayerInfo;
import ServerNew.Packet.RequestPacket;
import ServerNew.Packet.ResponsePacket;
import ServerNew.Packet.Trade.TypeRequest;
import ServerNew.Packet.Trade.TypeResponse;
import ServerNew.Utils.JsonUtils;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

public class ServerManager extends WebSocketServer {

    private MongoClient mongoClient;
    private ShopController shopController;
    private AuthController authController;

    public ServerManager(int port) {
        super(new InetSocketAddress(port));
        mongoClient = MongoClients.create("mongodb://localhost:27017");
        shopController = new ShopController(mongoClient);
        authController = new AuthController(mongoClient);
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        System.out.println("Connect: " + webSocket.getRemoteSocketAddress());
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        authController.ForceLogout(webSocket);
        System.out.println("Đã out: " + webSocket.getRemoteSocketAddress());
    }

    @Override
    public void onMessage(WebSocket webSocket, String s) {
        RequestPacket getRequest = null;
        try {
            getRequest = JsonUtils.fromJson(s, RequestPacket.class);
        } catch (Exception e) {
            System.out.println("Lỗi convert: " + e);
            ResponsePacket errorResponse = new ResponsePacket(TypeResponse.RESPONSE_ERROR, "Dữ liệu không hợp lệ!");
            ResponseDataToClient(webSocket, errorResponse);
            return; // Dừng xử lý tiếp theo
        }

        if(getRequest!=null) {
            switch (getRequest.getTypeRequest()) {
                case TypeRequest.BUY:
                    //Todo buy
                    break;

                case TypeRequest.SELL:
                    //Todo sell
                    break;

                case TypeRequest.REGISTER_NEW_PLAYER:
                    AuthData authDataRegister = getRequest.getAuthData();
                    authDataRegister.setSocket(webSocket);
                    System.out.println("Đăng kí người chơi mới");
                    ResponseDataToClient(webSocket, authController.RegisterNewPlayer(authDataRegister));
                    break;

                case TypeRequest.LOGIN_PLAYER:
                    AuthData authDataLogin = getRequest.getAuthData();
                    authDataLogin.setSocket(webSocket);
                    System.out.println("Đăng nhập");
                    ResponseDataToClient(webSocket, authController.LoginPlayer(authDataLogin));
                    break;

                case TypeRequest.LOGOUT_PLAYER:
                    AuthData authDataLogout = getRequest.getAuthData();
                    ResponseDataToClient(webSocket, authController.LogoutPlayer(authDataLogout));
                    break;

                case TypeRequest.REGISTER_NAME:
                    PlayerInfo playerInfo = getRequest.getPlayerInfo();
                    ResponseDataToClient(webSocket, authController.LogoutPlayer(authDataLogout));
                    break;

                default:
                    System.err.println("Chưa có hàm xử lý yêu cầu này!!!");
                    break;
            }
        }
        else
        {
            System.err.println("Lỗi request: " + s);
            ResponsePacket errorResponse = new ResponsePacket(TypeResponse.RESPONSE_ERROR, "Dữ liệu không hợp lệ!");
            ResponseDataToClient(webSocket, errorResponse);
        }
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {

    }

    @Override
    public void onStart() {
        System.out.println("WebSocket server started!");
    }

    public void ResponseDataToClient(WebSocket conn, ResponsePacket packet) {
        if(conn.isOpen()) {
            conn.send(JsonUtils.toJson(packet));
        }
    }

    // Gửi tin nhắn đến tất cả client
    public void broadcastMessage(ResponsePacket packet) {
        for (AuthData conn : authController.getPlayerInfoMap()) {
            WebSocket socket = conn.getSocket();
            if (socket != null && socket.isOpen()) {
                socket.send(JsonUtils.toJson(packet));
            }
        }
    }

    // Main method để khởi chạy server
    public static void main(String[] args) {
        int port = 5555; // Cổng của server
        ServerManager server = new ServerManager(port);
        server.start();
    }
}
