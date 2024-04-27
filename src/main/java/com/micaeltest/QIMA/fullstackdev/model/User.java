package com.micaeltest.QIMA.fullstackdev.model;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@NotEmpty
	@Column(nullable = false)
	private String firstName;

	private String lastName;

	@NotEmpty
	@Column(nullable = false, unique = true)
	@Email(message = "{Errors.invalid_email}")
	private String email;

	private String password;

	@ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	@JoinTable(name = "user_role", joinColumns = {
			@JoinColumn(name = "USER_ID", referencedColumnName = "ID") }, inverseJoinColumns = {
					@JoinColumn(name = "ROLE_ID", referencedColumnName = "ID") })
	private List<Role> roles;

	public User() {
	}

	public User(User user) {
		this.firstName = user.getFirstName();
		this.lastName = user.getLastName();
		this.email = user.getEmail();
		this.password = user.getPassword();
		this.roles = user.getRoles();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("User{");
		sb.append("id=").append(id);
		sb.append(", firstName='").append(firstName != null ? firstName : "N/A").append('\'');
		sb.append(", lastName='").append(lastName != null ? lastName : "N/A").append('\'');
		sb.append(", email='").append(email != null ? email : "N/A").append('\'');
		sb.append(", roles=")
				.append(roles != null ? roles.stream().map(Role::getName).collect(Collectors.toList()) : "None");
		sb.append('}');
		return sb.toString();
	}

}