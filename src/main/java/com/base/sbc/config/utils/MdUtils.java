package com.base.sbc.config.utils;

import java.awt.image.BufferedImage;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import gui.ava.html.image.generator.HtmlImageGenerator;

/**
 * md文档工具类
 *
 * @author shenzhixiong
 * @email 731139982@qq.com
 * @date 2023年7月17日
 */
public class MdUtils {
	/**
	 * md转换为图片
	 * 
	 * @param mdText md文本
	 * @return
	 */
	public static BufferedImage mdToImage(String mdText) {
		Parser parser = Parser.builder().build();
		Node document = parser.parse(mdText);
		HtmlRenderer renderer = HtmlRenderer.builder().build();
		String content = renderer.render(document);
		content = content.replace("\n", "<br/>");
		if (content.endsWith("<br/>")) {
			content = content.substring(0, content.length() - 5);
		}
		// 宽度600px
		content = "<div style=\"font-size:20px;width:600px\">" + content + "</div>";
		// 间距调整
		content = content.replace("<p>", "<p style=\"line-height:28px;margin:0px\">");
		// 加粗同时变红
		content = content.replace("<strong>", "<strong style=\"line-height:28px;color:red\">");
		HtmlImageGenerator gen = new HtmlImageGenerator();
		gen.loadHtml(content);
		return gen.getBufferedImage();
	}
}
