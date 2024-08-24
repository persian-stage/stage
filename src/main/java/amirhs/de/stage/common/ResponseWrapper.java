package amirhs.de.stage.common;

import java.util.HashMap;
import java.util.Map;

public class ResponseWrapper {
    private Map<String, String> data;

    public ResponseWrapper() {
        this.data = new HashMap<>();
    }

    public ResponseWrapper add(String key, String value) {
        this.data.put(key, value);
        return this;
    }

    public Map<String, String> getData() {
        return data;
    }
}