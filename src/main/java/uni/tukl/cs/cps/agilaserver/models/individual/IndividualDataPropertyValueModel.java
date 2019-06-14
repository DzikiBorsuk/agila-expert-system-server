
package uni.tukl.cs.cps.agilaserver.models.individual;

import uni.tukl.cs.cps.agilaserver.models.ontology.DataPropertyModel;

public class IndividualDataPropertyValueModel {

    private DataPropertyModel property;

    private String value;

    public IndividualDataPropertyValueModel() {

    }

    public IndividualDataPropertyValueModel(DataPropertyModel property, String value) {
        this.property = property;
        this.value = value;
    }

    public DataPropertyModel getProperty() {
        return property;
    }

    public void setProperty(DataPropertyModel property) {
        this.property = property;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}