package com.dailystudy.swinglab.service.framework.utils;

/** 
 * Base64 클래스 
 * Base64 관련된 유틸리티 
 */	
public class Base64 {

	private static final char[] BASE64CHAR = {
		'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
		'X', 'Y', 'Z',
		'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
		'x', 'y', 'z',
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'
	};
	private static final byte[] BASE64VAL = {
		-99, -99, -99, -99, -99, -99, -99, -99, -99, -99, // ASCII value from 0 to 9
		-98, -99, -99, -98, -99, -99, -99, -99, -99, -99, // ASCII value from 10 to 19
		-99, -99, -99, -99, -99, -99, -99, -99, -99, -99, // ASCII value from 20 to 29
		-99, -99, -99, -99, -99, -99, -99, -99, -99, -99, // ASCII value from 30 to 39
		-99, -99, -99, 62, -99, -99, -99, 63, 52, 53, // ASCII value from 40 to 49
		54, 55, 56, 57, 58, 59, 60, 61, -99, -99, // ASCII value from 50 to 59
		-99, -1, -99, -99, -99, 0, 1, 2, 3, 4, // ASCII value from 60 to 69
		5, 6, 7, 8, 9, 10, 11, 12, 13, 14, // ASCII value from 70 to 79
		15, 16, 17, 18, 19, 20, 21, 22, 23, 24, // ASCII value from 80 to 89
		25, -99, -99, -99, -99, -99, -99, 26, 27, 28, // ASCII value from 90 to 99
		29, 30, 31, 32, 33, 34, 35, 36, 37, 38, // ASCII value from 100 to 109
		39, 40, 41, 42, 43, 44, 45, 46, 47, 48, // ASCII value from 110 to 119
		49, 50, 51, -99, -99, -99, -99, -99, -99, -99, // ASCII value from 120 to 129
		-99, -99, -99, -99, -99, -99, -99, -99, -99, -99, // ASCII value from 130 to 139
		-99, -99, -99, -99, -99, -99, -99, -99, -99, -99, // ASCII value from 140 to 149
		-99, -99, -99, -99, -99, -99, -99, -99, -99, -99, // ASCII value from 150 to 159
		-99, -99, -99, -99, -99, -99, -99, -99, -99, -99, // ASCII value from 160 to 169
		-99, -99, -99, -99, -99, -99, -99, -99, -99, -99, // ASCII value from 170 to 179
		-99, -99, -99, -99, -99, -99, -99, -99, -99, -99, // ASCII value from 180 to 189
		-99, -99, -99, -99, -99, -99, -99, -99, -99, -99, // ASCII value from 190 to 199
		-99, -99, -99, -99, -99, -99, -99, -99, -99, -99, // ASCII value from 200 to 209
		-99, -99, -99, -99, -99, -99, -99, -99, -99, -99, // ASCII value from 210 to 219
		-99, -99, -99, -99, -99, -99, -99, -99, -99, -99, // ASCII value from 220 to 229
		-99, -99, -99, -99, -99, -99, -99, -99, -99, -99, // ASCII value from 230 to 239
		-99, -99, -99, -99, -99, -99, -99, -99, -99, -99, // ASCII value from 240 to 249
		-99, -99, -99, -99, -99, -99 // ASCII value from 250 to 255
	};
	private static final char BASE64PAD = '=';
	public static final char CR = 13; // == \r
	public static final char LF = 10; // == \n

	private Base64() {
	}

	/**
	 * Base64 인코딩.
	 * @param source String
	 * @return String 인코딩된 문자열
	 */
	public static String Encode(String source) {
		return Base64.Encode(source, true);
	}

	/**
	 * Base64 인코딩. 76 자마다 라인브레이크를 할지를 판단하여 인코딩한다.
	 * @param source String
	 * @param line_break 76 자마다 라인브레이크를 할지의 여부
	 * @return String 인코딩된 문자열
	 */
	public static String Encode(String source, boolean line_break) {
		if (source == null) {
			return "";
		}
		byte temp[] = source.getBytes();
		return Base64.Encode(temp, line_break);
	}

	/**
	 * Base64 인코딩.
	 * @param source byte[]
	 * @return 인코딩된 문자열
	 */
	public static String Encode(byte[] source) {
		return Base64.Encode(source, true);
	}

	/**
	 * Base64 인코딩. 76 자마다 라인브레이크를 할지를 판단하여 인코딩한다.
	 * @param source byte[]
	 * @param line_break 76 자마다 라인브레이크를 할지의 여부
	 * @return 인코딩된 문자열
	 */
	public static String Encode(byte[] source, boolean line_break) {
		if (source == null) {
			return "";
		}
		byte temp[] = source;
		int digit_3_length = temp.length / 3;
		int digit_3_remain = temp.length % 3;
		int temp_val;
		int encoded_length = (digit_3_remain > 0) ? (digit_3_length * 4 + 4) : (digit_3_length * 4);
		char[] retval = new char[encoded_length];
		int encoded_index = 0;
		int i = 0;
		String retString;

		for (; i < (digit_3_length * 3); i += 3) { // process step by 3 digit
			// make int value from 3 bytes
			temp_val = (temp[i + 0] & 0xFF) << 16;
			temp_val = temp_val | ((temp[i + 1] & 0xFF) << 8);
			temp_val = temp_val | ((temp[i + 2] & 0xFF));
			// make sure int value only have 24 digit
			temp_val = (temp_val & 0x00FFFFFF);
			retval[encoded_index++] = BASE64CHAR[(((temp_val & 0x00FC0000) >> 18) & 0x0000003F)];
			retval[encoded_index++] = BASE64CHAR[(((temp_val & 0x0003F000) >> 12) & 0x0000003F)];
			retval[encoded_index++] = BASE64CHAR[(((temp_val & 0x00000FC0) >> 6) & 0x0000003F)];
			retval[encoded_index++] = BASE64CHAR[(((temp_val & 0x0000003F)) & 0x0000003F)];
		}
		if (digit_3_remain == 1) { // process padding
			// make int value from 3 bytes
			temp_val = (temp[i + 0] & 0xFF) << 16;
			temp_val = temp_val | (0x00 << 8);
			temp_val = temp_val | 0x00;
			// make sure int value only have 24 digit
			temp_val = (temp_val & 0x00FFFFFF);

			retval[encoded_index++] = BASE64CHAR[(((temp_val & 0x00FC0000) >> 18) & 0x0000003F)];
			retval[encoded_index++] = BASE64CHAR[(((temp_val & 0x0003F000) >> 12) & 0x0000003F)];
			retval[encoded_index++] = BASE64PAD;
			retval[encoded_index++] = BASE64PAD;
		} else if (digit_3_remain == 2) {
			// make int value from 3 bytes
			temp_val = (temp[i + 0] & 0xFF) << 16;
			temp_val = temp_val | ((temp[i + 1] & 0xFF) << 8);
			temp_val = temp_val | 0x00;
			// make sure int value only have 24 digit
			temp_val = (temp_val & 0x00FFFFFF);

			retval[encoded_index++] = BASE64CHAR[(((temp_val & 0x00FC0000) >> 18) & 0x0000003F)];
			retval[encoded_index++] = BASE64CHAR[(((temp_val & 0x0003F000) >> 12) & 0x0000003F)];
			retval[encoded_index++] = BASE64CHAR[(((temp_val & 0x00000FC0) >> 6) & 0x0000003F)];
			retval[encoded_index++] = BASE64PAD;
		}

		temp = null;

		if (line_break == true) {
			int soft_line_break = 76;
			char[] retval_soft_line_break = new char[retval.length + (retval.length / soft_line_break) * 2];
			int insert_count = retval.length / soft_line_break;
			int insert_remain = retval.length % soft_line_break;
			i = 0;
			int insert_index = 0;
			for (; i < insert_count; i++) {
				System.arraycopy(retval, (i * soft_line_break), retval_soft_line_break, insert_index, soft_line_break);
				insert_index += soft_line_break;
				retval_soft_line_break[insert_index] = CR;
				insert_index += 1;
				retval_soft_line_break[insert_index] = LF;
				insert_index += 1;
			}

			if (insert_remain > 0) {
				System.arraycopy(retval, (i * soft_line_break), retval_soft_line_break, insert_index, insert_remain);
			}

			retString = new String(retval_soft_line_break);
			retval = null;
			retval_soft_line_break = null;
		} else {
			retString = new String(retval);
			retval = null;
		}
		return retString;
	}

	/**
	 * Base64 디코딩.
	 * @param source char[]
	 * @return 디코딩된 문자열
	 */
	public static String Decode(char[] source) {
		if (source == null) {
			return "";
		}

		char[] char_temp = source;

		int i;
		for (i = 0; i < char_temp.length; i++) { // validation
			if (char_temp[i] > 255 || BASE64VAL[char_temp[i]] == -99) {
				char_temp = null;
				return "";
			}
		}
		boolean line_break = false;
		byte[] retval = null;
		char[] temp = null;
		int ret_index = 0;

		if (char_temp.length > 76 && char_temp[76] == CR) {
			if (!(char_temp.length > 77 && char_temp[77] == LF)) {
				char_temp = null;
				return "";
			}
			line_break = true;
		}

		if (line_break == true) {
			int soft_line_length = 78;
			int soft_line_break = 76;
			int soft_line_num = char_temp.length / soft_line_length;
			int soft_line_remain = char_temp.length % soft_line_length;
			int removed_soft_line_break_length = soft_line_num * soft_line_break + soft_line_remain;
			temp = new char[removed_soft_line_break_length];

			for (i = 0; i < soft_line_num; i++) { // remove soft_line_break
				System.arraycopy(char_temp, (i * soft_line_length), temp, (i * soft_line_break), soft_line_break);
			}

			if (soft_line_remain > 0) {
				System.arraycopy(char_temp, (i * soft_line_length), temp, (i * soft_line_break), soft_line_remain);
			}

			int original_length = (removed_soft_line_break_length / 4) * 3;
			if (BASE64VAL[temp[temp.length - 1]] == -1 && BASE64VAL[temp[temp.length - 2]] == -1) {
				original_length -= 2;
			} else if (BASE64VAL[temp[temp.length - 1]] == -1) {
				original_length--;
			}
			retval = new byte[original_length];
		} else {
			temp = char_temp;
			int original_length = (temp.length / 4) * 3;
			if (BASE64VAL[temp[temp.length - 1]] == -1 && BASE64VAL[temp[temp.length - 2]] == -1) {
				original_length -= 2;
			} else if (BASE64VAL[temp[temp.length - 1]] == -1) {
				original_length--;
			}
			retval = new byte[original_length];
		}

		int temp_val = 0;

		for (i = 0; i < temp.length - 4; i += 4) {
			temp_val = temp_val & 0x00000000;
			temp_val = temp_val | ((BASE64VAL[temp[i]] & 0x0000003F) << 18);
			temp_val = temp_val | ((BASE64VAL[temp[i + 1]] & 0x0000003F) << 12);
			temp_val = temp_val | ((BASE64VAL[temp[i + 2]] & 0x0000003F) << 6);
			temp_val = temp_val | (BASE64VAL[temp[i + 3]] & 0x0000003F);
			retval[ret_index++] = (byte) ((temp_val & 0x00FF0000) >> 16);
			retval[ret_index++] = (byte) ((temp_val & 0x0000FF00) >> 8);
			retval[ret_index++] = (byte) (temp_val & 0x000000FF);
		}

		temp_val = temp_val & 0x00000000;

		if (BASE64VAL[temp[temp.length - 1]] == -1 && BASE64VAL[temp[temp.length - 2]] == -1) {
			temp_val = temp_val | ((BASE64VAL[temp[i]] & 0x0000003F) << 18);
			temp_val = temp_val | ((BASE64VAL[temp[i + 1]] & 0x0000003F) << 12);
			retval[ret_index++] = (byte) ((temp_val & 0x00FF0000) >> 16);
		} else if (BASE64VAL[temp[temp.length - 1]] == -1) {
			temp_val = temp_val | ((BASE64VAL[temp[i]] & 0x0000003F) << 18);
			temp_val = temp_val | ((BASE64VAL[temp[i + 1]] & 0x0000003F) << 12);
			temp_val = temp_val | ((BASE64VAL[temp[i + 2]] & 0x0000003F) << 6);
			retval[ret_index++] = (byte) ((temp_val & 0x00FF0000) >> 16);
			retval[ret_index++] = (byte) ((temp_val & 0x0000FF00) >> 8);
		} else {
			temp_val = temp_val | ((BASE64VAL[temp[i]] & 0x0000003F) << 18);
			temp_val = temp_val | ((BASE64VAL[temp[i + 1]] & 0x0000003F) << 12);
			temp_val = temp_val | ((BASE64VAL[temp[i + 2]] & 0x0000003F) << 6);
			temp_val = temp_val | (BASE64VAL[temp[i + 3]] & 0x0000003F);
			retval[ret_index++] = (byte) ((temp_val & 0x00FF0000) >> 16);
			retval[ret_index++] = (byte) ((temp_val & 0x0000FF00) >> 8);
			retval[ret_index++] = (byte) (temp_val & 0x000000FF);
		}

		String retVal = new String(retval);
		retval = null;
		char_temp = null;

		return retVal;
	}

	/**
	 * Base64 디코딩.
	 * @param source byte[]
	 * @return 디코딩된 문자열
	 */
	public static String Decode(byte[] source) {
		if (source == null) {
			return "";
		}

		byte[] byte_temp = source;

		int i;
		for (i = 0; i < byte_temp.length; i++) { // validation
			if (BASE64VAL[byte_temp[i]] == -99) {
				byte_temp = null;
				return "";
			}
		}
		boolean line_break = false;
		byte[] retval = null;
		byte[] temp = null;
		int ret_index = 0;
		if (byte_temp.length > 76 && byte_temp[76] == 13) {
			if (!(byte_temp.length > 77 && byte_temp[77] == 10)) {
				byte_temp = null;
				return "";
			}
			line_break = true;
		}

		if (line_break == true) {
			int soft_line_length = 78;
			int soft_line_break = 76;
			int soft_line_num = byte_temp.length / soft_line_length;
			int soft_line_remain = byte_temp.length % soft_line_length;
			int removed_soft_line_break_length = soft_line_num * soft_line_break + soft_line_remain;
			temp = new byte[removed_soft_line_break_length];

			for (i = 0; i < soft_line_num; i++) { // remove soft_line_break
				System.arraycopy(byte_temp, (i * soft_line_length), temp, (i * soft_line_break), soft_line_break);
			}

			if (soft_line_remain > 0) {
				System.arraycopy(byte_temp, (i * soft_line_length), temp, (i * soft_line_break), soft_line_remain);
			}

			int original_length = (removed_soft_line_break_length / 4) * 3;
			if (BASE64VAL[temp[temp.length - 1]] == -1 && BASE64VAL[temp[temp.length - 2]] == -1) {
				original_length -= 2;
			} else if (BASE64VAL[temp[temp.length - 1]] == -1) {
				original_length--;
			}
			retval = new byte[original_length];
		} else {
			temp = byte_temp;
			int original_length = (temp.length / 4) * 3;
			if (BASE64VAL[temp[temp.length - 1]] == -1 && BASE64VAL[temp[temp.length - 2]] == -1) {
				original_length -= 2;
			} else if (BASE64VAL[temp[temp.length - 1]] == -1) {
				original_length--;
			}
			retval = new byte[original_length];
		}

		int temp_val = 0;

		for (i = 0; i < temp.length - 4; i += 4) {
			temp_val = temp_val & 0x00000000;
			temp_val = temp_val | ((BASE64VAL[temp[i]] & 0x0000003F) << 18);
			temp_val = temp_val | ((BASE64VAL[temp[i + 1]] & 0x0000003F) << 12);
			temp_val = temp_val | ((BASE64VAL[temp[i + 2]] & 0x0000003F) << 6);
			temp_val = temp_val | (BASE64VAL[temp[i + 3]] & 0x0000003F);
			retval[ret_index++] = (byte) ((temp_val & 0x00FF0000) >> 16);
			retval[ret_index++] = (byte) ((temp_val & 0x0000FF00) >> 8);
			retval[ret_index++] = (byte) (temp_val & 0x000000FF);
		}

		temp_val = temp_val & 0x00000000;

		if (BASE64VAL[temp[temp.length - 1]] == -1 && BASE64VAL[temp[temp.length - 2]] == -1) {
			temp_val = temp_val | ((BASE64VAL[temp[i]] & 0x0000003F) << 18);
			temp_val = temp_val | ((BASE64VAL[temp[i + 1]] & 0x0000003F) << 12);
			retval[ret_index++] = (byte) ((temp_val & 0x00FF0000) >> 16);
		} else if (BASE64VAL[temp[temp.length - 1]] == -1) {
			temp_val = temp_val | ((BASE64VAL[temp[i]] & 0x0000003F) << 18);
			temp_val = temp_val | ((BASE64VAL[temp[i + 1]] & 0x0000003F) << 12);
			temp_val = temp_val | ((BASE64VAL[temp[i + 2]] & 0x0000003F) << 6);
			retval[ret_index++] = (byte) ((temp_val & 0x00FF0000) >> 16);
			retval[ret_index++] = (byte) ((temp_val & 0x0000FF00) >> 8);
		} else {
			temp_val = temp_val | ((BASE64VAL[temp[i]] & 0x0000003F) << 18);
			temp_val = temp_val | ((BASE64VAL[temp[i + 1]] & 0x0000003F) << 12);
			temp_val = temp_val | ((BASE64VAL[temp[i + 2]] & 0x0000003F) << 6);
			temp_val = temp_val | (BASE64VAL[temp[i + 3]] & 0x0000003F);
			retval[ret_index++] = (byte) ((temp_val & 0x00FF0000) >> 16);
			retval[ret_index++] = (byte) ((temp_val & 0x0000FF00) >> 8);
			retval[ret_index++] = (byte) (temp_val & 0x000000FF);
		}

		return new String(retval);
	}

	/**
	 * Base64 디코딩.
	 * @param source String
	 * @return byte[] 디코딩값
	 */
	public static byte[] Decode(String source) {
		if (source == null) {
			return new byte[0];
		}

		byte[] byte_temp = source.getBytes();

		int i;
		for (i = 0; i < byte_temp.length; i++) { // validation
			if (BASE64VAL[byte_temp[i]] == -99) {
				return new byte[0];
			}
		}

		boolean line_break = false;
		byte[] retval = null;
		byte[] temp = null;
		int ret_index = 0;

		if (byte_temp.length > 76 && byte_temp[76] == 13) {
			if (!(byte_temp.length > 77 && byte_temp[77] == 10)) {
				byte_temp = null;
				return new byte[0];
			}
			line_break = true;
		}

		if (line_break == true) {
			int soft_line_length = 78;
			int soft_line_break = 76;
			int soft_line_num = byte_temp.length / soft_line_length;
			int soft_line_remain = byte_temp.length % soft_line_length;
			int removed_soft_line_break_length = soft_line_num * soft_line_break + soft_line_remain;
			temp = new byte[removed_soft_line_break_length];

			for (i = 0; i < soft_line_num; i++) { // remove soft_line_break
				System.arraycopy(byte_temp, (i * soft_line_length), temp, (i * soft_line_break), soft_line_break);
			}

			if (soft_line_remain > 0) {
				System.arraycopy(byte_temp, (i * soft_line_length), temp, (i * soft_line_break), soft_line_remain);
			}

			int original_length = (removed_soft_line_break_length / 4) * 3;
			if (BASE64VAL[temp[temp.length - 1]] == -1 && BASE64VAL[temp[temp.length - 2]] == -1) {
				original_length -= 2;
			} else if (BASE64VAL[temp[temp.length - 1]] == -1) {
				original_length--;
			}
			retval = new byte[original_length];
		} else {
			temp = byte_temp;
			int original_length = (temp.length / 4) * 3;
			if (BASE64VAL[temp[temp.length - 1]] == -1 && BASE64VAL[temp[temp.length - 2]] == -1) {
				original_length -= 2;
			} else if (BASE64VAL[temp[temp.length - 1]] == -1) {
				original_length--;
			}
			retval = new byte[original_length];
		}

		int temp_val = 0;

		for (i = 0; i < temp.length - 4; i += 4) {
			temp_val = temp_val & 0x00000000;
			temp_val = temp_val | ((BASE64VAL[temp[i]] & 0x0000003F) << 18);
			temp_val = temp_val | ((BASE64VAL[temp[i + 1]] & 0x0000003F) << 12);
			temp_val = temp_val | ((BASE64VAL[temp[i + 2]] & 0x0000003F) << 6);
			temp_val = temp_val | (BASE64VAL[temp[i + 3]] & 0x0000003F);
			retval[ret_index++] = (byte) ((temp_val & 0x00FF0000) >> 16);
			retval[ret_index++] = (byte) ((temp_val & 0x0000FF00) >> 8);
			retval[ret_index++] = (byte) (temp_val & 0x000000FF);
		}

		temp_val = temp_val & 0x00000000;

		if (BASE64VAL[temp[temp.length - 1]] == -1 && BASE64VAL[temp[temp.length - 2]] == -1) {
			temp_val = temp_val | ((BASE64VAL[temp[i]] & 0x0000003F) << 18);
			temp_val = temp_val | ((BASE64VAL[temp[i + 1]] & 0x0000003F) << 12);
			retval[ret_index++] = (byte) ((temp_val & 0x00FF0000) >> 16);
		} else if (BASE64VAL[temp[temp.length - 1]] == -1) {
			temp_val = temp_val | ((BASE64VAL[temp[i]] & 0x0000003F) << 18);
			temp_val = temp_val | ((BASE64VAL[temp[i + 1]] & 0x0000003F) << 12);
			temp_val = temp_val | ((BASE64VAL[temp[i + 2]] & 0x0000003F) << 6);
			retval[ret_index++] = (byte) ((temp_val & 0x00FF0000) >> 16);
			retval[ret_index++] = (byte) ((temp_val & 0x0000FF00) >> 8);
		} else {
			temp_val = temp_val | ((BASE64VAL[temp[i]] & 0x0000003F) << 18);
			temp_val = temp_val | ((BASE64VAL[temp[i + 1]] & 0x0000003F) << 12);
			temp_val = temp_val | ((BASE64VAL[temp[i + 2]] & 0x0000003F) << 6);
			temp_val = temp_val | (BASE64VAL[temp[i + 3]] & 0x0000003F);
			retval[ret_index++] = (byte) ((temp_val & 0x00FF0000) >> 16);
			retval[ret_index++] = (byte) ((temp_val & 0x0000FF00) >> 8);
			retval[ret_index++] = (byte) (temp_val & 0x000000FF);
		}

		return retval;
	}

	/**
	 * Base64 디코딩.
	 * @param source String
	 * @return 디코딩된 문자열
	 */
	public static String DecodeAsString(String source) {
		if (source == null) {
			return "";
		}

		byte[] byte_temp = source.getBytes();

		int i;
		for (i = 0; i < byte_temp.length; i++) { // validation
			if (BASE64VAL[byte_temp[i]] == -99) {
				return "";
			}
		}

		boolean line_break = false;
		byte[] retval = null;
		byte[] temp = null;
		int ret_index = 0;

		if (byte_temp.length > 76 && byte_temp[76] == 13) {
			if (!(byte_temp.length > 77 && byte_temp[77] == 10)) {
				byte_temp = null;
				return "";
			}
			line_break = true;
		}

		if (line_break == true) {
			int soft_line_length = 78;
			int soft_line_break = 76;
			int soft_line_num = byte_temp.length / soft_line_length;
			int soft_line_remain = byte_temp.length % soft_line_length;
			int removed_soft_line_break_length = soft_line_num * soft_line_break + soft_line_remain;
			temp = new byte[removed_soft_line_break_length];

			for (i = 0; i < soft_line_num; i++) { // remove soft_line_break
				System.arraycopy(byte_temp, (i * soft_line_length), temp, (i * soft_line_break), soft_line_break);
			}

			if (soft_line_remain > 0) {
				System.arraycopy(byte_temp, (i * soft_line_length), temp, (i * soft_line_break), soft_line_remain);
			}

			int original_length = (removed_soft_line_break_length / 4) * 3;
			if (BASE64VAL[temp[temp.length - 1]] == -1 && BASE64VAL[temp[temp.length - 2]] == -1) {
				original_length -= 2;
			} else if (BASE64VAL[temp[temp.length - 1]] == -1) {
				original_length--;
			}
			retval = new byte[original_length];
		} else {
			temp = byte_temp;
			int original_length = (temp.length / 4) * 3;
			if (BASE64VAL[temp[temp.length - 1]] == -1 && BASE64VAL[temp[temp.length - 2]] == -1) {
				original_length -= 2;
			} else if (BASE64VAL[temp[temp.length - 1]] == -1) {
				original_length--;
			}
			retval = new byte[original_length];
		}

		int temp_val = 0;

		for (i = 0; i < temp.length - 4; i += 4) {
			temp_val = temp_val & 0x00000000;
			temp_val = temp_val | ((BASE64VAL[temp[i]] & 0x0000003F) << 18);
			temp_val = temp_val | ((BASE64VAL[temp[i + 1]] & 0x0000003F) << 12);
			temp_val = temp_val | ((BASE64VAL[temp[i + 2]] & 0x0000003F) << 6);
			temp_val = temp_val | (BASE64VAL[temp[i + 3]] & 0x0000003F);
			retval[ret_index++] = (byte) ((temp_val & 0x00FF0000) >> 16);
			retval[ret_index++] = (byte) ((temp_val & 0x0000FF00) >> 8);
			retval[ret_index++] = (byte) (temp_val & 0x000000FF);
		}

		temp_val = temp_val & 0x00000000;

		if (BASE64VAL[temp[temp.length - 1]] == -1 && BASE64VAL[temp[temp.length - 2]] == -1) {
			temp_val = temp_val | ((BASE64VAL[temp[i]] & 0x0000003F) << 18);
			temp_val = temp_val | ((BASE64VAL[temp[i + 1]] & 0x0000003F) << 12);
			retval[ret_index++] = (byte) ((temp_val & 0x00FF0000) >> 16);
		} else if (BASE64VAL[temp[temp.length - 1]] == -1) {
			temp_val = temp_val | ((BASE64VAL[temp[i]] & 0x0000003F) << 18);
			temp_val = temp_val | ((BASE64VAL[temp[i + 1]] & 0x0000003F) << 12);
			temp_val = temp_val | ((BASE64VAL[temp[i + 2]] & 0x0000003F) << 6);
			retval[ret_index++] = (byte) ((temp_val & 0x00FF0000) >> 16);
			retval[ret_index++] = (byte) ((temp_val & 0x0000FF00) >> 8);
		} else {
			temp_val = temp_val | ((BASE64VAL[temp[i]] & 0x0000003F) << 18);
			temp_val = temp_val | ((BASE64VAL[temp[i + 1]] & 0x0000003F) << 12);
			temp_val = temp_val | ((BASE64VAL[temp[i + 2]] & 0x0000003F) << 6);
			temp_val = temp_val | (BASE64VAL[temp[i + 3]] & 0x0000003F);
			retval[ret_index++] = (byte) ((temp_val & 0x00FF0000) >> 16);
			retval[ret_index++] = (byte) ((temp_val & 0x0000FF00) >> 8);
			retval[ret_index++] = (byte) (temp_val & 0x000000FF);
		}

		return new String(retval);
	}

	/**
	 * Base64 디코딩.
	 * @param source String
	 * @param charset String
	 * @return 디코딩된 문자열
	 */
	public static String DecodeAsString(String source, String charset) {
		if (source == null) {
			return "";
		}

		byte[] byte_temp = source.getBytes();

		int i;
		for (i = 0; i < byte_temp.length; i++) { // validation
			if (BASE64VAL[byte_temp[i]] == -99) {
				return "";
			}
		}

		boolean line_break = false;
		byte[] retval = null;
		byte[] temp = null;
		int ret_index = 0;

		if (byte_temp.length > 76 && byte_temp[76] == 13) {
			if (!(byte_temp.length > 77 && byte_temp[77] == 10)) {
				byte_temp = null;
				return "";
			}
			line_break = true;
		}

		if (line_break == true) {
			int soft_line_length = 78;
			int soft_line_break = 76;
			int soft_line_num = byte_temp.length / soft_line_length;
			int soft_line_remain = byte_temp.length % soft_line_length;
			int removed_soft_line_break_length = soft_line_num * soft_line_break + soft_line_remain;
			temp = new byte[removed_soft_line_break_length];

			for (i = 0; i < soft_line_num; i++) { // remove soft_line_break
				System.arraycopy(byte_temp, (i * soft_line_length), temp, (i * soft_line_break), soft_line_break);
			}

			if (soft_line_remain > 0) {
				System.arraycopy(byte_temp, (i * soft_line_length), temp, (i * soft_line_break), soft_line_remain);
			}

			int original_length = (removed_soft_line_break_length / 4) * 3;
			if (BASE64VAL[temp[temp.length - 1]] == -1 && BASE64VAL[temp[temp.length - 2]] == -1) {
				original_length -= 2;
			} else if (BASE64VAL[temp[temp.length - 1]] == -1) {
				original_length--;
			}
			retval = new byte[original_length];
		} else {
			temp = byte_temp;
			int original_length = (temp.length / 4) * 3;
			if (BASE64VAL[temp[temp.length - 1]] == -1 && BASE64VAL[temp[temp.length - 2]] == -1) {
				original_length -= 2;
			} else if (BASE64VAL[temp[temp.length - 1]] == -1) {
				original_length--;
			}
			retval = new byte[original_length];
		}

		int temp_val = 0;

		for (i = 0; i < temp.length - 4; i += 4) {
			temp_val = temp_val & 0x00000000;
			temp_val = temp_val | ((BASE64VAL[temp[i]] & 0x0000003F) << 18);
			temp_val = temp_val | ((BASE64VAL[temp[i + 1]] & 0x0000003F) << 12);
			temp_val = temp_val | ((BASE64VAL[temp[i + 2]] & 0x0000003F) << 6);
			temp_val = temp_val | (BASE64VAL[temp[i + 3]] & 0x0000003F);
			retval[ret_index++] = (byte) ((temp_val & 0x00FF0000) >> 16);
			retval[ret_index++] = (byte) ((temp_val & 0x0000FF00) >> 8);
			retval[ret_index++] = (byte) (temp_val & 0x000000FF);
		}

		temp_val = temp_val & 0x00000000;

		if (BASE64VAL[temp[temp.length - 1]] == -1 && BASE64VAL[temp[temp.length - 2]] == -1) {
			temp_val = temp_val | ((BASE64VAL[temp[i]] & 0x0000003F) << 18);
			temp_val = temp_val | ((BASE64VAL[temp[i + 1]] & 0x0000003F) << 12);
			retval[ret_index++] = (byte) ((temp_val & 0x00FF0000) >> 16);
		} else if (BASE64VAL[temp[temp.length - 1]] == -1) {
			temp_val = temp_val | ((BASE64VAL[temp[i]] & 0x0000003F) << 18);
			temp_val = temp_val | ((BASE64VAL[temp[i + 1]] & 0x0000003F) << 12);
			temp_val = temp_val | ((BASE64VAL[temp[i + 2]] & 0x0000003F) << 6);
			retval[ret_index++] = (byte) ((temp_val & 0x00FF0000) >> 16);
			retval[ret_index++] = (byte) ((temp_val & 0x0000FF00) >> 8);
		} else {
			temp_val = temp_val | ((BASE64VAL[temp[i]] & 0x0000003F) << 18);
			temp_val = temp_val | ((BASE64VAL[temp[i + 1]] & 0x0000003F) << 12);
			temp_val = temp_val | ((BASE64VAL[temp[i + 2]] & 0x0000003F) << 6);
			temp_val = temp_val | (BASE64VAL[temp[i + 3]] & 0x0000003F);
			retval[ret_index++] = (byte) ((temp_val & 0x00FF0000) >> 16);
			retval[ret_index++] = (byte) ((temp_val & 0x0000FF00) >> 8);
			retval[ret_index++] = (byte) (temp_val & 0x000000FF);
		}

		try {
			return new String(retval, charset);
		} catch (Exception ex) {
			return new String(retval);
		}
	}
}
