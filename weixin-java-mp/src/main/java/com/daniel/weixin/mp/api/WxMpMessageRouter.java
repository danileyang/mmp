package com.daniel.weixin.mp.api;

import com.daniel.weixin.common.session.InternalSession;
import com.daniel.weixin.common.session.InternalSessionManager;
import com.daniel.weixin.common.session.StandardSessionManager;
import com.daniel.weixin.common.session.WxSessionManager;
import com.daniel.weixin.common.util.LogExceptionHandler;
import com.daniel.weixin.common.service.WxErrorExceptionHandler;
import com.daniel.weixin.common.service.WxMessageDuplicateChecker;
import com.daniel.weixin.common.service.WxMessageInMemoryDuplicateChecker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * <pre>
 * 微信消息路由器，通过代码化的配置，把来自微信的消息交给handler处理
 * 
 * 说明：
 * 1. 配置路由规则时要按照从细到粗的原则，否则可能消息可能会被提前处理
 * 2. 默认情况下消息只会被处理一次，除非使用 {@link com.daniel.weixin.mp.api.WxMpMessageRouterRule#next()}
 * 3. 规则的结束必须用{@link com.daniel.weixin.mp.api.WxMpMessageRouterRule#end()}或者{@link com.daniel.weixin.mp.api.WxMpMessageRouterRule#next()}，否则不会生效
 *
 * 使用方法：
 * WxMpMessageRouter router = new WxMpMessageRouter();
 * router
 *   .rule()
 *       .msgType("MSG_TYPE").event("EVENT").eventKey("EVENT_KEY").content("CONTENT")
 *       .interceptor(interceptor, ...).handler(handler, ...)
 *   .end()
 *   .rule()
 *       // 另外一个匹配规则
 *   .end()
 * ;
 *
 * // 将WxXmlMessage交给消息路由器
 * router.route(message);
 *
 * </pre>
 * @author Daniel Qian
 *
 */
public class WxMpMessageRouter {

  protected final Logger log = LoggerFactory.getLogger(WxMpMessageRouter.class);

  private static final int DEFAULT_THREAD_POOL_SIZE = 100;

  private final List<com.daniel.weixin.mp.api.WxMpMessageRouterRule> rules = new ArrayList<com.daniel.weixin.mp.api.WxMpMessageRouterRule>();

  private final com.daniel.weixin.mp.api.WxMpService wxMpService;

  private ExecutorService executorService;

  private WxMessageDuplicateChecker messageDuplicateChecker;

  private WxSessionManager sessionManager;

  private WxErrorExceptionHandler exceptionHandler;

  public WxMpMessageRouter(com.daniel.weixin.mp.api.WxMpService wxMpService) {
    this.wxMpService = wxMpService;
    this.executorService = Executors.newFixedThreadPool(DEFAULT_THREAD_POOL_SIZE);
    this.messageDuplicateChecker = new WxMessageInMemoryDuplicateChecker();
    this.sessionManager = new StandardSessionManager();
    this.exceptionHandler = new LogExceptionHandler();
  }

  /**
   * <pre>
   * 设置自定义的 {@link java.util.concurrent.ExecutorService}
   * 如果不调用该方法，默认使用 Executors.newFixedThreadPool(100)
   * </pre>
   * @param executorService
   */
  public void setExecutorService(ExecutorService executorService) {
    this.executorService = executorService;
  }

  /**
   * <pre>
   * 设置自定义的 {@link com.daniel.weixin.common.service.WxMessageDuplicateChecker}
   * 如果不调用该方法，默认使用 {@link com.daniel.weixin.common.service.WxMessageInMemoryDuplicateChecker}
   * </pre>
   * @param messageDuplicateChecker
   */
  public void setMessageDuplicateChecker(WxMessageDuplicateChecker messageDuplicateChecker) {
    this.messageDuplicateChecker = messageDuplicateChecker;
  }

  /**
   * <pre>
   * 设置自定义的{@link com.daniel.weixin.common.session.WxSessionManager}
   * 如果不调用该方法，默认使用 {@link com.daniel.weixin.common.session.StandardSessionManager}
   * </pre>
   * @param sessionManager
   */
  public void setSessionManager(WxSessionManager sessionManager) {
    this.sessionManager = sessionManager;
  }

  /**
   * <pre>
   * 设置自定义的{@link com.daniel.weixin.common.service.WxErrorExceptionHandler}
   * 如果不调用该方法，默认使用 {@link com.daniel.weixin.common.util.LogExceptionHandler}
   * </pre>
   * @param exceptionHandler
   */
  public void setExceptionHandler(WxErrorExceptionHandler exceptionHandler) {
    this.exceptionHandler = exceptionHandler;
  }

  List<com.daniel.weixin.mp.api.WxMpMessageRouterRule> getRules() {
    return this.rules;
  }

  /**
   * 开始一个新的Route规则
   * @return
   */
  public com.daniel.weixin.mp.api.WxMpMessageRouterRule rule() {
    return new com.daniel.weixin.mp.api.WxMpMessageRouterRule(this);
  }

  /**
   * 处理微信消息
   * @param wxMessage
   */
  public com.daniel.weixin.mp.bean.WxMpXmlOutMessage route(final com.daniel.weixin.mp.bean.WxMpXmlMessage wxMessage) {
    if (isDuplicateMessage(wxMessage)) {
      // 如果是重复消息，那么就不做处理
      return null;
    }

    final List<com.daniel.weixin.mp.api.WxMpMessageRouterRule> matchRules = new ArrayList<com.daniel.weixin.mp.api.WxMpMessageRouterRule>();
    // 收集匹配的规则
    for (final com.daniel.weixin.mp.api.WxMpMessageRouterRule rule : rules) {
      if (rule.test(wxMessage)) {
        matchRules.add(rule);
        if(!rule.isReEnter()) {
          break;
        }
      }
    }

    if (matchRules.size() == 0) {
      return null;
    }

    com.daniel.weixin.mp.bean.WxMpXmlOutMessage res = null;
    final List<Future> futures = new ArrayList<Future>();
    for (final com.daniel.weixin.mp.api.WxMpMessageRouterRule rule : matchRules) {
      // 返回最后一个非异步的rule的执行结果
      if(rule.isAsync()) {
        futures.add(
            executorService.submit(new Runnable() {
              public void run() {
                rule.service(wxMessage, wxMpService, sessionManager, exceptionHandler);
              }
            })
        );
      } else {
        res = rule.service(wxMessage, wxMpService, sessionManager, exceptionHandler);
        // 在同步操作结束，session访问结束
        log.debug("End session access: async=false, sessionId={}", wxMessage.getFromUserName());
        sessionEndAccess(wxMessage);
      }
    }

    if (futures.size() > 0) {
      executorService.submit(new Runnable() {
        @Override
        public void run() {
          for (Future future : futures) {
            try {
              future.get();
              log.debug("End session access: async=true, sessionId={}", wxMessage.getFromUserName());
              // 异步操作结束，session访问结束
              sessionEndAccess(wxMessage);
            } catch (InterruptedException e) {
              log.error("Error happened when wait task finish", e);
            } catch (ExecutionException e) {
              log.error("Error happened when wait task finish", e);
            }
          }
        }
      });
    }
    return res;
  }

  protected boolean isDuplicateMessage(com.daniel.weixin.mp.bean.WxMpXmlMessage wxMessage) {

    String messageId = "";
    if (wxMessage.getMsgId() == null) {
      messageId = String.valueOf(wxMessage.getCreateTime())
          + "-" + wxMessage.getFromUserName()
          + "-" + String.valueOf(wxMessage.getEventKey() == null ? "" : wxMessage.getEventKey())
          + "-" + String.valueOf(wxMessage.getEvent() == null ? "" : wxMessage.getEvent())
      ;
    } else {
      messageId = String.valueOf(wxMessage.getMsgId());
    }

    if (messageDuplicateChecker.isDuplicate(messageId)) {
      return true;
    }
    return false;

  }

  /**
   * 对session的访问结束
   * @param wxMessage
   */
  protected void sessionEndAccess(com.daniel.weixin.mp.bean.WxMpXmlMessage wxMessage) {

    InternalSession session = ((InternalSessionManager)sessionManager).findSession(wxMessage.getFromUserName());
    if (session != null) {
      session.endAccess();
    }

  }
}
