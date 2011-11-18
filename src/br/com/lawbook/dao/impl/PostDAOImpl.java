package br.com.lawbook.dao.impl;

import java.util.List;
import java.util.logging.Logger;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;

import br.com.lawbook.business.ProfileService;
import br.com.lawbook.dao.PostDAO;
import br.com.lawbook.model.Post;
import br.com.lawbook.util.HibernateUtil;

/**
 * @author Edilson Luiz Ales Junior
 * @version 18NOV2011-09 
 * 
 */
public class PostDAOImpl implements PostDAO {
	
	private final static Logger LOG = Logger.getLogger("PostDAOImpl");
	
	@Override
	public void create(Post post) {
		Session session = HibernateUtil.getSession();
		Transaction tx = session.beginTransaction();
		try {
			session.save(post);
			tx.commit();
		} catch (Exception e) {
			LOG.severe(e.getMessage());
			tx.rollback();
			throw new HibernateException(e);
		} finally {
			session.close();
			LOG.info("Hibernate Session closed");
		}
	}
	
	@Override
	public void delete(Post post) {
		Session session = HibernateUtil.getSession();
		Transaction tx = session.beginTransaction();
		try {
			session.delete(post);
			tx.commit();
		} catch (Exception e) {
			LOG.severe(e.getMessage());
			tx.rollback();
			throw new HibernateException(e);
		} finally {
			session.close();
			LOG.info("Hibernate Session closed");
		}
	}

	@Override
	public Post getPostById(Long postId) throws HibernateException {
		Session session = HibernateUtil.getSession();
		try {
			return (Post) session.get(Post.class, postId);
		} catch (Exception e) {
			LOG.severe(e.getMessage());
			throw new HibernateException(e);
		} finally {
			session.close();
			LOG.info("Hibernate Session closed");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Post> getStreamPosts(Long streamOwnerId, List<Long> sendersId, int first, int pageSize) throws HibernateException {
		Long publicId = ProfileService.getInstance().getPublicProfile().getId();
		Session session = HibernateUtil.getSession();
		Transaction tx = session.beginTransaction();
		try {
			Query query;
			StringBuilder stringQuery = new StringBuilder("select p from lwb_post p where ");
			if (sendersId.isEmpty()) {
				stringQuery.append("sender_id = :myId or receiver_id = :myId order by datetime desc");
				query = session.createQuery(stringQuery.toString());
			} else {
				stringQuery.append("(sender_id in :sendersId and receiver_id = :publicId) or sender_id = :myId or receiver_id = :myId order by datetime desc");
				query = session.createQuery(stringQuery.toString());
				query.setParameter("publicId", publicId);
				query.setParameterList("sendersId", sendersId);
			}
			query.setParameter("myId", streamOwnerId);
			query.setFirstResult(first);
			query.setMaxResults(pageSize);
			
			List<Post> posts = (List<Post>) query.list();
			tx.commit();
			return posts;
		} catch (Exception e) {
			LOG.severe(e.getMessage());
			tx.rollback();
			throw new HibernateException(e);
		} finally {
			session.close();
			LOG.info("Hibernate Session closed");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Post> getProfileWall(Long wallOwnerId, Long authProfileId, int first, int pageSize) throws HibernateException {
		Long publicId = ProfileService.getInstance().getPublicProfile().getId();
		Session session = HibernateUtil.getSession();
		Transaction tx = session.beginTransaction();
		try {
			String stringQuery = "from lwb_post where (sender_id = :wallOwnerId and receiver_id = :publicId) " +
								 "or (sender_id = :wallOwnerId and receiver_id = :authProfileId) " +
								 "or (sender_id = :authProfileId and receiver_id = :wallOwnerId) order by datetime desc";
			Query query = session.createQuery(stringQuery);
			query.setParameter("wallOwnerId", wallOwnerId);
			query.setParameter("authProfileId", authProfileId);
			query.setParameter("publicId", publicId);
			query.setFirstResult(first);
			query.setMaxResults(pageSize);

			List<Post> posts = (List<Post>) query.list();
			tx.commit();
			return posts;
		} catch (Exception e) {
			LOG.severe(e.getMessage());
			tx.rollback();
			throw new HibernateException(e);
		} finally {
			session.close();
			LOG.info("Hibernate Session closed");
		}
	}

	@Override
	public Long getPostsCount() throws HibernateException {
		Session session = HibernateUtil.getSession();
		Transaction tx = session.beginTransaction();
		try {
			Criteria crit = session.createCriteria(Post.class);
			crit.setProjection(Projections.rowCount());
			Long count = (Long) crit.uniqueResult();
			tx.commit();
			return count;
		} catch (Exception e) {
			LOG.severe(e.getMessage());
			tx.rollback();
			throw new HibernateException(e);
		} finally {
			session.close();
			LOG.info("Hibernate Session closed");
		}
	}

}

