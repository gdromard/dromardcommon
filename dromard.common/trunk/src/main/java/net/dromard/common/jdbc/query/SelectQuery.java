package net.dromard.common.jdbc.query;
// Java
import java.util.*;
import java.awt.Toolkit;

import net.dromard.common.util.StringHelper;

/**
 * Objet de gestion de requete de type Select
 * Sp�cialisation de l'interface Query
 * @author Gabriel Dromard (<a href="mailto:gabriel.dromard@airbus.com">gabriel.dromard@airbus.com</a>)
 * @version 1.2
 */
public class SelectQuery implements Query {
	/** Liste des champs � afficher */
	protected Vector fields;
	/** Liste des libell�s des champs � afficher */
	protected Vector labels;
	/** Liste des tables � utiliser */
	protected Vector tables;
	/** Liste des champs d'ordre */
	protected Conditions conditions;
	/** Liste des champs d'ordre */
	protected Vector orderFields;
	/** Liste des champs de group */
	protected Vector groupFields;
	/** Si la requete est de type distinct ou pas !! */
	protected boolean distinct = false;

	/** Construction d'un objet vide */
	public SelectQuery() {}

	/**
	 * Construction de l'objet � partir d'une string
	 * @param query Requete select sous forme de string
	 */
	public SelectQuery(String query) { setQuery(query); }

	/**
	 *
	 * Construction de l'objet � partir de la liste de tous les �l�ments
	 * @param fields      Liste des champs � afficher
	 * @param labels      Liste des libell�s des champs � afficher
	 * @param tables      Liste des tables � utiliser
	 * @param orderFields Liste des champs d'ordre
	 * @param groupFields Liste des champs de group
	 * @param distinct    Si la requete est de type distinct ou pas !!
	 */
	public SelectQuery(Vector fields, Vector labels, Vector tables, Vector orderFields, Vector groupFields, boolean distinct) {
		this.fields = fields;
		this.labels = labels;
		this.tables = tables;
//		this.conditions.set = conditions;
		this.orderFields = orderFields;
		this.groupFields = groupFields;
		this.distinct = distinct;
	}

	/**
	 * Initialisation de la liste des champs � afficher
	 * @param fields Liste des champs � afficher
	 */
	final public void setFields(Vector fields) { this.fields = fields; }

	/**
	 * Initialisation de la liste des libell�s des champs � afficher
	 * @param labels Liste des libell�s des champs � afficher
	 */
	final public void setLabels(Vector labels) { this.labels = labels; }

	/**
	 * Initialisation de la liste des tables � utiliser
	 * @param tables Liste des tables � utiliser
	 */
	final public void setTables(Vector tables) { this.tables = tables; }

	/**
	 * Initialisation de la liste des conditions
	 * @param conditions Liste des conditions
	 */
//    final public void setConditions(Vector conditions) { this.conditions = conditions; }
	final public void setConditions(Conditions conditions) { this.conditions = conditions; }

	/**
	 * Initialisation de la liste des champs d'ordre
	 * @param orderFields Liste des champs d'ordre
	 */
	final public void setOrderFields(Vector orderFields) { this.orderFields = orderFields; }

	/**
	 * Initialisation de la liste des champs de group
	 * @param groupFields Liste des champs de group
	 */
	final public void setGroupFields(Vector groupFields) { this.groupFields = groupFields; }

	/**
	 * Initialisation du type de la requete : distinct ou pas !!
	 * @param distinct Si la requete est de type distinct ou pas !!
	 */
	final public void setDistinct(boolean distinct) { this.distinct = distinct; }

	/**
	 * R�cup�ration de la liste des champs � afficher
	 * @return Liste des champs � afficher
	 */
	final public Vector getFields() { return fields; }

	/**
	 * R�cup�ration de la liste des libell�s des champs � afficher
	 * @return Liste des libell�s des champs � afficher
	 */
	final public Vector getLabels() { return labels; }

	/**
	 * R�cup�ration de la liste des tables � utiliser
	 * @return Liste des tables � utiliser
	 */
	final public Vector getTables() { return tables; }

	/**
	 * R�cup�ration de la liste des conditions
	 * @return Liste des conditions
	 */
//    final public Vector getConditions() { return conditions; }
	final public Conditions getConditions() { return conditions; }


	/**
	 * R�cup�ration de la liste des champs d'ordre
	 * @return Liste des champs d'ordre
	 */
	final public Vector getOrderFields() { return orderFields; }

	/**
	 * R�cup�ration de la liste des champs de group
	 * @return Liste des champs de group
	 */
	final public Vector getGroupFields() { return groupFields; }

	/**
	 * R�cup�ration de la requete au format String
	 * @return La requete au format String
	 */
	final public String getQuery() { return toString(); }

	/**
	 * R�cup�ration du type de la requete : distinct ou pas !!
	 * @return Si la requete est de type distinct ou pas !!
	 */
	final public boolean isDistinct() { return distinct; }


	/**
	 * Ajout d'un �l�ment de type SELECT
	 * @param field Champs � ajouter dans la s�l�ction (SELECT)
	 */
	final public void addField(String field) {
		if(fields == null) fields = new Vector();
		fields.add(field);
	}

	/**
	 * Ajout d'un alias
	 * @param label Alias � ajouter
	 */
	final public void addLabel(String label) {
		if(labels == null) labels = new Vector();
		labels.add(label);
	}

	/**
	 * Ajout d'un �l�ment de type FROM
	 * @param table Table � ajouter dans la s�l�ction (FROM)
	 */
	final public void addTable(String table) {
		if(tables == null) tables = new Vector();
		tables.add(table);
	}

	/** Type d'association d'une condition de type AND */
	public static int AND   = 0;
	/** Type d'association d'une condition de type OR */
	public static int OR    = 1;
	/** Derni�re condition */
	public static int LAST  = 2;

	/**
	 * Ajout d'un �l�ment de type WHERE PAR DEFAULT de TYPE AND
	 * @param condition Champs � ajouter dans la clause WHERE
	 */
	final public void addCondition(String condition) {
		if(conditions == null) conditions = new Conditions();
		conditions.addConditions(condition);
	}

	/**
	 * Ajout d'un �l�ment de type ORDER
	 * @param field Champs � ajouter dans le ORDER BY
	 */
	final public void addOrderField(String field) {
		if(orderFields == null) orderFields = new Vector();
		orderFields.add(field);
	}

	/**
	 * Ajout d'un �l�ment de type GROUP
	 * @param field Champs � ajouter dans le GROUP BY
	 */
	final public void addGroupField(String field) {
		if(groupFields == null) groupFields = new Vector();
		groupFields.add(field);
	}

	/**
	 * R�initialisation de l'objet
	 */
	public void clear() {
		if(fields != null)      fields = new Vector();
		if(labels != null)      labels = new Vector();
		if(tables != null)      tables = new Vector();
		if(conditions != null)  conditions = new Conditions();
		if(orderFields != null) orderFields = new Vector();
		if(groupFields != null) groupFields = new Vector();
		this.distinct = false;
	}

	/** r� initialisation de l'objet */
	public void clearFields() { fields = null; }
	/** r� initialisation de l'objet */
	public void clearLabels() { labels = null; }
	/** r� initialisation de l'objet */
	public void clearTables() { tables = null; }
	/** r� initialisation de l'objet */
	public void clearConditions() { conditions = null; }
	/** r� initialisation de l'objet */
	public void clearOrderFields() { orderFields = null; }
	/** r� initialisation de l'objet */
	public void clearGroupFields() { groupFields = null; }

	/**
	 * Transforme l'bjet en une String
	 * @return la requete en String
	 */
	public String toString() {
		String query = null;
		Iterator i, j;

		// Verification de l'int�gralit� des donn�es
		if(this.getFields() == null) return null;

		// R�cup�ration de la liste des champs � s�l�ctionner
		query = "SELECT ";
		if(isDistinct()) query += "DISTINCT ";

		// Il existe des champs !
		if(getFields().size() > 0) {
			// Les aliases ne sont pas valides
			if(getLabels() == null || getLabels().size() != getFields().size()) {
				query += StringHelper.iteratorToString(getFields().iterator(), ", ") + " ";
			// Il y a des aliases
			} else {
				j = getLabels().iterator();
				i = getFields().iterator();
				while (i.hasNext() && j.hasNext()) {
					query += (String)i.next() +" "+ (String)j.next() ;
					if (i.hasNext()) query += ", ";
					else query += " ";
				}
			}
		} else {
			query += "* ";
		}

		// R�cup�ration de la liste des tables � utiliser
		if (getTables() == null || getTables().size() == 0) {
			return query = null;
		}
		query += "FROM " + StringHelper.iteratorToString(getTables().iterator(), ", ") +" ";

		// R�cup�ration de la liste des conditions
		if(getConditions() != null) {
			String condition = getConditions().toString();
			if(condition != null && condition.length()>0)
				query += "WHERE "+getConditions().toString();
		}

		// R�cup�ration de la liste des champs Group By
		if (getGroupFields() != null && getGroupFields().size() > 0) {
			query += "GROUP BY " + StringHelper.iteratorToString(getGroupFields().iterator(), ", ") +" ";
		}

		// R�cup�ration de la liste des champs Order By
		if(getOrderFields() != null && getOrderFields().size() > 0) {
			query += "ORDER BY " + StringHelper.iteratorToString(getOrderFields().iterator(), ", ") +" ";
		}

		return query;
	}

	/**
	 * Initialisation de l'objet � partir d'une requete au format String
	 * @param query Requete au format String
	 */
	public void setQuery(String query) {
		// Initialisation de l'objet
		clear();

		// String temporaire
		String sFields, sLabels, sTables, sConditions, sOrderBy, sGroupBy;

		// R�cup�ration des diff�rents �l�ments de la requete
		sFields     = StringHelper.subStringBeetwen(query, "SELECT ", " FROM");
		sTables     = StringHelper.subStringBeetwen(query, "FROM ", " WHERE");
		sConditions = StringHelper.subStringAfter(query, "WHERE ");
		sOrderBy    = StringHelper.subStringAfter(query, "ORDER BY ");
		sGroupBy    = StringHelper.subStringAfter(query, "GROUP BY ");


		// R�cup�ration de la liste des conditions
		if(sConditions != null) {
			if(sOrderBy != null && sConditions.indexOf("ORDER ") > -1)
				sConditions = StringHelper.subStringBefore(sConditions, "ORDER ");
			if(sGroupBy != null && sConditions.indexOf("GROUP ") > -1)
				sConditions = StringHelper.subStringBefore(sConditions, "GROUP ");
		}

		// R�cup�ration de la liste des Groupes et des Order by
		if(sGroupBy != null && sOrderBy != null) {
			if(sGroupBy.indexOf("ORDER") > -1) sGroupBy = StringHelper.subStringBefore(sGroupBy, "ORDER");
			if(sOrderBy.indexOf("GROUP") > -1) sOrderBy = StringHelper.subStringBefore(sOrderBy, "GROUP");
		}

		// Test si DISTINCT ou pas ?
		if( sFields.indexOf("DISTINCT") > -1) {
			sFields = StringHelper.subStringAfter(sFields, "DISTINCT ");
			setDistinct(true);
		}

		// Test de l'existance des diff�rents �l�ments
		if(sFields == null || sTables == null) return;

		// Ajout des �l�ments
		if(sFields     != null) setFields(new Vector<String>(Arrays.asList(StringHelper.split(sFields, ","))));
		if(sTables     != null) setTables(new Vector<String>(Arrays.asList(StringHelper.split(sTables, ","))));
		if(sConditions != null) setConditions(new Conditions(sConditions));
		if(sOrderBy    != null) setOrderFields(new Vector<String>(Arrays.asList(StringHelper.split(sOrderBy, ","))));
		if(sGroupBy    != null) setGroupFields(new Vector<String>(Arrays.asList(StringHelper.split(sGroupBy, ","))));

		// Taitement des LABELS
		String field = new String();
		int index = -1;
		Iterator iterator;
		if(getFields() != null) {
			iterator = ((Vector)getFields().clone()).iterator();
			fields.clear();
			while(iterator.hasNext()) {
				field = (String)iterator.next();
				index = field.lastIndexOf(' ');
				if(index > 0) {
					addField(field.substring(0, index).trim());
					addLabel(field.substring(index, field.length()).trim());
				} else {
					addField(field.trim());
					this.addLabel("");
				}
			}
		}
	}

	/**
	 * Execution de la requete.
	 * Cette m�thode n'est pas impl�ment�e car elle doit utiliser une connection � une Base de donn�e.
	 * Vous devez surcharger la classe et impl�menter cette m�thode :
	 * <pre>
		import sql.query.*;
		import java.sql.*;

		public class myUpdateSelect extends SelectQuery {
			Connection conn;

			public void setConnection(Connection conn) { this.conn = conn; }

			public Connection getConnection() { return conn; }

			public Object execute() {
				if(conn == null) return null;
				Statement stmt = null;
				try {
					stmt = conn.createStatement();
					return stmt.executeQuery(super.getQuery());
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
			throw new Exception("Function execute is not implement. You will have to do it by heriting SelectQuery class :o)");
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
		if(debug) System.out.println("[SelectQuery] - "+msg);
	}

	public static void main(String[] args) {
		String query = "SELECT DISTINCT champs1 aliase1, champs2 aliase2 FROM table1, table2 WHERE chLIKEps1 = 'toto' OR chamorps2 = 'titi' AND champs3 = 'tutu'";
		debug(query);
		SelectQuery select = new SelectQuery(query);
		debug(select.toString());
	}
}