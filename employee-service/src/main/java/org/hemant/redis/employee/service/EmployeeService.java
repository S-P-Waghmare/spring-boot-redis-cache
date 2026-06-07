package org.hemant.redis.employee.service;

import lombok.RequiredArgsConstructor;
import org.hemant.redis.employee.DTO.EmployeeDTO;
import org.hemant.redis.employee.model.Employee;
import org.hemant.redis.employee.repository.EmployeeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeService {



    private final EmployeeRepository employeeRepository;
    private final ModelMapper modelMapper;


    @Cacheable(cacheNames = "employee" , key = "#id")
    public EmployeeDTO getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee Not Found with Id " + id));
        return modelMapper.map(employee, EmployeeDTO.class);
    }


    @CachePut(cacheNames = "employee" , key = "#result.id")
    public EmployeeDTO create(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        employee.setName(employeeDTO.getName());
        employee.setEmail(employeeDTO.getEmail());
        employee.setPassword(employeeDTO.getPassword());
        Employee savedEmployee = employeeRepository.save(employee);
        return modelMapper.map(savedEmployee, EmployeeDTO.class);
    }


    @CachePut(cacheNames = "employee" , key = "#id")
    public EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee Not Found with id " + id));

        if (employeeDTO.getName() != null) employee.setName(employeeDTO.getName());
        if (employeeDTO.getEmail() != null) employee.setEmail(employeeDTO.getEmail());
        if (employeeDTO.getPassword() != null) employee.setPassword(employeeDTO.getPassword());

        Employee savedEmployee = employeeRepository.save(employee);
        return modelMapper.map(savedEmployee, EmployeeDTO.class);
    }

    @CacheEvict(cacheNames = "employee" , key = "#id")
    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new RuntimeException("Employee Not Found with id " + id);
        }
        employeeRepository.deleteById(id);
    }
}
