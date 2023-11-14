package free.servpp.generator.openapi;

import io.swagger.v3.oas.models.Operation;

/**
 * @author lidong@date 2023-11-14@version 1.0
 */
public class MyOperation extends Operation {
    private String method;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
