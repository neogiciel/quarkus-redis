package com.neogiciel.service;

//import java.util.ArrayList;
import java.util.List;

    
import com.neogiciel.util.Trace;

import io.quarkus.cache.CacheInvalidate;
import io.quarkus.cache.CacheName;
import io.quarkus.cache.CacheResult;
import io.quarkus.cache.Cache;

import com.neogiciel.model.Personne;
import com.neogiciel.model.Service;
import com.neogiciel.model.ServicePersonne;
import com.neogiciel.redis.RedisManager;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.Cacheable;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;


@ApplicationScoped
public class BddManagerImpl implements BddManager{
    
    /*
     * EntityManager
     */
    @PersistenceContext
    EntityManager em;

    public String test(){
        Trace.info("[BddManager] test ");
        return "test";
    }

    public String redis(){
        Trace.info("[BddManager] redis ");
        return "redis";
    }

    public String invalidate(){
        Trace.info("[BddManager] invalidate ");
        return "invalidate";
    }
	

    /*
     * getListPersonnes
     */
    public List<Personne> getListPersonnes(){
        Trace.info("[BddManager] select * from PERSONNE");
        @SuppressWarnings("unchecked")
        List<Personne> liste =  em.createNativeQuery("select * from PERSONNE",Personne.class).getResultList();
        return liste;
	}
	
	/*
     * getListPersonnesSQL
     */
    public List<Personne> getListPersonnesSQL(){
	    return getListPersonnes();
    }

	/*
     * addPersonne
     */
    @Transactional
    public Long addPersonne(Personne personne){
        Trace.info("[BddManager] addPersonne");
        em.persist(personne);
        em.flush();
        Trace.info("[BddManager] id = "+ personne.id);
    	return personne.id;
    }

	/*
     * addPersonneSQL
     */
    @Transactional
    public Long addPersonneSQL(Personne personne){
        Trace.info("[BddManager] addPersonneSQL");
        String sql = "INSERT INTO PERSONNE(PRENOM,NOM,AGE) values ('" + personne.prenom +"','"+ personne.nom +"','"+ personne.age+"')";
        em.createNativeQuery(sql,Personne.class).executeUpdate();
        Long id = (Long) em.createNativeQuery("SELECT LAST_INSERT_ID() AS ID FROM PERSONNE WHERE ID = LAST_INSERT_ID()").getSingleResult();
        return id;
    }

	/*
     * getPersonneFromId
     */
    public Personne getPersonneFromId(int Id){
        Trace.info("[BddManager] getPersonnesFromId = "+Id);
	    Personne personne = (Personne) em.find(Personne.class, Id);
        Trace.info("[BddManager] nom = "+personne.nom);
        Trace.info("[BddManager] prenom = "+personne.prenom);
        Trace.info("[BddManager] age = "+personne.age);
         return personne;
    }
	
	/*
     * getPersonnesFromIdSQL
     */
    public Personne getPersonneFromIdSQL(int Id){
        Trace.info("[BddManager] getPersonnesFromId = "+Id);
		Personne personne = (Personne) em.createNativeQuery("SELECT * FROM PERSONNE WHERE ID = "+ Id, Personne.class).getSingleResult();
        Trace.info("[BddManager] nom = "+personne.nom);
        Trace.info("[BddManager] nom = "+personne.prenom);
        Trace.info("[BddManager] nom = "+personne.age);
        return personne;
    }

    /*
     * deletePersonne
     */
    @Transactional
    public void deletePersonne(int id){
        Trace.info("[BddManager] deletePersonne = "+ id);
        Personne personne = getPersonneFromId(id);
		em.remove(personne);
        em.flush();
    }

    /*
     * deletePersonne
     */
    @Transactional
	public void deletePersonneSQL(int id){
		Trace.info("[BddManager] deletePersonne = "+ id);
		String sql = "DELETE FROM PERSONNE WHERE id = "+ id;
		em.createNativeQuery(sql).executeUpdate();
    }

    /*
     * updatePersonne
     */
    @Transactional
	public void updatePersonne(Personne personne){
        Trace.info("[BddManager] updatePersonne");
	    em.merge(personne);
    }
	
    /*
     * updatePersonne
     */
    @Transactional
	public void updatePersonneSQL(Personne personne){
        Trace.info("[BddManager] updatePersonneSQL");
        String sql = "UPDATE PERSONNE "
				+ "SET nom = '"+personne.nom+"',"
				+ "prenom = '"+personne.prenom+"',"
				+ "age = '"+personne.age+"' "
				+ "WHERE id = "+personne.id;

	    em.createNativeQuery(sql).executeUpdate();
    }
 
    /*
     * getListServices
     */
    public List<Service> getListServices(){
        Trace.info("[BddManager] select * from PERSONNE");
        @SuppressWarnings("unchecked")
        List<Service> liste =  em.createNativeQuery("select * from SERVICE",Service.class).getResultList();
        return liste;
	}

 	/*
     * getPersonneFromId
     */
	public Service getServiceFromId(int Id){
        Trace.info("[BddManager] getServiceFromId = "+Id);
	    Service service = (Service) em.find(Service.class, Id);
        return service;
    }
	

 	/*
     * addService
     */
    @Transactional
    public Long addService(Service service){
        Trace.info("[BddManager] addService");
        em.persist(service);
        em.flush();
        Trace.info("[BddManager] id = "+ service.id);
    	return service.id;
    }

    /*
     * updatePersonne
     */
    @Transactional
	public void updateService(Service service){
        Trace.info("[BddManager] updateService");
	    em.merge(service);
    }

    /*
     * deletePersonne
     */
    @Transactional
    public void deleteService(int id){
        Trace.info("[BddManager] deletePersonne = "+ id);
        Service service = getServiceFromId(id);
		em.remove(service);
        em.flush();
    }

    /*
     * getListServices
     */
    public List<ServicePersonne> getListPersonneFromServices(int idservice){
        Trace.info("[BddManager] getListPersonneFromServices = "+idservice);
      
        @SuppressWarnings("unchecked")
        //List<ServicePersonne> liste =  em.createNativeQuery("select P.* FROM SERVICEPERSONNE SP, PERSONNE P WHERE SP.IDSERVICE = '"+idservice+"' AND SP.ID = P.ID;",ServicePersonne.class).getResultList();
        List<ServicePersonne> liste =  em.createNativeQuery("select SP.ID,SP.IDSERVICE,SP.IDPERSONNE,P.PRENOM,P.NOM,P.AGE,S.LABEL FROM SERVICEPERSONNE SP, PERSONNE P, SERVICE S WHERE SP.IDSERVICE = "+ idservice +" AND SP.IDPERSONNE = P.ID AND SP.IDSERVICE = S.IDSERVICE",ServicePersonne.class).getResultList();

        /*Trace.info("[BddManager] Nb liste = "+ liste.size());
        for (int i= 0; i < liste.size(); i++ ){
            ServicePersonne sp = liste.get(i);
            Trace.info("[BddManager] idpersonne = "+ sp.getPersonne().id);
            Trace.info("[BddManager] Nom = "+ sp.getPersonne().nom);
            Trace.info("[BddManager] Prenom = "+ sp.getPersonne().prenom);
            Trace.info("[BddManager] Age = "+ sp.getPersonne().age);
            Trace.info("[BddManager] idservice = "+ sp.getService().id);
            Trace.info("[BddManager] Label = "+ sp.getService().label);
       
        }*/

        return liste;
	}


}