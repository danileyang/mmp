package com.daniel.weixin.mp.api;

/**
 * 消息匹配器，用在消息路由的时候
 */
public interface WxMpMessageMatcher {

  /**
   * 消息是否匹配某种模式
   * @param message
   * @return
   */
  public boolean match(com.daniel.weixin.mp.bean.WxMpXmlMessage message);

}
