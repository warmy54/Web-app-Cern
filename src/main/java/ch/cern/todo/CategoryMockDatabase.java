package ch.cern.todo;
import java.util.Vector;
public class CategoryMockDatabase implements CategoryDatabase{
    private Vector<Task_Category> vect = new Vector<Task_Category> ();
    //check if any element conflicts with the new adition
    private boolean check_conflicts(Task_Category obj){
        for(Task_Category elem: vect){
            if(Task_Category.conflict(obj,elem)){
                return true;
            }
        }
        return false;
    }
    //to maintain uniqueness we prevent adition in case of conflict
    public void add(Task_Category obj) throws ConflictException{
        if(check_conflicts(obj)){
            throw new ConflictException();
        }
        vect.add(obj);
    }
    //Iterate throught list trying to find matching id, return nulls if not found.
    public Task_Category getId(Long id){
        for(Task_Category elem: vect){
            if(elem.getId().longValue() == id.longValue()){
                return elem;
            } 
        }
        return null;
    }
    //Iterate throught list trying to find matching name, return nulls if not found.
    public Task_Category getName(String name){
        for(Task_Category elem: vect){
            if(elem.getName().equals(name)){
                
                return elem;
            } 
        }
        return null;
    }
}
