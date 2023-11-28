package free.servpp.openapi3;

import io.swagger.oas.models.media.ObjectSchema;

/**
 * @author lidong@date 2023-11-25@version 1.0
 */
public class SppSchema extends ObjectSchema {
    private String sppType;

    public String getSppType() {
        return sppType;
    }

    public void setSppType(String sppType) {
        this.sppType = sppType;
    }
}
