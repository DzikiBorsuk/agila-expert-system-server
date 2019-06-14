package uni.tukl.cs.cps.agilaserver.models.ontology;

import java.util.List;

public class HierarchyModel {

	private String text;

	private List<HierarchyModel> nodes;

	public HierarchyModel(String text, List<HierarchyModel> nodes) {
		this.text = text;
		this.nodes = nodes;
	}

	public List<HierarchyModel> getNodes() {
		return nodes;
	}

	public String getText() {
		return text;
	}

	public void setNodes(List<HierarchyModel> nodes) {
		this.nodes = nodes;
	}

	public void setText(String text) {
		this.text = text;
	}

}
