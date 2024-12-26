package serverGameGiaoDich;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import serverGameGiaoDich.Trade.InfoPlayer;
import serverGameGiaoDich.Trade.RequestPacket;
import serverGameGiaoDich.Trade.SoldierData;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServerManager extends WebSocketServer {

    // <editor-fold desc="Tạm ẩn các thuộc tính">
    private final List<WebSocket> clients = new ArrayList<>();
    private final Map<WebSocket, InfoPlayer> playerInfoMap = new ConcurrentHashMap<>();
    private ShopHandel shop = new ShopHandel();
    // </editor-fold>
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

                case 8: // PacketType 8: Lưu ngày chơi của player
                    handleDayPlay(conn, packet);
                    break;

                case 11:
                    handleFindPlayerAttack(conn);
                    break;

                case 13: // PacketType 13: Đăng ký tên Player
                    handleRegisterPlayer(conn, packet);
                    break;

                case 15: // PacketType 15: Gửi lại tên player đã đăng ký
                    handleRequestPlayerName(conn);
                    break;

                case 17: // PacketType 17: Nhận tin nhắn từ player
                    handleMessagePlayer(conn, packet);
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
        if (playerInfoMap.containsKey(conn)) {
            InfoPlayer info = playerInfoMap.remove(conn);
            System.out.println("Player đã thoát: " + info.getNamePlayer());
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

        synchronized (playerInfoMap) {
            boolean nameExists = playerInfoMap.values().stream().anyMatch(info -> namePlayer.equals(info.getNamePlayer()));
            if (nameExists) {
                System.out.println("Tên player đã tồn tại: " + namePlayer);
            } else {
                playerInfoMap.put(conn, new InfoPlayer(
                        conn.getRemoteSocketAddress().getAddress().getHostAddress(),
                        namePlayer,
                        null,
                        null));
                System.out.println("Player mới được đăng ký: " + namePlayer);
                String dataNameJoin = "Người chơi mới " + namePlayer;
                String dataJoin = " đã gia nhập server";

                // Gửi phản hồi PacketType 18 đến tất cả client
                RequestPacket response = new RequestPacket(18, dataNameJoin, dataJoin);
                broadcastMessage(response);
            }
        }

        // Gửi phản hồi PacketType 19
        sendPlayerRegistrationResponse(conn, namePlayer);
    }

    // Gửi phản hồi đăng ký player về client
    private void sendPlayerRegistrationResponse(WebSocket conn, String namePlayer) {
        RequestPacket response = new RequestPacket(19, namePlayer,  true); // PacketType 19
        ResponseDataToClient(conn, response);
        System.out.println("Phản hồi đăng ký gửi cho player: " + namePlayer);
    }

    // Xử lý gửi lại tên player đã đăng ký
    private void handleRequestPlayerName(WebSocket conn) {
        synchronized (playerInfoMap) {
            InfoPlayer info = playerInfoMap.get(conn);
            if (info != null && info.getNamePlayer() != null) {
                RequestPacket response = new RequestPacket(16, info.getNamePlayer(), null); // PacketType 16
                ResponseDataToClient(conn, response);
                System.out.println("Tên player được gửi lại: " + info.getNamePlayer());
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

        synchronized (playerInfoMap) {
            InfoPlayer info = playerInfoMap.get(conn);
            if (info != null) {
                info.setDayPlayer(dayPlay);
                info.setSoldierData(packet.getSoldierData());
                System.out.println("Ngày chơi được lưu: " + dayPlay +
                        " cho player: " + info.getNamePlayer() +
                        " asset: " + info.getSoldierData());
            } else {
                System.err.println("Player chưa đăng ký, không thể lưu ngày chơi!");
            }
        }
    }
    private void handleFindPlayerAttack(WebSocket conn) {
        // Danh sách những người chơi trên 30 ngày
        List<InfoPlayer> activePlayers = new ArrayList<>();

        synchronized (playerInfoMap) {
            for (Map.Entry<WebSocket, InfoPlayer> entry : playerInfoMap.entrySet()) {
                InfoPlayer info = entry.getValue();

                if (info.getDayPlayer() != null) {
                    try {
                        int days = Integer.parseInt(info.getDayPlayer());
                        if (days >= 10) {
                            InfoPlayer player = new InfoPlayer(info.getIpPlayer(), info.getNamePlayer(), String.valueOf(days), info.getSoldierData());
                            activePlayers.add(player);
                        }
                    } catch (NumberFormatException e) {
                        System.err.println("Lỗi chuyển đổi ngày chơi: " + info.getDayPlayer());
                    }
                }
            }
        }

        // Gửi danh sách những người chơi về cho client
        RequestPacket response = new RequestPacket(9, activePlayers); // Gói tin số 9
        ResponseDataToClient(conn, response);
        System.out.println("Gửi danh sách những người chơi trên 30 ngày về client.");
    }
    // Xử lý tin nhắn từ player (Task 1)
    private void handleMessagePlayer(WebSocket conn, RequestPacket packet) {
        String namePlayer = packet.getNamePlayer();
        String messagePlayer = packet.getMessagePlayer();

        if (namePlayer == null || namePlayer.isEmpty() || messagePlayer == null || messagePlayer.isEmpty()) {
            System.err.println("Thông tin tin nhắn không hợp lệ!");
            return;
        }

        // Gửi phản hồi PacketType 18 đến tất cả client
        RequestPacket response = new RequestPacket(18, namePlayer, messagePlayer);
        broadcastMessage(response);
        System.out.println("Tin nhắn từ player: " + namePlayer + ", Nội dung: " + messagePlayer);
    }
    private void handleMessagePlayerJoinServer(WebSocket conn, RequestPacket packet) {
        String namePlayer = packet.getNamePlayer();
        String messagePlayer = packet.getMessagePlayer();

        if (namePlayer == null || namePlayer.isEmpty() || messagePlayer == null || messagePlayer.isEmpty()) {
            System.err.println("Thông tin tin nhắn không hợp lệ!");
            return;
        }

        // Gửi phản hồi PacketType 18 đến tất cả client
        RequestPacket response = new RequestPacket(18, namePlayer, messagePlayer);
        broadcastMessage(response);
        System.out.println("Tin nhắn từ player: " + namePlayer + ", Nội dung: " + messagePlayer);
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

