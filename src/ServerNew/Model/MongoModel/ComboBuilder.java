package ServerNew.Model.MongoModel;

import org.bson.Document;

public class ComboBuilder {
    private int sttBuild;
    private String typeBuild;
    private int reward;
    private String statusBuild;

    public ComboBuilder(int sttBuild, String typeBuild, int reward , String statusBuild) {
        this.sttBuild = sttBuild;
        this.typeBuild = typeBuild;
        this.reward = reward;
        this.statusBuild = statusBuild;
    }

    public int getSttBuild() {
        return sttBuild;
    }

    public void setSttBuild(int sttBuild) {
        this.sttBuild = sttBuild;
    }

    public String getTypeBuild() {
        return typeBuild;
    }

    public void setTypeBuild(String typeBuild) {
        this.typeBuild = typeBuild;
    }

    public int getReward() {
        return reward;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }

    public String getStatusBuild() {
        return statusBuild;
    }

    public void setStatusBuild(String statusBuild) {
        this.statusBuild = statusBuild;
    }

    // Convert ComboBuilder to MongoDB Document
    public Document toDocument() {
        return new Document("sttBuild", sttBuild)
                .append("typeBuild", typeBuild)
                .append("reward", reward)
                .append("statusBuild", statusBuild);
    }

    // Convert MongoDB Document to ComboBuilder
    public static ComboBuilder fromDocument(Document document) {
        int sttBuild = document.getInteger("sttBuild");
        String typeBuild = document.getString("typeBuild");
        int reward = document.getInteger("reward");
        String statusBuild = document.getString("statusBuild");
        return new ComboBuilder(sttBuild, typeBuild, reward, statusBuild);
    }
}
