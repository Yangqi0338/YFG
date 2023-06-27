package com.base.sbc.selenium.plan;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 企划数据抓取
 *
 * @author shenzhixiong
 * @email 731139982@qq.com
 * @date 2023年5月23日
 */
public class YflPlanningTest {
	public static void main(String[] args) throws InterruptedException {
		System.setProperty("webdriver.chrome.driver", "D:\\下载\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		// 登录系统
		LoginSystem(driver);
		// 品牌名称和url
		String planName = "MM商品企划";
		String planUrl = "C308755";
		// 获取存放路径
		String path = "D:\\";

//		System.out.println("开始获取企划文件夹数据");
//		// 获取企划文件夹数据
//		List<PlanFold> planFold = getPlanFold(driver, planName, planUrl);
//		// 写入excel
//		EasyExcel.write(path + planName + "_企划文件夹.xlsx", PlanFold.class).sheet("企划文件夹").doWrite(planFold);

		// 读取企划文件夹excel
//		List<PlanFold> planFold = new ArrayList<>();
//		EasyExcel.read(path + planName + "_企划文件夹.xlsx", PlanFold.class, new AnalysisEventListener<PlanFold>() {
//			@Override
//			public void invoke(PlanFold data, AnalysisContext context) {
//				planFold.add(data);
//			}
//
//			@Override
//			public void doAfterAllAnalysed(AnalysisContext context) {
//			}
//		}).sheet().doRead();
////		// 获取企划目标数据
//		System.out.println("开始获取企划目标数据");
//		List<PlanTarget> targetList = getPlanTarget(driver, planFold);
//		// 写入excel
//		EasyExcel.write(path + planName + "_企划目标.xlsx", PlanTarget.class).sheet("企划目标").doWrite(targetList);

//		 System.out.println("开始获取企划计划数据");
//		 //读取企划目标excel
		List<PlanTarget> planTarget = new ArrayList<>();
		EasyExcel.read(path + planName + "_企划目标.xlsx", PlanTarget.class, new AnalysisEventListener<PlanTarget>() {
			@Override
			public void invoke(PlanTarget data, AnalysisContext context) {
				planTarget.add(data);
			}

			@Override
			public void doAfterAllAnalysed(AnalysisContext context) {
			}
		}).sheet().doRead();
		 //获取企划目标数据
		List<PlanPlan> planList = getPlanPlan(driver, planTarget);
		 //写入excel
		EasyExcel.write(path + planName + "_企划计划.xlsx", PlanPlan.class).sheet("企划计划").doWrite(planList);

	}

	/**
	 * 获取企划计划
	 *
	 * @param driver
	 * @param planTarget
	 * @return
	 * @throws InterruptedException
	 */
	private static List<PlanPlan> getPlanPlan(WebDriver driver, List<PlanTarget> planTarget)
			throws InterruptedException {
		List<PlanPlan> ptList = new ArrayList<>();
		PlanPlan pp = null;
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		Thread.sleep(8000);
		String nextName = "";
		for (PlanTarget pt : planTarget) {
			driver.get(pt.getSeriesUrl());
			Thread.sleep(500);
			driver.get(driver.getCurrentUrl());
			Thread.sleep(3500);

			if (!pt.getVersion().equals(nextName)) {
				nextName = pt.getVersion();
				Thread.sleep(2000);
				WebElement versionInput = driver.findElement(By.xpath("//*[@id=\"uniqName_14_0\"]"));
				versionInput.click();
				versionInput.clear();
				Thread.sleep(200);
				versionInput.sendKeys(pt.getVersion());
				Thread.sleep(200);
				versionInput.sendKeys(Keys.ENTER);
				Thread.sleep(1000);
			}

			WebElement page = driver.findElement(By.xpath("//*[@id=\"uniqName_14_3\"]"));
			page.click();
			Thread.sleep(300);
			page.clear();
			Thread.sleep(200);
			page.sendKeys("所有");
			Thread.sleep(200);
			page.sendKeys(Keys.ENTER);
			Thread.sleep(5000);

			List<WebElement> trs = driver.findElements(
					By.xpath("//*[starts-with(@id,\"uniqName_65_\")]/div[5]/div/div[1]/table/tbody[2]/tr"));
			if (trs == null || trs.size() < 2) {
				continue;
			}
			for (int i = 1; i < trs.size() - 1; i++) {
				WebElement tr = trs.get(i);
				List<WebElement> a = tr.findElements(By.className("browse"));
				List<WebElement> tds = tr.findElements(By.tagName("td"));
				pp = new PlanPlan();
				pp.setUrl(pt.getSeriesUrl());
				pp.setVersion(pt.getVersion());

				pp.setProduct(a.get(0).getText());
				pp.setProductUrl(a.get(0).getAttribute("href"));
				pp.setType(tds.get(1).getText());
				pp.setCenterType(tds.get(2).getText());
				pp.setTaoBaoType(tds.get(3).getText());
				pp.setBand(tds.get(4).getText());
				if (a != null && a.size() > 1) {
					pp.setStyle(a.get(1).getText());
					pp.setStyleUrl(a.get(1).getAttribute("href"));
				} else {
					pp.setStyle(tds.get(5).getText());
				}
				pp.setTargetPrice(tds.get(6).getText());
				pp.setTargetMultiplier(tds.get(7).getText());
				pp.setTargetCost(tds.get(8).getText());
				pp.setRefStyle(tds.get(9).getText());
				pp.setRefStyleNo(tds.get(10).getText());
				pp.setRefStyleImg(tds.get(11).getText());
				pp.setRefCloth(tds.get(12).getText());
				pp.setImg(tds.get(13).getText());
				pp.setRefMaterial(tds.get(14).getText());
				pp.setRefMaterialImg(tds.get(15).getText());
				System.out.println(pp);
				ptList.add(pp);
			}
			Thread.sleep(500);
		}
		return ptList;
	}

	/**
	 * 获取企划目标
	 *
	 * @param driver
	 * @param planFold
	 * @return
	 * @throws InterruptedException
	 */
	private static List<PlanTarget> getPlanTarget(WebDriver driver, List<PlanFold> planFold)
			throws InterruptedException {
		List<PlanTarget> ptList = new ArrayList<>();
		PlanTarget pt = null;
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
		Thread.sleep(5000);
		String nextName = "";
		for (PlanFold pf : planFold) {
			driver.get(pf.getFoldUrl());
			Thread.sleep(500);
			driver.get(driver.getCurrentUrl());
			Thread.sleep(3000);

			if (!pf.getVersion().equals(nextName)) {
				nextName = pf.getVersion();
				Thread.sleep(2000);
				WebElement versionInput = driver.findElement(By.xpath("//*[@id=\"uniqName_14_0\"]"));
				versionInput.click();
				versionInput.clear();
				Thread.sleep(500);
				versionInput.sendKeys(pf.getVersion());
				Thread.sleep(500);
				versionInput.sendKeys(Keys.ENTER);
				Thread.sleep(1000);
			}

			List<WebElement> trs = driver
					.findElements(
							By.xpath("//*[starts-with(@id,\"uniqName_65_\")]/div[5]/div/div[1]/table/tbody[2]/tr"));
			if (trs == null || trs.size() < 2) {
				continue;
			}
			for (int i = 2; i < trs.size() - 1; i++) {
				WebElement tr = trs.get(i);
				WebElement fold = tr.findElement(By.className("browse"));
				pt = new PlanTarget();
				pt.setUrl(pf.getFoldUrl());
				pt.setSeries(fold.getText());
				pt.setSeriesUrl(fold.getAttribute("href"));
				pt.setVersion(pf.getVersion());
				List<WebElement> tds = tr.findElements(By.tagName("td"));
				pt.setMonth(tds.get(1).getText());
				pt.setModelSkc(tds.get(2).getText());
				pt.setColorSkc(tds.get(3).getText());
				pt.setTypeSku(tds.get(4).getText());
				pt.setRequireNum(tds.get(5).getText());
				pt.setDesignNum(tds.get(6).getText());
				pt.setDevelopmentNum(tds.get(7).getText());
				System.out.println(pt);
				ptList.add(pt);
			}
			Thread.sleep(500);
		}
		return ptList;
	}

	/**
	 * 获取企划文件夹
	 *
	 * @param driver
	 * @param name
	 * @param urlcode
	 * @return
	 * @throws InterruptedException
	 */
	private static List<PlanFold> getPlanFold(WebDriver driver, String name, String urlcode)
			throws InterruptedException {
		List<PlanFold> pfList = new ArrayList<>();
		driver.get("http://10.8.240.175/WebAccess/home.html#URL=" + urlcode + "&RURL=&Tab=Target");
		Thread.sleep(9000);
		WebElement versionInput = driver.findElement(By.xpath("//*[@id=\"uniqName_14_0\"]"));
		versionInput.click();
		Thread.sleep(2000);
		WebElement versionSelect = driver.findElement(By.xpath("//*[@id=\"uniqName_14_0_popup\"]"));
		String[] versionList = versionSelect.getText().split("\n");
		System.out.println("获取下拉栏：" + versionList);

		Thread.sleep(500);
		PlanFold pf = null;
		for (String v : versionList) {
			if ("更多选项".equals(v)) {
				continue;
			}
			versionInput.clear();
			Thread.sleep(500);
			versionInput.sendKeys(v);
			Thread.sleep(500);
			versionInput.sendKeys(Keys.ENTER);
			Thread.sleep(500);
			List<WebElement> trs = driver
					.findElements(By.xpath("//*[@id=\"uniqName_65_0\"]/div[5]/div/div[1]/table/tbody[2]/tr"));
			for (int i = 2; i < trs.size() - 1; i++) {
				WebElement tr = trs.get(i);
				WebElement fold = tr.findElement(By.className("browse"));
				pf = new PlanFold();
				pf.setUrl("http://10.8.240.175/WebAccess/home.html#URL=" + urlcode);
				pf.setBrand(name);
				pf.setVersion(v);
				pf.setFold(fold.getText());
				pf.setFoldUrl(fold.getAttribute("href"));
				List<WebElement> tds = tr.findElements(By.tagName("td"));
				pf.setProductType(tds.get(1).getText());
				pf.setBigType(tds.get(2).getText());
				pf.setType(tds.get(3).getText());
				pf.setSkuNum(tds.get(4).getText());
				System.out.println(pf);
				pfList.add(pf);
			}
		}
		return pfList;
	}

	/**
	 * 登录系统
	 *
	 * @param driver
	 * @throws InterruptedException
	 */
	private static void LoginSystem(WebDriver driver) throws InterruptedException {
		// EdgeOptions chromeOptions = new EdgeOptions();
		// // 禁用沙箱
		// chromeOptions.addArguments("--no-sandbox");
		// chromeOptions.addArguments("--disable-dev-shm-usage");
		// chromeOptions.addArguments("--headless");
		driver.manage().window().setPosition(new Point(0, 0));
//				driver.manage().window().maximize();
		// driver.manage().deleteAllCookies();
		// 与浏览器同步非常重要，必须等待浏览器加载完毕
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.get("http://10.8.240.175/WebAccess/home.html#URL=C308755&RURL=&Tab=Target");
		Thread.sleep(1000);
		// 登录
		driver.findElement(By.xpath("//*[@id=\"LoginID\"]")).sendKeys("1100946");
		driver.findElement(By.xpath("//*[@id=\"Password\"]")).sendKeys("123456z");
		driver.findElement(By.xpath("//*[@id=\"loginPane\"]/div[2]/div[3]/div/span[2]/span")).click();
		Thread.sleep(1000);
	}

}
