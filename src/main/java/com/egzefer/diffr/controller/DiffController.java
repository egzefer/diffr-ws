package com.egzefer.diffr.controller;

import com.egzefer.diffr.model.Diff;
import com.egzefer.diffr.model.Side;
import com.egzefer.diffr.service.DiffService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.NoSuchElementException;

@RestController
public class DiffController {

	@PostMapping(value = "/v1/diff/{id}/{side}")
	public ResponseEntity<String> addDiff(
		@PathVariable String id, @PathVariable Side side, @RequestBody String content) {

		Diff diff = diffService.addDiff(id, side, content);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().buildAndExpand(diff.getId()).toUri();

		return ResponseEntity.created(location).build();
	}

	@GetMapping(value = "/v1/diff/{id}")
	public ResponseEntity<String> compare(@PathVariable String id) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return ResponseEntity.ok(mapper.writeValueAsString(diffService.compare(id)));
	}

	@GetMapping(value = "/v1/diff/{id}/{side}")
	public ResponseEntity<String> getDiffContent(@PathVariable String id, @PathVariable Side side) {
		return getDiffContent(id, side, false);
	}

	@GetMapping(value = "/v1/diff/{id}/{side}/decoded")
	public ResponseEntity<String> getDecodedDiffContent(@PathVariable String id, @PathVariable Side side) {
		return getDiffContent(id, side, true);
	}

	private ResponseEntity<String> getDiffContent(@PathVariable String id, Side side, boolean decoded) {
		try {
			String diffContent = diffService.getDiffContent(id, side);
			return ResponseEntity.ok(decoded ? diffService.decodeJsonContent(diffContent) : diffContent);
		} catch (NoSuchElementException nee) {
			return ResponseEntity.notFound().build();
		}
	}

	@Autowired
	private DiffService diffService;
}
