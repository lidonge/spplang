package free.servpp.generator.general;


import free.servpp.lang.antlr.SppParser;

/**
 * @author lidong@date 2023-11-01@version 1.0
 */
public interface IRoleGenerator extends IEntityBodyGenerator{
    @Override
    default void enterRole(SppParser.RoleContext ctx) {
        boolean isContract = "Contract".equals(ctx.getChild(0).getText());
        String objName = ctx.getChild(1).getText();
//        System.out.println("enterRole:"+objName);
        generateClass(isContract ? CompilationUnitType.contract : CompilationUnitType.role, ctx, objName);
    }
}
