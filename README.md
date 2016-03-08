# objectdecoupler
A queue that handles added object asynchronously.

##Features
- Easy to use
- Is scalable


##Example

```Java
public class TaskHandler implements IObjectReceiver<Task> {

    private ObjectDecoupler<Task> decoupler;

    public TaskHandler() {
        decoupler = new ObjectDecoupler<Task>();
    }

    /**
     *  This method ...
     *      ... receives the tasks
     *      ... hands the tasks to the ObjectDecouper
     *      ... returns immediately 
     */
    public void addTask(Task task) {
        decoupler.add(task);
    }

    /**
     * This method is asynchronously called in the order each task was
     * previously added.
     */
    @Override
    public void receiveObject(Task task) {
        // do something with the task object
    }

}
```
