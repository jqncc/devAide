package org.jflame.devAide;

import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONValidator;

public final class Globals {

    public static void main(String[] args) {
        String jsonText = "{\"k1\":[1,2,4],\"k2\":1,\"k3\":{\"l3\":\"lval3\"}}";
        JSONValidator validator = JSONValidator.from(jsonText);
        if (JSON.isValidArray(jsonText)) {
            JSONArray root = JSON.parseArray(jsonText);
            child(root);
            /* int size = root.size();
            String nodeName;
            for (int i = 0; i < size; i++) {
                Object node = root.get(i);
                nodeName = "[" + i + "]:";
                if (node instanceof JSONObject) {
                    nodeName = nodeName + "[Object]";
                    JSONObject jnode = (JSONObject) node;
                    for (Map.Entry<String,Object> kv : jnode.entrySet()) {
                        System.out.println(kv.getKey() + ":" + kv.getValue());
                    }
                } else if (node instanceof JSONArray) {
                    nodeName = nodeName + "[Array]";
                    JSONArray jnode = (JSONArray) node;
                } else {
                    nodeName = nodeName + node;
                }
                System.out.println(nodeName);
            }*/
        } else {
            JSONObject root = JSON.parseObject(jsonText);
            child(root);
        }
    }

    static void child(JSON jnode) {
        String nodeName = "";
        if (jnode instanceof JSONArray) {
            JSONArray root = (JSONArray) jnode;
            int size = root.size();
            for (int i = 0; i < size; i++) {
                Object node = root.get(i);
                nodeName = "[" + i + "]:";

                if (node instanceof JSONObject) {
                    nodeName = nodeName + "[Object]";
                    System.out.println(nodeName);
                    if (!((JSONObject) node).isEmpty()) {
                        child((JSONObject) node);
                    }
                } else if (node instanceof JSONArray) {
                    nodeName = nodeName + "[Array]";
                    System.out.println(nodeName);
                    if (!((JSONArray) node).isEmpty()) {
                        child((JSONArray) node);
                    }
                } else {
                    nodeName = nodeName + node;
                    System.out.println(nodeName);
                }

            }
        } else {
            JSONObject root = (JSONObject) jnode;
            for (Map.Entry<String,Object> kv : root.entrySet()) {
                nodeName = kv.getKey() + ":";
                if (kv.getValue() instanceof JSONObject) {
                    nodeName = nodeName + "[Object]";
                    System.out.println(nodeName);
                    if (!((JSONObject) kv.getValue()).isEmpty()) {
                        child((JSONObject) kv.getValue());
                    }
                } else if (kv.getValue() instanceof JSONArray) {
                    nodeName = nodeName + "[Array]";
                    System.out.println(nodeName);
                    if (!((JSONArray) kv.getValue()).isEmpty()) {
                        child((JSONArray) kv.getValue());
                    }
                } else {
                    nodeName = nodeName + kv.getValue();
                    System.out.println(nodeName);
                }
            }
        }
    }
}
