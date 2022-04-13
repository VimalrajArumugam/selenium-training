package brokenLinks;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.List;
import java.util.Properties;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class FindBrokenLinks {

	WebDriver driver = null;
	int linkcounts = 0;
	String[] broken;
	String[] unbroken;
//	String filelocation = "D:\\Madhan\\Vimalral\\flipkart1.xlsx";
//	String requiredDetils = System.setProperty("config_FindBroken.txt","C:\\Users\\ELCOT\\eclipse-workspace\\MavenDepend\\config_Folder");
//	String webLinks = "Website_Links";
	
		
	public Properties fileReader() throws IOException {
		
		FileReader fr = new FileReader("./config_Folder/config_FindBroken.txt");
		Properties pr = new Properties();
		pr.load(fr);
		return pr;
	}
	
	public void broswerHomePage() throws Exception {

		Properties pr = fileReader();
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		String link = pr.getProperty("Website_Links");
		driver.get(link);
//		fileReader();
		
		
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
	}

	public void findBrokenAndNonBrokenLinks() {

		List<WebElement> homepage = driver.findElements(By.tagName("a"));

		linkcounts = homepage.size();
		System.out.println("Total links are:" + linkcounts);

		for (int i = 0; i < homepage.size(); i++) {
			WebElement element = homepage.get(i);
			String url = element.getAttribute("href");
			// System.out.println(url);
			if (url == null || url.isEmpty()) {
				System.out.println(url + "URL is Empty");
				continue;
			}

			try {
				HttpsURLConnection link = (HttpsURLConnection) new URL(url).openConnection();
				link.connect();
				if (link.getResponseCode() >= 400) {
					broken = (String[]) ArrayUtils.add(broken, url);
					System.out.println(url + " --is broken");
				} else {
					unbroken = (String[]) ArrayUtils.add(unbroken, url);
					System.out.println(url + " --is valid");
				}

			} catch(Exception e) {
				e.printStackTrace();
			}
//			catch (MalformedURLException e) {
//				System.err.println(url + e.getMessage());
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}catch (StaleElementReferenceException ser) {
//				System.out.println(ser.getMessage());
//			}catch (ClassCastException cce) {
//				System.out.println(cce.getMessage());
//			}
		}

		
	}
	
	public void brokenLinks() {
		try {
		System.out.println("Broken Links:"+broken.length);
		for (String brokenurl : broken) {
			System.out.println(brokenurl);
		}
		}catch(Exception e) {
		System.out.println(e.getMessage());	
		}
	}
	
	public void unbrokenLinks() {
		try {
		System.out.println("UnBroken Links:"+unbroken.length);
		for (String unbrokenurl : unbroken) {
			System.out.println(unbrokenurl);
			
		}
		}catch(Exception e) {
			System.out.println(e.getMessage());	
		}
	}
	public void ImportExcelsheet() throws IOException {

//		int j = 0;
//		int y = 1;

		Properties pr = fileReader();
		String fileLocation = pr.getProperty("ExcelFile");
		try {
//		fileReader();
		
//		String  project_path = System.getProperty("user.dir");
//		System.out.println(project_path);
//		File excelfile = new File("vimalraj1.xlsx");
//		File excelfile = new File(project_path + "/config_Folder/vimalraj.xlsx");
		File excelfile = new File(fileLocation);
		FileOutputStream fos = new FileOutputStream(excelfile);
		boolean exists = excelfile.exists();
		System.out.println(exists);
		if (exists == false) {
		boolean present = excelfile.createNewFile();
		System.out.println(present);
		}
		XSSFWorkbook wbook = new XSSFWorkbook();
		XSSFSheet wsheet = wbook.createSheet();
		wsheet.createRow(0);
		
		wsheet.getRow(0).createCell(0).setCellValue("UnBroken Links");
		wsheet.getRow(0).createCell(1).setCellValue("Broken Links");
//		wsheet.getRow(0).createCell(0).setCellErrorValue(0);
		System.out.println("Heading ok");

//		for (int i = 0; i < broken.length; i++) {
//			wsheet.createRow(i+1);
//			wsheet.getRow(i+1).createCell(1).setCellValue(broken[i]);
//			System.out.println(broken[i]);
//			}
//			System.out.println("broken ok");
		
		for (int x = 0; x < unbroken.length; x++) {
		wsheet.createRow(x+1);
		wsheet.getRow(x+1).createCell(0).setCellValue(unbroken[x]);

		}
		System.out.println("unbroken ok");
		
		for (int i = 0; i < broken.length; i++) {
//			wsheet.createRow(i+1);
			wsheet.getRow(i+1).createCell(1).setCellValue(broken[i]);
			System.out.println(broken[i]);
			}
			System.out.println("broken ok");

		wbook.write(fos);
		wbook.close();
		} catch (NullPointerException NPE) {
		System.out.println(NPE.getMessage());
		} catch (ArrayIndexOutOfBoundsException AE) {
		AE.printStackTrace();
		} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		}
		System.out.println("File created sucessfully");
		driver.close();
		}
}
