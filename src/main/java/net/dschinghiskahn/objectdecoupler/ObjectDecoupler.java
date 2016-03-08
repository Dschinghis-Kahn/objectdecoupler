package net.dschinghiskahn.objectdecoupler;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import net.dschinghiskahn.objectstore.ObjectStore;
import net.dschinghiskahn.worker.AbstractWorker;

/**
 * Provides asynchronous access to added objects.
 * 
 * @param <E>
 *            The type of objects to work with.
 */
public class ObjectDecoupler<E> {

    private final Logger logger = Logger.getLogger(getClass()); // NOPMD
    private final ObjectStore<E> objectStore;
    private final List<IObjectReceiver<E>> objectReceivers;
    private final ObjectDistributor<E> worker;

    /**
     * Creates a default ObjectDecoupler.
     */
    public ObjectDecoupler() {
        this.objectStore = new ObjectStore<E>();
        this.objectReceivers = new ArrayList<IObjectReceiver<E>>();

        worker = new ObjectDistributor<E>(this);
        worker.start();
    }

    /**
     * Adds an object to the ObjectDecoupler.
     * 
     * @param object
     *            The object to add.
     */
    public void add(E object) {
        objectStore.add(object);
        if (logger.isDebugEnabled()) {
            logger.debug("Object added");
        }
        worker.wakeUpAllWorkers();
    }

    protected E get() {
        return objectStore.get();
    }

    /**
     * Registers the {@link IObjectReceiver} to be called when an object is
     * added.
     */
    public void registerObjectReceiver(IObjectReceiver<E> objectReceiver) {
        synchronized (objectReceivers) {
            objectReceivers.add(objectReceiver);
        }
    }

    /**
     * Unregisters the {@link IObjectReceiver} from being called when an object
     * is added.
     */
    public void unregisterObjectReceiver(IObjectReceiver<E> objectReceiver) {
        synchronized (objectReceivers) {
            objectReceivers.remove(objectReceiver);
        }
    }

    protected List<IObjectReceiver<E>> getObjectReceivers() {
        synchronized (objectReceivers) {
            return objectReceivers;
        }
    }

    /**
     * Returns the total number of objects that have been added.
     * 
     * @return The total number of objects that have been added.
     */
    public long getSize() {
        return objectStore.getSize();
    }

    /**
     * Returns if no objects are left.
     * 
     * @return True if no objects are left, else otherwise.
     */
    public boolean isEmpty() {
        return objectStore.isEmpty();
    }

    /**
     * Stops the ObjectDecoupler. Currently processed item is still being
     * finished.
     */
    public void stop() {
        worker.stop();
    }

    /**
     * Starts the ObjectDecoupler.
     */
    public void start() {
        worker.start();
    }
}

/**
 * Sends an object to a specific ObjectReceiver.
 * 
 * @param <E>
 *            The object type to send.
 */
class ObjectDistributor<E> extends AbstractWorker<E> {
    private final ObjectDecoupler<E> objectDecoupler;

    /**
     * Creates an ObjectDistributor for the given decoupler
     * 
     * @param objectDecoupler
     *            The decoupler to use in this distributer.
     */
    ObjectDistributor(ObjectDecoupler<E> objectDecoupler) {
        super("ObjectDistributor", true, objectDecoupler);
        this.objectDecoupler = objectDecoupler;
    }

    @Override
    protected void doWork(E item) {
        for (IObjectReceiver<E> receiver : objectDecoupler.getObjectReceivers()) {
            receiver.receiveObject(item);
        }
    }

    @Override
    protected E getWork() {
        return objectDecoupler.get();
    }

    @Override
    protected boolean isWorkAvailable() {
        return !objectDecoupler.isEmpty();
    }

    @Override
    protected Long getSuspendTime() {
        return null;
    }
}
