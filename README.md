# objectstore
A data store with size limitation capabilities.

##Features
- Easy to use
- Blocks add method when full
- Can prioritize its items


##Example

```Java
public class TaskStore {

    private ObjectStore<Task> store;

    public static void main(String[] args) {
        TaskStore taskStore = new TaskStore();
        taskStore.addTask(new Task(0));
        taskStore.addTask(new Task(1));
        taskStore.addTask(new Task(3));

        // this task will be sorted to the correct position
        taskStore.addTask(new Task(2));

        taskStore.addTask(new Task(4));

       /*
         * At this point the store holds these tasks in order: 
         * 1. Task(0) 
         * 2. Task(1) 
         * 3. Task(2) 
         * 4. Task(3) 
         * 5. Task(4)
         */

        // this method will block because the store is full now
        taskStore.addTask(new Task(5));
    }

    public TaskStore() {
        // An Object store with maxSize=5 and priorized=true is created
        store = new ObjectStore<Task>(5, true);
    }

    public void addTask(Task task) {
        // Just add the task, the method will block if necessary
        store.add(task);
    }

    public Task getNextTask() {
        return store.get();
    }

}
```

```Java
public class Task implements Comparable<Task> {

    private Integer id;

    public Task(int id) {
        this.id = id;
    }

    @Override
    public int compareTo(Task o) {
        return id.compareTo(o.id);
    }

    @Override
    public String toString() {
        return "Task [id=" + id + "]";
    }
    
}
```
