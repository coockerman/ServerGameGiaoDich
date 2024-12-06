package serverGameGiaoDich;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import serverGameGiaoDich.Trade.RequestPacket;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

public class ServerManager extends WebSocketServer {

    // Danh sách quản lý các client đã kết nối
    private final List<WebSocket> clients = new ArrayList<>();
    private ShopHandel shop = new ShopHandel();

    // Constructor
    public ServerManager(int port) {
        super(new InetSocketAddress(port));
    }

    // Khi một client kết nối
    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        clients.add(conn);
        System.out.println("Connect: " + conn.getRemoteSocketAddress());
        ResponseDataToClient(conn, shop.HandelUpdatePriceStore());
    }

    // Khi server nhận tin nhắn từ client
    @Override
    public void onMessage(WebSocket conn, String message) {
        try {
            RequestPacket packet = RequestPacket.fromJson(message);

            switch (packet.getPacketType()) {
                case 0:
                    ResponseDataToClient(conn, shop.HandelBuyByClient(packet.getAbstractData()));
                    broadcastMessage(shop.HandelUpdatePriceStore());
                    break;

                case 1:
                    ResponseDataToClient(conn, shop.HandelSellByClient(packet.getAbstractData()));
                    broadcastMessage(shop.HandelUpdatePriceStore());
                    break;

                case 4:
                    ResponseDataToClient(conn, shop.HandelUpdatePriceStore());

                    break;

            }

        } catch (Exception e) {
            System.err.println("Error processing message: " + e.getMessage());
        }
    }

    // Khi một client đóng kết nối
    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        clients.remove(conn);
        System.out.println("Connection closed: " + conn.getRemoteSocketAddress());
    }

    // Khi xảy ra lỗi
    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.err.println("Error occurred: " + ex.getMessage());
    }

    @Override
    public void onStart() {
        System.out.println("WebSocket server started!");
    }

    public void ResponseDataToClient(WebSocket conn, RequestPacket packet) {
        conn.send(RequestPacket.toJson(packet));
    }

    // Gửi tin nhắn đến tất cả client
    public void broadcastMessage(RequestPacket packet) {
        for (WebSocket client : clients) {
            client.send(RequestPacket.toJson(packet));
        }
    }

    // Main method để khởi chạy server
    public static void main(String[] args) {
        int port = 5555; // Cổng của server
        ServerManager server = new ServerManager(port);
        server.start();

    }
}
