package org.hibernate.test.annotations.derivedidentities.e1.c;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author Emmanuel Bernard
 */
@Entity
public class Employee {
	@Id
	long empId;
	String empName;
}
