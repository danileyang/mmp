package com.daniel.weixin.mp.bean.outxmlbuilder;

/**
 * 语音消息builder
 * @author chanjarster/danielyang
 */
public final class VoiceBuilder extends com.daniel.weixin.mp.bean.outxmlbuilder.BaseBuilder<VoiceBuilder, com.daniel.weixin.mp.bean.WxMpXmlOutVoiceMessage> {

  private String mediaId;

  public VoiceBuilder mediaId(String mediaId) {
    this.mediaId = mediaId;
    return this;
  }
  
  public com.daniel.weixin.mp.bean.WxMpXmlOutVoiceMessage build() {
    com.daniel.weixin.mp.bean.WxMpXmlOutVoiceMessage m = new com.daniel.weixin.mp.bean.WxMpXmlOutVoiceMessage();
    setCommon(m);
    m.setMediaId(mediaId);
    return m;
  }
  
}
