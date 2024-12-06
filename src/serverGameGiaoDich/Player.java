package serverGameGiaoDich;

import org.java_websocket.WebSocket;

public class Player {
    private WebSocket addressPlayer;
    private int melee;
    private int arrow;
    private int cavalry;

    public Player(WebSocket addressPlayer, int melee, int arrow, int cavalry) {
        this.addressPlayer = addressPlayer;
        this.melee = melee;
        this.arrow = arrow;
        this.cavalry = cavalry;
    }

    public WebSocket getAddressPlayer() {
        return addressPlayer;
    }

    public void setAddressPlayer(WebSocket addressPlayer) {
        this.addressPlayer = addressPlayer;
    }

    public int getMelee() {
        return melee;
    }

    public void setMelee(int melee) {
        this.melee = melee;
    }

    public int getArrow() {
        return arrow;
    }

    public void setArrow(int arrow) {
        this.arrow = arrow;
    }

    public int getCavalry() {
        return cavalry;
    }

    public void setCavalry(int cavalry) {
        this.cavalry = cavalry;
    }
}
