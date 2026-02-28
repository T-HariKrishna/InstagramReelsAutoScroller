package com.hari;

import java.time.Duration;
import java.util.List;
import java.util.Scanner;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class InstagramReelsAutoScroller {
	public static void main(String[] args) throws InterruptedException {
		Scanner scan = new Scanner(System.in);
		WebDriver driver = new ChromeDriver();
		driver.get("https://www.instagram.com/");
		driver.manage().window().maximize();
		Thread.sleep(3000);

		driver.findElement(By.xpath("//*[@id=\"_R_32d9lplcldcpbn6b5ipamH1_\"]"))
				.sendKeys("Enter your Mobile No, Username  or Email");

		System.out.println("Enter your instagram password");
		driver.findElement(By.xpath("//*[@id=\"_R_33d9lplcldcpbn6b5ipamH1_\"]"))
				.sendKeys("Enter your instagram password*");

		driver.findElement(By.xpath("//*[@id=\"login_form\"]/div/div[1]/div/div[3]/div/div")).click();
		
		
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));

		//If an alert presents after login this three lines check
//		wait.until(ExpectedConditions.alertIsPresent());
//		driver.switchTo().alert().accept();

		//-------------------------------------------------

		JavascriptExecutor js = (JavascriptExecutor) driver;
		Actions actions = new Actions(driver);
//		WebDriverWait wait1 = new WebDriverWait(driver, Duration.ofSeconds(20));
		System.out.println("login completed");

		// --- Navigate to Instagram Reels page (after login) ---
		WebElement reels = wait
				.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(@href,'/reels')]")));
		reels.click();

		// wait for first video element to appear
		wait.until(d -> js.executeScript("return document.querySelector('video') != null"));
		int reelsToWatch = 5; // number of reels
		int reelsWatched = 0;
		//while condition if you want to watch on condition 'reelsWatched < reelsToWatch'
		//while condition if you want to watch more 'true'
		while (true) {

			// --- Get all preloaded video elements in DOM ---
			List<WebElement> videos = driver.findElements(By.tagName("video"));

			System.out.println("Videos loaded "+videos.size());
			boolean videoWatched = false;

			for (WebElement video : videos) {

				// check if this video is already watched
				Object played = video.getAttribute("data-watched"); // custom attribute
				if (played != null && played.equals("true"))
					continue;

				// safely get duration
				Number durNum = (Number) js.executeScript(
						"let v = arguments[0]; return v ? (isFinite(v.duration) ? v.duration : -1) : -1;", video);

				double durationSeconds = (durNum != null && durNum.doubleValue() > 0) ? durNum.doubleValue() : 6;

				// mark as watched (to avoid replaying the same video)
				js.executeScript("arguments[0].setAttribute('data-watched','true');", video);

				reelsWatched++;
				System.out.println("Reel #" + reelsWatched + " duration: " + durationSeconds + " sec");

				// wait full video
				Thread.sleep((long) (durationSeconds * 1000) + 500);
				videoWatched = true;
				actions.sendKeys(Keys.ARROW_DOWN).perform();
				break; // watch one video per iteration
			}


		}


	}
}
