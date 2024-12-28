package ServerNew;

import ServerNew.Controller.ShopController;
import ServerNew.Model.AuthData;
import ServerNew.Model.PlayerInfo;
import ServerNew.Packet.RequestPacket;
import ServerNew.Packet.ResponsePacket;
import ServerNew.Packet.Trade.TypeRequest;
import ServerNew.Utils.JsonUtils;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class ServerManager extends WebSocketServer {

    MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
    ShopController shopController = new ShopController();
    List<AuthData> playerInfoMap = new ArrayList<>();
    public ServerManager(int port) {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {

    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {

    }

    @Override
    public void onMessage(WebSocket webSocket, String s) {
        RequestPacket getRequest = JsonUtils.fromJson(s, RequestPacket.class);

        if(getRequest!=null) {
            switch (getRequest.getTypeRequest()) {
                case TypeRequest.BUY:
                    // Xử lý yêu cầu mua
                    //shopController.handleRequest(TypeRequest.BUY, requestData, webSocket);
                    break;
                case TypeRequest.SELL:
                    // Xử lý yêu cầu bán
                    //shopController.handleRequest(TypeRequest.SELL, requestData, webSocket);
                    break;
                default:
                    // Nếu loại yêu cầu không hợp lệ
                    //webSocket.send("Invalid request type.");
                    break;
            }
        }
        else
        {

        }



    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {

    }

    @Override
    public void onStart() {

    }

    public void ResponseDataToClient(WebSocket conn, ResponsePacket packet) {
        conn.send(JsonUtils.toJson(packet));
    }

    // Gửi tin nhắn đến tất cả client
    public void broadcastMessage(ResponsePacket packet) {
        for (AuthData conn : playerInfoMap) {
            if (conn.getSocket().isOpen()) {
                conn.getSocket().send(JsonUtils.toJson(packet));
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
