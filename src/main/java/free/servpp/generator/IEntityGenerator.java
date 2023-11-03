package free.servpp.generator;

import free.servpp.SppParser;

/**
 * @author lidong@date 2023-11-01@version 1.0
 */
public interface IEntityGenerator extends IObjectBodyGenerator{
    @Override
    default void enterObject(SppParser.ObjectContext ctx) {
//        String objOrRef = ctx.getChild(0).getText();
        boolean isReference = "Reference".equals(ctx.getChild(0).getText());
        String objName = ctx.getChild(1).getText();
//        System.out.println("enterObject:" + objName);

        generateClass(CompilationUnitType.entity, ctx, objName);
    }

}
