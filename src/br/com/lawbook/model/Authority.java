package br.com.lawbook.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

/**
 * @author Edilson Luiz Ales Junior
 * @version 24SEP2011-01 
 * 
 */
@Entity(name="lwb_authority")
public class Authority implements Serializable {

	@Id
	@GeneratedValue
	public Long id;
	@Column(length = 100)
	public String name;
	@Transient
	private static final long serialVersionUID = 1L;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
