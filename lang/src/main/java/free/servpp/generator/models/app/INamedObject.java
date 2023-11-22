package free.servpp.generator.models.app;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @author lidong@date 2023-11-22@version 1.0
 */
public interface INamedObject<T extends INamedObject> {
    String getName();
    default String addToList(List<T> namedObjects, T namedObject){
        int exist = Arrays.binarySearch(namedObjects.toArray(new INamedObject[namedObjects.size()]), namedObject, new Comparator<INamedObject>() {

            @Override
            public int compare(INamedObject o1, INamedObject o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        if( exist >= 0 )
            return "Duplicate name "+ namedObject.getName() +" at position: " +exist;
        namedObjects.add(namedObject);
        return null;
    }

    default T searchAppHeader(List<T> namedObjects, T namedObject){
        int exist = Arrays.binarySearch(namedObjects.toArray(new INamedObject[namedObjects.size()]), namedObject, new Comparator<INamedObject>() {

            @Override
            public int compare(INamedObject o1, INamedObject o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        if(exist >= 0)
            return namedObjects.get(exist);
        return null;
    }

}
