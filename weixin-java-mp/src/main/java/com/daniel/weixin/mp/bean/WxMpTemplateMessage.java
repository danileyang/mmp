package com.daniel.weixin.mp.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class WxMpTemplateMessage implements Serializable {

  private String toUser;
  private String templateId;
  private String url;
  private String topColor;
  private List<com.daniel.weixin.mp.bean.WxMpTemplateData> datas = new ArrayList<com.daniel.weixin.mp.bean.WxMpTemplateData>();

  public String getToUser() {
    return toUser;
  }

  public void setToUser(String toUser) {
    this.toUser = toUser;
  }

  public String getTemplateId() {
    return templateId;
  }

  public void setTemplateId(String templateId) {
    this.templateId = templateId;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getTopColor() {
    return topColor;
  }

  public void setTopColor(String topColor) {
    this.topColor = topColor;
  }

  public List<com.daniel.weixin.mp.bean.WxMpTemplateData> getDatas() {
    return datas;
  }

  public void setDatas(List<com.daniel.weixin.mp.bean.WxMpTemplateData> datas) {
    this.datas = datas;
  }

  public String toJson() {
    return com.daniel.weixin.mp.util.json.WxMpGsonBuilder.INSTANCE.create().toJson(this);
  }
}
