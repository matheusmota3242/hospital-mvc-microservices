package org.inpatient.unit.service.model;

import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;

import org.core.mvc.model.Internment;

@Entity
public class HospitalInternment extends Internment {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ElementCollection
	@CollectionTable(name = "clinical_conditions")
	private List<String> clinicalConditions;

	public List<String> getClinicalConditions() {
		return clinicalConditions;
	}

	public void setClinicalCondition(List<String> clinicalConditions) {
		this.clinicalConditions = clinicalConditions;
	}

}
