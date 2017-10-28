package models;

import java.util.List;

import iceblock.ann.*;

@Table(name="type_occupation")
public class TypeOccupation {
	
	@Id
	@Column(name="id_type_occupation")
	private Integer idTypeOccupation;
	
	@Column(name="description")
	private String description;
	
	@OneToMany(type=Occupation.class, attr="typeOccupation")
	private List<Occupation> occupations;

	public Integer getIdTypeOccupation() {
		return idTypeOccupation;
	}

	public void setIdTypeOccupation(Integer idTypeOccupation) {
		this.idTypeOccupation = idTypeOccupation;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Occupation> getOccupations() {
		return occupations;
	}

	public void setOccupations(List<Occupation> occupations) {
		this.occupations = occupations;
	}
	
}
