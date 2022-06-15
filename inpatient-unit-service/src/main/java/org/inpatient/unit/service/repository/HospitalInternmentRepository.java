package org.inpatient.unit.service.repository;

import org.inpatient.unit.service.model.HospitalInternment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalInternmentRepository extends JpaRepository<HospitalInternment, Integer> {

	HospitalInternment findByPatientId(Integer patientId);
}
