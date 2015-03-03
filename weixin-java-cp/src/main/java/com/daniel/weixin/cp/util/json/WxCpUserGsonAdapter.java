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
import com.daniel.weixin.cp.bean.WxCpUser;

import java.lang.reflect.Type;

/**
 * @author Daniel Qian
 */
public class WxCpUserGsonAdapter implements JsonDeserializer<WxCpUser>, JsonSerializer<WxCpUser> {

  public WxCpUser deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    JsonObject o = json.getAsJsonObject();
    WxCpUser user = new WxCpUser();
    user.setUserId(GsonHelper.getString(o, "userid"));
    user.setName(GsonHelper.getString(o, "name"));

    if(o.get("department") != null) {
      JsonArray departJsonArray = o.get("department").getAsJsonArray();
      Integer[] departIds = new Integer[departJsonArray.size()];
      int i = 0;
      for (JsonElement jsonElement : departJsonArray) {
        departIds[i++] = jsonElement.getAsInt();
      }
      user.setDepartIds(departIds);
    }

    user.setPosition(GsonHelper.getString(o, "position"));
    user.setMobile(GsonHelper.getString(o, "mobile"));
    Integer gender = GsonHelper.getInteger(o, "gender");
    if (new Integer(1).equals(gender)) {
      user.setGender("男");
    } else if (new Integer(2).equals(gender)) {
      user.setGender("女");
    } else {
      user.setGender("未知");
    }
    user.setTel(GsonHelper.getString(o, "tel"));
    user.setEmail(GsonHelper.getString(o, "email"));
    user.setWeiXinId(GsonHelper.getString(o, "weixinid"));
    user.setAvatar(GsonHelper.getString(o, "avatar"));
    user.setStatus(GsonHelper.getInteger(o, "status"));

    if (GsonHelper.isNotNull(o.get("extattr"))) {
      JsonArray attrJsonElements = o.get("extattr").getAsJsonObject().get("attrs").getAsJsonArray();
      for (JsonElement attrJsonElement : attrJsonElements) {
        WxCpUser.Attr attr = new WxCpUser.Attr(
            GsonHelper.getString(attrJsonElement.getAsJsonObject(), "name"),
            GsonHelper.getString(attrJsonElement.getAsJsonObject(), "value")
        );
        user.getExtAttrs().add(attr);
      }
    }
    return user;
  }

  @Override
  public JsonElement serialize(WxCpUser user, Type typeOfSrc, JsonSerializationContext context) {
    JsonObject o = new JsonObject();
    if (user.getUserId() != null) {
      o.addProperty("userid", user.getUserId());
    }
    if (user.getName() != null) {
      o.addProperty("name", user.getName());
    }
    if (user.getDepartIds() != null) {
      JsonArray jsonArray = new JsonArray();
      for (Integer departId : user.getDepartIds()) {
        jsonArray.add(new JsonPrimitive(departId));
      }
      o.add("department", jsonArray);
    }
    if (user.getPosition() != null) {
      o.addProperty("position", user.getPosition());
    }
    if (user.getMobile() != null) {
      o.addProperty("mobile", user.getMobile());
    }
    if (user.getGender() != null) {
      o.addProperty("gender", user.getGender().equals("男") ? 0 : 1);
    }
    if (user.getTel() != null) {
      o.addProperty("tel", user.getTel());
    }
    if (user.getEmail() != null) {
      o.addProperty("email", user.getEmail());
    }
    if (user.getWeiXinId() != null) {
      o.addProperty("weixinid", user.getWeiXinId());
    }
    if (user.getAvatar() != null) {
      o.addProperty("avatar", user.getAvatar());
    }
    if (user.getStatus() != null) {
      o.addProperty("status", user.getStatus());
    }
    if (user.getExtAttrs().size() > 0) {
      JsonArray attrsJsonArray = new JsonArray();
      for (WxCpUser.Attr attr : user.getExtAttrs()) {
        JsonObject attrJson = new JsonObject();
        attrJson.addProperty("name", attr.getName());
        attrJson.addProperty("value", attr.getValue());
        attrsJsonArray.add(attrJson);
      }
      JsonObject attrsJson = new JsonObject();
      attrsJson.add("attrs", attrsJsonArray);
      o.add("extattr", attrsJson);
    }
    return o;
  }

}
