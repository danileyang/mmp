/*
 * KINGSTAR MEDIA SOLUTIONS Co.,LTD. Copyright c 2005-2013. All rights reserved.
 *
 * This source code is the property of KINGSTAR MEDIA SOLUTIONS LTD. It is intended
 * only for the use of KINGSTAR MEDIA application development. Reengineering, reproduction
 * arose from modification of the original source, or other redistribution of this source
 * is not permitted without written permission of the KINGSTAR MEDIA SOLUTIONS LTD.
 */
package com.daniel.weixin.mp.util.json;

import com.google.gson.*;
import com.daniel.weixin.common.util.json.GsonHelper;

import java.lang.reflect.Type;

public class WxMpGroupGsonAdapter implements JsonSerializer<com.daniel.weixin.mp.bean.WxMpGroup>, JsonDeserializer<com.daniel.weixin.mp.bean.WxMpGroup> {

  public JsonElement serialize(com.daniel.weixin.mp.bean.WxMpGroup group, Type typeOfSrc, JsonSerializationContext context) {
    JsonObject json = new JsonObject();
    JsonObject groupJson = new JsonObject();
    groupJson.addProperty("name", group.getName());
    groupJson.addProperty("id", group.getId());
    groupJson.addProperty("count", group.getCount());
    json.add("group", groupJson);
    return json;
  }

  public com.daniel.weixin.mp.bean.WxMpGroup deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    com.daniel.weixin.mp.bean.WxMpGroup group = new com.daniel.weixin.mp.bean.WxMpGroup();
    JsonObject groupJson = json.getAsJsonObject();
    if (json.getAsJsonObject().get("group") != null) {
      groupJson = json.getAsJsonObject().get("group").getAsJsonObject();
    }
    if (groupJson.get("name") != null && !groupJson.get("name").isJsonNull()) {
      group.setName(GsonHelper.getAsString(groupJson.get("name")));
    }
    if (groupJson.get("id") != null && !groupJson.get("id").isJsonNull()) {
      group.setId(GsonHelper.getAsPrimitiveLong(groupJson.get("id")));
    }
    if (groupJson.get("count") != null && !groupJson.get("count").isJsonNull()) {
      group.setCount(GsonHelper.getAsPrimitiveLong(groupJson.get("count")));
    }
    return group;
  }
  
}
