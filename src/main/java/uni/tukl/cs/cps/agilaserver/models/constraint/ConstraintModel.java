package uni.tukl.cs.cps.agilaserver.models.constraint;

import uni.tukl.cs.cps.agilaserver.domain.Constraint;

import java.util.Date;

public class ConstraintModel {

	public ConstraintModel () { }

	public ConstraintModel (Constraint constraint) {
		this.name = constraint.getName();
		this.formula = constraint.getFormula();
		this.description = constraint.getDescription();
		this.output = constraint.getOutput();
		this.equation = new EquationModel(constraint.getEquation());
		this.id = constraint.getId();
		this.deleted = constraint.isDeleted();
		this.dateCreated = constraint.getDateCreated();
		this.createdBy = constraint.getUser().getFirstName() + " " + constraint.getUser().getLastName();
		this.dataType = constraint.getDataType();
	}

	private String id;

	private String name;

	private String formula;

	private String description;

	private String output;

	private boolean deleted;

	private Date dateCreated;

	private String createdBy;

	private EquationModel equation;

	private String dataType;

	public String getOutput() {
		return output;
	}

	public void setOutput(String output) {
		this.output = output;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}

	public EquationModel getEquation() {
		return equation;
	}

	public void setEquation(EquationModel equation) {
		this.equation = equation;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public Date getDateCreated() { return dateCreated; }

	public String getCreatedBy() { return createdBy; }

	public String getDataType() { return dataType; }

	public void setDataType(String dataType) { this.dataType = dataType; }
}
