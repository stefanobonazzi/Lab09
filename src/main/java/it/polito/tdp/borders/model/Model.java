package it.polito.tdp.borders.model;

import java.util.*;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.jgrapht.traverse.DepthFirstIterator;

import it.polito.tdp.borders.db.BordersDAO;

public class Model {

	private List<Country> allCountries;
	private List<Country> countries;
	private Map<Integer, Country> mAllCountries;
	private Map<Country, Integer> stats; 
	private BordersDAO dao;
	private Graph<Country, DefaultEdge> graph;
	
	public Model() {
		this.dao = new BordersDAO();
		this.allCountries = dao.loadAllCountries();
		this.mAllCountries = new HashMap<>();
		
		for(Country c: allCountries) {
			mAllCountries.put(c.getCCode(), c);
		}
	}

	public Graph<Country, DefaultEdge> createGraph(int year) {
		this.graph = new SimpleDirectedGraph<>(DefaultEdge.class);
		
		List<Border> borders = new ArrayList<>(dao.getCountryPairs(year));
		this.countries = new ArrayList<>();
		
		for(Border b: borders) {
			if(!countries.contains(this.mAllCountries.get(b.getState1no())))
				countries.add(this.mAllCountries.get(b.getState1no()));
			if(!countries.contains(this.mAllCountries.get(b.getStateno2())))
				countries.add(this.mAllCountries.get(b.getStateno2()));
		}
		
		Graphs.addAllVertices(graph, this.countries);

		for(Border b: borders) {
			graph.addEdge(this.mAllCountries.get(b.getState1no()), this.mAllCountries.get(b.getStateno2()));
		}
		
		for(Country c: countries) {
			int grade = graph.degreeOf(c);
			c.setGrade(grade);
		}
		
		return this.graph;
	}
	
	public int getNumberOfConnectedComponents() {
		ConnectivityInspector<Country, DefaultEdge> inspector = new ConnectivityInspector<>(this.graph);
		int n = 0;
		List<Set<Country>> set = inspector.connectedSets();
		n = set.size();
		
		return n;
	}
	
	public List<Country> getCountries() {
		return this.countries;
	}
	
	public Map<Country, Integer> getCountryCounts() {
		return null;
	}
	
	public Set<Country> getPath(Country country) {
		Set<Country> set = new HashSet<>();
		if(!this.graph.containsVertex(country)) 
			return null;
		
		DepthFirstIterator<Country, DefaultEdge> iterator = new DepthFirstIterator<>(this.graph, country);
		
		while (iterator.hasNext()) {
			set.add(iterator.next());
		}
		
		return set;
	}
	
}
