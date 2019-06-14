package uni.tukl.cs.cps.agilaserver.models.dashboard;

public class DashboardModel {

	private long individuals;

	private long classes;

	private long objectProperties;

	private long dataProperties;

	public long getIndividuals() {
		return individuals;
	}

	public void setIndividuals(long individuals) {
		this.individuals = individuals;
	}

	public long getClasses() {
		return classes;
	}

	public void setClasses(long classes) {
		this.classes = classes;
	}

	public long getObjectProperties() {
		return objectProperties;
	}

	public void setObjectProperties(long objectProperties) {
		this.objectProperties = objectProperties;
	}

	public long getDataProperties() {
		return dataProperties;
	}

	public void setDataProperties(long dataProperties) {
		this.dataProperties = dataProperties;
	}
}
