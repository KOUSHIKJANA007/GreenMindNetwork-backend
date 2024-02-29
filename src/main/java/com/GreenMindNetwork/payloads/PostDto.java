package com.GreenMindNetwork.payloads;

import java.util.Date;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
public class PostDto {

	private int id;
	@NotNull
	@Size(min=20,max=100,message = "Title must containt miniumn 20 letter and maximum 100 letter")
	private String title;
	@NotNull
	@Size(min=30,max=200,message = "Sub Title must containt miniumn 30 letter and maximum 200 letter")
	private String subTitle;
	@NotNull
	@Size(min=100,max=100000,message = "Content must containt miniumn 100 letter")
	private String content;
	private String imageName;
	private Date postDate;
	
	private UserDto user;
	
}
