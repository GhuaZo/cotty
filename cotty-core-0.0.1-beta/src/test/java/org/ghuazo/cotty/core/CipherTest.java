package org.ghuazo.cotty.core;

import org.ghuazo.cotty.core.util.PassWordUtil;
import org.ghuazo.cotty.core.util.DataTypeUtil;

public class CipherTest {

	public static void main(String[] args) {
		PassWordUtil cipher = new PassWordUtil();
		byte[] verifyKey = DataTypeUtil
				.string2ByteArray("\\x00\\x00\\x00\\x00\\x73\\xaf\\xee\\xf2");
		String s = cipher.encrypt(verifyKey, "jh2dd13+14", "!JIP");
		System.out.println(s);
	}

}
