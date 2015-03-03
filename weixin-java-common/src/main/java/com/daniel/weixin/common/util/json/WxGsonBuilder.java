package com.daniel.weixin.common.util.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.daniel.weixin.common.bean.WxMenu;
import com.daniel.weixin.common.bean.result.WxError;
import com.daniel.weixin.common.bean.WxAccessToken;
import com.daniel.weixin.common.bean.result.WxMediaUploadResult;

public class WxGsonBuilder {

  public static final GsonBuilder INSTANCE = new GsonBuilder();

  static {
    INSTANCE.disableHtmlEscaping();
    INSTANCE.registerTypeAdapter(WxAccessToken.class, new WxAccessTokenAdapter());
    INSTANCE.registerTypeAdapter(WxError.class, new WxErrorAdapter());
    INSTANCE.registerTypeAdapter(WxMenu.class, new WxMenuGsonAdapter());
    INSTANCE.registerTypeAdapter(WxMediaUploadResult.class, new WxMediaUploadResultAdapter());
  }

  public static Gson create() {
    return INSTANCE.create();
  }

}
