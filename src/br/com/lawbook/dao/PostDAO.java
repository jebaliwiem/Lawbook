package br.com.lawbook.dao;

import java.util.List;

import org.hibernate.HibernateException;

import br.com.lawbook.model.Post;

/**
 * @author Edilson Luiz Ales Junior
 * @version 18NOV2011-07
 * 
 */
public interface PostDAO {
	
	void create(Post post) throws HibernateException;
	
	void delete(Post post) throws HibernateException;

	Post getPostById(Long postId) throws HibernateException;
	
	List<Post> getStreamPosts(Long streamOwnerId, List<Long> sendersId, int first, int pageSize) throws HibernateException;
	
	List<Post> getProfileWall(Long wallOwnerId, Long authProfileId, int first, int pageSize) throws HibernateException;

	Long getPostsCount() throws HibernateException;

}
