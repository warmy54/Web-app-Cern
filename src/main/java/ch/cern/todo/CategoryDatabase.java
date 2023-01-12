package ch.cern.todo;
public interface CategoryDatabase {
    //similar to TaskDatabase but we need to be able to search by name
    public void add(Task_Category obj) throws ConflictException;
    public Task_Category getId(Long id);
    public Task_Category getName(String name);
}
