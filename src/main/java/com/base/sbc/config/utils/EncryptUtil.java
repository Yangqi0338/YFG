package com.base.sbc.config.utils;

import java.io.UnsupportedEncodingException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

/**
 * 
 *
 * @author shenzhixiong
 * @email 731139982@qq.com
 * @date 2023年7月26日
 */
public class EncryptUtil {

	private EncryptUtil() {

	}

	public static final String PASSWD_REGENT = "Passwd-Regent";

	/**
	 * Encrypt data with key.
	 *
	 * @param data
	 * @param key
	 * @return
	 */
	public static byte[] encrypt(byte[] data, byte[] key) {
		if (data.length == 0) {
			return data;
		}
		return toByteArray(encrypt(toIntArray(data, true), toIntArray(key, false)), false);
	}

	/**
	 * Decrypt data with key.
	 *
	 * @param data
	 * @param key
	 * @return
	 */
	public static byte[] decrypt(byte[] data, byte[] key) {
		if (data.length == 0) {
			return data;
		}
		return toByteArray(decrypt(toIntArray(data, false), toIntArray(key, false)), true);
	}

	/**
	 * Encrypt data with key.
	 *
	 * @param v
	 * @param k
	 * @return
	 */
	public static int[] encrypt(int[] v, int[] k) {
		int n = v.length - 1;

		if (n < 1) {
			return v;
		}
		if (k.length < 4) {
			int[] key = new int[4];

			System.arraycopy(k, 0, key, 0, k.length);
			k = key;
		}
		int z = v[n], y = v[0], delta = 0x9E3779B9, sum = 0, e;
		int p, q = 6 + 52 / (n + 1);

		while (q-- > 0) {
			sum = sum + delta;
			e = sum >>> 2 & 3;
			for (p = 0; p < n; p++) {
				y = v[p + 1];
				z = v[p] += (z >>> 5 ^ y << 2) + (y >>> 3 ^ z << 4) ^ (sum ^ y) + (k[p & 3 ^ e] ^ z);
			}
			y = v[0];
			z = v[n] += (z >>> 5 ^ y << 2) + (y >>> 3 ^ z << 4) ^ (sum ^ y) + (k[p & 3 ^ e] ^ z);
		}
		return v;
	}

	/**
	 * Decrypt data with key.
	 *
	 * @param v
	 * @param k
	 * @return
	 */
	public static int[] decrypt(int[] v, int[] k) {
		int n = v.length - 1;

		if (n < 1) {
			return v;
		}
		if (k.length < 4) {
			int[] key = new int[4];

			System.arraycopy(k, 0, key, 0, k.length);
			k = key;
		}
		int z = v[n], y = v[0], delta = 0x9E3779B9, sum, e;
		int p, q = 6 + 52 / (n + 1);

		sum = q * delta;
		while (sum != 0) {
			e = sum >>> 2 & 3;
			for (p = n; p > 0; p--) {
				z = v[p - 1];
				y = v[p] -= (z >>> 5 ^ y << 2) + (y >>> 3 ^ z << 4) ^ (sum ^ y) + (k[p & 3 ^ e] ^ z);
			}
			z = v[n];
			y = v[0] -= (z >>> 5 ^ y << 2) + (y >>> 3 ^ z << 4) ^ (sum ^ y) + (k[p & 3 ^ e] ^ z);
			sum = sum - delta;
		}
		return v;
	}

	/**
	 * Convert byte array to int array.
	 *
	 * @param data
	 * @param includeLength
	 * @return
	 */
	private static int[] toIntArray(byte[] data, boolean includeLength) {
		int n = (((data.length & 3) == 0) ? (data.length >>> 2) : ((data.length >>> 2) + 1));
		int[] result;

		if (includeLength) {
			result = new int[n + 1];
			result[n] = data.length;
		} else {
			result = new int[n];
		}
		n = data.length;
		for (int i = 0; i < n; i++) {
			result[i >>> 2] |= (0x000000ff & data[i]) << ((i & 3) << 3);
		}
		return result;
	}

	/**
	 * Convert int array to byte array.
	 *
	 * @param data
	 * @param includeLength
	 * @return
	 */
	private static byte[] toByteArray(int[] data, boolean includeLength) {
		int n = data.length << 2;

		;
		if (includeLength) {
			int m = data[data.length - 1];

			if (m > n) {
				return null;
			} else {
				n = m;
			}
		}
		byte[] result = new byte[n];

		for (int i = 0; i < n; i++) {
			result[i] = (byte) ((data[i >>> 2] >>> ((i & 3) << 3)) & 0xff);
		}
		return result;
	}

	public static String encrypt(String str) {
		return encrypt(str, PASSWD_REGENT);
	}

	public static String decrypt(String str) {
		return decrypt(str, PASSWD_REGENT);
	}

	/**
	 * 加密字符串
	 *
	 * @param str 传入的明文
	 * @param key 传入的Key
	 * @return 返回密文
	 */
	public static String encrypt(String str, String key) {
		byte[] data = encrypt(str.getBytes(), key.getBytes());
		String s = Encode(data);
		return s;
	}

	public static String encryptE(String str, String key) {
		byte[] keyArray = new byte[16];
		byte[] keyArray1 = key.getBytes();
		for (int i = 1; i <= keyArray.length; i++) {
			if (i <= keyArray1.length) {
                keyArray[i - 1] = keyArray1[i - 1];
            } else {
                break;
            }
		}
		byte[] data = encrypt(str.getBytes(), keyArray);
		String s = Encode(data);
		return s;
	}

	public static String EncryptE2(String content, String key) throws Exception {
		byte[] raw = key.getBytes("utf-8");
		byte[] keyArray = new byte[16];
		for (int i = 1; i <= raw.length; i++) {
			if (i <= keyArray.length) {
				keyArray[i - 1] = raw[i - 1];
			} else {
                break;
            }
		}
		SecretKeySpec skeySpec = new SecretKeySpec(keyArray, "AES");
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");// "算法/模式/补码方式"
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		byte[] encrypted = cipher.doFinal(content.getBytes("utf-8"));

		// 此处使用BASE64做转码功能，同时能起到2次加密的作用。
		return new Base64().encodeToString(encrypted);
	}

	/**
	 * 解密字符串
	 *
	 * @param str 传入的加密字符串
	 * @param key 传入的Key
	 * @return 返回明文
	 */
	public static String decrypt(String str, String key) {
		byte[] str2;
		try {
			str2 = decode(str);
		} catch (Exception e) {
			return null;
		}
		byte[] data = decrypt(str2, key.getBytes());
		String s = new String(data);
		return s;
	}

	private static final byte[] ENCODING_TABLE = { (byte) 'A', (byte) 'B', (byte) 'C', (byte) 'D', (byte) 'E',
			(byte) 'F', (byte) 'G', (byte) 'H', (byte) 'I', (byte) 'J', (byte) 'K', (byte) 'L', (byte) 'M', (byte) 'N',
			(byte) 'O', (byte) 'P', (byte) 'Q', (byte) 'R', (byte) 'S', (byte) 'T', (byte) 'U', (byte) 'V', (byte) 'W',
			(byte) 'X', (byte) 'Y', (byte) 'Z', (byte) 'a', (byte) 'b', (byte) 'c', (byte) 'd', (byte) 'e', (byte) 'f',
			(byte) 'g', (byte) 'h', (byte) 'i', (byte) 'j', (byte) 'k', (byte) 'l', (byte) 'm', (byte) 'n', (byte) 'o',
			(byte) 'p', (byte) 'q', (byte) 'r', (byte) 's', (byte) 't', (byte) 'u', (byte) 'v', (byte) 'w', (byte) 'x',
			(byte) 'y', (byte) 'z', (byte) '0', (byte) '1', (byte) '2', (byte) '3', (byte) '4', (byte) '5', (byte) '6',
			(byte) '7', (byte) '8', (byte) '9', (byte) '+', (byte) '/' };

	private static final byte[] DECODING_TABLE;

	static {
		DECODING_TABLE = new byte[128];
		for (int i = 0; i < 128; i++) {
			DECODING_TABLE[i] = (byte) -1;
		}
		for (int i = 'A'; i <= 'Z'; i++) {
			DECODING_TABLE[i] = (byte) (i - 'A');
		}
		for (int i = 'a'; i <= 'z'; i++) {
			DECODING_TABLE[i] = (byte) (i - 'a' + 26);
		}
		for (int i = '0'; i <= '9'; i++) {
			DECODING_TABLE[i] = (byte) (i - '0' + 52);
		}
		DECODING_TABLE['+'] = 62;
		DECODING_TABLE['/'] = 63;
	}

	public static String Encode(byte[] data) {
		byte[] result = encode(data);
		return new String(result);
	}

	public static byte[] encode(byte[] data) {
		byte[] bytes;
		int modulus = data.length % 3;
		if (modulus == 0) {
			bytes = new byte[(4 * data.length) / 3];
		} else {
			bytes = new byte[4 * ((data.length / 3) + 1)];
		}
		int dataLength = (data.length - modulus);
		int a1;
		int a2;
		int a3;
		for (int i = 0, j = 0; i < dataLength; i += 3, j += 4) {
			a1 = data[i] & 0xff;
			a2 = data[i + 1] & 0xff;
			a3 = data[i + 2] & 0xff;
			bytes[j] = ENCODING_TABLE[(a1 >>> 2) & 0x3f];
			bytes[j + 1] = ENCODING_TABLE[((a1 << 4) | (a2 >>> 4)) & 0x3f];
			bytes[j + 2] = ENCODING_TABLE[((a2 << 2) | (a3 >>> 6)) & 0x3f];
			bytes[j + 3] = ENCODING_TABLE[a3 & 0x3f];
		}
		int b1;
		int b2;
		int b3;
		int d1;
		int d2;
		switch (modulus) {
		case 0: /* nothing left to do */
			break;
		case 1:
			d1 = data[data.length - 1] & 0xff;
			b1 = (d1 >>> 2) & 0x3f;
			b2 = (d1 << 4) & 0x3f;
			bytes[bytes.length - 4] = ENCODING_TABLE[b1];
			bytes[bytes.length - 3] = ENCODING_TABLE[b2];
			bytes[bytes.length - 2] = (byte) '=';
			bytes[bytes.length - 1] = (byte) '=';
			break;
		case 2:
			d1 = data[data.length - 2] & 0xff;
			d2 = data[data.length - 1] & 0xff;
			b1 = (d1 >>> 2) & 0x3f;
			b2 = ((d1 << 4) | (d2 >>> 4)) & 0x3f;
			b3 = (d2 << 2) & 0x3f;
			bytes[bytes.length - 4] = ENCODING_TABLE[b1];
			bytes[bytes.length - 3] = ENCODING_TABLE[b2];
			bytes[bytes.length - 2] = ENCODING_TABLE[b3];
			bytes[bytes.length - 1] = (byte) '=';
			break;
		default:
			break;
		}
		return bytes;
	}

	public static byte[] decode(byte[] data) {
		byte[] bytes;
		byte b1;
		byte b2;
		byte b3;
		byte b4;
		data = discardNonBase64Bytes(data);
		if (data[data.length - 2] == '=') {
			bytes = new byte[(((data.length / 4) - 1) * 3) + 1];
		} else if (data[data.length - 1] == '=') {
			bytes = new byte[(((data.length / 4) - 1) * 3) + 2];
		} else {
			bytes = new byte[((data.length / 4) * 3)];
		}
		for (int i = 0, j = 0; i < (data.length - 4); i += 4, j += 3) {
			b1 = DECODING_TABLE[data[i]];
			b2 = DECODING_TABLE[data[i + 1]];
			b3 = DECODING_TABLE[data[i + 2]];
			b4 = DECODING_TABLE[data[i + 3]];
			bytes[j] = (byte) ((b1 << 2) | (b2 >> 4));
			bytes[j + 1] = (byte) ((b2 << 4) | (b3 >> 2));
			bytes[j + 2] = (byte) ((b3 << 6) | b4);
		}
		if (data[data.length - 2] == '=') {
			b1 = DECODING_TABLE[data[data.length - 4]];
			b2 = DECODING_TABLE[data[data.length - 3]];
			bytes[bytes.length - 1] = (byte) ((b1 << 2) | (b2 >> 4));
		} else if (data[data.length - 1] == '=') {
			b1 = DECODING_TABLE[data[data.length - 4]];
			b2 = DECODING_TABLE[data[data.length - 3]];
			b3 = DECODING_TABLE[data[data.length - 2]];
			bytes[bytes.length - 2] = (byte) ((b1 << 2) | (b2 >> 4));
			bytes[bytes.length - 1] = (byte) ((b2 << 4) | (b3 >> 2));
		} else {
			b1 = DECODING_TABLE[data[data.length - 4]];
			b2 = DECODING_TABLE[data[data.length - 3]];
			b3 = DECODING_TABLE[data[data.length - 2]];
			b4 = DECODING_TABLE[data[data.length - 1]];
			bytes[bytes.length - 3] = (byte) ((b1 << 2) | (b2 >> 4));
			bytes[bytes.length - 2] = (byte) ((b2 << 4) | (b3 >> 2));
			bytes[bytes.length - 1] = (byte) ((b3 << 6) | b4);
		}
		return bytes;
	}

	public static byte[] decode(String data) {
		byte[] bytes;
		byte b1;
		byte b2;
		byte b3;
		byte b4;
		data = discardNonBase64Chars(data);
		if (data.charAt(data.length() - 2) == '=') {
			bytes = new byte[(((data.length() / 4) - 1) * 3) + 1];
		} else if (data.charAt(data.length() - 1) == '=') {
			bytes = new byte[(((data.length() / 4) - 1) * 3) + 2];
		} else {
			bytes = new byte[((data.length() / 4) * 3)];
		}
		for (int i = 0, j = 0; i < (data.length() - 4); i += 4, j += 3) {
			b1 = DECODING_TABLE[data.charAt(i)];
			b2 = DECODING_TABLE[data.charAt(i + 1)];
			b3 = DECODING_TABLE[data.charAt(i + 2)];
			b4 = DECODING_TABLE[data.charAt(i + 3)];
			bytes[j] = (byte) ((b1 << 2) | (b2 >> 4));
			bytes[j + 1] = (byte) ((b2 << 4) | (b3 >> 2));
			bytes[j + 2] = (byte) ((b3 << 6) | b4);
		}
		if (data.charAt(data.length() - 2) == '=') {
			b1 = DECODING_TABLE[data.charAt(data.length() - 4)];
			b2 = DECODING_TABLE[data.charAt(data.length() - 3)];
			bytes[bytes.length - 1] = (byte) ((b1 << 2) | (b2 >> 4));
		} else if (data.charAt(data.length() - 1) == '=') {
			b1 = DECODING_TABLE[data.charAt(data.length() - 4)];
			b2 = DECODING_TABLE[data.charAt(data.length() - 3)];
			b3 = DECODING_TABLE[data.charAt(data.length() - 2)];
			bytes[bytes.length - 2] = (byte) ((b1 << 2) | (b2 >> 4));
			bytes[bytes.length - 1] = (byte) ((b2 << 4) | (b3 >> 2));
		} else {
			b1 = DECODING_TABLE[data.charAt(data.length() - 4)];
			b2 = DECODING_TABLE[data.charAt(data.length() - 3)];
			b3 = DECODING_TABLE[data.charAt(data.length() - 2)];
			b4 = DECODING_TABLE[data.charAt(data.length() - 1)];
			bytes[bytes.length - 3] = (byte) ((b1 << 2) | (b2 >> 4));
			bytes[bytes.length - 2] = (byte) ((b2 << 4) | (b3 >> 2));
			bytes[bytes.length - 1] = (byte) ((b3 << 6) | b4);
		}
		return bytes;
	}

	private static byte[] discardNonBase64Bytes(byte[] data) {
		byte[] temp = new byte[data.length];
		int bytesCopied = 0;
        for (byte datum : data) {
            if (isValidBase64Byte(datum)) {
                temp[bytesCopied++] = datum;
            }
        }
		byte[] newData = new byte[bytesCopied];
		System.arraycopy(temp, 0, newData, 0, bytesCopied);
		return newData;
	}

	private static String discardNonBase64Chars(String data) {
		StringBuilder sb = new StringBuilder();
		int length = data.length();
		for (int i = 0; i < length; i++) {
			if (isValidBase64Byte((byte) (data.charAt(i)))) {
				sb.append(data.charAt(i));
			}
		}
		return sb.toString();
	}

	private static boolean isValidBase64Byte(byte b) {
		if (b == '=') {
			return true;
		} else if ((b < 0) || (b >= 128)) {
			return false;
		} else if (DECODING_TABLE[b] == -1) {
			return false;
		}
		return true;
	}

	/**
	 * AES加密，算法结果和MYSQL相同 MYSQL语句：SELECT HEX(AES_ENCRYPT('123456','Passwd-Regent'));
	 */
	public static String encryptByAES(String password) {
		String strKey = PASSWD_REGENT;
		try {
			SecretKey key = generateMySQLAESKey(strKey, "ASCII");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			byte[] cleartext = password.getBytes("UTF-8");
			byte[] ciphertextBytes = cipher.doFinal(cleartext);
			return new String(Hex.encodeHex(ciphertextBytes));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * AES解密，算法结果和MYSQL相同 MYSQL语句：SELECT
	 * AES_DECRYPT(UNHEX('4623feca203dd12ba107d35686ab06f4'),'Passwd-Regent');
	 */
	public static String decryptByAES(String sSrc) {
		String strKey = PASSWD_REGENT;
		try {
			SecretKey key = generateMySQLAESKey(strKey, "ASCII");
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] cleartext = Hex.decodeHex(sSrc.toCharArray());
			byte[] ciphertextBytes = cipher.doFinal(cleartext);
			return new String(ciphertextBytes, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static SecretKeySpec generateMySQLAESKey(final String key, final String encoding) {
		try {
			final byte[] finalKey = new byte[16];
			int i = 0;
			for (byte b : key.getBytes(encoding)) {
				finalKey[i++ % 16] ^= b;
			}
			return new SecretKeySpec(finalKey, "AES");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}

}
