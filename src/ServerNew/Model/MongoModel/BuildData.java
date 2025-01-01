package ServerNew.Model.MongoModel;

import ServerNew.Packet.ManagerType.TypeStatusGround;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class BuildData {
    private List<ComboBuilder> comboBuilders;

    public BuildData() {
        this.comboBuilders = new ArrayList<>();
        this.comboBuilders.add(new ComboBuilder(0, null, TypeStatusGround.OPEN));
        this.comboBuilders.add(new ComboBuilder(1, null, TypeStatusGround.OPEN));
        this.comboBuilders.add(new ComboBuilder(2, null, TypeStatusGround.OPEN));
        this.comboBuilders.add(new ComboBuilder(3, null, TypeStatusGround.OPEN));
        this.comboBuilders.add(new ComboBuilder(4, null, TypeStatusGround.NOT_OPEN));
        this.comboBuilders.add(new ComboBuilder(5, null, TypeStatusGround.NOT_OPEN));
        this.comboBuilders.add(new ComboBuilder(6, null, TypeStatusGround.NOT_OPEN));
        this.comboBuilders.add(new ComboBuilder(7, null, TypeStatusGround.NOT_OPEN));
        this.comboBuilders.add(new ComboBuilder(8, null, TypeStatusGround.NOT_OPEN));
        this.comboBuilders.add(new ComboBuilder(9, null, TypeStatusGround.NOT_OPEN));
        this.comboBuilders.add(new ComboBuilder(10, null, TypeStatusGround.NOT_OPEN));
    }

    public List<ComboBuilder> getComboBuilders() {
        return comboBuilders;
    }

    public void setComboBuilders(List<ComboBuilder> comboBuilders) {
        this.comboBuilders = comboBuilders;
    }

    // Convert BuildData to MongoDB Document
    public static Document ToDocument(BuildData buildData) {
        List<Document> comboBuilderDocs = new ArrayList<>();
        for (ComboBuilder comboBuilder : buildData.comboBuilders) {
            comboBuilderDocs.add(comboBuilder.toDocument());
        }

        return new Document("comboBuilders", comboBuilderDocs);
    }

    // Convert MongoDB Document to BuildData
    public static BuildData FromDocument(Document document) {
        BuildData buildData = new BuildData();
        buildData.comboBuilders = new ArrayList<>();
        List<Document> comboBuilderDocs = (List<Document>) document.get("comboBuilders");
        for (Document comboBuilderDoc : comboBuilderDocs) {
            buildData.comboBuilders.add(ComboBuilder.fromDocument(comboBuilderDoc));
        }
        return buildData;
    }
}
