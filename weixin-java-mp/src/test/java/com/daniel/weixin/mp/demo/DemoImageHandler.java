package com.daniel.weixin.mp.demo;

import com.daniel.weixin.common.exception.WxErrorException;
import com.daniel.weixin.common.model.response.WxMediaUploadResult;
import com.daniel.weixin.common.session.WxSessionManager;
import com.daniel.weixin.common.util.WxConsts;
import com.daniel.weixin.mp.api.WxMpMessageHandler;
import com.daniel.weixin.mp.api.WxMpService;
import com.daniel.weixin.mp.bean.WxMpXmlMessage;
import com.daniel.weixin.mp.bean.WxMpXmlOutImageMessage;
import com.daniel.weixin.mp.bean.WxMpXmlOutMessage;

import java.io.IOException;
import java.util.Map;

public class DemoImageHandler implements WxMpMessageHandler {
  @Override
  public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context,
      WxMpService wxMpService, WxSessionManager sessionManager) {
    try {
      WxMediaUploadResult wxMediaUploadResult = wxMpService
          .mediaUpload(WxConsts.MEDIA_IMAGE, WxConsts.FILE_JPG, ClassLoader.getSystemResourceAsStream("mm.jpeg"));
      WxMpXmlOutImageMessage m
          = WxMpXmlOutMessage
          .IMAGE()
          .mediaId(wxMediaUploadResult.getMediaId())
          .fromUser(wxMessage.getToUserName())
          .toUser(wxMessage.getFromUserName())
          .build();
      return m;
    } catch (WxErrorException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
}
