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
 * dept_manager 
 * @author iisimpler  2017-03-16 23:50:02
*/
@Entity
@Table(name = "dept_manager")
public class DeptManager implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "emp_no")
	private Integer empNo;

	@Column(name = "dept_no")
	private String deptNo;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(name = "from_date")
	private Date fromDate;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(name = "to_date")
	private Date toDate;

	public Integer getEmpNo() {
		return empNo;
	}

 	public void setEmpNo(Integer empNo) {
		this.empNo = empNo;
	}

	public String getDeptNo() {
		return deptNo;
	}

 	public void setDeptNo(String deptNo) {
		this.deptNo = deptNo;
	}

	public Date getFromDate() {
		return fromDate;
	}

 	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

 	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	@Override
	public String toString() {
		return "DeptManager{empNo='+" + empNo + "',deptNo='+" + deptNo + "',fromDate='+" + fromDate + "',toDate='+" + toDate + "'}";
	}

}
