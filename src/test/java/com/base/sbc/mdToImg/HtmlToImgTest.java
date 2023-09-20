package com.base.sbc.mdToImg;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import gui.ava.html.image.generator.HtmlImageGenerator;

public class HtmlToImgTest {
	public static void main(String[] args) {
		String sql = "SELECT count(0)FROM t_planning_category_item c JOIN t_planning_season s ON (s.id = C.planning_season_id)LEFT JOIN t_style sd ON (sd.planning_category_item_id = c.id AND sd." +
				"del_flag = '0' )" +
				"planning_channel_id = '2') " +
				"where ( C.brand in ('5') ) and c.del flag = 0'";
		String sss =disposeSql(sql,true);
		System.out.println(sss);
		System.out.println(disposeSql(sss,false));
	}
	public static String disposeSql(String sql, Boolean type){
		if(type){
			sql=sql.replaceAll("\\?"," ::;,11,;:: ")
					.replaceAll("\\+"," ::;,22,;:: ")
					.replaceAll("\\|"," ::;,33,;:: ")
					.replaceAll("\\."," ::;,44,;:: ")
					.replaceAll("\\*"," ::;,55,;:: ")
					.replaceAll("\\$"," ::;,66,;:: ")
					.replaceAll("\\("," ::;,77,;:: ")
					.replaceAll("\\)"," ::;,88,;:: ");
		}else {
			sql=sql.replaceAll(" ::;,11,;:: ","\\?")
					.replaceAll(" ::;,22,;:: ","\\+")
					.replaceAll(" ::;,33,;:: ","\\|")
					.replaceAll(" ::;,44,;:: ","\\.")
					.replaceAll(" ::;,55,;:: ","\\*")
					.replaceAll(" ::;,66,;:: ","\\$")
					.replaceAll(" ::;,77,;:: ","\\(")
					.replaceAll(" ::;,88,;:: ","\\)");
		}
		return sql;
	}
	/**
	 * 
	 * @Description 转换成24位图的BMP
	 * @param image
	 * @return
	 */
	public static BufferedImage transform_Gray24BitMap(BufferedImage image) {

		int h = image.getHeight();
		int w = image.getWidth();
		int[] pixels = new int[w * h]; // 定义数组，用来存储图片的像素
		int gray;
		PixelGrabber pg = new PixelGrabber(image, 0, 0, w, h, pixels, 0, w);
		try {
			pg.grabPixels(); // 读取像素值
		} catch (InterruptedException e) {
			throw new RuntimeException("转换成24位图的BMP时，处理像素值异常");
		}

		for (int j = 0; j < h; j++) { // 扫描列
			for (int i = 0; i < w; i++) { // 扫描行
				// 由红，绿，蓝值得到灰度值
				gray = (int) (((pixels[w * j + i] >> 16) & 0xff) * 0.8);
				gray += (int) (((pixels[w * j + i] >> 8) & 0xff) * 0.1);
				gray += (int) (((pixels[w * j + i]) & 0xff) * 0.1);
				pixels[w * j + i] = (255 << 24) | (gray << 16) | (gray << 8) | gray;
			}
		}

		MemoryImageSource s = new MemoryImageSource(w, h, pixels, 0, w);
		Image img = Toolkit.getDefaultToolkit().createImage(s);
		BufferedImage buf = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);// 如果要转换成别的位图，改这个常量即可
		buf.createGraphics().drawImage(img, 0, 0, null);
		return buf;
	}
}
