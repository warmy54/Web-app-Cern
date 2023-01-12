package ch.cern.todo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.*;
import java.time.*;
import org.springframework.http.*;
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ApplicationTests {
	@Value(value="${local.server.port}")
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;
	//are we online yet?
	@Test
	void TestBaseGet() {
		assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/",
				String.class)).contains("Greetings from Frédéric Necker!");
	}

	@Test
	void TaskCreation() {
		Task tsk = new Task("Stuff", "DoStuff", "0", "2007-12-03");
		assertThat(tsk).isNotNull();
	}
	@Test
	void TaskAreUnique() {
		//two task should not be identical even if initialised with same paraneters
		Task tsk1 = new Task("Stuff", "DoStuff", "0", "2007-12-03");
		Task tsk2 = new Task("Stuff", "DoStuff", "0", "2007-12-03");
		assertThat(tsk1).isNotEqualTo(tsk2);
	}
	@Test
	void PostTaskWorks() throws URISyntaxException{
		//create tsk to post and the uri to send it to.
		Task tsk1 = new Task("Stuff", "DoStuff", "0", "2007-12-03");
		final String baseUrl = "http://localhost:"+port+"/newtask/";
        URI uri = new URI(baseUrl);
        String tsk = "{\"name\": \"Stuff\", \"description\": \"DoStuff\", \"cat_id\": \"0\", \"deadl\": \"2007-12-03\"}";
         
        HttpHeaders headers = new HttpHeaders();
		headers.set("Content-type","application/json");  
        HttpEntity<String> request = new HttpEntity<>(tsk, headers);
        //do post
        ResponseEntity<String> result = this.restTemplate.postForEntity(uri, request, String.class);
		assertThat(result.getStatusCodeValue()).isEqualTo(200);
	}
	//check if post tasks return correct fields
	@Test
	void PostTaskReturnCorrect() throws URISyntaxException{
		//same thing
		final String baseUrl = "http://localhost:"+port+"/newtask/";
        URI uri = new URI(baseUrl);
        String tsk = "{\"name\": \"Stuff\", \"description\": \"DoStuff\", \"cat_id\": \"0\", \"deadl\": \"2007-12-03\"}";
         
        HttpHeaders headers = new HttpHeaders();
		headers.set("Content-type","application/json");  
        HttpEntity<String> request = new HttpEntity<>(tsk, headers);
         
        ResponseEntity<String> result = this.restTemplate.postForEntity(uri, request, String.class);
		assertThat(result.getBody()).contains("Stuff");
		assertThat(result.getBody()).contains("DoStuff");
		assertThat(result.getBody()).contains("0");
		assertThat(result.getBody()).contains("2007-12-03");
	}

	//Check if we are able to Post tasks to db then get it back
	@Test
	void PostAddtoDB() throws URISyntaxException{
		//do a post like before but we keep the id to fetch it back
		final String baseUrl = "http://localhost:"+port+"/newtask/";
        URI uri = new URI(baseUrl);
        String tsk = "{\"name\": \"Stuff\", \"description\": \"DoStuff\", \"cat_id\": \"0\", \"deadl\": \"2007-12-03\"}";
         
        HttpHeaders headers = new HttpHeaders();
		headers.set("Content-type","application/json");  
        HttpEntity<String> request = new HttpEntity<>(tsk, headers);
         
        ResponseEntity<String> result = this.restTemplate.postForEntity(uri, request, String.class);
		String bod = result.getBody();
		//this rather complicated line simply isolate the id in the returned string by finding bothe delimiters.
		String id = bod.substring(bod.indexOf("\"id\"")+5,bod.indexOf(",",bod.indexOf("\"id\"")));
		//check if we can get it back
		assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/task/id/" + id,
				String.class)).contains("Stuff");
	}

	//check if we get an element if we get a wrong id
	void IncorrectGetIsFail() throws URISyntaxException{
		//do a post like before but we keep the id to fetch it back
		final String baseUrl = "http://localhost:"+port+"/newtask/";
        URI uri = new URI(baseUrl);
        String tsk = "{\"name\": \"Stuff\", \"description\": \"DoStuff\", \"cat_id\": \"0\", \"deadl\": \"2007-12-03\"}";
         
        HttpHeaders headers = new HttpHeaders();
		headers.set("Content-type","application/json");  
        HttpEntity<String> request = new HttpEntity<>(tsk, headers);
         
        ResponseEntity<String> result = this.restTemplate.postForEntity(uri, request, String.class);
		String bod = result.getBody();
		//this rather complicated line simply isolate the id in the returned string by finding bothe delimiters.
		String id = "0";
		//check if we can get it back
		assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/task/id/" + id,
				String.class)).contains("404");
	}
	//check that a category can be created
	void CategoryCreation() {
		Task_Category cat = new Task_Category("1", "Stuffdoer", "Task that do stuff");
		assertThat(cat).isNotNull();
	}


	//check if post return correct fields
	@Test
	void PostCategoryReturnCorrect() throws URISyntaxException{
		//same thing as task but different names
		final String baseUrl = "http://localhost:"+port+"/newcategory/";
        URI uri = new URI(baseUrl);
        String tsk = "{\"id\": \"2\",\"name\": \"swii\", \"description\": \"task where we DoStuff\"}";
         
        HttpHeaders headers = new HttpHeaders();
		headers.set("Content-type","application/json");  
        HttpEntity<String> request = new HttpEntity<>(tsk, headers);
         
        ResponseEntity<String> result = this.restTemplate.postForEntity(uri, request, String.class);
		assertThat(result.getBody()).contains("task where we DoStuff");
		assertThat(result.getBody()).contains("swii");
		assertThat(result.getBody()).contains("2");
	}
	//check if post of two conflictual object result in a bad request
	@Test
	void NoDuplicatesInCat() throws URISyntaxException{
		//same thing
		final String baseUrl = "http://localhost:"+port+"/newcategory/";
        URI uri = new URI(baseUrl);
        String tsk = "{\"id\": \"1\",\"name\": \"doStuff\", \"description\": \"task where we DoStuff\"}";
         
        HttpHeaders headers = new HttpHeaders();
		headers.set("Content-type","application/json");  
        HttpEntity<String> request = new HttpEntity<>(tsk, headers);
         
        ResponseEntity<String> result = this.restTemplate.postForEntity(uri, request, String.class);
		ResponseEntity<String> result2 = this.restTemplate.postForEntity(uri, request, String.class);
		//400 = http code for bad request
		assertThat(result2.getStatusCodeValue()).isEqualTo(400);

		
	}

	//check that we can get categories by Id
	@Test
	void CanGetCategoryById() throws URISyntaxException{
		//same thing
		final String baseUrl = "http://localhost:"+port+"/newcategory/";
        URI uri = new URI(baseUrl);
        String tsk = "{\"id\": \"3\",\"name\": \"dotrStuff\", \"description\": \"task where we DoStuff\"}";
         
        HttpHeaders headers = new HttpHeaders();
		headers.set("Content-type","application/json");  
        HttpEntity<String> request = new HttpEntity<>(tsk, headers);
         
        ResponseEntity<String> result = this.restTemplate.postForEntity(uri, request, String.class);
		
		assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/category/id/3",
		String.class)).contains("dotrStuff");
		
	}
	//check that we get a 404 when trying to get an unexistant category
	@Test
	void CantGetWrongTaskId() throws URISyntaxException{
		//same thing
		final String baseUrl = "http://localhost:"+port+"/newcategory/";
        URI uri = new URI(baseUrl);
		//I shall keep testpiou here as a tribute to my lost hours of sleep
        String tsk = "{\"id\": \"4\",\"name\": \"testpiou\", \"description\": \"task where we DoStuff\"}";
         
        HttpHeaders headers = new HttpHeaders();
		headers.set("Content-type","application/json");  
        HttpEntity<String> request = new HttpEntity<>(tsk, headers);
         
        ResponseEntity<String> result = this.restTemplate.postForEntity(uri, request, String.class);
		
		assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/category/id/999",
		String.class)).contains("404");
		
	}


	//check that we can get categories by name
	@Test
	void CanGetCategoryByName() throws URISyntaxException{
		//same thing
		final String baseUrl = "http://localhost:"+port+"/newcategory/";
        URI uri = new URI(baseUrl);
        String tsk = "{\"id\": \"5\",\"name\": \"Stuffy\", \"description\": \"yes\"}";
         
        HttpHeaders headers = new HttpHeaders();
		headers.set("Content-type","application/json");  
        HttpEntity<String> request = new HttpEntity<>(tsk, headers);
         
        ResponseEntity<String> result = this.restTemplate.postForEntity(uri, request, String.class);
		
		assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/category/name/Stuffy",
		String.class)).contains("yes");
		
	}
	//check that we get a 404 when trying to get an unexistant category
	@Test
	void CantGetWrongName() throws URISyntaxException{
		//same thing
		final String baseUrl = "http://localhost:"+port+"/newcategory/";
        URI uri = new URI(baseUrl);
        String tsk = "{\"id\": \"6\",\"name\": \"Stuff!\", \"description\": \"no\"}";
         
        HttpHeaders headers = new HttpHeaders();
		headers.set("Content-type","application/json");  
        HttpEntity<String> request = new HttpEntity<>(tsk, headers);
         
        ResponseEntity<String> result = this.restTemplate.postForEntity(uri, request, String.class);
		
		assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/category/name/AAAAAA",
		String.class)).contains("404");
		
	}
	//check that delete remove an element by id
	@Test
	void DeleteDoesRemoveId() throws URISyntaxException{
		//same thing as task but different names
		final String baseUrl = "http://localhost:"+port+"/newcategory/";
        URI uri = new URI(baseUrl);
        String tsk = "{\"id\": \"10\",\"name\": \"DeletedCat1\", \"description\": \"i need to delete\"}";
         
        HttpHeaders headers = new HttpHeaders();
		headers.set("Content-type","application/json");  
        HttpEntity<String> request = new HttpEntity<>(tsk, headers);
         
        ResponseEntity<String> result = this.restTemplate.postForEntity(uri, request, String.class);
		final String baseUrl2 = "http://localhost:"+port+"/category/id/10";
        uri = new URI(baseUrl2);
		this.restTemplate.delete(uri);
		assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/category/id/10",
		String.class)).contains("404");
	}


	//check that delete remove an element by name
	@Test
	void DeleteDoesRemoveName() throws URISyntaxException{
		//same thing as task but different names
		final String baseUrl = "http://localhost:"+port+"/newcategory/";
        URI uri = new URI(baseUrl);
        String tsk = "{\"id\": \"11\",\"name\": \"DeletedCat2\", \"description\": \"i need to delete\"}";
         
        HttpHeaders headers = new HttpHeaders();
		headers.set("Content-type","application/json");  
        HttpEntity<String> request = new HttpEntity<>(tsk, headers);
         
        ResponseEntity<String> result = this.restTemplate.postForEntity(uri, request, String.class);
		final String baseUrl2 = "http://localhost:"+port+"/category/name/DeletedCat2";
        uri = new URI(baseUrl2);
		this.restTemplate.delete(uri);
		assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/category/id/11",
		String.class)).contains("404");
	}
	//we check if we can get a task List by filtering by categories
	@Test
	void PostFilterByCategoryCorrectly() throws URISyntaxException{
		final String baseUrl = "http://localhost:"+port+"/newtask/";
        URI uri = new URI(baseUrl);
        String tsk = "{\"name\": \"Stuff1.0\", \"description\": \"DoStuff\", \"cat_id\": \"15\", \"deadl\": \"2007-12-03\"}";
        String tsk2 = "{\"name\": \"Stuff2.0\", \"description\": \"DoStuff but better\", \"cat_id\": \"15\", \"deadl\": \"2006-11-02\"}";
        String tsk3 = "{\"name\": \"Stuff3.0\", \"description\": \"DoStuff but worse\", \"cat_id\": \"16\", \"deadl\": \"2008-12-04\"}";
         
        HttpHeaders headers = new HttpHeaders();
		headers.set("Content-type","application/json");  
        HttpEntity<String> request = new HttpEntity<>(tsk, headers);
		HttpEntity<String> request2 = new HttpEntity<>(tsk2, headers);
		HttpEntity<String> request3 = new HttpEntity<>(tsk3, headers);
        ResponseEntity<String> result = this.restTemplate.postForEntity(uri, request, String.class);
		ResponseEntity<String> result2 = this.restTemplate.postForEntity(uri, request2, String.class);
		ResponseEntity<String> result3 = this.restTemplate.postForEntity(uri, request3, String.class);
		
		String body = this.restTemplate.getForObject("http://localhost:" + port + "/task/category/15",
		String.class);
		assertThat(body).contains("Stuff1.0");
		assertThat(body).contains("Stuff2.0");
		assert(!body.contains("3.0"));
	}
}
