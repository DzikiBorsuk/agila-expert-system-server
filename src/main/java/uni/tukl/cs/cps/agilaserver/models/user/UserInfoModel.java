package uni.tukl.cs.cps.agilaserver.models.user;

import uni.tukl.cs.cps.agilaserver.domain.User;

public class UserInfoModel {

	private String email;

	private String firstName;

	private String lastName;

	public UserInfoModel(User user) {
		this.email = user.getEmail();
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
	}

	public String getEmail() {
		return email;
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
