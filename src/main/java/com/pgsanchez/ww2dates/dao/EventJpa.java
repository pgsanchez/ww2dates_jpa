package com.pgsanchez.ww2dates.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pgsanchez.ww2dates.model.Event;

public interface EventJpa extends JpaRepository<Event, Integer> {
	/*
	 * Por extender de la clas JpaRepository, ya heredamos una serie de métodos CRUD.
	 */
	
	// Con la terminología findBy + Atributo, spring ya nos genera una función para buscar en la BD. No hay que implementar nada.
	List<Event> findByImportance(int importance);
	
	Event findByName(String name);
	Event findById(int id);

}
