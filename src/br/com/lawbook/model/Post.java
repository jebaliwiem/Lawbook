package br.com.lawbook.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

/**
 * @author Edilson Luiz Ales Junior
 * @version 30OCT2011-13 
 *
 */
@Entity(name="lwb_post")
public class Post implements Serializable {
	
	@Id
	@SequenceGenerator(name="lwb_post_seq_id", sequenceName="lwb_post_seq_id",allocationSize=1,initialValue=1)
    @GeneratedValue(generator="lwb_post_seq_id", strategy= GenerationType.SEQUENCE)
	private Long id;
	@ManyToOne @ForeignKey(name="FK_POST_PROFILE_SENDER")
	@JoinColumn(name="sender_id")
	private Profile sender;
	@ManyToOne @ForeignKey(name="FK_POST_PROFILE_RECEIVER")
	@JoinColumn(name="receiver_id")
	private Profile receiver;
	@Column(length = 255)
	private String content;
	private Calendar dateTime;
	@OneToMany @ForeignKey(name="FK_POST_COMMENT", inverseName="FK_COMMENT_POST") @LazyCollection(LazyCollectionOption.FALSE) 
	@JoinTable(name="lwb_post_comments", joinColumns = { @JoinColumn(name = "post_id") }, inverseJoinColumns = { @JoinColumn(name = "comment_id") })
	private List<Comment> comments;
	@Transient
	private static final long serialVersionUID = 497092916483761201L;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Profile getSender() {
		return sender;
	}
	public void setSender(Profile sender) {
		this.sender = sender;
	}
	public Profile getReceiver() {
		return receiver;
	}
	public void setReceiver(Profile receiver) {
		this.receiver = receiver;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Calendar getDateTime() {
		return dateTime;
	}
	public void setDateTime(Calendar dateTime) {
		if (Calendar.getInstance().compareTo(dateTime) < 0) {
			throw new IllegalArgumentException("Datetime parameter cannot be a future datetime");
		}
		this.dateTime = dateTime;
	}
	public List<Comment> getComments() {
		return comments;
	}
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	
}
