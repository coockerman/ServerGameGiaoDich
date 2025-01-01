package ServerNew;

import ServerNew.Controller.AuthController;
import ServerNew.Controller.GroundController;
import ServerNew.Controller.PlayerController;
import ServerNew.Controller.ShopController;
import ServerNew.Model.Build.BuildGround;
import ServerNew.Model.MongoModel.AuthData;
import ServerNew.Model.MongoModel.PlayerInfo;
import ServerNew.Model.Trade;
import ServerNew.Packet.RequestPacket;
import ServerNew.Packet.ResponsePacket;
import ServerNew.Packet.ManagerType.TypeRequest;
import ServerNew.Packet.ManagerType.TypeResponse;
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
    private PlayerController playerController;
    private GroundController groundController;
    public ServerManager(int port) {
        super(new InetSocketAddress(port));
        mongoClient = MongoClients.create("mongodb://localhost:27017");
        shopController = new ShopController(mongoClient);
        authController = new AuthController(mongoClient);
        playerController = new PlayerController(mongoClient);
        groundController = new GroundController(mongoClient);
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
                    System.out.println("Đăng xuất");
                    ResponseDataToClient(webSocket, authController.LogoutPlayer(authDataLogout));
                    break;

                case TypeRequest.REGISTER_NAME:
                    PlayerInfo playerInfoRegisterName = getRequest.getPlayerInfo();
                    System.out.println("Đăng kí tên");
                    ResponseDataToClient(webSocket, playerController.RegisterName(playerInfoRegisterName));
                    break;

                case TypeRequest.GET_ALL_DATA_PLAYER:
                    PlayerInfo playerInfoGetAllData = getRequest.getPlayerInfo();
                    System.out.println("Lấy tất cả thông tin người chơi");
                    ResponseDataToClient(webSocket, playerController.GetAllDataPlayer(playerInfoGetAllData));
                    ResponseDataToClient(webSocket, shopController.getDataShop());
                    break;

                case TypeRequest.BUY:
                    Trade tradeBuy = getRequest.getTrade();
                    System.out.println("Mua");
                    if(shopController.CheckBuy(tradeBuy)) {
                        ResponseDataToClient(webSocket, playerController.GetAllDataPlayer(tradeBuy.getUsername()));
                        broadcastMessage(shopController.getDataShop());
                    }else{
                        //Todo Buy false
                    }

                    break;

                case TypeRequest.SELL:
                    Trade tradeSell = getRequest.getTrade();
                    System.out.println("Bán");
                    if(shopController.CheckSell(tradeSell)) {
                        ResponseDataToClient(webSocket, playerController.GetAllDataPlayer(tradeSell.getUsername()));
                        broadcastMessage(shopController.getDataShop());
                    }else{
                        //Todo Buy false
                    }
                    break;

                case TypeRequest.OPEN_BUILD:
                    BuildGround openBuildGround = getRequest.getBuildGround();

                    if(groundController.CheckOpenBuild(openBuildGround)) {
                        ResponseDataToClient(webSocket, playerController.GetAllDataPlayer(openBuildGround.getUsername()));
                        System.out.println("Mở ô đất thành công");
                    }else{
                        System.out.println("Mở ô đất thất bại");
                        //Todo Buy false
                    }
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
                System.out.println("Đã gửi dữ liệu shop cho: " + socket.getRemoteSocketAddress());
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
