package com.base.sbc.mdToImg;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;

import gui.ava.html.image.generator.HtmlImageGenerator;

public class HtmlToImgTest {
	public static void main(String[] args) throws InterruptedException, IOException {

		String saveImageLocation = "D:\\space-spring\\sjs_yfg_pdm\\src\\test\\java\\com\\base\\sbc\\mdToImg\\md.png";

		Parser parser = Parser.builder().build();
		Node document = parser.parse("粘衬编号：4121#（水洗衬）粘衬颜色: (      )色\r\n"
				+ "粘衬机器温度：**130-135**度，压力**2.5-3kg**,时间：**10-12**秒\r\n" + "根据面料特性适当调整机器温度与压力");
		HtmlRenderer renderer = HtmlRenderer.builder().build();
		String content = renderer.render(document);
		content = content.replace("\n", "<br/>");
		if (content.endsWith("<br/>")) {
			content = content.substring(0, content.length() - 5);
		}
		System.out.println(content);
		content = "<div style=\"font-size:16px;width:600px\">" + content + "</div>";
		content = content.replace("<p>", "<p style=\"line-height:28px;margin:0px\">");
		content = content.replace("<strong>", "<strong style=\"line-height:28px;color:red\">");

		System.out.println(content);
		HtmlImageGenerator gen = new HtmlImageGenerator();
		gen.loadHtml(content);
//		gen.getBufferedImage();// 获取图片流

		BufferedImage bufferedImage = gen.getBufferedImage();

//		ImageIcon imageIcon = new ImageIcon(bufferedImage);
//		Graphics2D g2D = (Graphics2D) bufferedImage.getGraphics();
//		g2D.drawImage(imageIcon.getImage(), 0, 0, imageIcon.getImageObserver());
//		int alpha = 0;
//		for (int j1 = bufferedImage.getMinY(); j1 < bufferedImage.getHeight(); j1++) {
//			for (int j2 = bufferedImage.getMinX(); j2 < bufferedImage.getWidth(); j2++) {
//				int rgb = bufferedImage.getRGB(j2, j1);
//				int R = (rgb & 0xff0000) >> 16;
//				int G = (rgb & 0xff00) >> 8;
//				int B = (rgb & 0xff);
//				if (((255 - R) < 30) && ((255 - G) < 30) && ((255 - B) < 30)) {
//					rgb = ((alpha + 1) << 24) | (rgb & 0x00ffffff);
//				}
//				bufferedImage.setRGB(j2, j1, rgb);
//			}
//		}
//		g2D.drawImage(bufferedImage, 0, 0, imageIcon.getImageObserver());
		ImageIO.write(bufferedImage, "png", new File(saveImageLocation));// 直接输出文件

//		gen.saveAsImage(saveImageLocation);
//		return bufferedImage;

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
