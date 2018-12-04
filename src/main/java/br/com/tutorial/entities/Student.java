package br.com.tutorial.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
public class Student {

	@Id
	@GeneratedValue
	private Long id;
	
	@Column(length = 30)
	@NotBlank(message = "The name can't be blank")
	@Size(min = 2, max = 30, message = "The name must between 2 and 30 characters")	
	private String name;
	
	@Column(precision = 0)
	@NotBlank(message = "The age can't be blank")		
	private int age;	
	
	@Column(length = 100)
	@NotBlank(message = "The address can't be blank")
	@Size(min = 2, max = 100, message = "The address must between 2 and 100 characters")	
	private String address;

    @ManyToOne    
	private Institution institution;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Institution getInstitution() {
		return institution;
	}

	public void setInstitution(Institution institution) {
		this.institution = institution;
	}
	
}
