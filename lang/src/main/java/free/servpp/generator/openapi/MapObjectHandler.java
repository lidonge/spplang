package free.servpp.generator.openapi;

import com.github.mustachejava.Iteration;
import com.github.mustachejava.reflect.ReflectionObjectHandler;

import java.io.Writer;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author lidong@date 2023-11-13@version 1.0
 */
public class MapObjectHandler extends ReflectionObjectHandler {
    @Override
    public Writer iterate(Iteration iteration, Writer writer, Object object, List<Object> scopes) {
        if (object instanceof Map) {
            for (Object c : ((Map) object).values()) {
                writer = iteration.next(writer, c, scopes);
            }
            return writer;
        }
        return super.iterate(iteration, writer, object, scopes);
    }
}


