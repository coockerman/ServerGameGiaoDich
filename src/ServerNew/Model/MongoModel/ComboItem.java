package ServerNew.Model.MongoModel;

import org.bson.Document;

public class ComboItem {
    private String type;
    private int count;

    public ComboItem(String type, int count) {
        this.type = type;
        this.count = count;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Document toDocument() {
        return new Document("type", type)
                .append("count", count);
    }

    public static ComboItem fromDocument(Document doc) {
        return new ComboItem(doc.getString("type"), doc.getInteger("count", 0));
    }
}

