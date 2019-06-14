package uni.tukl.cs.cps.agilaserver.models.signUp;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class SignUpModel {

	@NotNull
	@Email
	private String email;

	@NotNull
	@Min(10)
	private String password;

	@NotNull
	private String firstName;

	@NotNull
	private String lastName;

	public SignUpModel() {

	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
}
