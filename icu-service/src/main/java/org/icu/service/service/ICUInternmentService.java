package org.icu.service.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.core.mvc.enuns.TypeAction;
import org.core.mvc.exception.InternmentExcepion;
import org.core.mvc.exception.TransferException;
import org.core.mvc.model.dto.TransferDTO;
import org.icu.service.model.ICUInternment;
import org.icu.service.model.Procedure;
import org.icu.service.repository.ICUInternmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestTemplate;

@Service
public class ICUInternmentService {

	@Autowired
	private ICUInternmentRepository repository;

	@Autowired
	private RestTemplate restTemplate;

	@ResponseStatus(code = HttpStatus.CREATED)
	public void save(ICUInternment internment) {
		internment.setDateTime(LocalDateTime.now());
		repository.save(internment);
	}

	public List<ICUInternment> getAll() {
		return repository.findAll();
	}

	public void addProcedure(Integer id, Procedure procedure) throws InternmentExcepion {
		ICUInternment internment = repository.findById(id)
				.orElseThrow(() -> new InternmentExcepion(HttpStatus.NOT_FOUND,
						"Não há paciente internado correspondente ao 'patientId' passado"));
		procedure.setDateTime(LocalDateTime.now());
		internment.getProcedures().add(procedure);
		repository.save(internment);
	}

	public void transferPatient(TransferDTO transfer) throws TransferException, InternmentExcepion {
		ICUInternment internment = repository.findByPatientId(transfer.getPatientId())
				.orElseThrow(() -> new InternmentExcepion(HttpStatus.NOT_FOUND,
						"Não há paciente internado correspondente ao 'patientId' passado"));
		if (TypeAction.TO_INPATIENT_UNIT.equals(transfer.getTypeAction())) {
			Map<String, Integer> map = new HashMap<>();
			map.put("patientId", transfer.getPatientId());
			ResponseEntity<String> response;
			try {
				response = restTemplate.postForEntity("http://inpatient-unit-service", map, String.class);
			} catch (Exception e) {
				throw new TransferException(HttpStatus.SERVICE_UNAVAILABLE,
						"Não foi possível chamar o serviço de unidade de internação.");
			}

			if (!HttpStatus.CREATED.equals(response.getStatusCode())) {
				throw new TransferException(HttpStatus.SERVICE_UNAVAILABLE,
						"Não foi possível chamar o serviço de unidade de internação.");
			}

			internment.setStatus(false);
			repository.save(internment);
		}
	}
}
