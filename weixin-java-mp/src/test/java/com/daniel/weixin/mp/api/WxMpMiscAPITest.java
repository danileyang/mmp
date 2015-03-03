package com.daniel.weixin.mp.api;

import com.google.inject.Inject;
import com.daniel.weixin.common.api.WxConsts;
import com.daniel.weixin.common.bean.result.WxMediaUploadResult;
import com.daniel.weixin.common.exception.WxErrorException;
import com.daniel.weixin.common.session.WxSession;
import com.daniel.weixin.mp.bean.WxMpMassGroupMessage;
import com.daniel.weixin.mp.bean.WxMpMassNews;
import com.daniel.weixin.mp.bean.WxMpMassOpenIdsMessage;
import com.daniel.weixin.mp.bean.WxMpMassVideo;
import com.daniel.weixin.mp.bean.result.WxMpMassSendResult;
import com.daniel.weixin.mp.bean.result.WxMpMassUploadResult;
import com.daniel.weixin.mp.bean.result.WxMpUserCumulate;
import com.daniel.weixin.mp.bean.result.WxMpUserSummary;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 *
 * @author chanjarster/danielyang
 *
 */
@Test(groups = "miscAPI", dependsOnGroups = { "baseAPI"})
@Guice(modules = ApiTestModule.class)
public class WxMpMiscAPITest {

  @Inject
  protected WxMpServiceImpl wxService;

  @Test
  public void testGetCallbackIP() throws WxErrorException {
    String[] ipArray = wxService.getCallbackIP();
    System.out.println(Arrays.toString(ipArray));
    Assert.assertNotNull(ipArray);
    Assert.assertNotEquals(ipArray.length, 0);
  }

  @Test
  public void testGetUserSummary() throws WxErrorException, ParseException {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date beginDate = simpleDateFormat.parse("2015-01-01");
    Date endDate = simpleDateFormat.parse("2015-01-02");
    List<WxMpUserSummary> summaries = wxService.getUserSummary(beginDate, endDate);
    System.out.println(summaries);
    Assert.assertNotNull(summaries);
  }

  @Test
  public void testGetUserCumulate() throws WxErrorException, ParseException {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date beginDate = simpleDateFormat.parse("2015-01-01");
    Date endDate = simpleDateFormat.parse("2015-01-02");
    List<WxMpUserCumulate> cumulates =  wxService.getUserCumulate(beginDate, endDate);
    System.out.println(cumulates);
    Assert.assertNotNull(cumulates);
  }

}
