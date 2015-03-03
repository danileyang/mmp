package com.daniel.weixin.mp.bean.custombuilder;

import com.daniel.weixin.common.api.WxConsts;

/**
 * 语音消息builder
 * <pre>
 * 用法: WxMpCustomMessage m = WxMpCustomMessage.VOICE().mediaId(...).toUser(...).build();
 * </pre>
 * @author chanjarster/danielyang
 *
 */
public final class VoiceBuilder extends com.daniel.weixin.mp.bean.custombuilder.BaseBuilder<VoiceBuilder> {
  private String mediaId;

  public VoiceBuilder() {
    this.msgType = WxConsts.CUSTOM_MSG_VOICE;
  }

  public VoiceBuilder mediaId(String media_id) {
    this.mediaId = media_id;
    return this;
  }

  public com.daniel.weixin.mp.bean.WxMpCustomMessage build() {
    com.daniel.weixin.mp.bean.WxMpCustomMessage m = super.build();
    m.setMediaId(this.mediaId);
    return m;
  }
}
