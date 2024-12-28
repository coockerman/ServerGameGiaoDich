package ServerNew.Utils;

import com.google.gson.Gson;

public class JsonUtils {
    private static Gson gson = new Gson();

    // Phương thức chuyển đổi từ JSON thành đối tượng
    public static <T> T fromJson(String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }

    // Phương thức chuyển đổi từ đối tượng thành JSON
    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }
}
