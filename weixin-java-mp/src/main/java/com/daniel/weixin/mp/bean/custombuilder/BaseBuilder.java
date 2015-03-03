package com.daniel.weixin.mp.bean.custombuilder;

public class BaseBuilder<T> {
  protected String msgType;
  protected String toUser;

  public T toUser(String toUser) {
    this.toUser = toUser;
    return (T) this;
  }

  public com.daniel.weixin.mp.bean.WxMpCustomMessage build() {
    com.daniel.weixin.mp.bean.WxMpCustomMessage m = new com.daniel.weixin.mp.bean.WxMpCustomMessage();
    m.setMsgType(this.msgType);
    m.setToUser(this.toUser);
    return m;
  }
}
