package no.vinny.nightfly.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class H2JsonFunctions {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String jsonValue(String json, String path) {
        if (json == null || path == null) return null;

        try {
            JsonNode root = objectMapper.readTree(json);

            // crude JSONPath → JsonPointer mapping for simple '$.field' style
            if (path.startsWith("$."))
                path = path.substring(2); // remove "$."

            String[] parts = path.split("\\.");
            JsonNode node = root;
            for (String p : parts) {
                node = node.path(p);
            }
            return node.isMissingNode() || node.isNull() ? null : node.asText();
        } catch (Exception e) {
            return null;
        }
    }
}
