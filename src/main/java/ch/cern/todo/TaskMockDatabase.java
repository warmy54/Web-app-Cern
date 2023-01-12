package ch.cern.todo;
import java.util.Vector;
public class TaskMockDatabase implements TaskDatabase {
    //Mock database who stocks object in an array instead of an actual database
    private Vector<Task> vect = new Vector<Task> ();
    public void add(Task obj){
        vect.add(obj);
    }
    //Iterate throught list trying to find matching id, return nulls if not found.
    public Task get(Long id){
        for(Task elem: vect){
            if(elem.getId().longValue() == id.longValue()){
                return elem;
            } 
        }
        return null;
    }
    public Task delete(Long id){
        for(Task elem: vect){
            if(elem.getId().longValue() == id.longValue()){
                vect.remove(elem);
                return elem;
                
            } 
        }
        return null;
    }
    //
    public Task[] getTaskByCategory(Long cat_id){
        int count = 0;
        //to create an array we need its size so saddly we will have to iterate twice, we could take a 
        //memory penalty to speed things up by storing all the hits in a structure while counting.
        for(Task elem: vect){
            if(elem.getCategory().longValue() == cat_id.longValue()){
                count++;
            } 
        }
        Task res[] = new Task[count];
        int count2 = 0;
        for(Task elem: vect){
            if(elem.getCategory().longValue() == cat_id.longValue()){
                res[count2] = elem;
                count2++;
            } 
        }
        return res;
    }
}
