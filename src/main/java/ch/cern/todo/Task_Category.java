package ch.cern.todo;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

public class Task_Category {
    private @Id @GeneratedValue Long id;
    private String category_name;
    private String category_description;
    Task_Category(String id ,String name, String description){
        //here we want to decide what name is what id uniqueness will be done in the database
        this.id = Long.parseLong(id);
        this.category_name = name;
        this.category_description = description;
    }
    public Long getId() {
        return this.id;
    }
    public String getName(){
        return this.category_name;
    }
    public String getDescription(){
        return this.category_description;
    }
    @Override
    public boolean equals(Object o){
        //type check
        if (!(o instanceof Task_Category)){
            return false;
        }
        //if typechack we check equality of fields
        Task_Category categ = (Task_Category) o;
        return this.id.longValue() == categ.id.longValue() && this.category_name == categ.category_name &&
            this.category_description == categ.category_description;
    }
    //check if two Categories conflict by having same name or id.
    static public boolean conflict(Task_Category cat1, Task_Category cat2){
        return cat1.id.longValue() == cat2.id.longValue() || cat1.category_name == cat2.category_name;
    }
    
    @Override
  //Clean format  to make return value usefull in http
  public String toString(){
    return "Task_Category{" + "id=" + this.id + ", category_name='" + this.category_name + '\'' + ", category_description='" + this.category_description + '\'' + '}';
  }
}
