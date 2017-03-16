package com.iisimpler.mysql2entity.domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * employees 
 * @author iisimpler  2017-03-16 23:50:02
*/
@Entity
@Table(name = "employees")
public class Employees implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "emp_no")
	private Integer empNo;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(name = "birth_date")
	private Date birthDate;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "gender")
	private String gender;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(name = "hire_date")
	private Date hireDate;

	public Integer getEmpNo() {
		return empNo;
	}

 	public void setEmpNo(Integer empNo) {
		this.empNo = empNo;
	}

	public Date getBirthDate() {
		return birthDate;
	}

 	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
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

	public String getGender() {
		return gender;
	}

 	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getHireDate() {
		return hireDate;
	}

 	public void setHireDate(Date hireDate) {
		this.hireDate = hireDate;
	}

	@Override
	public String toString() {
		return "Employees{empNo='+" + empNo + "',birthDate='+" + birthDate + "',firstName='+" + firstName + "',lastName='+" + lastName + "',gender='+" + gender + "',hireDate='+" + hireDate + "'}";
	}

}
