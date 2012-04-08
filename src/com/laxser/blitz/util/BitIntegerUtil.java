package com.laxser.blitz.util;

public class BitIntegerUtil {

	/**
	 * 获取 @Param i 转化成二进制后从左到右 @Param pos 的值
	 * 
	 * @param i
	 * @param pos
	 * @return 0 / 1
	 */
	public static int getPosValue(int i, int pos) {
		String bs = Integer.toBinaryString(i);
		int bsLen = bs.length();
		if (bsLen < pos) {
			return 0;
		}
		return Integer.valueOf(bs.substring(bsLen - pos - 1, bsLen - pos));
	}
	
	/**
	 * 设置 @param i 在二进制时从左到右 @Param pos 的值为 @param value
	 * @param i
	 * @param pos
	 * @param value 0 / 1
	 * @return
	 */
	public static int setPosValue(int i, int pos, int value) {
		if (value > 1) {
			value = 1;
		}
		String bs = Integer.toBinaryString(i);
		int bsLen = bs.length();
		if (bsLen <= pos) {
			StringBuilder str = new StringBuilder();
			str.append(value);
			for (int j = 0; j < pos - bsLen; j++) {
				str.append("0");
			}
			str.append(bs);
			return Integer.valueOf(str.toString(), 2);
		} else if (bsLen > pos) {
			StringBuilder str = new StringBuilder();
			str.append(bs.substring(0, bsLen - pos - 1));
			str.append(value);
			str.append(bs.substring(bsLen - pos, bsLen));
			return Integer.valueOf(str.toString(), 2);
		}
		return 0;
	}

	public static void main(String[] args) {
		// System.out.println(getPosValue(11,2));
		System.out.println(BitIntegerUtil.setPosValue(0, 0, 1));
	}
}