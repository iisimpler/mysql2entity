package com.iisimpler.mysql2entity.domain;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

/**
 * departments 部门表
 * @author iisimpler  2017-03-16 23:50:02
*/
@Entity
@Table(name = "departments")
public class Departments implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/** 部门编号 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "dept_no")
	private String deptNo;

	/** 部门名字 */
	@Column(name = "dept_name")
	private String deptName;

	public String getDeptNo() {
		return deptNo;
	}

 	public void setDeptNo(String deptNo) {
		this.deptNo = deptNo;
	}

	public String getDeptName() {
		return deptName;
	}

 	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	@Override
	public String toString() {
		return "Departments{deptNo='+" + deptNo + "',deptName='+" + deptName + "'}";
	}

}
