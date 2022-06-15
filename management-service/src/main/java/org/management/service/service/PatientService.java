package org.management.service.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.core.mvc.enuns.TypeAction;
import org.core.mvc.exception.TransferException;
import org.core.mvc.model.dto.TransferDTO;
import org.management.service.PatientNotFoundException;
import org.management.service.model.ActionRecord;
import org.management.service.model.Patient;
import org.management.service.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PatientService {

	@Autowired
	private PatientRepository repository;

	@Autowired
	private RestTemplate restTemplate;

	private static final String ICU_URL = "http://icu-service";
	private static final String INPATIENT_UNIT_URL = "http://inpatient-unit-service";
	
	private static final String PATIENT_NOT_FOUND_MESSAGE = "Não existe paciente correspondente ao 'id' passado.";
	private static final String TRANSFER_EXCEPTION_MESSAGE = "Ocorreu um problema ao tentar chamar o serviço de UTI.";
	
	public Patient save(Patient patient) {
		patient.getRecords().add(new ActionRecord(TypeAction.CHECKIN));
		return repository.save(patient);
	}

	public List<Patient> getAll() {
		return repository.findAll();
	}

	public Patient addActionRecord(ActionRecord actionRecord, Integer patientId) throws PatientNotFoundException {
		Patient patient = repository.findById(patientId)
				.orElseThrow(() -> new PatientNotFoundException(HttpStatus.NOT_FOUND,
						PATIENT_NOT_FOUND_MESSAGE));
		actionRecord.setDateTime(LocalDateTime.now());
		patient.getRecords().add(actionRecord);
		actionRecord.setPatient(patient);
		return repository.save(patient);
	}

	public void transferPatient(TransferDTO transfer) throws PatientNotFoundException, TransferException {
		Optional<Patient> optionalPatient = repository.findById(transfer.getPatientId());
		if (optionalPatient.isPresent()) {
			if (TypeAction.TO_ICU.equals(transfer.getTypeAction())) {
				callHospitalService(transfer, ICU_URL);
			} else if (TypeAction.TO_INPATIENT_UNIT.equals(transfer.getTypeAction())) {
				callHospitalService(transfer, INPATIENT_UNIT_URL);
			}
			addActionRecord(new ActionRecord(transfer.getTypeAction()), transfer.getPatientId());
		} else {
			throw new PatientNotFoundException(HttpStatus.NOT_FOUND,
					PATIENT_NOT_FOUND_MESSAGE);
		}
	}

	private void callHospitalService(TransferDTO transfer, String url) throws TransferException {
		Map<String, Integer> map = new HashMap<>();
		map.put("patientId", transfer.getPatientId());
		ResponseEntity<String> response;
		try {
			response = restTemplate.postForEntity(url, map, String.class);
		} catch (Exception e) {
			throw new TransferException(HttpStatus.SERVICE_UNAVAILABLE,
					TRANSFER_EXCEPTION_MESSAGE);
		}
		if (!HttpStatus.CREATED.equals(response.getStatusCode())) {
			throw new TransferException(response.getStatusCode(),
					TRANSFER_EXCEPTION_MESSAGE);
		}
	}

}
