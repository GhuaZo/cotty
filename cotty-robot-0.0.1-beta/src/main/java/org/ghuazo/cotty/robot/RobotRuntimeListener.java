package org.ghuazo.cotty.robot;

import org.ghuazo.cotty.core.CottyManager;
import org.ghuazo.cotty.core.RuntimeListener;
import org.ghuazo.cotty.core.RuntimeListener.Response.Result;
import org.ghuazo.cotty.core.entity.CottyMessage;
import org.ghuazo.cotty.core.entity.DiscussMessage;
import org.ghuazo.cotty.core.entity.FriendMessage;
import org.ghuazo.cotty.core.entity.GroupMessage;

public class RobotRuntimeListener implements RuntimeListener {

	public void onReceive(CottyManager cottyManager, CottyMessage cottyMessage) {
		if(cottyMessage instanceof FriendMessage){
			FriendMessage friendMessage = (FriendMessage) cottyMessage;
			System.out.println("收到好友信息"+cottyMessage.getContent()+"好友UIN"+friendMessage.getUIN());
			/**
			 * 实现一些逻辑
			 */
			
			cottyManager.sendFriendMessage(friendMessage);
			
		}else if(cottyMessage instanceof GroupMessage){
			GroupMessage groupMessage = (GroupMessage) cottyMessage;
			System.out.println("收到群信息"+cottyMessage.getContent()+"群UIN"+groupMessage.getGroupUIN()+"作者UIN"+groupMessage.getAuthorUIN());
			
		}else if(cottyMessage instanceof DiscussMessage){
			DiscussMessage discussMessage = (DiscussMessage) cottyMessage ; 
			System.out.println("收到谈论组信息"+cottyMessage.getContent()+"DID"+discussMessage.getDID()+"作者UIN"+discussMessage.getAuthorUIN());
			
		}
		
	}

	public void onKickout(CottyManager cottyManager, String groupCode,
			Long adminUIN) {
		System.out.println("管理员"+adminUIN+"把你踢出群"+groupCode);
	}

	public Response onRequest(CottyManager cottyManager, String account,
			String message) {
		
		System.out.println(account+"请求加为好友");
		return new Response(account,Result.approve).setMessage("随便");
	}

	public void onApprove(CottyManager cottyManager, String groupCode,
			Long adminUIN) {
		System.out.println("群"+groupCode+"管理员"+adminUIN+"授权您为管理员");
	}

	public void onInput(CottyManager cottyManager, Long inputUIN) {
		System.out.println("UIN"+inputUIN+"正在输入。。。。。");
	}

	public void onReveke(CottyManager cottyManager, String groupCode,
			Long adminUIN) {
		System.out.println("群"+groupCode+"管理员"+adminUIN+"取消您的管理员权限");
		
	}

}
