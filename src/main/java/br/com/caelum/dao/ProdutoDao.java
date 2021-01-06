package br.com.caelum.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.caelum.model.Loja;
import br.com.caelum.model.Produto;

@Repository
public class ProdutoDao {

	@PersistenceContext
	private EntityManager em;

	public List<Produto> getProdutos() {
		return em.createQuery("from Produto", Produto.class).getResultList();
	}

	public Produto getProduto(Integer id) {
		Produto produto = em.find(Produto.class, id);
		return produto;
	}

	@Transactional
	public List<Produto> getProdutos(String nome, Integer categoriaId, Integer lojaId) {
//		String jpql = "select p from Produto p ";
//		
//		if (categoriaId != null ) {
//			jpql += " join fetch p.categoria c where c.id = :pCategoriaId and ";
//		} else 
//			jpql += " where ";
//		
//		if (lojaId != null ) {
//			jpql += " p.loja.id = :pLojaId and ";
//		}
//		
//		if (!nome.isEmpty()) {
//			jpql += " p.nome like :pProdutoNome and ";
//		}
//		jpql += " 1  = 1 ";
//		
//		TypedQuery<Produto> query = em.createQuery(jpql, Produto.class);
//		
//		if (categoriaId != null )
//			query.setParameter("pCategoriaId", categoriaId);
//		
//		if (lojaId != null )
//			query.setParameter("pLojaId", lojaId);
//		
//		if (!nome.isEmpty())
//			query.setParameter("pProdutoNome", nome);
//		
//		return query.getResultList();
	
/**/		//  DIVISÃO DE MÉTODOS-------------- Criteria da JPA	

		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Produto> query = criteriaBuilder.createQuery(Produto.class);
		Root<Produto> root = query.from(Produto.class);

		Path<String> nomePath = root.<String> get("nome");
		Path<Integer> lojaPath = root.<Loja> get("loja").<Integer> get("id");
		Path<Integer> categoriaPath = root.join("categoria").<Integer> get("id");

		List<Predicate> predicates = new ArrayList<>();

		if (!nome.isEmpty()) {
			Predicate nomeIgual = criteriaBuilder.like(nomePath, "%" + nome + "%");
			predicates.add(nomeIgual);
		}
		if (categoriaId != null) {
			Predicate categoriaIgual = criteriaBuilder.equal(categoriaPath, categoriaId);
			predicates.add(categoriaIgual);
		}
		if (lojaId != null) {
			Predicate lojaIgual = criteriaBuilder.equal(lojaPath, lojaId);
			predicates.add(lojaIgual);
		}

		query.where((Predicate[]) predicates.toArray(new Predicate[0]));

		TypedQuery<Produto> typedQuery = em.createQuery(query);
		
		//Coloca o resultado desta consulta em cache
		typedQuery.setHint("org.hibernate.cacheable", "true");
		return typedQuery.getResultList();

		
/**/		//  DIVISÃO DE MÉTODOS-------------- Criteria da Hibernate		
//	    Session session = em.unwrap(Session.class);
//	    
//	    @SuppressWarnings("deprecation")
//		Criteria criteria = session.createCriteria(Produto.class);
//
//	    if (!nome.isEmpty()) {
//	        criteria.add(Restrictions.like("nome", "%" + nome + "%"));
//	    }
//
//	    if (lojaId != null) {
//	        criteria.add(Restrictions.like("loja.id", lojaId));
//	    }
//
//	    if (categoriaId != null) {
//	        criteria.setFetchMode("categorias", FetchMode.JOIN)
//	            .createAlias("categorias", "c")
//	            .add(Restrictions.like("c.id", categoriaId));
//	    }
//
//	    return (List<Produto>) criteria.list();
  
	}

	public void insere(Produto produto) {
		if (produto.getId() == null)
			em.persist(produto);
		else
			em.merge(produto);
	}

}
