package com.daniel.weixin.mp.bean.custombuilder;

import com.daniel.weixin.common.api.WxConsts;

/**
 * 文本消息builder
 * <pre>
 * 用法: WxMpCustomMessage m = WxMpCustomMessage.TEXT().content(...).toUser(...).build();
 * </pre>
 * @author chanjarster/danielyang
 *
 */
public final class TextBuilder extends com.daniel.weixin.mp.bean.custombuilder.BaseBuilder<TextBuilder> {
  private String content;

  public TextBuilder() {
    this.msgType = WxConsts.CUSTOM_MSG_TEXT;
  }

  public TextBuilder content(String content) {
    this.content = content;
    return this;
  }

  public com.daniel.weixin.mp.bean.WxMpCustomMessage build() {
    com.daniel.weixin.mp.bean.WxMpCustomMessage m = super.build();
    m.setContent(this.content);
    return m;
  }
}
