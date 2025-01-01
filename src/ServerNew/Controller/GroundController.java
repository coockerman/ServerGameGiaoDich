package ServerNew.Controller;

import ServerNew.Model.Build.BuildGround;
import ServerNew.Model.MongoModel.AssetData;
import ServerNew.Model.MongoModel.BuildData;
import ServerNew.Model.MongoModel.ComboBuilder;
import ServerNew.Model.MongoModel.ComboItem;
import ServerNew.Packet.ManagerType.TypeStatusGround;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;

import java.util.List;
import java.util.Objects;

public class GroundController {
    private MongoCollection<Document> collectionPlayerInfo;
    public GroundController(MongoClient mongoClient) {
        MongoDatabase database = mongoClient.getDatabase("GameDatabase");
        this.collectionPlayerInfo = database.getCollection("playerInfo");
    }
    public boolean CheckOpenBuild(BuildGround buildGround) {
        Document query = new Document("username", buildGround.getUsername());
        System.out.println(buildGround.getUsername());
        Document result = collectionPlayerInfo.find(query).first();
        if(result == null) {
            System.out.println("ko tìm thấy trong cơ sở dữ liệu");
            return false;
        }
        Document assetDoc = (Document)result.get("assetData");
        Document buildDoc = (Document)result.get("buildData");
        AssetData assetData = AssetData.FromDocument(assetDoc);
        BuildData buildData = BuildData.FromDocument(buildDoc);



        int indexGround = buildGround.getPosition();
        List<ComboBuilder> getComboBuilds = buildData.getComboBuilders();
        ComboBuilder comboBuilder = getComboBuilds.get(indexGround);
        if(Objects.equals(comboBuilder.getStatusBuild(), TypeStatusGround.NOT_OPEN)) {
            for(ComboItem item : buildGround.getComboItemList()) {
                boolean found = false;
                for (ComboItem itemAsset : assetData.getAssets()) {
                    if (itemAsset.getType().equals(item.getType()) && itemAsset.getCount() >= item.getCount()) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    return false;
                }
            }

            for(ComboItem item : buildGround.getComboItemList()) {
                for(ComboItem itemAsset : assetData.getAssets()) {
                    if (itemAsset.getType().equals(item.getType())) {
                        int newCount = itemAsset.getCount() - item.getCount();
                        String type = item.getType();
                        collectionPlayerInfo.updateOne(
                                Filters.eq("username", buildGround.getUsername()),
                                Updates.set("assetData.assets.$[elem].count", newCount),
                                new com.mongodb.client.model.UpdateOptions().arrayFilters(
                                        List.of(Filters.eq("elem.type", type))
                                )
                        );
                    }
                }
            }

            collectionPlayerInfo.updateOne(
                    Filters.eq("username", buildGround.getUsername()),
                    Updates.set("buildData.comboBuilders.$[elem].statusBuild", TypeStatusGround.OPEN),
                    new com.mongodb.client.model.UpdateOptions().arrayFilters(
                            List.of(Filters.eq("elem.sttBuild", indexGround))
                    )
            );
            return true;
        }else{
            return false;
        }
    }
}
