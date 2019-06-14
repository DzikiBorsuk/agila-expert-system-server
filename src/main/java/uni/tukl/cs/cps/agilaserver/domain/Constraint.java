package uni.tukl.cs.cps.agilaserver.domain;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.util.Date;

@Document
@Table(name = "constraints")
public class Constraint {

	@javax.persistence.Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private String id;

	private String name;

	private String formula;

	private String description;

	private String output;

	private boolean deleted;

	private Equation equation;

	@DBRef
	private User user;

	@Version
	private Long version;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dateCreated;

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

	public Equation getEquation() {
		return equation;
	}

	public void setEquation(Equation equation) {
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

	public boolean isDeleted() { return deleted; }

	public void setDeleted(boolean deleted) { this.deleted = deleted; }

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getUserId() { return user.getId(); }

	@ManyToOne
	@JoinColumn(name = "user_id")
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getDataType() { return dataType; }

	public void setDataType(String dataType) { this.dataType = dataType; }
}
