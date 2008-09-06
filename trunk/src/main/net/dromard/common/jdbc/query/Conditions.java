package net.dromard.common.jdbc.query;

import java.util.Vector;
import net.dromard.common.brakets.Bracket;

/**
 * Classe de Gestion de groupe de conditions SQL (... WHERE conditions ...)
 * @author Gabriel Dromard (<a href="mailto:gabriel.dromard@airbus.com">gabriel.dromard@airbus.com</a>)
 * @version 1.0
 */
public class Conditions extends Bracket {
	public static int AND = 0;
	public static int OR = 1;

	private static String[] possibleConditionsTypes = { "AND ", "OR " };
	private Vector vConditionType;

	/** Construction d'un objet vide */
	public Conditions() {
		super.type = Bracket.ROUND_BRACKET;
	}

	/** Construction de l'objet */
	public Conditions(String conditions) {
		super(conditions, Bracket.ROUND_BRACKET);
	}

	/** Ajout d'une condition */
	public void addConditions(String conditions) {
		addBracket(conditions);
	}

	/** Ajout d'une condition */
	public void addCondition(String condition) {
		addText(condition);
	}

	/** Ajout d'une liste de condition sans hiérarchie */
	private void addConditionWithoutHierarchy(String conditions) {
		// Verification de l'existance d'une autre condition
		int moreAND = conditions.indexOf(" AND");
		int moreOR  = conditions.indexOf(" OR");

		// Récupération de l'index du premier élément suivant (le plus petit des supérieur à -1)
		int first = (moreAND > moreOR) ?
			( (moreOR  != -1) ? (moreOR)  : (moreAND) ):
			( (moreAND != -1) ? (moreAND) : (moreOR)  );
		if(first == -1) first = conditions.length();

		// Ajout de la premiére condition, création et ajout de l'objet
		if(conditions.substring(0, first).trim().length() > 0) {
			if(vConditionType == null) vConditionType = new Vector();

			// Ajout de la condition
			addObject(new Condition(conditions.substring(0, first).trim()));
		}

		// Ajout des conditions suivante
		if(conditions.substring(first).trim().length() > 0) {
			// Traitement du prochain éléement
			addText(conditions.substring(first));
		}
	}

	/** Ajout d'un groupe de condition */
	protected void addBracket(String conditions) {
		if(conditions != null) {
			// Retrait des espaces de debut de chaine
			conditions = conditions.trim();

			if(conditions.length() > 0) {
				if(vConditionType == null) vConditionType = new Vector();

				// ajout d'un type vide si non existant
				if(vConditionType.size() <= size()) vConditionType.add(possibleConditionsTypes[AND]);

				// Ajout d'une nouvelle condition
				addObject(new Conditions(conditions));
			}
		}
	}

	/** Ajout d'une condition de même niveau */
	protected void addText(String conditions) {
		if(conditions != null) {
			// Retrait des espaces de debut de chaine
			conditions = conditions.trim();

			if(vConditionType == null) vConditionType = new Vector();

			if(conditions.length() > 0) {
				// Verification du type de l'objet et association
				for(int i=0; i<possibleConditionsTypes.length; ++i) {
					conditions += " ";
					if(conditions.toUpperCase().startsWith(possibleConditionsTypes[i])) {
						// Suppréssion du type de la condition
						conditions = conditions.substring(possibleConditionsTypes[i].length(), conditions.length()).trim();

						// ajout d'un type vide si non existant
						if(vConditionType.size() <= size()) vConditionType.add(possibleConditionsTypes[i]);
						else vConditionType.set(size(), possibleConditionsTypes[i]);
						//debug("Ajout d'un type : '"+vConditionType.get(size())+"'");

						// Ajout de la condition et de son type
						addConditionWithoutHierarchy(conditions);
						return;
					}
				}

				// ajout d'un type vide si non existant
				if(vConditionType.size() <= size()) vConditionType.add(possibleConditionsTypes[AND]);
				else vConditionType.set(size(), possibleConditionsTypes[AND]);

				// La condition courante est une condition de type FIRST
				addConditionWithoutHierarchy(conditions);
			}
		}
	}

	public String toString() {
		String toReturn = "(";
		for(int i=0; i<content.size(); ++i) {
			if(i>0) toReturn += vConditionType.get(i);
			toReturn += content.get(i).toString();
			if(i<content.size()-1) toReturn += " ";
		}
		return toReturn+")";
	}

	public static void main(String[] args) {
		String test = "(titi=123 OR ( titi>456 AND titi<789 ) ) AND (toto NOT NULL ) OR (titi IN SELECT ID FROM toto )";
		debug("("+test+")");
		Conditions conditions = new Conditions(test);
		conditions.addCondition("1 = 1");
		conditions.addCondition("2 = 2");
		conditions.addCondition("3 = 3");
		conditions.addConditions("4 = 4");
		conditions.addConditions("5 = 5");
		conditions.addConditions("6 = 6");
		debug(conditions.toString());
	}
}








