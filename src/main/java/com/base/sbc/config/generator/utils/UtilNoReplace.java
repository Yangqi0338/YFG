package com.base.sbc.config.generator.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 不替换 xml和entity的部分字段 或者其他的 需要将不替换的 内容 放在 other_start other_end 所在行之间
 * 
 * @author xiong
 *
 */
public class UtilNoReplace {
	public static final String OTHER_START = "other_start";
	public static final String OTHER_END = "other_end";

	/**
	 * 将内容 放入文件的 other_start 与 other_end 之间
	 * 
	 * @param fileName
	 * @return
	 */
	public static void setTextToFile(String fileName, String startEnd) {
		// 定义一个file对象，用来初始化FileReader
		File file = new File(fileName);
		if (!file.exists()) {
			return ;
		}
		FileReader reader;
		// 定义一个字符串缓存，将字符串存放缓存中
		StringBuilder sb = new StringBuilder();
		try {
			reader = new FileReader(file);

			// 定义一个fileReader对象，用来初始化BufferedReader
			// new一个BufferedReader对象，将文件内容读取到缓存
			BufferedReader bReader = new BufferedReader(reader);
			String s = "";
			// 逐行读取文件内容，不读取换行符和末尾的空格
			while ((s = bReader.readLine()) != null) {
				// 将读取的字符串添加换行符后累加存放在缓存中
				sb.append(s + "\n");
			}
			bReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//覆盖文件
		PrintWriter pw;
		try {
			pw = new PrintWriter(new FileWriter(fileName));
			pw.println(replaceAll(sb.toString(),OTHER_START,OTHER_END,startEnd));
			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 替换文件的内容
	 * @param htmlString
	 * @param start
	 * @param end
	 * @param newString
	 * @return
	 */
	private static String replaceAll(String htmlString, String start, String end, String newString) {
		StringBuffer modString = new StringBuffer(htmlString.length());
		int i = 0, j = 0, j2 = 0;
		while (true) {
			// first check if there are any matching start & end
			i = htmlString.indexOf(start, j2);
			if (i != -1) {
				j = htmlString.indexOf(end, i);
			} else {
				j = htmlString.indexOf(end, j2);
			}
			if ((i != -1) && (j != -1)) {
				modString.append(htmlString.substring(j2, i)).append(newString);
				// 此处不可以改为
				j2 = j + end.length();
			} else {
				modString.append(htmlString.substring(j2));
				break;
			}
		}
		return modString.toString();
	}

	/**
	 * 获取文件内 other_start 到 other_end 的内容
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getNoReplaceText(String fileName) {
		// 定义一个file对象，用来初始化FileReader
		File file = new File(fileName);
		if (!file.exists()) {
			return "";
		}
		FileReader reader;
		// 定义一个字符串缓存，将字符串存放缓存中
		StringBuilder sb = new StringBuilder();
		try {
			reader = new FileReader(file);

			// 定义一个fileReader对象，用来初始化BufferedReader
			// new一个BufferedReader对象，将文件内容读取到缓存
			BufferedReader bReader = new BufferedReader(reader);
			String s = "";
			// 逐行读取文件内容，不读取换行符和末尾的空格
			while ((s = bReader.readLine()) != null) {
				// 将读取的字符串添加换行符后累加存放在缓存中
				sb.append(s + "\n");
			}
			bReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(sb.indexOf(OTHER_START)==-1|| sb.indexOf(OTHER_END)==-1) {
			return "";
		}
		return sb.substring(sb.indexOf(OTHER_START), sb.indexOf(OTHER_END) + 9);
	}

}
