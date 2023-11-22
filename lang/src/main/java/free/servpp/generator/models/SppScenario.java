package free.servpp.generator.models;

import free.servpp.generator.general.IConstance;

/**
 * @author lidong@date 2023-11-17@version 1.0
 */
public class SppScenario extends SppService {
    private IConstance.ScenarioType scenarioType;
    public SppScenario(String objName) {
        super(objName);
    }

    public IConstance.ScenarioType getScenarioType() {
        return scenarioType;
    }

    public void setScenarioType(IConstance.ScenarioType scenarioType) {
        this.scenarioType = scenarioType;
    }
}
