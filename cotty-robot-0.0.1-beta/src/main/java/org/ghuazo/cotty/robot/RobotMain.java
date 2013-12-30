package org.ghuazo.cotty.robot;

import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.ghuazo.cotty.core.CottyConstant.CottyStatus;
import org.ghuazo.cotty.core.CottyDisparcher;
import org.ghuazo.cotty.core.LifecycleListener;
import org.ghuazo.cotty.core.RuntimeListener;

public class RobotMain {

	private static Logger logger = Log.getLogger(RobotMain.class);
	public static void main(String[] args) throws Exception {
		
		LifecycleListener lifecycleListener = new RobotLifecycleListener(); 
		RuntimeListener runtimeListener = new RobotRuntimeListener();
		CottyDisparcher disparcher = new CottyDisparcher(lifecycleListener,runtimeListener);
		disparcher.start();
		//disparcher.join("2075955131", "cottydev", CottyStatus.online);
		
	}

}
