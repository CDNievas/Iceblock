package models;

import java.util.List;

import iceblock.ann.*;

@Table(name="occupation")
public class Occupation {
	
	@Id
	@Column(name="id_occupation")
	private Integer idOccupation;
	
	@Column(name="description")
	private String description;
	
	@OneToOne(name="id_type_occupation", fetchType=OneToOne.LAZY)
	private TypeOccupation typeOccupation;
	
	@ManyToMany(type=Person.class, hashTable="person_x_occupation", colIn="id_occupation", colOut="id_person")
	private List<Person> persons;

	public Integer getIdOccupation() {
		return idOccupation;
	}

	public void setIdOccupation(Integer idOccupation) {
		this.idOccupation = idOccupation;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public TypeOccupation getTypeOccupation() {
		return typeOccupation;
	}

	public void setTypeOccupation(TypeOccupation typeOccupation) {
		this.typeOccupation = typeOccupation;
	}

	public List<Person> getPersons() {
		return persons;
	}

	public void setPersons(List<Person> persons) {
		this.persons = persons;
	}
	
}

