package org.ghuazo.cotty.robot;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.ghuazo.cotty.core.CottyConstant.CottyStatus;
import org.ghuazo.cotty.core.CottyManager;
import org.ghuazo.cotty.core.LifecysleListener;


public class RobotLifecysleListener implements LifecysleListener{

	public String onVerify(byte[] verifyImage) {
		String verifyCode = null ;
		try {
			OutputStream output = new FileOutputStream("d:\\verify.jpg");
			output.write(verifyImage);
			output.close();
			System.out.println("请输入验证码：");
			BufferedReader bufferReader = new BufferedReader(new InputStreamReader(System.in));
			verifyCode = bufferReader.readLine();
			bufferReader.close();
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return verifyCode;
	}

	public void onInitia(CottyManager cottyManager) {
		System.out.println(cottyManager);
		
	}

	public void onChange(CottyManager cottyManager, Long UIN,
			CottyStatus cottyStatus) {
		System.out.println("UIN:"+UIN+"状态改变成"+cottyStatus.getName());
	}

	public void onLogout(CottyManager cottyManager, String message) {
		System.out.println(cottyManager.getAccount()+"Logout");
	}

}
