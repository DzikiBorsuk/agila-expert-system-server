package uni.tukl.cs.cps.agilaserver.domain;

public class Enums {

	public enum MathOperation {

		DIVISION("Division", "/", 1),
		MULTIPLICATION("Multiplication", "*", 2),
		ADDITION("Addition", "+", 3),
		SUBTRACTION("Subtraction", "-", 4),
		ROOT("Root", "sqrt", 5),
		EXPONENTIATION("Exponentiation", "^", 6),
		LESS_THAN("Less than", "<", 7),
		GREATER_THAN("Greater than", ">", 8),
		LESS_OR_EQUAL_THAN("Less or equal than", "<=", 9),
		GREATER_OR_EQUAL_THAN("Greater or equal than", ">=", 10);

		private String name;

		private String operation;

		private int code;

		MathOperation(String name, String operation, int code) {
			this.name = name;
			this.operation = operation;
			this.code = code;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	public enum EquationTag {
		CONSTANT("Constant", 1),
		VARIABLE("Variable", 2),
		OPERATION("Operation", 3);

		private String name;

		private int code;

		EquationTag(String name, int code){
			this.name = name;
			this.code = code;
		}

		@Override
		public String toString() {
			return name;
		}
	}

}
