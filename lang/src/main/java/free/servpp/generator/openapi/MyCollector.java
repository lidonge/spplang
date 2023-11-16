package free.servpp.generator.openapi;

import com.samskivert.mustache.DefaultCollector;

import java.util.Iterator;
import java.util.Map;

/**
 * @author lidong@date 2023-11-15@version 1.0
 */
public class MyCollector extends DefaultCollector {
    public Iterator<?> toIterator (final Object value) {
        if(value instanceof Map<?,?>)
            return ((Map<?, ?>) value).values().iterator();
        return super.toIterator(value);
    }
}
