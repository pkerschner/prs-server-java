package com.maxtrain.bootcamp.requestline;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.maxtrain.bootcamp.request.RequestRepository;

@CrossOrigin
@RestController
@RequestMapping("/api/requestlines")
public class RequestlineController {
	
	@Autowired
	private RequestlineRepository reqlnRepo;
	
	@Autowired
	private RequestRepository reqRepo;
	
	@SuppressWarnings("rawtypes")
	private ResponseEntity recalculateRequestTotal(int requestId) {
		var reqOpt = reqRepo.findById(requestId);
		if(reqOpt.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		var request = reqOpt.get();
		var requestTotal = 0;
		for(var requestline : request.getRequestlines()) {
			requestTotal += requestline.getProduct().getPrice() * requestline.getQuantity();
		}
		request.setTotal(requestTotal);
		reqRepo.save(request);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@GetMapping
	public ResponseEntity<Iterable<Requestline>> getRequestlines() {
		var requestlines = reqlnRepo.findAll();
		return new ResponseEntity<Iterable<Requestline>>(requestlines, HttpStatus.OK);
	}
	
	@GetMapping("{id}")
	public ResponseEntity<Requestline> getRequestline(@PathVariable int id) {
		var requestline = reqlnRepo.findById(id);
		if(requestline.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Requestline>(requestline.get(), HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<Requestline> postRequestline(@RequestBody Requestline requestline) throws Exception {
		if(requestline == null || requestline.getId() != 0) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		reqlnRepo.save(requestline);
		var resEnt = this.recalculateRequestTotal(requestline.getRequest().getId());
		if(resEnt.getStatusCode() != HttpStatus.OK) {
			throw new Exception("Recalculate Request Total Failed");
		}
		return new ResponseEntity<Requestline>(requestline, HttpStatus.CREATED);
	}
	
	@SuppressWarnings("rawtypes")
	@PutMapping("{id}")
	public ResponseEntity putRequestline(@PathVariable int id, @RequestBody Requestline requestline) throws Exception {
		if(requestline == null || requestline.getId() != id) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		var reqlOpt = reqlnRepo.findById(requestline.getId());
		if(reqlOpt.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		reqlnRepo.save(requestline);
		var resEnt = this.recalculateRequestTotal(requestline.getRequest().getId());
		if(resEnt.getStatusCode() != HttpStatus.OK) {
			throw new Exception("Recalculate Request Total Failed");
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@SuppressWarnings("rawtypes")
	@DeleteMapping("{id}")
	public ResponseEntity deleteRequestline(@PathVariable int id, @RequestBody Requestline requestline) throws Exception {
		if(requestline == null || requestline.getId() != id) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		var reqlOpt = reqlnRepo.findById(requestline.getId());
		if(reqlOpt.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		reqlnRepo.delete(requestline);
		var resEnt = this.recalculateRequestTotal(requestline.getRequest().getId());
		if(resEnt.getStatusCode() != HttpStatus.OK) {
			throw new Exception("Recalculate Request Total Failed");
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
