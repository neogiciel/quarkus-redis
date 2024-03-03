package com.neogiciel.service;

import java.util.List;

import com.neogiciel.model.Personne;
import com.neogiciel.model.Service;
import com.neogiciel.model.ServicePersonne;

import io.quarkus.cache.CacheResult;

public interface BddManager {
    
    public String test();
    public String redis();
    public String invalidate();

    /*
     * Table Personne
     */
    public List<Personne> getListPersonnes();
    public List<Personne> getListPersonnesSQL();
    public Long addPersonne(Personne personne);
    public Long addPersonneSQL(Personne personne);
	public Personne getPersonneFromId(int Id);
    public Personne getPersonneFromIdSQL(int Id);
    public void deletePersonne(int id);
	public void deletePersonneSQL(int id);
	public void updatePersonne(Personne personne);
	public void updatePersonneSQL(Personne personne);

    /*
     * Table Service
     */
    public List<Service> getListServices();
    public Service getServiceFromId(int Id);
    public Long addService(Service service);
    public void deleteService(int id);
    public void updateService(Service service);

    /*
     * Table Service Persoone
     */
    public List<ServicePersonne> getListPersonneFromServices(int id);

}
