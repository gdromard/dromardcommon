package net.dromard.common.jdbc.query;
import java.util.*;
import java.awt.Toolkit;

import net.dromard.common.util.StringHelper;

/**
 * Objet de gestion de requete de type Update
 * Spécialisation de l'interface Query
 * @author Gabriel Dromard (<a href="mailto:gabriel.dromard@airbus.com">gabriel.dromard@airbus.com</a>)
 * @version 1.1
 */
public class UpdateQuery implements Query {
	/** Liste des champs à mettre à jours */
	 protected Vector fields;
	/** Liste des valeurs des champs à mettre à jour */
	 protected Vector values;
	/** Table à mettre à jour */
	 protected String table;
	/** Conditions de l'update */
	 protected Conditions conditions;

	/** Construction d'un objet vide */
	public UpdateQuery() {}

	/**
	 * Construction de l'objet à partir d'une string
	 * @param query Requete update sous forme de string
	 */
	public UpdateQuery(String query) { setQuery(query); }

	/**
	 * Construction de l'objet à partir de la liste de tous les éléments
	 * @param fields     Liste des champs à mettre à jours
	 * @param values     Liste des valeurs des champs à mettre à jour
	 * @param table      Table à mettre à jour
	 * @param conditions Conditions de l'update
	 */
	public UpdateQuery(Vector fields, Vector values, String table, Conditions conditions) {
		setFields(fields);
		setValues(values);
		setTable(table);
		setConditions(conditions);
	}

	/**
	 * Initialisation de la liste des champs à mettre à jours
	 * @param fields Liste des champs à mettre à jours
	 */
	 final public void setFields(Vector fields) { this.fields = fields; }

	/**
	 * Initialisation de la liste des valeurs des champs à mettre à jour
	 * @param values Liste des valeurs des champs à mettre à jour
	 */
	 final public void setValues(Vector values) { this.values = values; }

	/**
	 * Intitialisation de la table à mettre à jour
	 * @param table Table à mettre à jour
	 */
	 final public void setTable(String table)   { this.table = table; }

	/**
	 * Initialisation de la liste des conditions de l'update
	 * @param conditions Conditions de l'update
	 */
	 final public void setConditions(Conditions conditions) { this.conditions = conditions; }

	/**
	 * Récupération de la liste des champs à mettre à jours
	 * @return La liste des champs à mettre à jours
	 */
	 final public Vector getFields() { return fields; }

	/**
	 * Récupération de la liste des valeurs des champs à mettre à jour
	 * @return Liste des valeurs des champs à mettre à jour
	 */
	 final public Vector getValues() { return values; }

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
	 * Récupération de la liste des conditions de l'update
	 * @return La liste des conditions de l'update
	 */
	 final public Conditions getConditions() { return conditions; }


	/**
	 * Ajout d'un élément de type SELECT
	 * @param field Champs à ajouter dans la séléction (SELECT)
	 */
	 final  public void addField(String field) {
		if(fields == null) fields = new Vector();
		fields.add(field);
	}

	/**
	 * Ajout d'un élément de type SELECT
	 * @param value Champs à ajouter dans la séléction (SELECT)
	 */
	 final  public void addValue(String value) {
		if(values == null) values = new Vector();
		values.add(value);
	}

	/**
	 * Ajout d'un élément de type WHERE
	 * @param condition Champs à ajouter dans la clause WHERE
	 */
	 final public void addCondition(String condition) {
		if(getConditions() == null) setConditions(new Conditions());
		conditions.addConditions(condition);
	}

	/**
	 * Réinitialisation de l'objet
	 */
	public void clear() {
		if(fields != null)     fields = new Vector();
		if(values != null)     values = new Vector();
		if(getConditions() != null) setConditions(new Conditions());
		table = "";
	}

	/** ré initialisation de l'objet */
	public void clearFields() { fields = null; }
	/** ré initialisation de l'objet */
	public void clearValues() { values = null; }
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
		String sFields, sConditions;

		// Récupération des différents éléments de la requete
		table       = StringHelper.subStringBeetwen(query, "UPDATE ", " SET").trim();
		sFields     = StringHelper.subStringBeetwen(query, "SET ", " WHERE");
		sConditions = StringHelper.subStringAfter (query, "WHERE ");

		// Test de l'existance des différents éléments
		if(table==null) return;

		if(sFields     != null) setFields(new Vector<String>(Arrays.asList(StringHelper.split(sFields, ","))));
		if(sConditions != null) setConditions(new Conditions(sConditions));

		// Taitement des Valeurs
		String field = new String();
		int index = -1;
		Iterator iterator;
		if(getFields() != null) {
			iterator = ((Vector)getFields().clone()).iterator();
			fields.clear();
			while(iterator.hasNext()) {
				field = (String)iterator.next();
				index = field.lastIndexOf('=');
				if(index > 0) {
					addField(field.substring(0, index).trim());
					addValue(field.substring(index+1, field.length()).trim());
				} else {
					addField(field.trim());
					addValue("");
				}
			}
		}
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
		  if(fields == null || fields.size() == 0) { debug("Les champs ne sont pas initialisés"); return null; }
		  if(values == null || values.size() == 0) { debug("Les valeurs ne sont pas initialisées"); return null; }
		if(values.size() != fields.size())       { debug("Il n'y a pas autant de champs que de valeurs"); return null; }

		  // Récupération de la table à modifier
		  query += "UPDATE " + table + " SET ";

		  // Récupération de la liste des champs à modifier et des valeurs
		  Iterator iFields = fields.iterator();
		  Iterator iValues = values.iterator();

		// Association des valeurs aux champs
		  while (iFields.hasNext() && iValues.hasNext()) {
				query += iFields.next().toString() + " = " + iValues.next().toString();
				if (iFields.hasNext() && iValues.hasNext()) query += ", ";
			else query += " ";
		  }

		  // Récupération de la liste des conditions
		  if(getConditions() != null) {
			  query += "WHERE " + getConditions();
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

		public class myUpdateQuery extends UpdateQuery {
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
			 throw new Exception("Function execute is not implement. You will have to do it by heriting UpdateQuery class :o)");
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
		if(debug) System.out.println("[UpdateQuery] - "+msg);
	}

	public static void main(String[] args) {
		String query = "UPDATE table1 SET champs1 = '1', champs2 = '2' WHERE CLEF = 123";
		debug(query);
		UpdateQuery update = new UpdateQuery(query);
		debug(update.toString());
	}
}
