package com.daniel.weixin.mp.api;

import org.testng.annotations.Test;

@Test
public class WxMpBusyRetryTest {
//
//  @DataProvider(name="getService")
//  public Object[][] getService() {
//    WxMpService service = new WxMpServiceImpl() {
//
//      @Override
//      protected <T, E> T executeInternal(RequestExecutor<T, E> executor, String uri, E data) throws WxErrorException {
//        WxError error = new WxError();
//        error.setErrorCode(-1);
//        throw new WxErrorException(error);
//      }
//    };
//
//    service.setMaxRetryTimes(3);
//    service.setRetrySleepMillis(500);
//    return new Object[][] {
//        new Object[] { service }
//    };
//  }
//
//  @Test(dataProvider = "getService", expectedExceptions = RuntimeException.class)
//  public void testRetry(WxMpService service) throws WxErrorException {
//    service.execute(null, null, null);
//  }
//
//  @Test(dataProvider = "getService")
//  public void testRetryInThreadPool(final WxMpService service) throws InterruptedException, ExecutionException {
//    // 当线程池中的线程复用的时候，还是能保证相同的重试次数
//    ExecutorService executorService = Executors.newFixedThreadPool(1);
//    Runnable runnable = new Runnable() {
//      @Override
//      public void run() {
//        try {
//          System.out.println("=====================");
//          System.out.println(Thread.currentThread().getName() + ": testRetry");
//          service.execute(null, null, null);
//        } catch (WxErrorException e) {
//          throw new RuntimeException(e);
//        } catch (RuntimeException e) {
//          // OK
//        }
//      }
//    };
//    Future<?> submit1 = executorService.submit(runnable);
//    Future<?> submit2 = executorService.submit(runnable);
//
//    submit1.get();
//    submit2.get();
//  }

}
