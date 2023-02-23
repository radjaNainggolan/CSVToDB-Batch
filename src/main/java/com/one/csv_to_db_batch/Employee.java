package com.one.csv_to_db_batch;

public class Employee {

	private String firstName;
	private String lastName;
	
	public Employee() {
		super();
	}

	public Employee(String firstName, String lastname) {
		super();
		
		this.firstName = firstName;
		this.lastName = lastname;
	}

	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
	public String toString() {
		return "Employee [ firstName=" + firstName + ", lastname=" + lastName + "]";
	}
	
	
	
	
}
