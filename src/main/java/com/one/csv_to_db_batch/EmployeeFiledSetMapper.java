package com.one.csv_to_db_batch;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class EmployeeFiledSetMapper implements FieldSetMapper<Employee> {

	@Override
	public Employee mapFieldSet(FieldSet fieldSet) throws BindException {
		
		Employee employee = new Employee();
		employee.setFirstName(fieldSet.readString("firstName"));
		employee.setLastName(fieldSet.readString("lastName"));
		
		return employee;
	}

}
