package studio.istart.framework.tool;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

/**
 *
 * @author Se7en
 * @date 2016/4/25
 * Desc: Json序列化的简单封装；方便后期做替换
 */
public final class JsonHelper {

    public static <T> T deserialize(String jsonString, Class<T> tClass) {
        Gson json = new Gson();
        return json.fromJson(jsonString, tClass);
    }

    public static <T> T deserialize(String jsonString, Type type) {
        Gson json = new Gson();
        return json.fromJson(jsonString, type);
    }

    public static String serialize(Object entity) {
        Gson json = new Gson();
        return json.toJson(entity);
    }

    public static String serializeExposeObject(Object entity) {
        Gson json = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return json.toJson(entity);
    }

    public static String serializeModifierObject(Object entity) {
        Gson json = new GsonBuilder().excludeFieldsWithModifiers(Modifier.PROTECTED).create();
        return json.toJson(entity);
    }


}
