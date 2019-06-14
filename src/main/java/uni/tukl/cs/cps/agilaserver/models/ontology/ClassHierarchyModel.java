package uni.tukl.cs.cps.agilaserver.models.ontology;

import java.util.List;

public class ClassHierarchyModel extends HierarchyModel {

	private String description;

	private int id;

	public ClassHierarchyModel(String text, String description, List<HierarchyModel> nodes) {
		super(text, nodes);
		this.description = description;
		this.id = text.hashCode(); //fixme: find a better way to return id
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getId() {
		return id;
	}
}
