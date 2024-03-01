package com.dailystudy.swinglab.service.batch;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.item.database.AbstractPagingItemReader;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.util.ClassUtils;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;

public class QuerydslPagingItemReader<T> extends AbstractPagingItemReader<T>
{

    protected final Map<String, Object> jpaPropertyMap = new HashMap<>();
    protected EntityManagerFactory entityManagerFactory;
    protected EntityManager entityManager;
    protected Function<JPAQueryFactory, JPAQuery<T>> queryFunction;
    protected boolean transacted = true;//default value

    protected QuerydslPagingItemReader ()
    {
        setName(ClassUtils.getShortName(QuerydslPagingItemReader.class));
    }

    public QuerydslPagingItemReader (EntityManagerFactory entityManagerFactory,
                                     int pageSize,
                                     Function<JPAQueryFactory, JPAQuery<T>> queryFunction)
    {
        this();
        this.entityManagerFactory = entityManagerFactory;
        this.queryFunction = queryFunction;
        setPageSize(pageSize);
    }

    public void setTransacted (boolean transacted)
    {
        this.transacted = transacted;
    }

    @Override
    protected void doOpen () throws Exception
    {
        super.doOpen();

        entityManager = entityManagerFactory.createEntityManager(jpaPropertyMap);
        if (entityManager == null)
        {
            throw new DataAccessResourceFailureException("Unable to obtain an EntityManager");
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void doReadPage ()
    {
        clearIfTransacted();

        JPAQuery<T> query = createQuery()
                .offset((long) getPage() * getPageSize())
                .limit(getPageSize());

        initResults();

        fetchQuery(query);
    }

    protected void clearIfTransacted ()
    {
        if (transacted)
        {
            entityManager.clear();
        }
    }

    protected JPAQuery<T> createQuery ()
    {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        return queryFunction.apply(queryFactory);
    }

    protected void initResults ()
    {
        if (CollectionUtils.isEmpty(results))
        {
            results = new CopyOnWriteArrayList<>();
        } else
        {
            results.clear();
        }
    }

    protected void fetchQuery (JPAQuery<T> query)
    {
        if (!transacted)
        {
            List<T> queryResult = query.fetch();
            for (T entity : queryResult)
            {
                entityManager.detach(entity);
                results.add(entity);
            }
        } else
        {
            results.addAll(query.fetch());
        }
    }

    public int getResultSize()
    {
        return results.size();
    }

    @Override
    protected void doClose () throws Exception
    {
        entityManager.close();
        super.doClose();
    }
}