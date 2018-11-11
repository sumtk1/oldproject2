package com.gloiot.chatsdk.network.utils;


import org.json.JSONException;
import org.json.JSONStringer;

import java.util.HashMap;


/**
 * Created by JinzLin on 2016/3/3.
 */
public class JsonUtils {

    /**
     * hashMap转JSON
     *
     * @param hashMap
     * @return
     */
    public static JSONStringer createJSON(HashMap<String, Object> hashMap) {
        JSONStringer jsonText = new JSONStringer();
        try {
            jsonText.object();
            for (HashMap.Entry<String, Object> entry : hashMap.entrySet()) {
                jsonText.key(entry.getKey());
                jsonText.value(entry.getValue());
            }
            jsonText.endObject();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonText;
    }
}
