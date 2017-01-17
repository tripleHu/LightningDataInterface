package cn.cqu.edu.LightningDataInterface.services.hibernate;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.HibernateTemplate;

public class BaseService {
	protected HibernateTemplate hibernateTemplate;

	@Autowired
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.hibernateTemplate = new HibernateTemplate(sessionFactory);
	}

	@SuppressWarnings("unchecked")
	protected Object getOne(String hql) {
		List list = hibernateTemplate.find(hql);
		if (list.size() > 0)
			return list.get(0);
		return null;
	}

	@SuppressWarnings("unchecked")
	protected Object getOne(String hql, Object obj) {
		List list = hibernateTemplate.find(hql, obj);
		if (list.size() > 0)
			return list.get(0);
		return null;
	}

	@SuppressWarnings("unchecked")
	protected Object getOne(String hql, Object[] objs) {
		List list = hibernateTemplate.find(hql, objs);
		if (list.size() > 0)
			return list.get(0);
		return null;
	}
}
