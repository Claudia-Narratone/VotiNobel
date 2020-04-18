package it.polito.tdp.nobel.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.nobel.db.EsameDAO;

public class Model {
	
	private double bestMedia=0.0;
	private Set<Esame> bestSoluzione=null;
	private List<Esame> esami;
	
	public Model() {
		EsameDAO dao=new EsameDAO();
		this.esami=dao.getTuttiEsami();
	}
	
	public Set<Esame> calcolaSottoinsiemeEsami(int numeroCrediti) {
		
		Set<Esame> parziale=new HashSet<>();
		
		cerca(parziale, 0, numeroCrediti);
		
		return bestSoluzione;
	}
	
	private void cerca(Set<Esame> parziale, int livello, int m) {
		
		//casi terminali
		int crediti= sommaCrediti(parziale);
		if(crediti>m)
			return;
		if(livello==esami.size())
			return;
		if(crediti==m) {
			double media=calcolaMedia(parziale);
			if(media>bestMedia) {
				bestSoluzione=new HashSet<>(parziale);
				bestMedia=media;
			}
		}
		//generare sottoproblemi
		//esami[L] da aggiungere o no? provo entrambe le cose
		parziale.add(esami.get(livello));
		cerca(parziale, livello+1, m);
		parziale.remove(esami.get(livello));
		
		//provo a non aggiungerlo
		cerca(parziale, livello+1, m);
		
	}

	public double calcolaMedia(Set<Esame> parziale) {
		int crediti=0;
		int somma=0;
		for(Esame e:parziale) {
			crediti+=e.getCrediti();
			somma+=(e.getVoto())*e.getCrediti();
		}
		return somma/crediti;
	}

	private int sommaCrediti(Set<Esame> parziale) {
		int somma=0;
		for(Esame e:parziale) {
			somma+=e.getCrediti();
		}
		return somma;
	}

}


/*
 * 
 * INPUT:
 * -insieme di Esame (PARTENZA)
 * -# di crediti(m)
 * OUTPUT:
 *  -set di Esame, sottoinsieme di PARTENZA, tale per cui:
 *    *somma Esame.crediti==m
 *    *media Esame.voto sia MAX
 * APPROCCIO 1:generare i sottoinsiemi di PARTENZA 1 caso per volta,
 * decidendo esame per esame, se debba o non debba far parte della soluzione
 * 
 * Livello della ricorsione L mi indica quale corso sto decidendo se mettere
 * o non mettere
 * 
 * 		Analizzo Elemento 1 dell'insieme (livello 0)
 * 			-lo inserisco -> parziale={e1,e2}
 * 				Analizzo Elemento 2 dell'insieme(Livello 1)
 * 					-Lo inserisco -> parziale={e1,e2}
 * 					-non lo inserisco -> parziale{e1}
 * 			-non lo inserisco -> parziale={}
 * 				Analizzo Elemento 2 dell'insieme(Livello 1)
 * 					-Lo inserisco -> parziale={e2}
 * 					-non lo inserisco -> parziale{}
 * 
 * 
 * Soluzione parziale==un sottoinsieme composto dagli esami tra 0 eL-1
 * 
 * Generazione di un sottoproblema: decidere se inserire esame[L]
 * oppure no. due possibili sottoproblemi:
 * 		1. non aggiungo niente alla soluzione parziale
 * 		2. soluzione parziale + esami[L]
 * 
 * CASI TERMINALI
 * 
 * Nei casi terminali prima controllo il numero di crediti. Sr questa somma e >m
 * esco dalla ricorsione altrimenti controllo la media. Se la media è migliore
 * di tutte quelle viste fino a quel punto tengo traccia della soluz parziale.
 * 
 * 	-L=MAX (Non ci sono più corsi da aggiungere)
 * 		* se parziale.sommaCrediti==m -> calcolare la media
 * 				*se parziale.media()>media di tutte le altre soluzi
 * 				viste finora allora dovrò tenerne traccia
 * 
 * 		*se parziale.sommaCrediti!=m -> abbandono la soluz
 * 
 * 	-se parziale.sommaCrediti()>m -> mi fermo subito
 * 
 * 	-se parziale.sommaCrediti()==m -> calcolo subito la media
 * 			*se parziale.media()>media di tutte le altre soluzi
 * 				viste finora allora dovrò tenerne traccia
 * 
 * 
 * 
*/
