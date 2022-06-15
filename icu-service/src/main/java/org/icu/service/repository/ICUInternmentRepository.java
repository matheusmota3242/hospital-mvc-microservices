package org.icu.service.repository;

import java.util.Optional;

import org.icu.service.model.ICUInternment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICUInternmentRepository extends JpaRepository<ICUInternment, Integer> {

	Optional<ICUInternment> findByPatientId(Integer patientId);
}
