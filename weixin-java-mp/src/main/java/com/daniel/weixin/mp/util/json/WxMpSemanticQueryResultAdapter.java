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

/**
 * 
 * @author Daniel Qian
 *
 */
public class WxMpSemanticQueryResultAdapter implements JsonDeserializer<com.daniel.weixin.mp.bean.result.WxMpSemanticQueryResult> {

  public com.daniel.weixin.mp.bean.result.WxMpSemanticQueryResult deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    com.daniel.weixin.mp.bean.result.WxMpSemanticQueryResult result = new com.daniel.weixin.mp.bean.result.WxMpSemanticQueryResult();
    JsonObject resultJsonObject = json.getAsJsonObject();

    if (GsonHelper.getString(resultJsonObject, "query") != null) {
      result.setQuery(GsonHelper.getString(resultJsonObject, "query"));
    }
    if (GsonHelper.getString(resultJsonObject, "type") != null) {
      result.setType(GsonHelper.getString(resultJsonObject, "type"));
    }
    if (resultJsonObject.get("semantic") != null) {
      result.setSemantic(resultJsonObject.get("semantic").toString());
    }
    if (resultJsonObject.get("result") != null) {
      result.setResult(resultJsonObject.get("result").toString());
    }
    if (GsonHelper.getString(resultJsonObject, "answer") != null) {
      result.setAnswer(GsonHelper.getString(resultJsonObject, "answer"));
    }
    if (GsonHelper.getString(resultJsonObject, "text") != null) {
      result.setText(GsonHelper.getString(resultJsonObject, "text"));
    }
    return result;
  }
  
}
