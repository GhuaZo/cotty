package org.ghuazo.cotty.core.util;

public class DataTypeUtil {

	public static byte[] string2ByteArray(String string) {
		String[] stringArray = string.split("\\\\x");
		byte[] byteArray = new byte[stringArray.length - 1];
		for (int i = 0; i < stringArray.length - 1; i++) {
			byteArray[i] = getByte(Integer.parseInt(stringArray[i + 1], 16));
		}
		return byteArray;
	}

	public static byte getByte(int intValue) {
		int byteValue = 0;
		int temp = intValue % 256;
		if (intValue < 0) {
			byteValue = temp < -128 ? 256 + temp : temp;
		} else {
			byteValue = temp > 127 ? temp - 256 : temp;
		}
		return (byte) byteValue;
	}
}
