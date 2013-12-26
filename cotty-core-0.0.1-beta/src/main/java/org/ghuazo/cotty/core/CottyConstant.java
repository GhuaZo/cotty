package org.ghuazo.cotty.core;

public class CottyConstant {

	public enum CottyStatus {
		online(10, "online"), offline(2, "offline"), hidden(40, "hidden"), silent(
				70, "silent"), busy(50, "busy"), away(30, "away"), callme(60,
				"callme");

		private int index;
		private String name;

		private CottyStatus(int index, String name) {
			this.index = index;
			this.name = name;
		}

		public String getName() {
			return this.name;
		}

		public int getIndex() {
			return this.index;
		}

		public static CottyStatus nameOf(String name) {
			if (name.equals("online")) {
				return online;
			} else if (name.equals("online")) {
				return offline;
			} else if (name.equals("hidden")) {
				return hidden;
			} else if (name.equals("silent")) {
				return silent ; 
			} else if (name.equals("busy")) {
				return busy;
			} else if (name.equals("away")) {
				return away;
			} else if (name.equals("callme")) {
				return callme;
			} else{
				return offline;
			}
		}

	}

	
	public static final String APPID = "1003903";

	// public static final String

}
