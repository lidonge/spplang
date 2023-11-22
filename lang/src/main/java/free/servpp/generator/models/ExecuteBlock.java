package free.servpp.generator.models;

import free.servpp.generator.general.IConstance;
import free.servpp.generator.general.NameUtil;

import java.util.List;

/**
 * @author lidong@date 2023-11-18@version 1.0
 */
public class ExecuteBlock extends ServiceBaseBody implements IServiceBodyStatement{
    private IConstance.ScenarioType scenarioType;
    public List<IServiceBodyStatement> getExecuteCall() {
        return getSppServiceCallList();
    }

    public IConstance.ScenarioType getScenarioType() {
        return scenarioType;
    }

    public void setScenarioType(IConstance.ScenarioType scenarioType) {
        this.scenarioType = scenarioType;
    }

    public String getUpperType(){
        return NameUtil.firstToLowerCase(scenarioType.name(),false);
    }
    @Override
    public String toString() {
        return "ExecuteBlock{" +
                "scenarioType=" + scenarioType +
                ", sppServiceCallList=" + sppServiceCallList +
                '}';
    }
}
