package com.daniel.weixin.mp.util.json;

import com.daniel.weixin.mp.bean.result.WxMpUser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class WxMpGsonBuilder {

  public static final GsonBuilder INSTANCE = new GsonBuilder();
  
  static {
    INSTANCE.disableHtmlEscaping();
    INSTANCE.registerTypeAdapter(com.daniel.weixin.mp.bean.WxMpCustomMessage.class, new com.daniel.weixin.mp.util.json.WxMpCustomMessageGsonAdapter());
    INSTANCE.registerTypeAdapter(com.daniel.weixin.mp.bean.WxMpMassNews.class, new WxMpMassNewsGsonAdapter());
    INSTANCE.registerTypeAdapter(com.daniel.weixin.mp.bean.WxMpMassGroupMessage.class, new com.daniel.weixin.mp.util.json.WxMpMassGroupMessageGsonAdapter());
    INSTANCE.registerTypeAdapter(com.daniel.weixin.mp.bean.WxMpMassOpenIdsMessage.class, new com.daniel.weixin.mp.util.json.WxMpMassOpenIdsMessageGsonAdapter());
    INSTANCE.registerTypeAdapter(com.daniel.weixin.mp.bean.WxMpGroup.class, new com.daniel.weixin.mp.util.json.WxMpGroupGsonAdapter());
    INSTANCE.registerTypeAdapter(WxMpUser.class, new com.daniel.weixin.mp.util.json.WxMpUserGsonAdapter());
    INSTANCE.registerTypeAdapter(com.daniel.weixin.mp.bean.result.WxMpUserList.class, new com.daniel.weixin.mp.util.json.WxUserListGsonAdapter());
    INSTANCE.registerTypeAdapter(com.daniel.weixin.mp.bean.WxMpMassVideo.class, new com.daniel.weixin.mp.util.json.WxMpMassVideoAdapter());
    INSTANCE.registerTypeAdapter(com.daniel.weixin.mp.bean.result.WxMpMassSendResult.class, new com.daniel.weixin.mp.util.json.WxMpMassSendResultAdapter());
    INSTANCE.registerTypeAdapter(com.daniel.weixin.mp.bean.result.WxMpMassUploadResult.class, new com.daniel.weixin.mp.util.json.WxMpMassUploadResultAdapter());
    INSTANCE.registerTypeAdapter(com.daniel.weixin.mp.bean.result.WxMpQrCodeTicket.class, new com.daniel.weixin.mp.util.json.WxQrCodeTicketAdapter());
    INSTANCE.registerTypeAdapter(com.daniel.weixin.mp.bean.WxMpTemplateMessage.class, new com.daniel.weixin.mp.util.json.WxMpTemplateMessageGsonAdapter());
    INSTANCE.registerTypeAdapter(com.daniel.weixin.mp.bean.result.WxMpSemanticQueryResult.class, new com.daniel.weixin.mp.util.json.WxMpSemanticQueryResultAdapter());
    INSTANCE.registerTypeAdapter(com.daniel.weixin.mp.bean.result.WxMpOAuth2AccessToken.class, new com.daniel.weixin.mp.util.json.WxMpOAuth2AccessTokenAdapter());
    INSTANCE.registerTypeAdapter(com.daniel.weixin.mp.bean.result.WxMpUserSummary.class, new com.daniel.weixin.mp.util.json.WxMpUserSummaryGsonAdapter());
    INSTANCE.registerTypeAdapter(com.daniel.weixin.mp.bean.result.WxMpUserCumulate.class, new com.daniel.weixin.mp.util.json.WxMpUserCumulateGsonAdapter());
  }
  
  public static Gson create() {
    return INSTANCE.create();
  }
  
}
