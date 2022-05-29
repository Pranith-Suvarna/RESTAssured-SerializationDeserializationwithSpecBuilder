package demo;
import static io.restassured.RestAssured.given;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import pojo.Api;
import pojo.GetCourse;


public class DeserializeTest {

	public static void main(String[] args) throws InterruptedException {
		
      String[] courseTitles= { "Selenium Webdriver Java","Cypress","Protractor"};
      
 	    //  System.setProperty("webdriver.chrome.driver", "C://chromedriver.exe");
            WebDriverManager.chromedriver().setup();
			WebDriver driver= new ChromeDriver();
		//  ChromeOptions options = new ChromeOptions();
	   	//  options.addArguments("user-data-dir=C:\\Users\\prani\\AppData\\Local\\Google\\Chrome\\User Data");
		    
			driver.get("https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/userinfo.email&auth_url=https://accounts.google.com/o/oauth2/v2/auth&client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com&response_type=code&redirect_uri=https://rahulshettyacademy.com/getCourse.php&state=verifyfjdss");
			driver.findElement(By.cssSelector("input[type='email']")).sendKeys("pranithsuvarna1@gmail.com");
			driver.findElement(By.cssSelector("input[type='email']")).sendKeys(Keys.ENTER);
			Thread.sleep(3000);
			driver.findElement(By.cssSelector("input[type='password']")).sendKeys("xyz");
			driver.findElement(By.cssSelector("input[type='password']")).sendKeys(Keys.ENTER);
			Thread.sleep(4000);
			
			String url=driver.getCurrentUrl();
			String partialcode=url.split("code=")[1];
			String code=partialcode.split("&scope")[0];
			System.out.println(code);		
			
			
	 String accessTokenResponse = given().urlEncodingEnabled(false)
		.queryParams("code",code)
		.queryParams("client_id","692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
		.queryParams("client_secret","erZOWM9g3UtwNRj340YYaK_W")
		.queryParams("redirect_uri","https://rahulshettyacademy.com/getCourse.php")
		.queryParams("grant_type","authorization_code")
		.when().log().all().post("https://www.googleapis.com/oauth2/v4/token").asString();
	
	 JsonPath js=new JsonPath(accessTokenResponse);
	 String accessToken=js.getString("access_token");
		

     // Deserialization :
	 
	 GetCourse gc = given().queryParam("access_token", accessToken).expect().defaultParser(Parser.JSON)
		           .when().get("https://rahulshettyacademy.com/getCourse.php").as(GetCourse.class);
	 
/*	 Response:
	 {
		    "instructor": "RahulShetty",
		    "url": "rahulshettycademy.com",
		    "services": "projectSupport",
		    "expertise": "Automation",
		    "courses": {
		        "webAutomation": [
		            {
		                "courseTitle": "Selenium Webdriver Java",
		                "price": "50"
		            },
		            {
		                "courseTitle": "Cypress",
		                "price": "40"
		            },
		            {
		                "courseTitle": "Protractor",
		                "price": "40"
		            }
		        ],
		        "api": [
		            {
		                "courseTitle": "Rest Assured Automation using Java",
		                "price": "50"
		            },
		            {
		                "courseTitle": "SoapUI Webservices testing",
		                "price": "40"
		            }
		        ],
		        "mobile": [
		            {
		                "courseTitle": "Appium-Mobile Automation using Java",
		                "price": "50"
		            }
		        ]
		    },
		    "linkedIn": "https://www.linkedin.com/in/rahul-shetty-trainer/"
		} 
		
*/
		
		System.out.println(gc.getLinkedIn());
		System.out.println(gc.getInstructor());
		System.out.println(gc.getCourses().getApi().get(1).getCourseTitle());
	
		
     // Verify the presence of a course inside a nested/sub json body without using hardcoded index value as above
		
		List<Api> apiCourses=gc.getCourses().getApi();
		for(int i=0;i<apiCourses.size();i++)
		{
			if(apiCourses.get(i).getCourseTitle().equalsIgnoreCase("SoapUI Webservices testing"))
					{
				System.out.println(apiCourses.get(i).getPrice());
					}
		}
		
		
		
	// Verify presence of all the course titles in WebAutomation
		
		ArrayList<String> a = new ArrayList<String>();          // Create an array list object 'a' 
		
		List<pojo.WebAutomation> w = gc.getCourses().getWebAutomation();
		
		for(int j=0;j<w.size();j++)
		{
			a.add(w.get(j).getCourseTitle());                  // Add all the course titles in array list object 'a'
		}
		
		List<String> expectedList=	Arrays.asList(courseTitles);  // Convert the expected array 'courseTitles' into list as comparing two lists is easier
		
		Assert.assertTrue(a.equals(expectedList));
		
				
	}

}
