package org.ghuazo.cotty.core;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.ghuazo.cotty.core.CottyConstant.CottyStatus;
import org.ghuazo.cotty.core.CottyDisparcher;
import org.ghuazo.cotty.core.CottyManager;
import org.ghuazo.cotty.core.LifecysleListener;
import org.ghuazo.cotty.core.RuntimeListener;
import org.ghuazo.cotty.core.entity.CottyFriend;
import org.ghuazo.cotty.core.entity.CottyGroup;
import org.ghuazo.cotty.core.entity.CottyMessage;
import org.ghuazo.cotty.core.entity.DiscussGroup;
import org.ghuazo.cotty.core.entity.DiscussMessage;
import org.ghuazo.cotty.core.entity.FriendMessage;
import org.ghuazo.cotty.core.entity.GroupMessage;

public class CottyMain implements RuntimeListener, LifecysleListener {

	/**
	 * 本框架来源于QQ群 93772282，本框架持续更新中，获取最新版本请加入群 93772282
	 */
	
	private static final Logger logger = Log.getLogger(CottyMain.class);

	public CottyDisparcher dis = null ;
	public static void main(String[] args) throws Exception {
		CottyMain cottyMain = new CottyMain();
		cottyMain.dis = new CottyDisparcher(cottyMain,cottyMain);
		cottyMain.dis.start();
		cottyMain.dis.join("137336521", "密码", CottyStatus.online);
		
	}

	public String onVerify(byte[] verifyImage) {
		try {
			java.io.OutputStream out = new FileOutputStream("D:\\"+new Date().getTime()+".png");
			out.write(verifyImage);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("请输入验证码");
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));
		String verifyCode = null;
		try {
			verifyCode = reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(verifyCode);
		return verifyCode;
	}

	public void onInitia(CottyManager cottyManager) {
		System.out.println(cottyManager);
		Map<Long,CottyFriend> friendMap = cottyManager.getCottyFriend() ; 
		Map<Long,CottyGroup> groupMap = cottyManager.getCottyGroup();
		Map<Long,DiscussGroup> discussMap = cottyManager.getDiscussGroup() ;
		
		System.out.println("----------------------FRIEND----------------------");
		Iterator<CottyFriend> friendIterator = friendMap.values().iterator() ;
		while(friendIterator.hasNext()){
			CottyFriend friend = friendIterator.next();
			System.out.append("uin:"+friend.getUIN()+"\n");
		}
		System.out.println("----------------------GROUP----------------------");
		Iterator<CottyGroup> groupIterator = groupMap.values().iterator() ;
		while(groupIterator.hasNext()){
			CottyGroup group = groupIterator.next();
			System.out.append("code:"+group.getCode()+"\n");
		}
		System.out.println("----------------------DISCUSS----------------------");
		Iterator<DiscussGroup> discussIterator = discussMap.values().iterator() ;
		while(discussIterator.hasNext()){
			DiscussGroup discuss = discussIterator.next();
			System.out.append("did:"+discuss.getDID()+"\n");
		}
		
		System.out.println();
	
		
	}

	public void onChange(CottyManager cottyManager, Long UIN, 
			CottyStatus cottyStatus) {
		System.out.println("uin:"+UIN+"状态改为"+cottyStatus.getName());
	}

	public void onLogout(CottyManager cottyManager, String message) {
		// TODO Auto-generated method stub
		System.out.println(message);
	}

	public void onReceive(CottyManager cottyManager, CottyMessage cottyMessage) {
		System.out.print("TYPE:"+cottyMessage.getType().name()+"MES:"+cottyMessage.getContent());
		
		if(cottyMessage instanceof FriendMessage){
			FriendMessage friendMessage = (FriendMessage) cottyMessage ;
			System.out.println("UIN:"+friendMessage.getUIN());
			cottyManager.sendFriendMessage(friendMessage);
		}else if(cottyMessage instanceof GroupMessage){
			GroupMessage groupMessage = (GroupMessage) cottyMessage ;
			System.out.println("UIN:"+groupMessage.getGroupUIN()+"GID:"+groupMessage.getAuthorUIN());
			//cottyManager.sendGroupMessage(groupMessage);
		}else if(cottyMessage instanceof DiscussMessage){
			DiscussMessage discussMessage = (DiscussMessage) cottyMessage ;
			System.out.println("UIN:"+discussMessage.getAuthorUIN()+"DID:"+discussMessage.getDID());
			cottyManager.sendDiscussMessage(discussMessage);
			
		}
		
	}

	public void onKickout(CottyManager cottyManager, String groupCode,
			Long adminUIN) {
		System.out.println(groupCode+"["+adminUIN+"]把你提出群");
	}

	public Response onRequest(CottyManager cottyManager, String account,
			String message) {
		System.out.println("UIN["+account+"]MSG:"+message);
		Response response = new Response(account,Response.Result.approve).setMessage("我是我");
		return response;
	}

	public void onApprove(CottyManager cottyManager, String groupCode,
			Long adminUIN) {
		System.out.println(groupCode+"["+adminUIN+"]授权你为管理员");
		
	}

	public void onInput(CottyManager cottyManager, Long inputUIN) {
		System.out.println("UIN["+inputUIN+"]正在输入");
		
	}

	public void onReveke(CottyManager cottyManager, String groupCode,
			Long adminUIN) {
		System.out.println(groupCode+"["+adminUIN+"]撤销您管理员权限");
	}

	


	

}
