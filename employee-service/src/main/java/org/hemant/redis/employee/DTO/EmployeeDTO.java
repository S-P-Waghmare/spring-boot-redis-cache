package org.hemant.redis.employee.DTO;

import lombok.Data;

import java.io.Serializable;

@Data
public class EmployeeDTO  implements Serializable {

    private Long id;
    private String name;
    private String password;
    private String email;

}
