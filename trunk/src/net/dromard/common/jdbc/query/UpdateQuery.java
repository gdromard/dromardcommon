package net.dromard.common.jdbc.query;
import java.util.*;
import java.awt.Toolkit;

import net.dromard.common.util.StringHelper;

/**
 * Objet de gestion de requete de type Update
 * Sp�cialisation de l'interface Query
 * @author Gabriel Dromard (<a href="mailto:gabriel.dromard@airbus.com">gabriel.dromard@airbus.com</a>)
 * @version 1.1
 */
public class UpdateQuery implements Query {
	/** Liste des champs � mettre � jours */
	 protected Vector fields;
	/** Liste des valeurs des champs � mettre � jour */
	 protected Vector values;
	/** Table � mettre � jour */
	 protected String table;
	/** Conditions de l'update */
	 protected Conditions conditions;

	/** Construction d'un objet vide */
	public UpdateQuery() {}

	/**
	 * Construction de l'objet � partir d'une string
	 * @param query Requete update sous forme de string
	 */
	public UpdateQuery(String query) { setQuery(query); }

	/**
	 * Construction de l'objet � partir de la liste de tous les �l�ments
	 * @param fields     Liste des champs � mettre � jours
	 * @param values     Liste des valeurs des champs � mettre � jour
	 * @param table      Table � mettre � jour
	 * @param conditions Conditions de l'update
	 */
	public UpdateQuery(Vector fields, Vector values, String table, Conditions conditions) {
		setFields(fields);
		setValues(values);
		setTable(table);
		setConditions(conditions);
	}

	/**
	 * Initialisation de la liste des champs � mettre � jours
	 * @param fields Liste des champs � mettre � jours
	 */
	 final public void setFields(Vector fields) { this.fields = fields; }

	/**
	 * Initialisation de la liste des valeurs des champs � mettre � jour
	 * @param values Liste des valeurs des champs � mettre � jour
	 */
	 final public void setValues(Vector values) { this.values = values; }

	/**
	 * Intitialisation de la table � mettre � jour
	 * @param table Table � mettre � jour
	 */
	 final public void setTable(String table)   { this.table = table; }

	/**
	 * Initialisation de la liste des conditions de l'update
	 * @param conditions Conditions de l'update
	 */
	 final public void setConditions(Conditions conditions) { this.conditions = conditions; }

	/**
	 * R�cup�ration de la liste des champs � mettre � jours
	 * @return La liste des champs � mettre � jours
	 */
	 final public Vector getFields() { return fields; }

	/**
	 * R�cup�ration de la liste des valeurs des champs � mettre � jour
	 * @return Liste des valeurs des champs � mettre � jour
	 */
	 final public Vector getValues() { return values; }

	/**
	 * R�cup�ration de la table � mettre � jour
	 * @return Table � mettre � jour
	 */
	 final public String getTable()  { return table; }

	/**
	 * R�cup�ration de la requete sous forme de string
	 * @return La requete sous forme de string
	 */
	final public String getQuery()  { return toString(); }

	/**
	 * R�cup�ration de la liste des conditions de l'update
	 * @return La liste des conditions de l'update
	 */
	 final public Conditions getConditions() { return conditions; }


	/**
	 * Ajout d'un �l�ment de type SELECT
	 * @param field Champs � ajouter dans la s�l�ction (SELECT)
	 */
	 final  public void addField(String field) {
		if(fields == null) fields = new Vector();
		fields.add(field);
	}

	/**
	 * Ajout d'un �l�ment de type SELECT
	 * @param value Champs � ajouter dans la s�l�ction (SELECT)
	 */
	 final  public void addValue(String value) {
		if(values == null) values = new Vector();
		values.add(value);
	}

	/**
	 * Ajout d'un �l�ment de type WHERE
	 * @param condition Champs � ajouter dans la clause WHERE
	 */
	 final public void addCondition(String condition) {
		if(getConditions() == null) setConditions(new Conditions());
		conditions.addConditions(condition);
	}

	/**
	 * R�initialisation de l'objet
	 */
	public void clear() {
		if(fields != null)     fields = new Vector();
		if(values != null)     values = new Vector();
		if(getConditions() != null) setConditions(new Conditions());
		table = "";
	}

	/** r� initialisation de l'objet */
	public void clearFields() { fields = null; }
	/** r� initialisation de l'objet */
	public void clearValues() { values = null; }
	/** r� initialisation de l'objet */
	public void clearTable() { table = null; }
	/** r� initialisation de l'objet */
	public void clearConditions() { conditions = null; }

	/**
	 * Initialisation de l'objet � partir d'une requete au format String
	 * @param query Requete au format String
	 */
	 public void setQuery(String query) {
		// Initialisation de l'objet
		clear();

		// String temporaire
		String sFields, sConditions;

		// R�cup�ration des diff�rents �l�ments de la requete
		table       = StringHelper.subStringBeetwen(query, "UPDATE ", " SET").trim();
		sFields     = StringHelper.subStringBeetwen(query, "SET ", " WHERE");
		sConditions = StringHelper.subStringAfter (query, "WHERE ");

		// Test de l'existance des diff�rents �l�ments
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

		// V�rification de l'int�gralit� des donn�es
		  if(table == null) { debug("La table n'est pas initialis�e"); return null; }
		  if(fields == null || fields.size() == 0) { debug("Les champs ne sont pas initialis�s"); return null; }
		  if(values == null || values.size() == 0) { debug("Les valeurs ne sont pas initialis�es"); return null; }
		if(values.size() != fields.size())       { debug("Il n'y a pas autant de champs que de valeurs"); return null; }

		  // R�cup�ration de la table � modifier
		  query += "UPDATE " + table + " SET ";

		  // R�cup�ration de la liste des champs � modifier et des valeurs
		  Iterator iFields = fields.iterator();
		  Iterator iValues = values.iterator();

		// Association des valeurs aux champs
		  while (iFields.hasNext() && iValues.hasNext()) {
				query += iFields.next().toString() + " = " + iValues.next().toString();
				if (iFields.hasNext() && iValues.hasNext()) query += ", ";
			else query += " ";
		  }

		  // R�cup�ration de la liste des conditions
		  if(getConditions() != null) {
			  query += "WHERE " + getConditions();
		  }
		//
		  return query;
	 }

	/**
	 * Execution de la requete.
	 * Cette m�thode n'est pas impl�ment�e car elle doit utiliser une connection � une Base de donn�e.
	 * Vous devez surcharger la classe et impl�menter cette m�thode :
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
	 * @return Cette methode doit retourner un objet representant le resultat de l'ex�cution de la requete
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
