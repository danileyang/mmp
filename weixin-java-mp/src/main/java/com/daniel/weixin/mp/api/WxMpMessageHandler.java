package com.daniel.weixin.mp.api;

import com.daniel.weixin.common.exception.WxErrorException;
import com.daniel.weixin.common.session.WxSessionManager;

import java.util.Map;

/**
 * 处理微信推送消息的处理器接口
 *
 * @author Daniel Qian
 */
public interface WxMpMessageHandler {

  /**
   * @param wxMessage
   * @param context        上下文，如果handler或interceptor之间有信息要传递，可以用这个
   * @param wxMpService
   * @param sessionManager
   * @return xml格式的消息，如果在异步规则里处理的话，可以返回null
   */
  public com.daniel.weixin.mp.bean.WxMpXmlOutMessage handle(com.daniel.weixin.mp.bean.WxMpXmlMessage wxMessage,
                                                            Map<String, Object> context,
                                                            com.daniel.weixin.mp.api.WxMpService wxMpService,
                                                            WxSessionManager sessionManager) throws WxErrorException;

}
