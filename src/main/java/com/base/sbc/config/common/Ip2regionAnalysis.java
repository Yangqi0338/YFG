package com.base.sbc.config.common;

import com.base.sbc.config.utils.StringUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.xdb.Searcher;

/**
 *
 * 通过ip获取地址
 *
 * @author https://www.cnblogs.com/lixingwu https://gitee.com/lionsoul/ip2region
 * @date 2022-05-24 10:45:42
 */
@Slf4j
public class Ip2regionAnalysis {

	private static final String IP_DB_PATH = "/lib/ip2region.xdb";
	private static Searcher searcher = null;
	static {
		initAddress(IP_DB_PATH);
	}

//	public static void main(String[] args) {
//		String[] address = getAddressByIp("180.149.130.16");
//		System.out.println(JsonUtils.beanToJson(address));
//	}

	/**
	 * 通过ip获取地址 中国-广东省-广州市
	 *
	 * @param ip
	 * @return
	 */
	public static String getStringAddressByIp(String ip) {
		return StringUtils.join(getAddressByIp(ip), "-");
	}

	/**
	 * 加载地址文件
	 *
	 * @param dbpath2
	 */
	private static void initAddress(String dbpath2) {
		String path = System.getProperty("user.dir") + IP_DB_PATH;
		// 1、从 dbPath 加载整个 xdb 到内存。
		byte[] cBuff = null;
		try {
			cBuff = Searcher.loadContentFromFile(path);
		} catch (Exception e) {
			log.error("地址库文件不存在");
		}

		// 2、使用上述的 cBuff 创建一个完全基于内存的查询对象。
		try {
			searcher = Searcher.newWithBuffer(cBuff);
		} catch (Exception e) {
			log.error("创建地址库缓存错误: %s\n", e);
		}
	}

	/**
	 * 根据IP获取地址
	 *
	 * @param ip
	 * @return 国家|省份|城市
	 * @see  国家|区域|省份|城市|ISP
	 */
	@SneakyThrows
	public static String[] getAddressByIp(String ip) {
		//String[] result = { "中国", "广东省", "广州市" };
		String[] result = { "本机" };
		if ("127.0.0.1".equals(ip) || "localhost".equals(ip)) {
			return result;
		}
		try {
			String search = searcher.search(ip);
			// searcher.close();
			String[] split = search.split("\\|");
			String[] results = { split[0], split[2], split[3] };
			return results;
		} catch (Exception e) {
			log.error("搜索地址错误: %s\n", ip, e);
			return result;
		}

	}

	public static void main(String[] args) {
		System.out.println(getStringAddressByIp("10.98.250.44"));
	}
}
