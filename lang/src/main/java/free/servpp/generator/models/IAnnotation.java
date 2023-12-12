package free.servpp.generator.models;

import free.servpp.generator.models.app.AnnotationDefine;

import java.util.List;

/**
 * @author lidong@date 2023-12-05@version 1.0
 */
public interface IAnnotation {
    List<AnnotationDefine> getAnnotations();
    void setAnnotations(List<AnnotationDefine> annotations);
}
