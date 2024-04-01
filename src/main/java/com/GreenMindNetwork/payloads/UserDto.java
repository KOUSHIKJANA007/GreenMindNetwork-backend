package com.GreenMindNetwork.payloads;

import java.util.HashSet;
import java.util.Set;

import com.GreenMindNetwork.entities.BlockStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.UniqueElements;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {

	private int id;
	
	@NotNull(message = "First name must not be null")
	@Size(min=3,message = "Enter first name at least 3 character")
	private String fname;
	@NotNull(message = "Last name must not be null")
	@Size(min=3,message = "Enter last name at least 3 character")
	private String lname;
	
	@Email(message = "Enter valid email..")
	private String email;
	@NotNull(message = "Mobile number must not be null")
	private long mobile;
	private String dob;
	private String imageName;
	
	@Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[a-zA-Z]).{8,}$",message = "password must contain uppercase letter,symbol and number")
	@NotBlank(message = "Please enter password")
	@Size(min = 4,max=10)
	private String password;

	private String youtubeLink;
	private String twitterLink;
	private String facebookLink;
	private String instagramLink;
	
	@JsonIgnore
	public String getPassword() {
		return this.password;
	}
	
	@JsonProperty
	void setPassword(String password) {
		this.password=password;
	}
	private Set<RoleDto> roles=new HashSet<>();
	private Set<BlockStatusDto> status=new HashSet<>();
	
	
}
