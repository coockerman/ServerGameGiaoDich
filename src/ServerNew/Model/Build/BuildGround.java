package ServerNew.Model.Build;

import ServerNew.Model.MongoModel.ComboItem;

import java.util.List;

public class BuildGround {
    private String username;
    private int position;
    private String typeBuild;
    private List<ComboItem> comboItemList;

    public BuildGround(String username,int position, String typeBuild, List<ComboItem> comboItemList) {
        this.username = username;
        this.position = position;
        this.typeBuild = typeBuild;
        this.comboItemList = comboItemList;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getTypeBuild() {
        return typeBuild;
    }

    public void setTypeBuild(String typeBuild) {
        this.typeBuild = typeBuild;
    }

    public List<ComboItem> getComboItemList() {
        return comboItemList;
    }

    public void setComboItemList(List<ComboItem> comboItemList) {
        this.comboItemList = comboItemList;
    }
}
