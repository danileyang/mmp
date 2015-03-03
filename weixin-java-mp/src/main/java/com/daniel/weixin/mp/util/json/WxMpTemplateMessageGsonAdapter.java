/*
 * KINGSTAR MEDIA SOLUTIONS Co.,LTD. Copyright c 2005-2013. All rights reserved.
 *
 * This source code is the property of KINGSTAR MEDIA SOLUTIONS LTD. It is intended
 * only for the use of KINGSTAR MEDIA application development. Reengineering, reproduction
 * arose from modification of the original source, or other redistribution of this source
 * is not permitted without written permission of the KINGSTAR MEDIA SOLUTIONS LTD.
 */
package com.daniel.weixin.mp.util.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class WxMpTemplateMessageGsonAdapter implements JsonSerializer<com.daniel.weixin.mp.bean.WxMpTemplateMessage> {

  public JsonElement serialize(com.daniel.weixin.mp.bean.WxMpTemplateMessage message, Type typeOfSrc, JsonSerializationContext context) {
    JsonObject messageJson = new JsonObject();
    messageJson.addProperty("touser", message.getToUser());
    messageJson.addProperty("template_id", message.getTemplateId());
    if (message.getUrl() != null) {
      messageJson.addProperty("url", message.getUrl());
    }
    if (message.getTopColor() != null) {
      messageJson.addProperty("topcolor", message.getTopColor());
    }

    JsonObject datas = new JsonObject();
    messageJson.add("data", datas);

    for (com.daniel.weixin.mp.bean.WxMpTemplateData data : message.getDatas()) {
      JsonObject dataJson = new JsonObject();
      dataJson.addProperty("value", data.getValue());
      if (data.getColor() != null) {
        dataJson.addProperty("color", data.getColor());
      }
      datas.add(data.getName(), dataJson);
    }

    return messageJson;
  }

}
