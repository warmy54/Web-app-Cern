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
}
