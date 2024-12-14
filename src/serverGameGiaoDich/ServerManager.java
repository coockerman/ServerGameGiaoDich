package serverGameGiaoDich;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import serverGameGiaoDich.Trade.RequestPacket;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerManager extends WebSocketServer {

    // Danh sách quản lý các client đã kết nối
    private final List<WebSocket> clients = new ArrayList<>();

    // Quản lý tên các player
    private final Map<WebSocket, String> playerNames = new HashMap<>();

    // Quản lý ngày chơi của player
    private final Map<WebSocket, String> playerDays = new HashMap<>();

    // Shop handler (giữ nguyên để phục vụ các tính năng khác)
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

                case 13: // PacketType 13: Đăng ký tên Player
                    handleRegisterPlayer(conn, packet);
                    break;

                case 15: // PacketType 15: Gửi lại tên player đã đăng ký
                    handleRequestPlayerName(conn);
                    break;

                case 8: // PacketType 8: Lưu ngày chơi của player
                    handleDayPlay(conn, packet);
                    break;

                default:
                    System.err.println("Unknown packet type: " + packet.getPacketType());
            }

        } catch (Exception e) {
            System.err.println("Error processing message: " + e.getMessage());
        }
    }

    // Khi một client đóng kết nối
    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        synchronized (playerNames) {
            if (playerNames.containsKey(conn)) {
                String namePlayer = playerNames.remove(conn);
                System.out.println("Player đã thoát: " + namePlayer);
            }
        }
        synchronized (playerDays) {
            if (playerDays.containsKey(conn)) {
                playerDays.remove(conn);
            }
        }
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

    // Xử lý đăng ký tên player
    private void handleRegisterPlayer(WebSocket conn, RequestPacket packet) {
        String namePlayer = packet.getNamePlayer();

        if (namePlayer == null || namePlayer.isEmpty()) {
            System.err.println("Tên player không hợp lệ!");
            return;
        }

        synchronized (playerNames) {
            if (playerNames.containsValue(namePlayer)) {
                System.out.println("Tên player đã tồn tại: " + namePlayer);
            } else {
                playerNames.put(conn, namePlayer);
                System.out.println("Player mới được đăng ký: " + namePlayer);
            }
        }

        // Gửi phản hồi PacketType 16
        sendPlayerRegistrationResponse(conn, namePlayer);
    }

    // Gửi phản hồi đăng ký player về client
    private void sendPlayerRegistrationResponse(WebSocket conn, String namePlayer) {
        RequestPacket response = new RequestPacket(16, namePlayer, null); // PacketType 16
        ResponseDataToClient(conn, response);
        System.out.println("Phản hồi đăng ký gửi cho player: " + namePlayer);
    }

    // Xử lý gửi lại tên player đã đăng ký
    private void handleRequestPlayerName(WebSocket conn) {
        synchronized (playerNames) {
            if (playerNames.containsKey(conn)) {
                String namePlayer = playerNames.get(conn);
                RequestPacket response = new RequestPacket(16, namePlayer, null); // PacketType 16
                ResponseDataToClient(conn, response);
                System.out.println("Tên player được gửi lại: " + namePlayer);
            } else {
                System.err.println("Player chưa đăng ký tên!");
            }
        }
    }

    // Xử lý lưu ngày chơi của player
    private void handleDayPlay(WebSocket conn, RequestPacket packet) {
        String dayPlay = packet.getDayPlay();

        if (dayPlay == null || dayPlay.isEmpty()) {
            System.err.println("Ngày chơi không hợp lệ!");
            return;
        }

        synchronized (playerDays) {
            playerDays.put(conn, dayPlay);
            System.out.println("Ngày chơi được lưu: " + dayPlay + " cho player: " + playerNames.get(conn));
        }
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
