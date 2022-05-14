/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.borders;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;

import it.polito.tdp.borders.db.BordersDAO;
import it.polito.tdp.borders.model.Country;
import it.polito.tdp.borders.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	private Model model;
	private BordersDAO dao;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="cmbStati"
    private ComboBox<Country> cmbStati; // Value injected by FXMLLoader

    @FXML // fx:id="txtAnno1"
    private TextField txtAnno1; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCalcolaConfini(ActionEvent event) {
    	txtResult.setText("");
    	String y = txtAnno1.getText();
    	int year;
    	try {
    		year = Integer.parseInt(y);
		} catch (NumberFormatException e) {
			txtResult.setText("Inerisci un anno in formato numerico");
			return;
		}
    	if(year<1816 || year>2016) {
    		txtResult.setText("Inerisci una annata compresa tra il 1816 e il 2016");
    		return;
    	}
    	
    	model.createGraph(year);
    	List<Country> countries = this.model.getCountries();
    	Collections.sort(countries);
    	
    	for(Country c: countries) {
    		txtResult.appendText(c+" \t "+c.getGrade()+"\n");
    	}
    	
    	txtResult.appendText("Componenti connesse = "+model.getNumberOfConnectedComponents());
    	
    }

    @FXML
    void doStatiRaggiungibili(ActionEvent event) {
    	txtResult.setText("");
    	Country country = cmbStati.getValue();
    	
    	Set<Country> set = this.model.getPath(country);
    	if (set == null) {
    		txtResult.setText("Inserisci un Paese che sia presente nel grafo!");
    		return;
		}
    	
    	for(Country c: set) {
    		txtResult.appendText(c+"\n");
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert cmbStati != null : "fx:id=\"cmbStati\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtAnno1 != null : "fx:id=\"txtAnno1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
		this.dao = new BordersDAO();
		
		List<Country> countries = new ArrayList<>(dao.loadAllCountries());
		
		cmbStati.getItems().addAll(countries);
	}

}

