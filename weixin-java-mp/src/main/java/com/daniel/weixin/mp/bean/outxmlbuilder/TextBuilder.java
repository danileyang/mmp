package com.daniel.weixin.mp.bean.outxmlbuilder;

import com.daniel.weixin.mp.bean.WxMpXmlOutTextMessage;

/**
 * 文本消息builder
 * @author chanjarster/danielyang
 *
 */
public final class TextBuilder extends com.daniel.weixin.mp.bean.outxmlbuilder.BaseBuilder<TextBuilder, com.daniel.weixin.mp.bean.WxMpXmlOutTextMessage> {
  private String content;

  public TextBuilder content(String content) {
    this.content = content;
    return this;
  }

  public com.daniel.weixin.mp.bean.WxMpXmlOutTextMessage build() {
    com.daniel.weixin.mp.bean.WxMpXmlOutTextMessage m = new WxMpXmlOutTextMessage();
    setCommon(m);
    m.setContent(this.content);
    return m;
  }
}
