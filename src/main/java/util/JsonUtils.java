package util;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonUtils {
    private static final String EMPTY_JSON = "{}";
    private static final String EMPTY_JSONS = "[]";
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final ObjectMapper MAPPER_HAS_NULL;

    public JsonUtils() {
    }

    public static String toJsonNotNullKey(Object obj) {
        return toJson(obj, MAPPER);
    }

    public static String toJsonHasNullKey(Object obj) {
        return toJson(obj, MAPPER_HAS_NULL);
    }

    private static String toJson(Object obj, ObjectMapper mapper) {
        if (obj == null) {
            return "";
        } else {
            try {
                StringWriter writer = new StringWriter();
                mapper.writeValue(writer, obj);
                return writer.toString();
            } catch (IOException var3) {
                return "";
            }
        }
    }

    public static <T> T toObj(String jsonStr, Class<T> objClass) {
        if (isEmpty(jsonStr)) {
            return newInstance(objClass);
        } else {
            try {
                return MAPPER.readValue(jsonStr, objClass);
            } catch (IOException var3) {
                return newInstance(objClass);
            }
        }
    }

    private static <T> T newInstance(Class<T> objClass) {
        try {
            return objClass.newInstance();
        } catch (Exception var2) {
            throw new RuntimeException("cannot create new instance", var2);
        }
    }

    public static <T> List<T> toObjList(String jsonStr, Class<T> objClass) {
        if (isEmpty(jsonStr)) {
            return Collections.emptyList();
        } else {
            try {
                return (List)MAPPER.readValue(jsonStr, MAPPER.getTypeFactory().constructParametrizedType(ArrayList.class, List.class, new Class[]{objClass}));
            } catch (IOException var3) {
                return Collections.emptyList();
            }
        }
    }
    public static boolean isBlank(CharSequence cs) {
        int strLen;
        if (cs != null && (strLen = cs.length()) != 0) {
            for(int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }

            return true;
        } else {
            return true;
        }
    }

    public static boolean isEmpty(String content) {
        return isBlank(content) || "{}".equals(content) || "[]".equals(content);
    }

    public static boolean isNotEmpty(String content) {
        return !isEmpty(content);
    }

    public static JavaType getCollectionType(Class<?> paramClass, Class<?>... elementClasses) {
        return MAPPER.getTypeFactory().constructParametricType(paramClass, elementClasses);
    }

    public static <T> T parse(String jsonStr, Class<T> paramClass, Class<?>... elementClass) {
        try {
            return MAPPER.readValue(jsonStr, getCollectionType(paramClass, elementClass));
        } catch (IOException var4) {
            return null;
        }
    }

    public static <T> T parse(String jsonStr, TypeReference<T> typeReference) {
        try {
            return MAPPER.readValue(jsonStr, typeReference);
        } catch (IOException var3) {
            return null;
        }
    }

    public static JsonNode tree(String jsonString) {
        try {
            return MAPPER.readTree(jsonString);
        } catch (IOException var2) {
            return null;
        }
    }

    public static Map toMap(String jsonStr, Class<?> keyClass, Class<?> valueClass) {
        try {
            return (Map)MAPPER.readValue(jsonStr, MAPPER.getTypeFactory().constructParametricType(HashMap.class, new Class[]{keyClass, valueClass}));
        } catch (IOException var4) {
            var4.printStackTrace();
            return Collections.EMPTY_MAP;
        }
    }

    public static Map toMapForSecretManager(String jsonStr, Class<?> keyClass, Class<?> valueClass) throws Exception {
        return (Map)MAPPER.readValue(jsonStr, MAPPER.getTypeFactory().constructParametricType(HashMap.class, new Class[]{keyClass, valueClass}));
    }

    public static ObjectNode createObjectNode() {
        return MAPPER.createObjectNode();
    }

    public static ArrayNode createArrayNode() {
        return MAPPER.createArrayNode();
    }

    public static JsonNode valueToTree(Object fromObj) {
        return MAPPER.valueToTree(fromObj);
    }

    static {
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        MAPPER.setSerializationInclusion(Include.NON_NULL);
        MAPPER_HAS_NULL = new ObjectMapper();
        MAPPER_HAS_NULL.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        MAPPER_HAS_NULL.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }
}
