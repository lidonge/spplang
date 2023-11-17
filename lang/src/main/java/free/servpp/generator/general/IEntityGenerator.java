package free.servpp.generator.general;


import free.servpp.lang.antlr.SppParser;

/**
 * @author lidong@date 2023-11-01@version 1.0
 */
public interface IEntityGenerator extends IEntityBodyGenerator{
    @Override
    default void enterEntity(SppParser.EntityContext ctx) {
//        String objOrRef = ctx.getChild(0).getText();
        boolean isReference = "Reference".equals(ctx.getChild(0).getText());
        String objName = ctx.getChild(1).getText();
//        System.out.println("enterEntity:" + objName);

        generateClass(isReference ? CompilationUnitType.reference: CompilationUnitType.entity, ctx, objName);
    }

}
