package org.ghuazo.cotty.core;

import org.ghuazo.cotty.core.entity.CottyMessage;

public interface RuntimeListener {

	public static class Response {
		public static enum Result {
			agree, reject, approve
		}

		private String account;
		private String message;
		private Result result ; 
		public Response(String account, Result result) {
			this.account = account;
			this.result = result;
		}
		public Response setMessage(String message){
			this.message = message ; 
			return this ; 
		}
		public Result getResult() {
			return this.result;
		}
		public String getAccount(){
			return this.account ; 
		}
		public String getMessage(){
			return this.message ; 
		}
	}

	/**
	 * 收到消息时候调用
	 * 
	 * @param cottyManager
	 * @param cottyMessage
	 */
	public void onReceive(CottyManager cottyManager, CottyMessage cottyMessage);

	/**
	 * 被管理员踢出群时调用
	 * 
	 * @param cottyManager
	 * @param groupCode
	 * @param adminUIN
	 */
	public void onKickout(CottyManager cottyManager, String groupCode,
			Long adminUIN);

	/**
	 * 当用好友请求加好友时调用
	 * 
	 * @param cottyManager
	 * @param UIN
	 * @return
	 */
	public Response onRequest(CottyManager cottyManager, String account,
			String message);

	/**
	 * 当群主给您授权管理员时候调用
	 * 
	 * @param cottyManager
	 * @param groupCode
	 * @param adminUIN
	 */
	public void onApprove(CottyManager cottyManager, String groupCode,
			Long adminUIN);

	/**
	 * 当好友开始编辑发送消息时候调用
	 */
	public void onInput(CottyManager cottyManager, Long inputUIN);

	public void onReveke(CottyManager cottyManager, String groupCode,
			Long adminUIN);

}
