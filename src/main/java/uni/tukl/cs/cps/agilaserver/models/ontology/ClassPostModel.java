package uni.tukl.cs.cps.agilaserver.models.ontology;

public class ClassPostModel {

	private String name;

	private String parentName;

	public ClassPostModel() {
	}

	public ClassPostModel(String name, String parentName) {
		this.name = name;
		this.parentName = parentName;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
