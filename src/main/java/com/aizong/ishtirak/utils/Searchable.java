/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aizong.ishtirak.utils;

import java.util.Collection;

/**
 *
 * @author c.simon
 */

public interface Searchable<E, V>{

	

	/**

	 * Searches an underlying inventory of items consisting of type E

	 * @param value A searchable value of type V

	 * @return A Collection of items of type E.

	 */

	public Collection<E> search(V value);

	

}