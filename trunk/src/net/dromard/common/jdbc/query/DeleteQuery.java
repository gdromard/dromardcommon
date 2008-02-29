package net.dromard.common.jdbc.query;

// Java
import java.awt.Toolkit;

import net.dromard.common.util.StringHelper;

/**
 * Objet de gestion de requete de type Delete
 * Spécialisation de l'interface Query
 * @author Gabriel Dromard (<a href="mailto:gabriel.dromard@airbus.com">gabriel.dromard@airbus.com</a>)
 * @version 1.1
 */
public class DeleteQuery implements Query {
	/** Table à mettre à jour */
	 protected String table;
	/** Conditions du DELETE */
	 protected Conditions conditions;

	/** Construction d'un objet vide */
	public DeleteQuery() {}

	/**
	 * Construction de l'objet à partir d'une string
	 * @param query Requete DELETE sous forme de string
	 */
	public DeleteQuery(String query) { setQuery(query); }

	/**
	 * Construction de l'objet à partir de la liste de tous les éléments
	 * @param table      Table à mettre à jour
	 * @param conditions Conditions du DELETE
	 */
	public DeleteQuery(String table, Conditions conditions) {
		this.table  = table;
		this.conditions = conditions;
	}

	/**
	 * Intitialisation de la table à mettre à jour
	 * @param table Table à mettre à jour
	 */
	 final public void setTable(String table)   { this.table = table; }

	/**
	 * Initialisation de la liste des conditions du DELETE
	 * @param conditions Conditions du DELETE
	 */
	 final public void setConditions(Conditions conditions) { this.conditions = conditions; }

	/**
	 * Récupération de la table à mettre à jour
	 * @return Table à mettre à jour
	 */
	 final public String getTable()  { return table; }

	/**
	 * Récupération de la requete sous forme de string
	 * @return La requete sous forme de string
	 */
	final public String getQuery()  { return toString(); }

	/**
	 * Récupération de la liste des conditions du DELETE
	 * @return La liste des conditions du DELETE
	 */
	 final public Conditions getConditions() { return conditions; }

	/**
	 * Ajout d'un élément de type WHERE
	 * @param condition Champs à ajouter dans la clause WHERE
	 */
	 final public void addCondition(String condition) {
		if(getConditions() == null) setConditions(new Conditions(condition));
		else getConditions().addCondition(condition);
	}

	/**
	 * Ajout d'un élément de type WHERE
	 * @param condition Champs à ajouter dans la clause WHERE
	 */
	 final public void addConditions(String condition) {
		if(getConditions() == null) setConditions(new Conditions(condition));
		else getConditions().addConditions(condition);
	}

	/**
	 * Réinitialisation de l'objet
	 */
	public void clear() {
		if(getConditions() != null) setConditions(new Conditions());
		table = "";
	}

	/** ré initialisation de l'objet */
	public void clearTable() { table = null; }
	/** ré initialisation de l'objet */
	public void clearConditions() { conditions = null; }

	/**
	 * Initialisation de l'objet à partir d'une requete au format String
	 * @param query Requete au format String
	 */
	 public void setQuery(String query) {
		// Initialisation de l'objet
		clear();

		// String temporaire
		String sConditions;

		// Récupération des différents éléments de la requete
		table       = StringHelper.subStringBeetwen(query, "FROM ", " WHERE").trim();
		sConditions = StringHelper.subStringAfter (query, "WHERE ");

		// Test de l'existance des différents éléments
		if(table==null) return;

		if(sConditions != null) setConditions(new Conditions(sConditions));

/**
		if(debug) {
			Iterator i;
			String string;

			string = "TABLES       :"+getTable();
			debug(string);
			string = "FIELDS       :";
			if(this.getFields() != null) {
				i = this.getFields().iterator();
				while(i.hasNext()) {
					string += (String)i.next() +"-";
				}
			}
			debug(string);
			string = "VALUES       :";
			if(this.getFields() != null) {
				i = this.getValues().iterator();
				while(i.hasNext()) {
					string += (String)i.next() +"-";
				}
			}
			debug(string);
			string = "CONDITIONS   :";
			if(this.getConditions() != null) {
				string += this.getConditions().toString();
			}
			debug(string);
		}
/**/
	}

	/**
	 * Transforme l'bjet en une String
	 * @return la requete en String
	 */
	 public String toString() {
		  String query = "";

		// Vérification de l'intégralité des données
		  if(table == null) { debug("La table n'est pas initialisée"); return null; }

		  // Récupération de la table à modifier
		  query += "DELETE FROM " + table + " ";

		  // Récupération de la liste des conditions
		  if(getConditions() != null) {
			  query += "WHERE " + getConditions().toString();
		  }
		//
		  return query;
	 }

	/**
	 * Execution de la requete.
	 * Cette méthode n'est pas implémentée car elle doit utiliser une connection à une Base de donnée.
	 * Vous devez surcharger la classe et implémenter cette méthode :
	 * <pre>
		import sql.query.*;
		import java.sql.*;

		public class myDeleteQuery extends DeleteQuery {
			Connection conn;

			public void setConnection(Connection conn) { this.conn = conn; }

			public Connection getConnection() { return conn; }

			public Object execute() {
				if(conn == null) return null;
				Statement stmt = null;
				try {
					stmt = conn.createStatement();
					return new Integer(stmt.executeUpdate(super.getQuery()));
				} catch (Exception ex) {
					ex.printStackTrace();
				} finally {
					try { if(stmt != null) stmt.close(); } catch (Exception ex) { ex.printStackTrace(); }
				}
				return null;
			}
		}
		</pre>
	 * @return Cette methode doit retourner un objet representant le resultat de l'exécution de la requete
	 */
	 public Object execute() {
		try {
			 throw new Exception("Function execute is not implement. You will have to do it by heriting DeleteQuery class :o)");
		} catch (Exception ex) {
			Toolkit.getDefaultToolkit().beep();
			Toolkit.getDefaultToolkit().beep();
			Toolkit.getDefaultToolkit().beep();
			ex.printStackTrace();
		}
		return null;
	 }

	private static boolean debug = true;
	private static void debug(String msg) {
		if(debug) System.out.println("[DeleteQuery] - "+msg);
	}

	public static void main(String[] args) {
		String query = "DELETE FROM table1 WHERE champs1 = '1' AND champs2 = '2' AND CLEF = 123";
		debug(query);
		DeleteQuery delete = new DeleteQuery(query);
		debug(delete.toString());
	}
}
