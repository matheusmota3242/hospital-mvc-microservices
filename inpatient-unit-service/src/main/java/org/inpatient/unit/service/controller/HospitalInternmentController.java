package org.inpatient.unit.service.controller;

import java.util.Map;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.MediaType;

import org.core.mvc.exception.InternmentExcepion;
import org.core.mvc.exception.TransferException;
import org.core.mvc.model.dto.TransferDTO;
import org.inpatient.unit.service.model.HospitalInternment;
import org.inpatient.unit.service.service.HospitalInternmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HospitalInternmentController {

	@Autowired
	private HospitalInternmentService service;
	
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public HospitalInternment save(@Valid @RequestBody HospitalInternment internment) {
		return service.save(internment);
	}
	
	@PutMapping("{id}/clinical-condition")
	@Consumes(value = MediaType.APPLICATION_JSON)
	@ResponseStatus(code = HttpStatus.CREATED)
	public HospitalInternment addClinicalCondition(@RequestBody Map<String, String> map, @PathVariable Integer id) throws InternmentExcepion {
		return service.addClinicalCondition(id, map);
	}
	
	public void transferPatient(@Valid @RequestBody TransferDTO transfer) throws TransferException {
		service.transferPatient(transfer);
	}
}
