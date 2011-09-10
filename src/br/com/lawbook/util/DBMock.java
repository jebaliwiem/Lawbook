package br.com.lawbook.util;

import java.util.ArrayList;
import java.util.List;

import br.com.lawbook.model.*;

/**
 * @author Edilson Luiz Ales Junior
 * @version 10SEP2011-06
 * 
 */

public class DBMock {

	private static DBMock instance;
	private List<User> tableUser;
	private List<Profile> tableProfile;
	private List<Post> tablePost;
	private List<Comment> tableComment;
	private List<Location> tableLocation;

	public static DBMock getInstance() {
		if (instance == null) {
			instance = new DBMock();
		}
		return instance;
	}
	
	private DBMock() {
		tableLocation = new ArrayList<Location>();
		tableUser = new ArrayList<User>();
		tableProfile = new ArrayList<Profile>();
		tableComment = new ArrayList<Comment>();
		tablePost = new ArrayList<Post>();
		generateTables();
		applyDependencies();
	}

	private void generateTables() {
		DBMockPopulate dbp = new DBMockPopulate(tableUser, tableProfile, tablePost, tableComment, tableLocation);
		tableLocation = dbp.generateTableLocation();
		tableUser = dbp.generateTableUser();
		tableProfile = dbp.generateTableProfile();
		tableComment = dbp.generateTableComment();
		tablePost = dbp.generateTablePost();
	}
	
	private void applyDependencies() {
		for (int i = 0; i < tableProfile.size(); i++) {
			tableProfile.get(i).setLocation(tableLocation.get(i));
		}
		for (int i = 0; i < tablePost.size(); i++) {
			List<User> receivers = new ArrayList<User>();
			List<Comment> comments = new ArrayList<Comment>();
			tablePost.get(i).setSender(tableUser.get(i));
			receivers.add(tableUser.get(i));
			tablePost.get(i).setReceivers(receivers);
			comments.add(tableComment.get(i));
			tablePost.get(i).setComments(comments);
		}
		for (int i = 0; i < tableComment.size(); i++) {
			tableComment.get(i).setSender(tableUser.get(i));
		}
	}

	public List<User> getTableUser() {
		return tableUser;
	}

	public void setTableUser(List<User> tableUser) {
		this.tableUser = tableUser;
	}

	public List<Profile> getTableProfile() {
		return tableProfile;
	}

	public void setTableProfile(List<Profile> tableProfile) {
		this.tableProfile = tableProfile;
	}

	public List<Post> getTablePost() {
		return tablePost;
	}

	public void setTablePost(List<Post> tablePost) {
		this.tablePost = tablePost;
	}

	public List<Comment> getTableComment() {
		return tableComment;
	}

	public void setTableComment(List<Comment> tableComment) {
		this.tableComment = tableComment;
	}

	public List<Location> getTableLocation() {
		return tableLocation;
	}

	public void setTableLocation(List<Location> tableLocation) {
		this.tableLocation = tableLocation;
	}
	
}
