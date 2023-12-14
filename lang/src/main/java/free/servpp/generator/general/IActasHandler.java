package free.servpp.generator.general;

import free.servpp.generator.models.SppClass;
import free.servpp.generator.models.SppRoleField;
import free.servpp.generator.models.SppRoleMapper;
import free.servpp.generator.models.SppVarMaker;
import free.servpp.lang.antlr.SppParser;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;

/**
 * @author lidong@date 2023-11-03@version 1.0
 */
public interface IActasHandler extends ICompilationUnitHandler {
    @Override
    default void enterActas(SppParser.ActasContext ctx){
        List<TerminalNode> tokens = ctx.getTokens(SppParser.Identifier);
        String entityName = "";
        String roleName = getLastElement(tokens).getText();
//        System.out.println("entity : " + entityName + ", role:"+roleName);
        for(int i = 0;i< tokens.size() -1;i++){
            entityName += tokens.get(i).getText();
        }
        SppRoleMapper sppRoleMapper = (SppRoleMapper) generateClass(CompilationUnitType.rolemapper,ctx,
                "Mapper"+entityName+"To"+roleName);
        sppRoleMapper.setRole((SppClass) getSppDomain().getSppClass(CompilationUnitType.role,roleName));
        for(int i = 0;i< tokens.size() -1;i++) {
            sppRoleMapper.addEntity((SppClass) getSppDomain().getSppClass(CompilationUnitType.entity,
                    tokens.get(i).getText()));
        }
    }
    @Override
    default void exitActas(SppParser.ActasContext ctx){

    }
    @Override
    default void enterMapBody(SppParser.MapBodyContext ctx){

    }
    @Override
    default void exitMapBody(SppParser.MapBodyContext ctx){

    }
    @Override
    default void enterMapDeclaration(SppParser.MapDeclarationContext ctx){
    }
    @Override
    default void exitMapDeclaration(SppParser.MapDeclarationContext ctx){

    }
    default void enterMapfield(SppParser.MapfieldContext ctx){
        String type = ctx.getChild(0).getText();
        String roleName = ctx.getToken(SppParser.Identifier,0).getText();
        String entityName = ctx.getToken(SppParser.Identifier,1).getText();
//        System.out.println(type + "," +roleName+"," +entityName);

        SppVarMaker<SppClass, String> maker = (cls, name) ->{return new SppRoleField(cls,name);};
        String err = generateField(type,roleName,maker);
        SppRoleField roleField = (SppRoleField) ((SppRoleMapper) getSppDomain().getCurrentClass()).getField(roleName);
        roleField.setEntityName(entityName);
        logSppError(ctx,err);
    }
    default void exitMapfield(SppParser.MapfieldContext ctx){

    }

    @Override
    default void enterTakeall(SppParser.TakeallContext ctx) {
        SppRoleMapper sppRoleMapper = (SppRoleMapper) getSppDomain().getCurrentClass();
        sppRoleMapper.setTakeAll(true);
    }

    @Override
    default void exitTakeall(SppParser.TakeallContext ctx) {

    }

    @Override
    default void enterMapsame(SppParser.MapsameContext ctx) {
        SppRoleMapper sppRoleMapper = (SppRoleMapper) getSppDomain().getCurrentClass();
        sppRoleMapper.setMapSame(true);
    }

    @Override
    default void exitMapsame(SppParser.MapsameContext ctx) {

    }


}
