package demo;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import pojo.AddPlace;
import pojo.Location;
import static io.restassured.RestAssured.given;
import java.util.ArrayList;
import java.util.List;

public class SpecBuilderTest {

	public static void main(String[] args) {

		
      RestAssured.baseURI="https://rahulshettyacademy.com";

      AddPlace p =new AddPlace();
      p.setAccuracy(50);
      p.setAddress("29, side layout, cohen 09");
      p.setLanguage("French-IN");
      p.setPhone_number("(+91) 983 893 3937");
      p.setWebsite("https://rahulshettyacademy.com");
      p.setName("Frontline house");
      
      List<String> myList =new ArrayList<String>();
      myList.add("shoe park");
      myList.add("shop");
      p.setTypes(myList);   // As setTypes() setter expects a list as a return type an arraylist object was created first and all values were added in the arraylist using it's object with add()
      
      Location l =new Location();
      l.setLat(-38.383494);
      l.setLng(33.427362);
      p.setLocation(l);      // As setLocation() setter expects Location class as a return type a location class object was created first

//SpecBuilder for requests
 RequestSpecification req = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").addQueryParam("key", "qaclick123")
                            .setContentType(ContentType.JSON).build();
 
//SpecBuilder for response
 ResponseSpecification resp = new ResponseSpecBuilder().expectStatusCode(200).expectContentType(ContentType.JSON).build();

 
   RequestSpecification request = given().spec(req).body(p);

   Response response = request.when().post("/maps/api/place/add/json")
                       .then().spec(resp).extract().response();

   String responseString = response.asString();

   System.out.println(responseString);


	}

}
