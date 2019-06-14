package uni.tukl.cs.cps.agilaserver.validators;

import uni.tukl.cs.cps.agilaserver.models.ontology.PropertyModel;

import java.util.List;

public abstract class PropertyValidator {

    protected boolean findProperty(List<? extends PropertyModel> objectProperties, String propertyName) {
        if (objectProperties.stream().anyMatch(p -> p.getName().equals(propertyName))) {
            return true;
        } else {
            for (PropertyModel objectPrpoerty : objectProperties) {
                if (objectPrpoerty.getSubProperties() != null
                        && findProperty(objectPrpoerty.getSubProperties(), propertyName)) {
                    return true;
                }
            }
        }
        return false;
    }

}
