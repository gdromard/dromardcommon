package net.dromard.common.jdbc.query;

/**
 * Interface de Gesion d'une requete
 * @author Gabriel Dromard (<a href="mailto:gabriel.dromard@airbus.com">gabriel.dromard@airbus.com</a>)
 * @version 1.1
 */
public interface Query {
	/** execution de la requete */
	 Object execute();
	/** Transformation de l'objet en une requete */
	 String toString();
	/** Initialisation de la requete */
	 void setQuery(String query);
	/** Récupération de la requete */
	 String getQuery();
	/** Réinitialisation de l'objet */
	void clear();
}
