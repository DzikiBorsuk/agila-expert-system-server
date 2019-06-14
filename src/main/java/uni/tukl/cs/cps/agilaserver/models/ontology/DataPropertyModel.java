package uni.tukl.cs.cps.agilaserver.models.ontology;

import org.semanticweb.owlapi.vocab.OWL2Datatype;
import uni.tukl.cs.cps.agilaserver.domain.Constraint;

public class DataPropertyModel extends PropertyModel {

	private String dataType;

	public DataPropertyModel() {

	}

	public DataPropertyModel(String name) {
		super();
		this.name = name;
	}

	public DataPropertyModel(String name, String dataType) {
		this(name);
		this.dataType = dataType;
	}

	public DataPropertyModel(String name, OWL2Datatype dataType) {
		this(name);
		this.dataType = dataType.name();
	}

	public DataPropertyModel(Constraint constraint) {
		this(constraint.getOutput(), constraint.getDataType());
		this.description = constraint.getDescription();
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public void setDataType(OWL2Datatype dataType) {
		this.dataType = dataType.name();
	}

}
