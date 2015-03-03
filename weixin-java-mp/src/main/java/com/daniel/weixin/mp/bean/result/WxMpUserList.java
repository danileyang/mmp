package com.daniel.weixin.mp.bean.result;

import java.util.ArrayList;
import java.util.List;

/**
 * 关注者列表
 * @author chanjarster/danielyang
 *
 */
public class WxMpUserList {

  protected int total = -1;
  protected int count = -1;
  protected List<String> openIds = new ArrayList<String>();
  protected String nextOpenId;
  public int getTotal() {
    return total;
  }
  public void setTotal(int total) {
    this.total = total;
  }
  public int getCount() {
    return count;
  }
  public void setCount(int count) {
    this.count = count;
  }
  public List<String> getOpenIds() {
    return openIds;
  }
  public void setOpenIds(List<String> openIds) {
    this.openIds = openIds;
  }
  public String getNextOpenId() {
    return nextOpenId;
  }
  public void setNextOpenId(String nextOpenId) {
    this.nextOpenId = nextOpenId;
  }
  
  public static WxMpUserList fromJson(String json) {
    return com.daniel.weixin.mp.util.json.WxMpGsonBuilder.INSTANCE.create().fromJson(json, WxMpUserList.class);
  }
}
