package com.GreenMindNetwork.payloads;

import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class UserEditDto {


	private int id;
	
	@NotNull(message = "First name must not be null")
	@Size(min=3,message = "Enter first name at least 3 character")
	private String fname;
	@NotNull(message = "Last name must not be null")
	@Size(min=3,message = "Enter last name at least 3 character")
	private String lname;
	
	@Email(message = "Enter valid email..")
//	@UniqueElements(message = "Email already exists, enter anothor one")
	private String email;
	@NotNull(message = "Mobile number must not be null")
	private long mobile;
	private String dob;
	private String imageName;
}
