package com.daniel.weixin.mp.util.json;

import com.google.gson.*;
import com.daniel.weixin.common.util.json.GsonHelper;

import java.lang.reflect.Type;

public class WxMpOAuth2AccessTokenAdapter implements JsonDeserializer<com.daniel.weixin.mp.bean.result.WxMpOAuth2AccessToken> {

  public com.daniel.weixin.mp.bean.result.WxMpOAuth2AccessToken deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws
      JsonParseException {
    com.daniel.weixin.mp.bean.result.WxMpOAuth2AccessToken accessToken = new com.daniel.weixin.mp.bean.result.WxMpOAuth2AccessToken();
    JsonObject accessTokenJsonObject = json.getAsJsonObject();

    if (accessTokenJsonObject.get("access_token") != null && !accessTokenJsonObject.get("access_token").isJsonNull()) {
      accessToken.setAccessToken(GsonHelper.getAsString(accessTokenJsonObject.get("access_token")));
    }
    if (accessTokenJsonObject.get("expires_in") != null && !accessTokenJsonObject.get("expires_in").isJsonNull()) {
      accessToken.setExpiresIn(GsonHelper.getAsPrimitiveInt(accessTokenJsonObject.get("expires_in")));
    }
    if (accessTokenJsonObject.get("refresh_token") != null && !accessTokenJsonObject.get("refresh_token").isJsonNull()) {
      accessToken.setRefreshToken(GsonHelper.getAsString(accessTokenJsonObject.get("refresh_token")));
    }
    if (accessTokenJsonObject.get("openid") != null && !accessTokenJsonObject.get("openid").isJsonNull()) {
      accessToken.setOpenId(GsonHelper.getAsString(accessTokenJsonObject.get("openid")));
    }
    if (accessTokenJsonObject.get("scope") != null && !accessTokenJsonObject.get("scope").isJsonNull()) {
      accessToken.setScope(GsonHelper.getAsString(accessTokenJsonObject.get("scope")));
    }
    return accessToken;
  }

}
