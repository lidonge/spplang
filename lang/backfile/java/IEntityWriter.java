package free.servpp.generator.java;

import free.servpp.generator.general.IConstance;
import free.servpp.generator.general.IFileGenerator;
import free.servpp.generator.models.SppClass;
import free.servpp.generator.models.SppField;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * @author lidong@date 2023-11-15@version 1.0
 */
public interface IEntityWriter extends IClassWriter{
    default void genObject(SppClass sppClass) throws FileNotFoundException {
        genClassDefine(sppClass);
        List<SppField> sppFieldList = sppClass.getSppFieldList();
        for (SppField field : sppFieldList) {
            String sppType = field.getType().getName();
            if (field.getType().getType() == null)
                sppType = IFileGenerator.toJavaPrimaryType(sppType);
            printlnFromBeg("public " + sppType + " " + field.getName() + ";");
        }
        println("}");
    }
}
