package ch.cern.todo;
//Interface of very basic DB most of logic is already in Task/Task_Categories
public interface TaskDatabase {
    public void add(Task obj);
    public Task get(Long id);
    public Task delete(Long id);
    public Task[] getTaskByCategory(Long cat_id);
}
