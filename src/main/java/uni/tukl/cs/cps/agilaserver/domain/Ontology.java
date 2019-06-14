package uni.tukl.cs.cps.agilaserver.domain;

import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;

@Document
@Table(name = "ontologies")
public class Ontology {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private String id;

	private String name;

	@Lob
	private byte[] file;

	@Version
	private Long version;

	public Ontology() {
	}

	public Ontology(String name, byte[] file) {
		this.name = name;
		this.file = file;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}

	public String getId() {
		return id;
	}
}
