package io.github.chait.swaggerdemonew.dto;

import io.github.chait.swaggerdemonew.enums.Gender;
import lombok.*;

@Getter@Setter
@Builder(setterPrefix = "with")
@AllArgsConstructor
public class EmployeeDTO {

    private String name;
    private Gender gender;
    private Integer age;

}
