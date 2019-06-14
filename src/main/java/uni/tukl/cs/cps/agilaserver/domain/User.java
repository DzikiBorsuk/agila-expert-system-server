package uni.tukl.cs.cps.agilaserver.domain;

import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Document
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private String id;

	private String firstName;

	private String lastName;

	private String email;

	private String password;

    private Set<Role> roles;

	@Version
	private Long version;

	@DBRef
	private Company company;

	@DBRef
	private List<Constraint> constraints;

	public User(String firstName, String lastName, String email, String password) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getId() {
		return id;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@OneToMany(mappedBy = "Constraint", cascade = CascadeType.ALL)
	public List<Constraint> getConstraints() {
		return constraints;
	}
}
