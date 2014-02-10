package org.ghuazo.cotty.core;

import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.ghuazo.cotty.core.CottyConstant.CottyStatus;
import org.ghuazo.cotty.core.CottyManager.ManagerCallback;
import org.ghuazo.cotty.core.entity.CottyEvent;
import org.ghuazo.cotty.core.entity.CottyFriend;
import org.ghuazo.cotty.core.entity.CottyGroup;
import org.ghuazo.cotty.core.entity.CottyMessage;
import org.ghuazo.cotty.core.entity.DiscussGroup;
import org.ghuazo.cotty.core.service.AgreeService;
import org.ghuazo.cotty.core.service.ApproveService;
import org.ghuazo.cotty.core.service.AuthoriseService;
import org.ghuazo.cotty.core.service.CheckService;
import org.ghuazo.cotty.core.service.CottyService;
import org.ghuazo.cotty.core.service.CottyService.ServiceCallback;
import org.ghuazo.cotty.core.service.DiscussService;
import org.ghuazo.cotty.core.service.FriendService;
import org.ghuazo.cotty.core.service.GroupService;
import org.ghuazo.cotty.core.service.InitializeService;
import org.ghuazo.cotty.core.service.LoginService;
import org.ghuazo.cotty.core.service.MessageService;
import org.ghuazo.cotty.core.service.PollService;
import org.ghuazo.cotty.core.service.RedirectService;
import org.ghuazo.cotty.core.service.RejectService;
import org.ghuazo.cotty.core.service.StatusService;
import org.ghuazo.cotty.core.service.VerifyService;
import org.ghuazo.cotty.core.service.VfwebqqService;

public class CottyDisparcher implements ServiceCallback, ManagerCallback {

	private static final Logger logger = Log.getLogger(CottyDisparcher.class);

	private HttpClient httpClient;

	private LifecycleListener lifecycleListener;

	private RuntimeListener runtimeListener;

	private Map<String, CottyManager> cottyManagerMap;

	public CottyDisparcher(LifecycleListener lifecycleListener,
			RuntimeListener runtimeListener) {
		this.lifecycleListener = lifecycleListener;
		this.runtimeListener = runtimeListener;
		this.httpClient = new HttpClient(new SslContextFactory());
		this.httpClient.setFollowRedirects(false);
		this.cottyManagerMap = new HashMap<String, CottyManager>();
	}

	public void start() throws Exception {
		httpClient.start();
		logger.info("started...");
	}

	public void stop() throws Exception {
		httpClient.stop();
		logger.info("stoped...");

	}

	public void join(String userName, String paeeWord, CottyStatus loginStatus) {
		CottySession cottySession = new CottySession();
		cottySession.addAttribute("userName", userName);
		cottySession.addAttribute("passWord", paeeWord);
		cottySession.addAttribute("loginStatus", loginStatus);
		new CheckService(this.httpClient, cottySession).execute(this);
		logger.info("join...");

	}

	// 用于标识并发时状态，并发完成会加1
	private int syncFlag = 0;

	public void onServiceComplete(CottySession cottySession) {

		if (cottySession.getObjectAttribute("focusService") instanceof CheckService) {

			if (cottySession.getBooleanAttribute("verifyStatus")) {
				logger.info("需要验证");
				logger.info(cottySession.getStringAttribute("verifyImgid"));
				new VerifyService(this.httpClient, cottySession).execute(this);
			} else {
				logger.info("不需要验证");
				//logger.info(cottySession.getStringAttribute("verifyCode"));
				new LoginService(this.httpClient, cottySession).execute(this);
			}

		} else if (cottySession.getObjectAttribute("focusService") instanceof VerifyService) {
			byte[] verifyImage = cottySession.getBytesAttribute("verifyImage");
			String verifyCode = this.lifecycleListener.onVerify(verifyImage);
			cottySession.addAttribute("verifyCode", verifyCode);
			new LoginService(this.httpClient, cottySession).execute(this);
		} else if (cottySession.getObjectAttribute("focusService") instanceof LoginService) {
			logger.info("login success nick={}",
					cottySession.getStringAttribute("nick"));
			new RedirectService(this.httpClient, cottySession).execute(this);
		} else if (cottySession.getObjectAttribute("focusService") instanceof RedirectService) {
			logger.info("redirect service complete...");
			CookieStore cookieStore = this.httpClient.getCookieStore();
			for (HttpCookie httpCookie : cookieStore.getCookies()) {
				cottySession.addAttribute(httpCookie.getName(),
						httpCookie.getValue());
			}
			new AuthoriseService(this.httpClient, cottySession).execute(this);
			;
		} else if (cottySession.getObjectAttribute("focusService") instanceof AuthoriseService) {
			logger.info("authorise service complete...");
			CottyManager cottyManager = new CottyManager(cottySession, this);
			this.cottyManagerMap.put(
					cottySession.getStringAttribute("account"), cottyManager);
			// 初始化
			new InitializeService(this.httpClient, cottySession).execute(this);
			// 获取vfwebqq
			// new VfwebqqService(this.httpClient, cottySession).execute(this);
			new FriendService(this.httpClient, cottySession).execute(this);
			new GroupService(this.httpClient, cottySession).execute(this);
			new DiscussService(this.httpClient, cottySession).execute(this);
			// 保持在线
			new PollService(this.httpClient, cottySession).execute(this);
		} else if (cottySession.getObjectAttribute("focusService") instanceof InitializeService) {
			logger.info("initialize service complete...");
			String account = cottySession.getStringAttribute("account");
			CottyManager cottyManager = this.cottyManagerMap.get(account)
					.setAccount(account)
					.setCountry(cottySession.getStringAttribute("country"))
					.setCity(cottySession.getStringAttribute("city"))
					.setNick(cottySession.getStringAttribute("nick"))
					.setSignature(cottySession.getStringAttribute("signature"));
			if (syncFlag == 3) {
				this.lifecycleListener.onInitia(cottyManager);
			} else {
				syncFlag++;
			}

		} else if (cottySession.getObjectAttribute("focusService") instanceof VfwebqqService) {
			logger.info("vfwebqq service complete...");
			new FriendService(this.httpClient, cottySession).execute(this);
			new GroupService(this.httpClient, cottySession).execute(this);
			new DiscussService(this.httpClient, cottySession).execute(this);

		} else if (cottySession.getObjectAttribute("focusService") instanceof FriendService) {
			logger.info("friend service complete...");
			CottyManager cottyManager = this.cottyManagerMap.get(cottySession
					.getStringAttribute("account"));
			cottyManager.setCottyFriend((Map<Long, CottyFriend>) cottySession
					.getObjectAttribute("cottyFriend"));
			new StatusService(this.httpClient, cottySession).execute(this);
		} else if (cottySession.getObjectAttribute("focusService") instanceof GroupService) {
			logger.info("group service complete...");
			CottyManager cottyManager = this.cottyManagerMap.get(
					cottySession.getStringAttribute("account")).setCottyGroup(
					(Map<Long, CottyGroup>) cottySession
							.getObjectAttribute("cottyGroup"));
			if (syncFlag == 3) {
				this.lifecycleListener.onInitia(cottyManager);
			} else {
				syncFlag++;
			}
		} else if (cottySession.getObjectAttribute("focusService") instanceof DiscussService) {
			logger.info("discuss service complete...");
			CottyManager cottyManager = this.cottyManagerMap.get(
					cottySession.getStringAttribute("account"))
					.setDiscussGroup(
							(Map<Long, DiscussGroup>) cottySession
									.getObjectAttribute("discussGroup"));
			if (syncFlag == 3) {
				this.lifecycleListener.onInitia(cottyManager);
			} else {
				syncFlag++;
			}
		} else if (cottySession.getObjectAttribute("focusService") instanceof StatusService) {
			CottyManager cottyManager = this.cottyManagerMap.get(cottySession
					.getStringAttribute("account"));
			if (syncFlag == 3) {
				this.lifecycleListener.onInitia(cottyManager);
			} else {
				syncFlag++;
			}
		} else if (cottySession.getObjectAttribute("focusService") instanceof PollService) {
			logger.info("poll complete...");
			if (cottySession.getStringAttribute("action").equals("nornal")) {
				CottyManager cottyManager = this.cottyManagerMap
						.get(cottySession.getStringAttribute("account"));
				List<CottyMessage> cottyMessageList = (List<CottyMessage>) cottySession
						.getObjectAttribute("cottyMessage");
				List<CottyEvent> cottyEventList = (List<CottyEvent>) cottySession
						.getObjectAttribute("cottyEvent");
				for (CottyMessage cottyMessage : cottyMessageList) {
					this.runtimeListener.onReceive(cottyManager, cottyMessage);
				}
				for (CottyEvent cottyEvent : cottyEventList) {

					if (cottyEvent.getType() == CottyEvent.Type.chenge) {
						Long changeUIN = cottyEvent
								.getLongAttribute("changeUIN");
						CottyStatus changeStatus = (CottyStatus) cottyEvent
								.getObjectAttribute("changeStatus");
						CottyFriend cottyFriend = cottyManager.getCottyFriend().get(changeUIN);
						if(cottyFriend != null){
							this.lifecycleListener.onChange(cottyManager,
									changeUIN, changeStatus);
							cottyFriend.setCottyStatus(changeStatus);
						}
								
					} else if (cottyEvent.getType() == CottyEvent.Type.input) {
						Long inputUIN = cottyEvent.getLongAttribute("inputUIN");
						this.runtimeListener.onInput(cottyManager, inputUIN);
					} else if (cottyEvent.getType() == CottyEvent.Type.kickout) {
						Long adminUIN = cottyEvent.getLongAttribute("adminUIN");
						String groupCode = cottyEvent
								.getStringAttribute("groupCode");
						this.runtimeListener.onKickout(cottyManager, groupCode,
								adminUIN);
					} else if (cottyEvent.getType() == CottyEvent.Type.approve) {
						Long adminUIN = cottyEvent.getLongAttribute("adminUIN");
						String groupCode = cottyEvent
								.getStringAttribute("groupCode");
						this.runtimeListener.onApprove(cottyManager, groupCode,
								adminUIN);
					} else if (cottyEvent.getType() == CottyEvent.Type.request) {
						String requestAccount = cottyEvent
								.getStringAttribute("account");
						String message = cottyEvent
								.getStringAttribute("message");
						RuntimeListener.Response response = this.runtimeListener
								.onRequest(cottyManager, requestAccount,
										message);
						if (response.getResult() == RuntimeListener.Response.Result.approve) {
							cottySession.addAttribute("approveAccount",
									response.getAccount()).addAttribute(
									"approveMarkName", response.getMessage());
							new ApproveService(this.httpClient, cottySession)
									.execute(this);
						} else if (response.getResult() == RuntimeListener.Response.Result.agree) {
							cottySession.addAttribute("agreeAccount",
									response.getAccount());
							new AgreeService(this.httpClient, cottySession)
									.execute(this);
						} else if (response.getResult() == RuntimeListener.Response.Result.reject) {
							cottySession.addAttribute("rejectAccount",
									response.getAccount()).addAttribute(
									"rejectMessage", response.getMessage());
							new RejectService(this.httpClient, cottySession)
									.execute(this);
						}

					} else if (cottyEvent.getType() == CottyEvent.Type.revoke) {
						Long adminUIN = cottyEvent.getLongAttribute("adminUIN");
						String groupCode = cottyEvent
								.getStringAttribute("groupCode");
						this.runtimeListener.onReveke(cottyManager, groupCode,
								adminUIN);

					} else if (cottyEvent.getType() == CottyEvent.Type.logout) {
						String message = cottyEvent
								.getStringAttribute("reason");
						this.lifecycleListener.onLogout(cottyManager, message);
						return;
					}
				}

			} else if (cottySession.getStringAttribute("action").equals(
					"changep")) {
				String ptwebqq = cottySession.getStringAttribute("ptwebqq");
				this.httpClient.getCookieStore().add(URI.create("/"),
						new HttpCookie("ptwebqq", ptwebqq));
			} else if (cottySession.getStringAttribute("action").equals(
					"timeout")){
					
			} else {
				return;
			}

			CottyService cottyService = (CottyService) cottySession
					.getObjectAttribute("focusService");
			cottyService.execute(this);

		}

	}

	public LifecycleListener getLifecycleListener() {
		return lifecycleListener;
	}

	public CottyDisparcher setLifecycleListener(
			LifecycleListener lifecycleListener) {
		this.lifecycleListener = lifecycleListener;
		return this;
	}

	public RuntimeListener getRuntimeListener() {
		return runtimeListener;
	}

	public CottyDisparcher setRuntimeListener(RuntimeListener runtimeListener) {
		this.runtimeListener = runtimeListener;
		return this;
	}

	public void onNeedSend(CottyManager cottyManager, CottyMessage cottyMessage) {

		// if(cottyMessage instanceof FriendMessage){
		// FriendMessageService friendMessageService = new
		// FriendMessageService(this.httpClient ,
		// cottyManager.getCottySession());
		// friendMessageService.setFriendMessage((FriendMessage) cottyMessage);
		// friendMessageService.execute(this);
		// }else if(cottyMessage instanceof GroupMessage){
		// GroupMessageService groupMessageService = new
		// GroupMessageService(this.httpClient ,
		// cottyManager.getCottySession());
		// groupMessageService.setGroupMessage((GroupMessage) cottyMessage);
		// groupMessageService.execute(this);
		// }else if(cottyMessage instanceof DiscussMessage){
		// DiscussMessageService discussMessageService = new
		// DiscussMessageService(this.httpClient ,
		// cottyManager.getCottySession());
		// discussMessageService.setDiscussMessage((DiscussMessage)
		// cottyMessage);
		// discussMessageService.execute(this);
		// }
		MessageService messageService = new MessageService(this.httpClient,
				cottyManager.getCottySession());
		messageService.setCottyMessage(cottyMessage).execute(this);
	}

}
