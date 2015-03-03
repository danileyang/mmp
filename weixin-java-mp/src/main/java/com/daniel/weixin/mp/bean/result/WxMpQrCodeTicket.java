package com.daniel.weixin.mp.bean.result;

import java.io.Serializable;

/**
 * 换取二维码的Ticket
 * 
 * @author chanjarster/danielyang
 */
public class WxMpQrCodeTicket implements Serializable {
  
  protected String ticket;
  protected int expire_seconds = -1;
  protected String url;

  public String getTicket() {
    return ticket;
  }

  public void setTicket(String ticket) {
    this.ticket = ticket;
  }

  /**
   * 如果返回-1说明是永久
   */
  public int getExpire_seconds() {
    return expire_seconds;
  }

  public void setExpire_seconds(int expire_seconds) {
    this.expire_seconds = expire_seconds;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public static WxMpQrCodeTicket fromJson(String json) {
    return com.daniel.weixin.mp.util.json.WxMpGsonBuilder.INSTANCE.create().fromJson(json, WxMpQrCodeTicket.class);
  }
}
