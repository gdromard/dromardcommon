package net.dromard.common.swing;

// Java
import java.util.*;
import javax.swing.*;

/**
 * Extends Swing component JTable
 * @author Gabriel Dromard
 * @version 1.0
 */
public class JVectorTable extends JTable {
	protected JVectorTableModel tableModel;
//	protected MyPopupMenu menu;

	/**
	 * Link vector to JTable.
	 * @param data Vector of Vector
	 */
	public void setData(Vector<Vector> data) {
		if(data.size() > 0 && data.get(0).size() > 0) {
			tableModel = new JVectorTableModel(data);
//			menu = new MyPopupMenu(this);
			setModel(tableModel);
			updateUI();
		}
	}

	/**
	 * Association d'un data container à la JTable.
	 * @param data Vector de Vector d'object representant les lignes. 
	 * @param columnsName Nom des colonnes à afficher
	 */
	public void setData(Vector<Vector> data, Vector<String> columnsName) {
		tableModel = new JVectorTableModel(data, columnsName);
//		menu = new MyPopupMenu(this);
		setModel(tableModel);
		this.getTableHeader().updateUI();
	}

	/**
	 * Initialisation des Intitulés des colonnes à afficher.
	 * @param columnsName Intitules des colonnes à afficher
	 */
	public void setIntitules(Vector<String> columnsName) {
		tableModel.setColumnsName(columnsName);
		this.getTableHeader().updateUI();
	}

	/**
	 * Récupération des Intitulés des colonnes à afficher.
	 * @return Les Intitulés des colonnes à afficher
	 */
	public Vector<String> getColumnsName() {
		return tableModel.getColumnsName();
	}

	/**
	 * Set column name.
	 */
	public void setColumnName(String columnName, int column) {
		tableModel.setColumnName(columnName, column);
	}

	/**
	 * Retreive data from JTable.
	 * @return A Vector of Vector
	 */
	public Vector<Vector> getData() {
		return tableModel.getData();
	}

	/**
	 * Transcription de la Table en String !!
	 * @return La table en tant que String
	 */
	public String toString() {
		return tableModel.getData().toString();
	}

	/**
	 * Fixation la taille des Colonnes de la JTable.
	 * @param width Vecteur comprennant toue les taille de chaque ligne
	 */
	public void setColumnsWidth(Vector width) {
		tableModel.fixeWidth(this, width);
	}
	
	public int setDefaultColumnWidth() {
		int panelWidth = 0;
		for(int i=0; i<getColumnCount(); i++) {
			int nbChar = getColumnName(i).length();
			getColumnModel().getColumn(i).setMinWidth(nbChar*10);
			panelWidth += nbChar*10;
		}
		return panelWidth;
	}
}