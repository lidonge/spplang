package free.servpp.generator;

import free.servpp.SppParser;

/**
 * @author lidong@date 2023-11-01@version 1.0
 */
public interface IRoleGenerator extends IObjectBodyGenerator{
    @Override
    default void enterRole(SppParser.RoleContext ctx) {
//        String objOrRef = ctx.getChild(0).getText();
        String objName = ctx.getChild(1).getText();
//        System.out.println("enterRole:"+objName);
        generateClass(CompilationUnitType.role, ctx, objName);
    }
}
