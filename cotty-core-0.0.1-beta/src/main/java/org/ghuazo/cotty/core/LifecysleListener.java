package org.ghuazo.cotty.core;

import org.ghuazo.cotty.core.CottyConstant.CottyStatus;

public interface LifecysleListener {
	
	public String onVerify(byte[] verifyImage);
	
	public void onInitia(CottyManager cottyManager) ; 
	
	public void onChange(CottyManager cottyManager , Long UIN , CottyStatus cottyStatus);

	public void onLogout(CottyManager cottyManager ,String message) ; 
}
