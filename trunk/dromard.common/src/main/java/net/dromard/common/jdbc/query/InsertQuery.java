package net.dromard.common.jdbc.query;

// Java
import java.util.*;
import java.awt.Toolkit;

import net.dromard.common.util.StringHelper;

/**
 * Objet de gestion de requete de type Insert
 * Sp�cialisation de l'interface Query
 * @author Gabriel Dromard (<a href="mailto:gabriel.dromard@airbus.com">gabriel.dromard@airbus.com</a>)
 * @version 1.1
 */
public class InsertQuery implements Query {
	/** Liste des champs � mettre � jours */
	 protected Vector fields;
	/** Liste des valeurs des champs � mettre � jour */
	 protected Vector values;
	/** Table � mettre � jour */
	 protected String table;

	/** Construction d'un objet vide */
	public InsertQuery() {}

	/**
	 * Construction de l'objet � partir d'une string
	 * @param query Requete update sous forme de string
	 */
	public InsertQuery(String query) { setQuery(query); }

	/**
	 * Construction de l'objet � partir de la liste de tous les �l�ments
	 * @param fields     Liste des champs � mettre � jours
	 * @param values     Liste des valeurs des champs � mettre � jour
	 * @param table      Table � mettre � jour
	 */
	public InsertQuery(Vector fields, Vector values, String table) {
		this.fields = fields;
		this.values = values;
		this.table  = table;
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
	 * Ajout d'un champs
	 * @param field Champs � ajouter dans la s�l�ction
	 */
	 final  public void addField(String field) {
		if(fields == null) fields = new Vector();
		fields.add(field);
	}

	/**
	 * Ajout d'une valeur
	 * @param value Champs � ajouter dans la s�l�ction
	 */
	 final  public void addValue(String value) {
		if(values == null) values = new Vector();
		values.add(value);
	}

	/**
	 * R�initialisation de l'objet
	 */
	public void clear() {
		if(fields != null) fields = new Vector();
		if(values != null) values = new Vector();
		table = "";
	}

	/** r� initialisation de l'objet */
	public void clearFields() { fields = null; }
	/** r� initialisation de l'objet */
	public void clearValues() { values = null; }
	/** r� initialisation de l'objet */
	public void clearTable() { table = null; }

	/**
	 * Initialisation de l'objet � partir d'une requete au format String
	 * @param query Requete au format String
	 */
	 public void setQuery(String query) {
		// Initialisation de l'objet
		clear();

		// String temporaire
		String sFields, sValues;

		// R�cup�ration des diff�rents �l�ments de la requete
		table   = StringHelper.subStringBeetwen(StringHelper.subStringBefore(query, "VALUES"), "INSERT INTO", "(").trim();
		sFields = StringHelper.subStringBeetwen(StringHelper.subStringBefore(query, "VALUES"), "(", ")");
		sValues = StringHelper.subStringBeetwen(StringHelper.subStringAfter (query, "VALUES"), "(", ")");

		// Test de l'existance des diff�rents �l�ments
		if(table==null || sValues==null) return;

		debug("sFields : "+sFields);
		debug("sValues : "+sValues);

		if(sFields != null) setFields(new Vector<String>(Arrays.asList(StringHelper.split(sFields, ","))));
		if(sValues != null) setValues(new Vector<String>(Arrays.asList(StringHelper.split(sValues, ","))));

/**
		if(debug) {
			Iterator i;
			String string;

			string = "TABLES       :\n"+getTable();
			debug(string);
			string = "FIELDS       :\n";
			if(this.getFields() != null) {
				i = this.getFields().iterator();
				while(i.hasNext()) {
					string += (String)i.next() +"-";
				}
			}
			debug(string);
			string = "VALUES       :\n";
			if(this.getValues() != null) {
				i = this.getValues().iterator();
				while(i.hasNext()) {
					string += (String)i.next() +"-";
				}
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
		  if(values == null || values.size() == 0) { debug("Les valeurs ne sont pas initialis�es"); return null; }
		if(values.size() != fields.size())       { debug("Il n'y a pas autant de champs que de valeurs"); return null; }

		  // R�cup�ration de la table � modifier
		  query += "INSERT INTO " + table;

		if(fields != null && fields.size() != 0) {
			query += "( " + StringHelper.iteratorToString(fields.iterator(), ", ") + " )";
		}

		query += " VALUES ( " + StringHelper.iteratorToString(values.iterator(), ", ") + " )";

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
		if(debug) System.out.println("[InsertQuery] - "+msg);
	}

	public static void main(String[] args) {
		String query = "INSERT INTO CLIENTS(TATA, TUTU) VALUES('TITI', 123)";
		debug(query);
		InsertQuery insert = new InsertQuery(query);
		debug(insert.toString());
	}
}
