package models;

import java.util.List;

import iceblock.ann.*;

@Table(name="person")
public class Person {
	
	@Id(strategy=Id.ASSIGMENT)
	@Column(name="id_person")
	private Integer idPerson;
	
	@Column(name="name")
	private String name;
	
	@Column(name="age")
	private Integer age;
	
	@Column(name="weight")
	private Double weigth;
	
	@OneToOne(name="id_address", fetchType=OneToOne.EAGER)
	private Address address;

	@ManyToMany(type=Occupation.class, hashTable="person_x_occupation", colIn="id_person", colOut="id_occupation")
	private List<Occupation> occupations;

	public Integer getIdPerson() {
		return idPerson;
	}

	public void setIdPerson(Integer idPerson) {
		this.idPerson = idPerson;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Double getWeigth() {
		return weigth;
	}

	public void setWeigth(Double weigth) {
		this.weigth = weigth;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public List<Occupation> getOccupations() {
		return occupations;
	}

	public void setOccupations(List<Occupation> occupations) {
		this.occupations = occupations;
	} 
	
}
