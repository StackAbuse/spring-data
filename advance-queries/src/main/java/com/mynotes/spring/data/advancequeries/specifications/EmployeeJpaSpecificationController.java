package com.mynotes.spring.data.advancequeries.specifications;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mynotes.spring.data.advancequeries.common.SearchCriteria;

@RestController
@RequestMapping("spec")
public class EmployeeJpaSpecificationController {

    @Autowired
    private EmployeeSpecificationRepository employeeSpecificationRepository;

    @RequestMapping(method = RequestMethod.POST, value = "employee")
    public ResponseEntity<?> saveEmployee(@RequestBody final Employee employee) {

        employeeSpecificationRepository.save(employee);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @RequestMapping(method = RequestMethod.GET, value = "employees")
    public ResponseEntity<?> searchEmployees(@RequestParam(value = "search", required = false) final String search) {
        List<Employee> employeeList = new ArrayList<>();
        final EmployeeSearchSpecificationBuilder builder = new EmployeeSearchSpecificationBuilder();
        if (StringUtils.hasText(search)) {
            final Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(\\w+?),");
            final Matcher matcher = pattern.matcher(search + ",");
            while (matcher.find()) {
                builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
            }
            final Specification<Employee> spec = builder.build();
            employeeList = employeeSpecificationRepository.findAll(spec);
        }else {
        	employeeList = employeeSpecificationRepository.findAll();
        }
        return ResponseEntity.ok(employeeList);
    }

}
