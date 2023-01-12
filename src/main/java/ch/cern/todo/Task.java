package ch.cern.todo;


import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.*;
import java.util.Random;
//entity class that represent a given task, the id is randomly generated

@Entity
public class Task {
  private @Id @GeneratedValue Long id;
  private String task_name;
  private String task_description;
  private Long category_id;
  private LocalDate deadline;
  Task(String name, String description, String cat_id, String deadl) {
    //random id  to simulate uniqueness would use more certain method in production
    id = new Random().nextLong();
    task_name = name;
    task_description = description;
    category_id = Long.parseLong(cat_id);
    deadline = LocalDate.parse(deadl);
  }
  //we use getters id we need to to but no setters as task are supposed to be imutable.
  public Long getId(){
    return id;
  }
  public String getName(){
    return task_name;
  }
  public String getDescription(){
    return task_description;
  }
  public Long getCategory(){
    return category_id;
  }
  public LocalDate getDeadline(){
    return deadline;
  }
  @Override //checks equality of type and fields, to be used to check for uniqueness
  public boolean equals(Object o){
    //type check
    if (!(o instanceof Task)){
        return false;
    }
    //if good type this is safe
    Task task = (Task) o;
    // seems scary but is just a bunch of equals anded together
    return this.id.longValue() == task.id.longValue() && this.task_name == task.task_name && this.task_description == task.task_description
        && this.category_id.longValue() == task.category_id.longValue() && this.deadline == task.deadline;
  }
  @Override
  //Clean format  to make return value usefull in http
  public String toString(){
    return "Task{" + "id=" + this.id + ", task_name='" + this.task_name + '\'' + ", task_description='" + this.task_description +  
    '\'' + ", category_id='" + this.category_id  + '\'' + ", deadline='" + this.deadline.toString() + '\'' + '}';
  }
}



