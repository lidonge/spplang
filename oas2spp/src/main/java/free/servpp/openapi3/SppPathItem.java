package free.servpp.openapi3;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lidong@date 2023-11-26@version 1.0
 */
public class SppPathItem {
    String operationId;
    String modifier;
    Map<String,String> parameters = new HashMap<>();

    public String getModifier() {
        return modifier;
    }

    public void setModifier(String modifier) {
        this.modifier = modifier;
    }

    public String getOperationId() {
        return operationId;
    }

    public void setOperationId(String operationId) {
        this.operationId = operationId;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }
}
