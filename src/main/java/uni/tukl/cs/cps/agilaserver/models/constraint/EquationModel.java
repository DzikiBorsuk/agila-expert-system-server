package uni.tukl.cs.cps.agilaserver.models.constraint;

import uni.tukl.cs.cps.agilaserver.domain.Enums;
import uni.tukl.cs.cps.agilaserver.domain.Equation;

public class EquationModel {

	private Enums.EquationTag tag;

	private Enums.MathOperation operation;

	private String constant;

	private String variable;

	private EquationModel firstElement;

	private EquationModel secondElement;

	public EquationModel () {

	}

	public EquationModel (Equation equation) {
		this.tag = equation.getTag();
		switch (this.tag) {
			case CONSTANT:
				this.constant = equation.getConstant();
				break;
			case VARIABLE:
				this.variable = equation.getVariable();
				break;
			case OPERATION:
				this.operation = equation.getOperation();
				this.firstElement = new EquationModel(equation.getFirstElement());
				this.secondElement = new EquationModel(equation.getSecondElement());
				break;
			default:
				throw new IllegalArgumentException("Invalid tag");
		}
	}

	public EquationModel (Enums.MathOperation operation, String constant) {
		this.operation = operation;
		this.constant = constant;
	}

	public EquationModel (Enums.MathOperation operation, String constant, String variable) {
		this(operation, constant);
		this.variable = variable;
	}

	public EquationModel (Enums.MathOperation operation, EquationModel firstElement) {
		this.operation = operation;
		this.firstElement = firstElement;
	}

	public EquationModel (Enums.MathOperation operation, EquationModel firstElement, EquationModel secondElement) {
		this(operation, firstElement);
		this.secondElement = secondElement;
	}

	public Enums.EquationTag getTag() {
		return tag;
	}

	public void setTag(Enums.EquationTag tag) {
		this.tag = tag;
	}

	public Enums.MathOperation getOperation() {
		return operation;
	}

	public void setOperation(Enums.MathOperation operation) {
		this.operation = operation;
	}

	public EquationModel getFirstElement() {
		return firstElement;
	}

	public void setFirstElement(EquationModel firstElement) {
		this.firstElement = firstElement;
	}

	public EquationModel getSecondElement() {
		return secondElement;
	}

	public void setSecondElement(EquationModel secondElement) {
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
}
