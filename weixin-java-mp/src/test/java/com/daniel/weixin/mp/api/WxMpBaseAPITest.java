package com.daniel.weixin.mp.api;

import com.daniel.weixin.common.exception.WxErrorException;
import com.google.inject.Inject;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

/**
 * 基础API测试
 * @author chanjarster/danielyang
 *
 */
@Test(groups = "baseAPI")
@Guice(modules = ApiTestModule.class)
public class WxMpBaseAPITest {

  @Inject
  protected WxMpServiceImpl wxService;

  public void testRefreshAccessToken() throws WxErrorException {
//    WxMpConfigStorage configStorage = wxService.wxMpConfigStorage;
//    String before = configStorage.getAccessToken();
//    wxService.getAccessToken(false);
//
//    String after = configStorage.getAccessToken();
//    Assert.assertNotEquals(before, after);
//    Assert.assertTrue(StringUtils.isNotBlank(after));
  }

}
