package net.dschinghiskahn.objectdecoupler;

/**
 * Interface for receivers registering at the {@link ObjectDecoupler}.
 *
 * @param <E>
 *            The type of the objects the {@link ObjectDecoupler} is configured
 *            for.
 */
public interface IObjectReceiver<E> {

    /**
     * Asynchronously receives the objects.
     * 
     * @param object
     *            The object previously added to the {@link ObjectDecoupler}.
     */
    void receiveObject(E object);
}
