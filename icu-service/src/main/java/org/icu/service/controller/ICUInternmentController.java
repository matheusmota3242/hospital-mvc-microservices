package org.icu.service.controller;

import java.time.LocalDateTime;
import java.util.List;

import javax.validation.Valid;

import org.core.mvc.exception.InternmentExcepion;
import org.core.mvc.exception.TransferException;
import org.core.mvc.model.dto.TransferDTO;
import org.icu.service.model.ICUInternment;
import org.icu.service.model.Procedure;
import org.icu.service.service.ICUInternmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class ICUInternmentController {

	@Autowired
	private ICUInternmentService service;
	
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public void save(@Valid @RequestBody ICUInternment internment) {
		internment.setDateTime(LocalDateTime.now());
		internment.setStatus(true);
		service.save(internment);
	}
	
	@GetMapping
	public List<ICUInternment> getAll() {
		return service.getAll();
	}
	
	
	@PutMapping(value = "{id}")
	public void addProcedure(@PathVariable Integer id, @Valid @RequestBody Procedure procedure) {
		try {
			service.addProcedure(id, procedure);
		} catch (InternmentExcepion e) {
			throw new ResponseStatusException(e.getStatus(), e.getMessage());
		}
	}
	
	@PostMapping(value = "transfer")
	public void refer(@Valid @RequestBody TransferDTO transfer) throws TransferException {
		try {
			service.transferPatient(transfer);
		} catch (TransferException | InternmentExcepion e) {
			throw new ResponseStatusException(e.getStatus(), e.getMessage());
		}
		
	}
}
