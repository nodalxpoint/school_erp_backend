package com.schoolerp.school_erp_backend.modules.params;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.schoolerp.school_erp_backend.common.response.PagedResponse;




@RestController
@RequestMapping("/api/param")
public class ParamController {
	
	@Autowired
	private ParamService paramService;
	
	@PostMapping("/list")
	public ResponseEntity<PagedResponse<ResponseDropdownOption>> getParamList(@RequestBody ParamListRequest request) {
	    PagedResponse<ResponseDropdownOption> response = paramService.getParamList(request);
	    return ResponseEntity.ok(response);
	}
}
