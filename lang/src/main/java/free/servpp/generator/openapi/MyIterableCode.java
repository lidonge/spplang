package free.servpp.generator.openapi;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheException;
import com.github.mustachejava.TemplateContext;
import com.github.mustachejava.codes.IterableCode;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author lidong@date 2023-11-14@version 1.0
 */
public class MyIterableCode extends IterableCode {
    private String header;

    public MyIterableCode(TemplateContext tc, DefaultMustacheFactory df, Mustache mustache, String variable, String type) {
//        String var = header(this,variable);
        super(tc, df, mustache, variable, type);
    }
    public MyIterableCode(TemplateContext tc, DefaultMustacheFactory df, Mustache mustache, String variable) {
        super(tc, df, mustache, variable);
    }

    public MyIterableCode(String header, TemplateContext tc, DefaultMustacheFactory df, Mustache mustache, String variable) {
        this(tc, df, mustache, variable);
        this.header = header;
    }
    @Override
    protected Writer handle(Writer writer, Object resolved, List<Object> scopes) {
        if (resolved != null && header != null) {
            int size = 0;
            Method method = null;
            try {
                method = resolved.getClass().getMethod("size");
                size = (int) method.invoke(resolved);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            }
            try {
                if(size != 0 || method == null)
                    writer.write(header);
            } catch (IOException e) {
                throw new MustacheException("Failed to write header", e, tc);
            }
        }
        return super.handle(writer,resolved,scopes);
    }
}
