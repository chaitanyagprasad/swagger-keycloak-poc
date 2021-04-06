package io.github.chait.swaggerdemonew.controller;


import io.github.chait.swaggerdemonew.dto.EmployeeDTO;
import io.github.chait.swaggerdemonew.enums.Gender;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private final List<EmployeeDTO> EMPLOYEE = Arrays.asList(
            new EmployeeDTO("chait", Gender.MALE, 22),
            new EmployeeDTO("akki", Gender.FEMALE, 25),
            new EmployeeDTO("paddu", Gender.FEMALE, 27)

    );



    @PostMapping
    public ResponseEntity<Void> createEmployee(@RequestBody EmployeeDTO employeeDTO) {

        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        return ResponseEntity.ok(EMPLOYEE);
    }

    @GetMapping("/{name}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable("name") String name) {
        return ResponseEntity.ok(EMPLOYEE
                .stream()
                .filter(e -> e.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Unable to find user")));
    }


}
