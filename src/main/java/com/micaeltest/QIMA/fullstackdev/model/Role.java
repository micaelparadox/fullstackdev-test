package com.micaeltest.QIMA.fullstackdev.model;

import java.util.List;
import javax.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@ToString(exclude = "users") // Excludes the 'users' field to avoid potential recursive toString() calls
@Table(name = "roles")
public class Role {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	@Column(nullable = false, unique = true)
	@NonNull
	private String name;

	@ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY) // Changed to LAZY to avoid eager fetching loop
	private List<User> users;
}