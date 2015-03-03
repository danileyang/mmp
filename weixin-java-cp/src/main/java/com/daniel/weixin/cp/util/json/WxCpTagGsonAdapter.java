/*
 * KINGSTAR MEDIA SOLUTIONS Co.,LTD. Copyright c 2005-2013. All rights reserved.
 *
 * This source code is the property of KINGSTAR MEDIA SOLUTIONS LTD. It is intended
 * only for the use of KINGSTAR MEDIA application development. Reengineering, reproduction
 * arose from modification of the original source, or other redistribution of this source
 * is not permitted without written permission of the KINGSTAR MEDIA SOLUTIONS LTD.
 */
package com.daniel.weixin.cp.util.json;

import com.google.gson.*;
import com.daniel.weixin.common.util.json.GsonHelper;
import com.daniel.weixin.cp.bean.WxCpTag;

import java.lang.reflect.Type;

/**
 * @author Daniel Qian
 */
public class WxCpTagGsonAdapter implements JsonSerializer<WxCpTag>, JsonDeserializer<WxCpTag> {

  public JsonElement serialize(WxCpTag group, Type typeOfSrc, JsonSerializationContext context) {
    JsonObject o = new JsonObject();
    o.addProperty("tagid", group.getId());
    o.addProperty("tagname", group.getName());
    return o;
  }

  public WxCpTag deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    JsonObject jsonObject = json.getAsJsonObject();
    return new WxCpTag(GsonHelper.getString(jsonObject, "tagid"), GsonHelper.getString(jsonObject, "name"));
  }

}
