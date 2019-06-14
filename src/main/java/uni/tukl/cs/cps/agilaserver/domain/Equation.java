package uni.tukl.cs.cps.agilaserver.domain;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Table;
import javax.persistence.Version;

@Document
@Table(name = "equations")
public class Equation {

	private Enums.MathOperation operation;

	private String constant;

	private String variable;

	private Equation firstElement;

	private Equation secondElement;

	private Enums.EquationTag tag;

	@Version
	private Long version;

	public Equation () {

	}

	public Equation (Enums.MathOperation operation, String constant) {
		this.operation = operation;
		this.constant = constant;
	}

	public Equation (Enums.MathOperation operation, String constant, String variable) {
		this(operation, constant);
		this.variable = variable;
	}

	public Equation (Enums.MathOperation operation, Equation firstElement) {
		this.operation = operation;
		this.firstElement = firstElement;
	}

	public Equation (Enums.MathOperation operation, Equation firstElement, Equation secondElement) {
		this(operation, firstElement);
		this.secondElement = secondElement;
	}

	public Enums.MathOperation getOperation() {
		return operation;
	}

	public void setOperation(Enums.MathOperation operation) {
		this.operation = operation;
	}

	public Equation getFirstElement() {
		return firstElement;
	}

	public void setFirstElement(Equation firstElement) {
		this.firstElement = firstElement;
	}

	public Equation getSecondElement() {
		return secondElement;
	}

	public void setSecondElement(Equation secondElement) {
		this.secondElement = secondElement;
	}

	public String getConstant() {
		return constant;
	}

	public void setConstant(String constant) {
		this.constant = constant;
	}

	public String getVariable() {
		return variable;
	}

	public void setVariable(String variable) {
		this.variable = variable;
	}

	public Enums.EquationTag getTag() {  return tag; }

	public void setTag(Enums.EquationTag tag) { this.tag = tag; }
}
