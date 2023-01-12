package ch.cern.todo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.*;
import org.springframework.http.*;
@RestController
public class Controller {
    private int getNum = 0;
    
    //mock db to simulate the real thing
    private TaskDatabase taskdb = new TaskMockDatabase();
    private CategoryDatabase catdb = new CategoryMockDatabase();
    //default get counts the number of time it was called and explain how to use the api
    @GetMapping("/")
	public String index() {
        getNum++;
		return "Greetings from Frédéric Necker! You are the " + getNum + 
        "th visitor \nThis should be a guide on the API.\n" +
        "the different endpoints are: \n" + 
        "post /newtask  creates a new task when given a json of the format:\n"+
        "{\"name\": \"Stuff\", \"description\": \"DoStuff\", \"cat_id\": \"0\", \"deadl\": \"2007-12-03\"}\n" + 
        "/task/id/{id} looks in the database for a matching taskId" +
        "/newcategory does the same as newtask but for categories with the format:\n" + 
        "{\"id\": \"1\",\"name\": \"doStuff\", \"description\": \"task where we DoStuff\"}\n" + 
        "/category/id/{id} search the database of categories for a given id\n"+ 
        "/category/name/{name} does the same for names\n"+
        "delete /task/id/{id} removes value with id from the db\n" + 
        "delete /category/name/{name} and /category/id/{id} remove elements from the category db\n "+
        "/task/category/{id} will return all tasks of a given category\n";
        
	}
    
    //post to add new task to DB 
    @PostMapping("/newtask")
    public Task newTask(@RequestBody Task newTask) {
        taskdb.add(newTask);
        return newTask;
    }
    //lookup in task db using id
    @GetMapping("/task/id/{id}")
	public Task GetTaskByID(@PathVariable Long id) {
        Task res = taskdb.get(id) ;
        if(res == null){
            //not found in db
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
        } else {
            return res;
        }
	}
    //post new category
    @PostMapping("/newcategory")
    public Task_Category newTask(@RequestBody Task_Category newCategory) {
        try {
            //try to add in db
            catdb.add(newCategory);
        } catch (ConflictException e) {
            //get refused if conflict
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST  , "conflict in database");
        }
        return newCategory;
    }
    //lookup in category db using id
    @GetMapping("/category/id/{id}")
	public Task_Category GetCategoryByID(@PathVariable Long id) {
        Task_Category res = catdb.getId(id) ;
        if(res == null){
            //not found in db
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
        } else {
            return res;
        }
	}
     //lookup in category db using name
    @GetMapping("/category/name/{name}")
	public Task_Category GetCategoryByName(@PathVariable String name) {
        Task_Category res = catdb.getName(name) ;
        if(res == null){
            //not found in db
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
        } else {
            return res;
        }
	}
    //tries to delete task with id id and return it, or gives a 400 if not found
    @DeleteMapping("/task/id/{id}")
    public Task deleteTask(@PathVariable Long id) {

        Task result = taskdb.delete(id);

        if(result == null){
            //not found in db
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
        } else {
            return result;
        }
    }
    //tries to delete category with id id and return it, or gives a 400 if not found
    @DeleteMapping("/category/id/{id}")
    public Task_Category deleteCategoryId(@PathVariable Long id) {

        Task_Category result = catdb.deleteId(id);

        if(result == null){
            //not found in db
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
        } else {
            return result;
        }
    }
    //tries to delete category with name name and return it, or gives a 400 if not found
    @DeleteMapping("/category/name/{name}")
    public Task_Category deleteTask(@PathVariable String name) {

        Task_Category result = catdb.deleteName(name);

        if(result == null){
            //not found in db
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "entity not found");
        } else {
            return result;
        }
    }
    //search db for all task of a given category
    @GetMapping("/task/category/{cat_id}")
	public Task[] GetTasksByCategory(@PathVariable Long cat_id) {
        Task[] res = taskdb.getTaskByCategory(cat_id) ;
        return res;
	}

}
