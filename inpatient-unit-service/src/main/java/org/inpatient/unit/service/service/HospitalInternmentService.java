package org.inpatient.unit.service.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.core.mvc.enuns.TypeAction;
import org.core.mvc.exception.InternmentExcepion;
import org.core.mvc.exception.TransferException;
import org.core.mvc.model.dto.TransferDTO;
import org.inpatient.unit.service.model.HospitalInternment;
import org.inpatient.unit.service.repository.HospitalInternmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

@Service
public class HospitalInternmentService {

	@Autowired
	private HospitalInternmentRepository repository;
	
	@Autowired
	private RestTemplate restTemplate;
	
	public HospitalInternment save(@Valid @RequestBody HospitalInternment internment) {
		internment.setDateTime(LocalDateTime.now());
		internment.setStatus(true);
		return repository.save(internment);
	}
	
	public HospitalInternment addClinicalCondition(Integer id, Map<String, String> map) throws InternmentExcepion {
		HospitalInternment internment = repository.findById(id)
				.orElseThrow(() -> new InternmentExcepion(HttpStatus.NOT_FOUND,
						"Não existe internação corresponde ao 'id' passado."));
		if (internment.getStatus()) {
			internment.getClinicalConditions().add(map.get("clinicalCondition"));
			return repository.save(internment);
		}
		throw new InternmentExcepion(HttpStatus.BAD_REQUEST, "Esse paciente não se encontra mais na unidade de internação.");
	}
	
	public void transferPatient(TransferDTO transfer) throws TransferException {
		
		if (TypeAction.TO_ICU.equals(transfer.getTypeAction())) {
			Map<String, Integer> map = new HashMap<>();
			map.put("patientId", transfer.getPatientId());
			ResponseEntity<String> response;
			try {
				response = restTemplate.postForEntity("http://icu-service", map,
						String.class);
			} catch (Exception e) {
				throw new TransferException(HttpStatus.SERVICE_UNAVAILABLE,
						"Não foi possível chamar o serviço de unidade de internação.");
			}
			
			if (!HttpStatus.CREATED.equals(response.getStatusCode())) {
				throw new TransferException(HttpStatus.SERVICE_UNAVAILABLE,
						"Não foi possível chamar o serviço de unidade de internação.");
			}
			HospitalInternment internment = repository.findByPatientId(transfer.getPatientId());
			internment.setStatus(false);
			repository.save(internment);
		}
	}
	
}
