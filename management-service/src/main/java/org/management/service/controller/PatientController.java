package org.management.service.controller;

import java.util.List;

import javax.validation.Valid;

import org.core.mvc.exception.TransferException;
import org.core.mvc.model.dto.TransferDTO;
import org.management.service.PatientNotFoundException;
import org.management.service.model.ActionRecord;
import org.management.service.model.Patient;
import org.management.service.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("patient")
public class PatientController {

	@Autowired
	private PatientService service;
	
	@PostMapping
	public Patient save(@Valid @RequestBody Patient patient) {
		return service.save(patient); 
	}
	
	@GetMapping
	public List<Patient> getAll() {
		return service.getAll();
	}
	
	@PutMapping("{patientId}")
	public Patient addActionRecord(@Valid @RequestBody ActionRecord actionRecord, @PathVariable Integer patientId) {
		try {
			return service.addActionRecord(actionRecord, patientId);
		} catch (PatientNotFoundException e) {
			throw new ResponseStatusException(e.getStatus(), e.getSpecificMessage());
		}
		
	}
	
	@PostMapping("transfer")
	@ResponseStatus(code = HttpStatus.CREATED)
	public void transferPatient(@Valid @RequestBody TransferDTO transfer) {
		try {
			service.transferPatient(transfer);
		} catch (PatientNotFoundException | TransferException e) {
			throw new ResponseStatusException(e.getStatus(), e.getSpecificMessage());
		}
	}
}
