package ServerNew.Packet;

public class ResponsePacket {
    private String typeResponse;
    private String callbackResult;
    public ResponsePacket(String typeResponse, String callbackResult) {
        this.typeResponse = typeResponse;
        this.callbackResult = callbackResult;
    }

    public String getTypeResponse() {
        return typeResponse;
    }

    public void setTypeResponse(String typeResponse) {
        this.typeResponse = typeResponse;
    }

    public String getCallbackResult() {
        return callbackResult;
    }

    public void setCallbackResult(String callbackResult) {
        this.callbackResult = callbackResult;
    }
}
