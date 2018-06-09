package app.API.JNAinterface;

//import app.API.JNAinterface.BossaAPI;
import com.sun.jna.Structure;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * Helper class for wrapping JNI structures to objects. Appropriate getters should be implemented by childs.
 *
 * @param <T> wrapper class for JNI structure
 * @param <Q> class of the object to be wrapped
 */
//extracted to help composition
abstract class BossaAPIClassWrapper<T, Q extends Structure> {
    protected static final Logger logger =
            Logger.getLogger(BossaAPIClassWrapper.class.getName());
    protected final Q wrappee; //TODO change to protected

    protected BossaAPIClassWrapper(Q wrappee) {
        this.wrappee = wrappee;
    }
    /**
     * Method used to convert Java Native Access mapped pointer to array of given class,
     * to Java List of wrapper class. Pointer class must extend {@link Structure} or implement
     * {@link Structure.ByReference}.
     *
     * @param size         number of elements in JNA mapped array
     * @param pointer      to JNA mapped array
     * @param wrapperClass pointer will be wrapped by this class, the class should have a constructor which uses <i>pointer</i>
     * @param <U>          pointer class
     * @param <T>          class wrapper designed to wrap class of the pointer {@link BossaAPIClassWrapper}
     * @return list of wrapper class elements
     */
    //TODO this may be converted to Set instead of List
    static <U extends Structure,
            T extends BossaAPIClassWrapper<? extends BossaAPIClassWrapper<T, U>, U>> //added type parameters to bossa api class wrapper
    List<T> convertPointerToListHelper(int size, U pointer, Class<T> wrapperClass) {
        Object[] params = {size, pointer, wrapperClass};
        logger.entering(BossaAPIClassWrapper.class.toString(), "convertPointerToListHelper", params);

        if (size == 0) {
            return Collections.emptyList();
        }
        Structure[] wrappeeList = pointer.toArray(size);
        List<T> wrappedAPIList = new ArrayList<>(size);

        //reflection will not find the right constructor if the pointer is ToReference extension of Structure
        Class pointerClass = pointer.getClass();
        if (pointer instanceof Structure.ByReference) {
            pointerClass = pointer.getClass().getSuperclass();
        }
        try {
            Constructor<T> wrapperConstructor = wrapperClass.getDeclaredConstructor(pointerClass);
            wrapperConstructor.setAccessible(true);
            for (Structure element : wrappeeList) {
                wrappedAPIList.add(wrapperConstructor.newInstance(element));
            }
        } catch (Exception e) {
            StringBuilder stackTrace = new StringBuilder();
            for (StackTraceElement element : e.getStackTrace()) {
                stackTrace.append(element).append(System.lineSeparator());
            }
            logger.severe("Pointer is of different class than the wrapper: " + e + "\n" + stackTrace);
            throw new RuntimeException(e);
        }

        logger.exiting(BossaAPIClassWrapper.class.toString(), "convertPointerToListHelper", wrappedAPIList);
        return wrappedAPIList;
    }
}
