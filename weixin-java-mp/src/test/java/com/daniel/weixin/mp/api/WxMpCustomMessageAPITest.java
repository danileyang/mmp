package com.daniel.weixin.mp.api;

import com.google.inject.Inject;
import com.daniel.weixin.common.util.WxConsts;
import com.daniel.weixin.common.exception.WxErrorException;
import com.daniel.weixin.mp.bean.WxMpCustomMessage;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

/***
 * 测试发送客服消息
 * @author chanjarster/danielyang
 *
 */
@Test(groups="customMessageAPI", dependsOnGroups = "baseAPI")
@Guice(modules = ApiTestModule.class)
public class WxMpCustomMessageAPITest {

  @Inject
  protected WxMpServiceImpl wxService;

  public void testSendCustomMessage() throws WxErrorException {
    ApiTestModule.WxXmlMpInMemoryConfigStorage configStorage = (ApiTestModule.WxXmlMpInMemoryConfigStorage) wxService.wxMpConfigStorage;
    WxMpCustomMessage message = new WxMpCustomMessage();
    message.setMsgType(WxConsts.CUSTOM_MSG_TEXT);
    message.setToUser(configStorage.getOpenId());
    message.setContent("欢迎欢迎，热烈欢迎\n换行测试\n超链接:<a href=\"http://www.baidu.com\">Hello World</a>");

    wxService.customMessageSend(message);
  }

}
