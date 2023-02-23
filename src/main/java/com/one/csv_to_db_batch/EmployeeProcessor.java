package com.one.csv_to_db_batch;

import org.springframework.batch.item.ItemProcessor;

public class EmployeeProcessor implements ItemProcessor<Employee, Employee>{

	@Override
	public Employee process(Employee item) throws Exception {
		Employee employee = new Employee();
		employee.setFirstName(item.getFirstName().toUpperCase());
		employee.setLastName(item.getLastName().toUpperCase());
		return employee;
	}

}
