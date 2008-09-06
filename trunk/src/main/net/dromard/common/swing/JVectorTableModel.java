package net.dromard.common.swing;

// Java
import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

/**
 * Modele à Associé à une <b>JTable</b>
 * Classe Swing derivant de la classe <b>TableModel</b>. Elle gére un lien à une base de donnée. 
 * Elle est lié à un <b>SQLDataContainer</b> et à une <b>JdbTable</b>
 * @author Gabriel Dromard
 * @version 1.1
 */
public class JVectorTableModel implements TableModel {
	/** Conteneur des données */
	protected Vector<Vector> data;
	/** Intitulé des colonnes */
	protected Vector<String> columnsName;
	/** Cell have been modified ?*/
	protected boolean modified = false;
	/** say if the first line is the header or not */
	protected int offset = 1;

	/**
	 * Empty constructor. 
	 * It does nothing, the caller have to do it after.
	 */
	public JVectorTableModel() { /*It does nothing, the caller have to do it after*/ }

	/**
	 * Constructeur.
	 * @param data DataContaineur à associer à la JTable
	 */
    @SuppressWarnings("unchecked")
	public JVectorTableModel(Vector<Vector> data) {
		this.data = data;
		offset = 1;
		setColumnsName(data.get(0));
	}

	/**
	 * Constructeur.
	 * @param data        DataContaineur à associer à la JTable
	 * @param columnsName Intitulé des colonnes à afficher
	 */
	public JVectorTableModel(Vector<Vector> data, Vector<String> columnsName) {
		this.data = data;
		offset = 0;
		setColumnsName(columnsName);
	}

	/**
	 * Initialisation des Intitulé des colonnes à afficher
	 * @param columnsName Intitulé des colonnes à afficher
	 */
	public void setColumnsName(Vector<String> columnsName) {
		this.columnsName = columnsName;
	}

	/**
	 * Set the Column Name
	 */
	@SuppressWarnings("unchecked")
    public void setColumnName(String columnName, int columnIndex) {
		getData().get(0).setElementAt(columnName, columnIndex);
		columnsName = getData().get(0);
		setModified(true);
	}
	
	/**
	 * Retreive columns names
	 * @return Columns names
	 */
	public Vector<String> getColumnsName() {
		return columnsName;
	}

	/**
	 * Fix width of JTable cell
	 * @param jt
	 * @param width
	 */
	public void fixeWidth(JTable jt, Vector width) {
		for(int i=0; i<getColumnCount(); i++) {
			double tailleCol = Double.parseDouble(width.elementAt(i).toString());
			float factor = (float)java.awt.Toolkit.getDefaultToolkit().getScreenResolution() / (float) 1024;

			int dim = (int) ((float)(tailleCol)*factor);
			jt.getColumnModel().getColumn(i).setPreferredWidth(dim);
		}
	}

	/**
	 * Retrieve the object type of one cell
	 * @param columnIndex
	 * @return Class of column (this return String each time !!)
	 */
    @SuppressWarnings("unchecked")
	public Class getColumnClass(int columnIndex) {
		return String.class;
	}

	/**
	 * Retrieve data from model
	 * @return A Vector used by the model
	 */
	public Vector<Vector> getData() { return data; }

	/**
	 * Retreive columns names
	 * @param columnIndex The index of the column
	 * @return            The column name
	 */
	public String getColumnName(int columnIndex) {
		if(columnsName != null) return columnsName.get(columnIndex);
		return null;
	}

	/**
	 * Get number of columns
	 * @return The number of columns
	 */
	public int getColumnCount() {
		return columnsName.size();
	}
	
	/**
	 * Get number of lines
	 * @return The number of lines
	 */
	public int getRowCount() {
		if(data != null) return data.size()-offset;
		else return -1;
	}

	/**
	 * Retreive value of one cell
	 * @param rowIndex    Line index
	 * @param columnIndex Column index
	 * @return            Value of cell
	 */
	public Object getValueAt(int rowIndex, int columnIndex) {
		Vector row = data.get(rowIndex+offset);
		if(row.size() <= columnIndex) return "";
		return row.get(columnIndex);
	}

	/**
	 * Remove one line from data
	 * @param rowIndex Number of line to remove
	 */
	public void deleteRow(int rowIndex) {
		data.remove(rowIndex+offset);
		setModified(true);
	}

	/**
	 * Remove one column from data
	 * @param columnIndex The column index to remove
	 */
    @SuppressWarnings("unchecked")
	public void deleteColumn(int columnIndex) {
		for(int i=0; i<data.size(); ++i) data.get(i).remove(columnIndex);
		columnsName = getData().get(0);		
		setModified(true);
	}

	/**
	 * Retreive edition informations
	 * @param rowIndex    line number
	 * @param columnIndex column number
	 * @return            If cell is editable or not
	 */
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return true;
	}

	/** 
	 * Set value of one cell
	 */
    @SuppressWarnings("unchecked")
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		Vector col = data.get(rowIndex+offset);
		// Verification of data
		if(data.size() <= rowIndex)    for(int i=data.size(); i<=rowIndex;    ++i) data.add(new Vector<String>());
		if(col.size()  <= columnIndex) for(int i=col.size();  i<=columnIndex; ++i) col.add("");

		col.setElementAt(aValue, columnIndex);
		setModified(true);
	}
	
	/**
	 * Add one row in table
	 */
	public void addRow() {
		getData().add(new Vector<String>(getColumnCount()));
		setModified(true);
	}

	/**
	 * Add one column in table
	 */
    @SuppressWarnings("unchecked")
	public void addColumn(String columnName) {
		getData().get(0).add(columnName);
		for(int i=offset; i<getRowCount()+offset; ++i) getData().get(i).add("");
		columnsName = getData().get(0);
		setModified(true);
	}

	public boolean hasBeenModified() { return modified; }
	public void setModified(boolean modified) { this.modified = modified; }	
	
	/** not implémented */
	public void removeTableModelListener(TableModelListener l) { /*to be implemented by childs*/ }
	/** not implémented */
	public void addTableModelListener(TableModelListener l) { /*to be implemented by childs*/ }
}
