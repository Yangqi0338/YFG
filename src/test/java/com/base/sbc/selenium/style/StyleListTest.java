package com.base.sbc.selenium.style;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeDriverService;
import org.slf4j.LoggerFactory;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.base.sbc.config.utils.JsonUtils;
import com.base.sbc.config.utils.StringUtils;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * 1、按设计款号清单 获取物料BOM和尺码的excel 2、根据尺码excel获取详情 3、
 * 根据bom的Excel获取详情(需要手动缩放浏览器到33%及以下（BOM列式动态渲染的，看的见才渲染）)
 * 
 * @author shenzhixiong
 * @email 731139982@qq.com
 * @date 2023年7月5日
 */
@Slf4j
public class StyleListTest {
	static {
		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		List<ch.qos.logback.classic.Logger> loggerList = loggerContext.getLoggerList();
		loggerList.forEach(logger -> {
			logger.setLevel(Level.INFO);
		});
	}

	public static void main(String[] args) throws InterruptedException, IOException {
//		System.setProperty("webdriver.chrome.driver", "D:\\下载\\chromedriver.exe");
//		WebDriver driver = new ChromeDriver();
		System.setProperty(EdgeDriverService.EDGE_DRIVER_EXE_PROPERTY, "D:\\msedgedriver.exe");
		WebDriver driver = new EdgeDriver();
		// 登录系统
		LoginSystem(driver);
		Date dateStart = new Date();
		log.info("开始");

		String path = "D:\\space-spring\\sjs_yfg_pdm\\src\\test\\java\\com\\base\\sbc\\selenium\\style\\";

//		// 1.1 读取PLM_STYLE设计款号excel
//		List<Style> styleList = getListByExcel(path + "PLM_STYLE.xlsx", Style.class);
//		List<StyleSizeObj> soList = new ArrayList<>();
//		List<StyleBomObj> boList = new ArrayList<>();
//		// 1.2 抓取BOM清单excel和尺寸清单excel
//		getBomAndSize(driver, styleList, soList, boList);
//		// 1.3 写入抓取结果PLM_STYLE_SIZE和PLM_STYLE_BOM
//		EasyExcel.write(path + "PLM_STYLE.xlsx", Style.class).sheet("result").doWrite(styleList);
//		EasyExcel.write(path + "PLM_STYLE_SIZE.xlsx", StyleSizeObj.class).sheet("sheet1").doWrite(soList);
//		EasyExcel.write(path + "PLM_STYLE_BOM.xlsx", StyleBomObj.class).sheet("sheet1").doWrite(boList);

//		// 2.1 读取PLM_STYLE_SIZE
//		List<StyleSizeObj> sizeObjList = getListByExcel(path + "PLM_STYLE_SIZE.xlsx", StyleSizeObj.class);
//		// 2.2 抓取尺寸数据(选水洗尺寸)
//		List<StyleSizeItem> sizeItemList = getItemListBySizeObj(driver, sizeObjList);
//		// 2.3 结果存入excel
//		EasyExcel.write(path + "PLM_STYLE_SIZE_ITEM.xlsx", StyleSizeItem.class).sheet("sheet1").doWrite(sizeItemList);


		// 运行注意缩放浏览器 （33%）
		// 3.1 读取PLM_STYLE_BOM
		List<StyleBomObj> bomObjList = getListByExcel(path + "PLM_STYLE_BOM.xlsx", StyleBomObj.class);
		// 3.2 抓取bom数据 (选择MM工艺员,样品和大货生产 2个版本都要)
		List<StyleBomItem> bomItemList = getItemListByBomObj(driver, bomObjList);
		// 3.3 结果存入excel
		EasyExcel.write(path + "PLM_STYLE_BOM_ITEM.xlsx", StyleBomItem.class).sheet("sheet1").doWrite(bomItemList);

		log.info("导出完成");
		System.out.printf("执行时长: %d 分钟", 1 + (new Date().getTime() - dateStart.getTime()) / (60 * 1000));
	}

	/**
	 * 3.2 根据bomObj列表数据，获取具体的BOM
	 * 
	 * @param driver
	 * @param bomObjList
	 * @return
	 * @throws InterruptedException
	 */
	private static List<StyleBomItem> getItemListByBomObj(WebDriver driver, List<StyleBomObj> bomObjList)
			throws InterruptedException {
		driver.get(bomObjList.get(0).getBomUrl() + "&RURL=&Tab=PartMaterials");
		Thread.sleep(5000);
//		driver.get(driver.getCurrentUrl());
//		Thread.sleep(5000);
		driver.manage().window().maximize();
		Thread.sleep(2000);
		// 选择MM工艺员视图
		WebElement gy = driver.findElement(By.xpath("//*[starts-with(@id,\"uniqName_14_\")]"));
		gy.click();
		Thread.sleep(200);
		gy.clear();
		Thread.sleep(200);
		gy.sendKeys("MM工艺员");
		Thread.sleep(200);
		gy.sendKeys(Keys.ENTER);
		Thread.sleep(500);

		List<StyleBomItem> list = new ArrayList<>();
		int speed = 1;
		for (StyleBomObj obj : bomObjList) {
			driver.get(obj.getBomUrl());
			Thread.sleep(1000);
			driver.get(driver.getCurrentUrl());
			log.info((speed++) + "/" + bomObjList.size() + "  BOM详情路径：" + obj.getBomUrl());
			Thread.sleep(6000);

//			JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
//			jsExecutor.executeScript("document.body.style.width='50000px'");

			// 如果最后一行是more就查询全部
			if (driver.findElement(By.xpath("//*[@id=\"dijit__WidgetBase_0\"]")).getText().contains("of")) {
				// 切换分页为所有
				WebElement page = driver.findElement(By.xpath("//*[@id=\"uniqName_14_2\"]"));
				page.click();
				Thread.sleep(300);
				page.clear();
				Thread.sleep(200);
				page.sendKeys("所有");
				Thread.sleep(200);
				page.sendKeys(Keys.ENTER);
				Thread.sleep(6000);
			}

			// 数据
			List<WebElement> trs = driver.findElements(
					By.xpath("//*[starts-with(@id,\"uniqName_70_\")]/div[5]/div/div[1]/table/tbody[2]/tr"));
			if (trs == null || trs.size() == 0) {
				continue;
			}

			log.info("bom详情数量：" + (trs.size() - 2));
			if (trs.size() <= 2) {
				continue;
			}
			// 获取版本
			WebElement type = driver.findElement(
					By.xpath("//*[@id=\"dijit_form_DropDownButton_0_label\"]"));
			
			// 滚动条
//			WebElement right = driver.findElement(
//					By.xpath("//*[starts-with(@id,\"dijit_form_HorizontalSlider_\")]/tbody/tr[2]/td[5]/div"));
//			clickNum(right, 30);
//			Thread.sleep(500);
			
			// 获取当前尺码头
			List<WebElement> ths = driver.findElements(
					By.xpath("//*[starts-with(@id,\"uniqName_70_\")]/div[5]/div/div[1]/table/thead/tr[1]/th"));
			Boolean colorFlag = StringUtils.isNotBlank(obj.getColor());
			int sizeNum = ths.size() - 18 - 3 - (colorFlag ? 1 : 0);
			int sizeStartIdx = 18 + (colorFlag ? 1 : 0);
			String sizeName = "";
			for (int s = sizeStartIdx; s < sizeStartIdx + sizeNum; s++) {
				sizeName += "," + ths.get(s).getText();
			}
			sizeName = sizeName.substring(1);
			log.info("尺码数量：" + sizeNum + "-" + sizeName);
			StyleBomItem o;
			for (int i = 0; i < trs.size(); i++) {
				WebElement tr = trs.get(i);
				List<WebElement> tds = tr.findElements(By.tagName("td"));
				String first = tds.get(0).getText();
				if (StringUtils.isNotBlank(first) && (first.contains("Actions") || first.contains("Report"))) {
					continue;
				}
				o = new StyleBomItem();
				// 上级数据
				o.setUrl(obj.getBomUrl());
				o.setBomCode(obj.getBomCode());
				o.setOrderStyle(obj.getOrderStyle());
				o.setCode(obj.getCode());
				o.setColor(obj.getColor());
				o.setType(type.getText());

				o.setSendStatus(tds.get(0).getText());
				o.setGroup(tds.get(1).getText());
				o.setMainFabric(tds.get(2).getText());

				o.setFabric(tds.get(4).getText());
				if (StringUtils.isNotBlank(tds.get(4).getText())) {
					WebElement col4 = tds.get(4).findElement(By.className("browse"));
					o.setFabricUrl(col4 != null ? col4.getAttribute("href") : "");
				} else {
					o.setFabricUrl("");
				}

				o.setFabricName(tds.get(5).getText());
				o.setFactoryComposition(tds.get(6).getText());
				o.setComposition(tds.get(7).getText());

				o.setSupplierPrice(tds.get(8).getText());
				if (StringUtils.isNotBlank(tds.get(8).getText())) {
					WebElement col8 = tds.get(8).findElement(By.className("browse"));
					o.setSupplierPriceUrl(col8 != null ? col8.getAttribute("href") : "");
				} else {
					o.setSupplierPriceUrl("");
				}

				o.setUnitPrice(tds.get(9).getText());


				o.setPart(tds.get(10).getText());
				o.setUnit(tds.get(11).getText());
				o.setAuxiliaryMaterial(tds.get(12).getText());
				o.setColorAll(tds.get(13).getText());
				o.setSizeAll(tds.get(14).getText());
				o.setItemUsage(tds.get(15).getText());
				o.setLoss(tds.get(16).getText());
				o.setCost(tds.get(17).getText());

				if (StringUtils.isNotBlank(obj.getColor())) {
					o.setColorName(obj.getColor());
					o.setColorValue(tds.get(18).getText());
				} else {
					o.setColorName("");
					o.setColorValue("");
				}

				o.setSizeName(sizeName);

				String sizeValue = "";
				int idx = 18 + (colorFlag ? 1 : 0);
				for (int s = idx; s < idx + sizeNum * 2; s++) {
					sizeValue += "," + tds.get(s).getText();
				}
//				log.info("sizeValue: " + sizeValue);
				o.setSizeValue(sizeValue.substring(1));

				log.info(JsonUtils.beanToJson(o));
				list.add(o);
			}

		}
		return list;
	}

	private static void clickNum(WebElement we, int i) throws InterruptedException {
		for (int j = 0; j < i; j++) {
			we.click();
			Thread.sleep(50);
		}
	}

	/**
	 * 2.2 根据sizeObj列表数据 获取具体的尺寸数据
	 * 
	 * @param driver
	 * @param sizeObjList
	 * @return
	 * @throws InterruptedException
	 */
	private static List<StyleSizeItem> getItemListBySizeObj(WebDriver driver, List<StyleSizeObj> sizeObjList)
			throws InterruptedException {
		driver.get(sizeObjList.get(0).getSizeUrl() + "&RURL=&Tab=TDS");
		Thread.sleep(2000);
		driver.get(driver.getCurrentUrl());
		Thread.sleep(5000);
		int speed = 1;
		List<StyleSizeItem> list = new ArrayList<>();
		for (StyleSizeObj obj : sizeObjList) {
			driver.get(obj.getSizeUrl());
			log.info((speed++) + "/" + sizeObjList.size() + "尺寸详情路径：" + obj.getSizeUrl());
			Thread.sleep(2000);
			List<WebElement> trs = driver.findElements(
					By.xpath("//*[starts-with(@id,\"uniqName_70_\")]/div[5]/div/div[1]/table/tbody[2]/tr"));
			if (trs == null) {
				continue;
			}
			log.info("尺寸详情部位数量：" + (trs.size() - 1));
			if (trs.size() < 2) {
				continue;
			}
			// 获取当前有多少个尺码
			int sizeIdx = obj.getSize().split(",").length * 3 + 1;

			StyleSizeItem o;
			for (int i = 0; i < trs.size(); i++) {
				WebElement tr = trs.get(i);
				List<WebElement> tds = tr.findElements(By.tagName("td"));
				o = new StyleSizeItem();
				// 上级数据
				o.setUrl(obj.getSizeUrl());
				o.setSizeCode(obj.getSizeCode());
				o.setOrderStyle(obj.getOrderStyle());
				o.setCode(obj.getCode());
				o.setSize(obj.getSize());
				String part = tds.get(0).getText();
				if (StringUtils.isNotBlank(part) && part.contains("Actions")) {
					continue;
				}
				// 页面数据
				o.setPart(part);
				o.setSay(tds.get(1).getText());

				String sizeItem = "";
				for (int s = 2; s <= sizeIdx; s++) {
					sizeItem += "," + tds.get(s).getText();
				}
				o.setSizeItem(sizeItem.substring(1));
				o.setShrink(tds.get(sizeIdx + 1).getText());
				o.setToleranceReduction(tds.get(sizeIdx + 2).getText());
				o.setTolerancePlus(tds.get(sizeIdx + 3).getText());
				log.info(JsonUtils.beanToJson(o));
				list.add(o);
			}

		}
		return list;

	}

	/**
	 * 1.2 获取bom和尺寸的数据
	 * 
	 * @param driver
	 * @param styleList
	 * @throws InterruptedException
	 */
	private static void getBomAndSize(WebDriver driver, List<Style> styleList, List<StyleSizeObj> soList,
			List<StyleBomObj> boList) throws InterruptedException {
		// 切换为搜索款式
		WebElement searchType = driver.findElement(By.xpath("//*[@id=\"csiHeaderSearchSelect\"]/tbody/tr/td[1]"));
		searchType.click();
		Thread.sleep(200);
		WebElement styleType = driver.findElement(By.xpath("//*[@id=\"csiHeaderSearchSelect_menu\"]/tbody/tr[3]"));
		styleType.click();
		Thread.sleep(200);

		// 遍历款号
		int speed = 1;
		for (Style style : styleList) {
			log.info((speed++) + "/" + styleList.size() + "当前设计款号：" + style.getCode());
			WebElement search = driver.findElement(By.xpath("//*[@id=\"headerSearchText\"]"));
			search.clear();
			Thread.sleep(200);
			search.sendKeys(style.getCode());
			Thread.sleep(200);
			search.sendKeys(Keys.ENTER);
			Thread.sleep(1000);
			List<WebElement> result = driver.findElements(By.xpath("//*[@id=\"searchResultsGrid\"]/div[2]/div/div"));
			style.setSearchNum(result.size());
			log.info("搜索款式数量：" + result.size());
			if (result.size() > 0) {
				String url = result.get(0).findElement(By.tagName("a")).getAttribute("href");
				style.setUrl(url);
				Thread.sleep(200);
				String bomTableUrl = url + "&RURL=&Tab=ProductSpec&Tab1=BOMs";
				// 1.2.1 获取bom相关数据
				getStyleBomObjByUrl(driver, bomTableUrl, style, boList);
			}
		}

		for (Style style : styleList) {
			String sizeTableUrl = style.getUrl() + "&RURL=&Tab=ProductSpec&Tab1=SizeChartLayout";
			// 1.2.2 获取尺寸表相关数据
			getStyleSizeObjByUrl(driver, sizeTableUrl, style, soList);
		}
	}

	/**
	 * 1.2.2 获取尺寸表相关数据
	 * 
	 * @param driver
	 * @param url
	 * @param style
	 * @param soList
	 * @throws InterruptedException
	 */
	private static void getStyleSizeObjByUrl(WebDriver driver, String url, Style style, List<StyleSizeObj> soList)
			throws InterruptedException {
		driver.get(url);
		log.info("尺寸路径：" + url);
		Thread.sleep(1000);
		List<WebElement> trs = driver
				.findElements(By.xpath("//*[starts-with(@id,\"uniqName_50_\")]/div[5]/div/div[1]/table/tbody[2]/tr"));

		if (trs == null) {
			return;
		}
		style.setSizeNum(trs.size() - 1);
		log.info("尺寸表数量：" + style.getSizeNum());
		if (trs.size() < 2) {
			return;
		}
		StyleSizeObj o;
		for (int i = 0; i < trs.size() - 1; i++) {
			WebElement tr = trs.get(i);
			List<WebElement> tds = tr.findElements(By.tagName("td"));
			o = new StyleSizeObj();
			o.setUrl(url);
			WebElement col1 = tds.get(0).findElement(By.className("browse"));
			o.setSizeUrl(col1 != null ? col1.getAttribute("href") : "");
			o.setSizeCode(tds.get(0).getText());

			WebElement col2 = tds.get(1).findElement(By.className("browse"));
			o.setColorUrl(col2 != null ? col2.getAttribute("href") : "");
			o.setColor(tds.get(1).getText());

			o.setOrderStyle(tds.get(2).getText());
			o.setCode(style.getCode());
			o.setSizeType(tds.get(3).getText());

			WebElement col4 = tds.get(3).findElement(By.className("browse"));
			o.setSizeTypeUrl(col4 != null ? col4.getAttribute("href") : "");

			o.setSize(tds.get(4).getText());
			o.setBaseSize(tds.get(6).getText());
			soList.add(o);
		}
		Thread.sleep(500);
	}

	/**
	 * 1.2.1获取bom相关数据
	 * 
	 * @param driver
	 * @param url
	 * @param style
	 * @param boList
	 * @throws InterruptedException
	 */
	private static void getStyleBomObjByUrl(WebDriver driver, String url, Style style, List<StyleBomObj> boList)
			throws InterruptedException {
		driver.get(url);
		log.info("Bom路径：" + url);
		Thread.sleep(1000);
		// 选择MM工艺员视图
		WebElement gy = driver.findElement(By.xpath("//*[starts-with(@id,\"uniqName_14_\")]"));
		gy.click();
		gy.clear();
		Thread.sleep(200);
		gy.sendKeys("MM工艺员");
		Thread.sleep(200);
		gy.sendKeys(Keys.ENTER);
		Thread.sleep(500);

		List<WebElement> trs = driver
				.findElements(By.xpath("//*[starts-with(@id,\"uniqName_50_\")]/div[5]/div/div[1]/table/tbody[2]/tr"));
		if (trs == null) {
			return;
		}
		style.setBomNum(trs.size() - 1);
		log.info("Bom表数量：" + style.getBomNum());
		if (trs.size() < 2) {
			return;
		}
		StyleBomObj o;
		for (int i = 0; i < trs.size() - 1; i++) {
			WebElement tr = trs.get(i);
			List<WebElement> a = tr.findElements(By.className("browse"));
			List<WebElement> tds = tr.findElements(By.tagName("td"));
			o = new StyleBomObj();
			o.setUrl(url);
			o.setBomUrl(a.get(0).getAttribute("href"));
			o.setBomCode(a.get(0).getText());
			o.setSay(tds.get(1).getText());

			String color = tds.get(2).getText();
			if (StringUtils.isNotBlank(color)) {
				WebElement col2 = tds.get(2).findElement(By.className("browse"));
				o.setColorUrl(col2 != null ? col2.getAttribute("href") : "");
				o.setColor(color);
			} else {
				o.setColorUrl("");
				o.setColor(color);
			}

			o.setOrderStyle(tds.get(3).getText());
			o.setCode(style.getCode());
			o.setProductName(tds.get(4).getText());
			o.setStage(tds.get(6).getText());
			o.setCodeOld(tds.get(9).getText());
			o.setMarkingInformation(tds.get(10).getText());
			o.setParticularAttention(tds.get(11).getText());
			o.setSpecialProcess(tds.get(12).getText());
			boList.add(o);
		}
		Thread.sleep(500);
	}

	/**
	 * 根据实体读取文件
	 * 
	 * @param fileName 文件名称
	 * @param clazz    类
	 * @return
	 */
	private static <T> List<T> getListByExcel(String fileName, Class<T> clazz) {
		List<T> list = new ArrayList<>();
		EasyExcel.read(fileName, clazz, new AnalysisEventListener<T>() {
			@Override
			public void invoke(T data, AnalysisContext context) {
				list.add(data);
			}

			@Override
			public void doAfterAllAnalysed(AnalysisContext context) {
			}
		}).sheet().doRead();
		return list;
	}

	/**
	 * 登录系统
	 *
	 * @param driver
	 * @throws InterruptedException
	 */
	private static void LoginSystem(WebDriver driver) throws InterruptedException {


		// // 禁用沙箱
		// chromeOptions.addArguments("--no-sandbox");
		// chromeOptions.addArguments("--disable-dev-shm-usage");
		// chromeOptions.addArguments("--headless");
		driver.manage().window().setPosition(new Point(0, 0));
//		driver.manage().window().maximize();

		// driver.manage().deleteAllCookies();
		// 与浏览器同步非常重要，必须等待浏览器加载完毕
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.get("http://10.8.240.175/WebAccess/home.html#URL=C12277514&RURL=&Tab=ProductSpec&Tab1=BOMs");
		Thread.sleep(1000);
		// 登录
		driver.findElement(By.xpath("//*[@id=\"LoginID\"]")).sendKeys("1100946");
		driver.findElement(By.xpath("//*[@id=\"Password\"]")).sendKeys("123456z");
		driver.findElement(By.xpath("//*[@id=\"loginPane\"]/div[2]/div[3]/div/span[2]/span")).click();
		Thread.sleep(1000);
	}
}
