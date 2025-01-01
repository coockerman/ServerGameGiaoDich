package ServerNew.Model.MongoModel;

import ServerNew.Packet.ManagerType.TypeObject;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class AssetData {
    private int countMoney;
    private List<ComboItem> assets; // Dùng danh sách động cho tài nguyên

    // Constructor mặc định
    public AssetData() {
        this.countMoney = 500;
        this.assets = new ArrayList<>();
        this.assets.add(new ComboItem(TypeObject.FOOD, 100));
        this.assets.add(new ComboItem(TypeObject.IRON, 100));
        this.assets.add(new ComboItem(TypeObject.GOLD, 100));
        this.assets.add(new ComboItem(TypeObject.MELEE, 0));
        this.assets.add(new ComboItem(TypeObject.ARROW, 0));
        this.assets.add(new ComboItem(TypeObject.CAVALRY, 0));
        this.assets.add(new ComboItem(TypeObject.CITIZEN, 0));
    }

    // Constructor tùy chỉnh
    public AssetData(int countMoney, List<ComboItem> assets) {
        this.countMoney = countMoney;
        this.assets = assets;
    }
    // Hàm lấy dữ liệu theo loại
    public int getAssetCountByType(String type) {
        for (ComboItem asset : assets) {
            if (asset.getType().equalsIgnoreCase(type)) {
                return asset.getCount();
            }
        }
        return 0; // Trả về 0 nếu không tìm thấy
    }
    // Getter và Setter
    public int getCountMoney() {
        return countMoney;
    }

    public void setCountMoney(int countMoney) {
        this.countMoney = countMoney;
    }

    public List<ComboItem> getAssets() {
        return assets;
    }

    public void setAssets(List<ComboItem> assets) {
        this.assets = assets;
    }

    // Chuyển đối tượng sang Document
    public static Document ToDocument(AssetData assetData) {
        List<Document> assetDocs = new ArrayList<>();
        for (ComboItem asset : assetData.getAssets()) {
            assetDocs.add(asset.toDocument());
        }
        return new Document("countMoney", assetData.getCountMoney())
                .append("assets", assetDocs);
    }

    // Chuyển Document thành đối tượng
    public static AssetData FromDocument(Document document) {
        int countMoney = document.getInteger("countMoney", 0);
        List<Document> assetDocs = (List<Document>) document.get("assets");
        List<ComboItem> assets = new ArrayList<>();
        for (Document doc : assetDocs) {
            assets.add(ComboItem.fromDocument(doc));
        }
        return new AssetData(countMoney, assets);
    }
}