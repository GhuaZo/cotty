package org.ghuazo.cotty.spring;

import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.ghuazo.cotty.core.CottyDisparcher;
import org.ghuazo.cotty.core.CottyConstant.CottyStatus;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RobotMain {

	private static Logger logger = Log.getLogger(RobotMain.class);
	public static void main(String[] args) throws Exception {
		//LogbackConfigurer.initLogging("classpath:Logback.xml");
		
		ApplicationContext application = new ClassPathXmlApplicationContext("classpath:Application.xml");
	
		CottyDisparcher disparcher = application.getBean("cottyDisparcher", CottyDisparcher.class);
		
		disparcher.start();
		disparcher.join("2075955131", "cottydev", CottyStatus.online);
		
	}

}
