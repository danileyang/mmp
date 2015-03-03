package com.daniel.weixin.mp.demo;

import com.daniel.weixin.common.session.WxSessionManager;
import com.daniel.weixin.mp.api.WxMpMessageHandler;
import com.daniel.weixin.mp.api.WxMpService;
import com.daniel.weixin.mp.bean.WxMpXmlMessage;
import com.daniel.weixin.mp.bean.WxMpXmlOutMessage;

import java.util.Map;

/**
 * Created by qianjia on 15/1/22.
 */
public class DemoLogHandler implements WxMpMessageHandler {
  @Override
  public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> context, WxMpService wxMpService,
      WxSessionManager sessionManager) {
    System.out.println(wxMessage.toString());
    return null;
  }
}
