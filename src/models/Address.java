package models;

import java.util.List;

import iceblock.ann.*;

@Table(name="address")
public class Address {
	
	@Id(strategy=Id.ASSIGMENT)
	@Column(name="id_address")
	private Integer idAddress;
	
	@Column(name="street")
	private String street;

	@Column(name="number")
	private Integer number;
	
	@OneToMany(type=Person.class, attr="address")
	private List<Person> persons;

	public Integer getIdAddress() {
		return idAddress;
	}

	public void setIdAddress(Integer idAddress) {
		this.idAddress = idAddress;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public List<Person> getPersons() {
		return persons;
	}

	public void setPersons(List<Person> persons) {
		this.persons = persons;
	}
	
}
