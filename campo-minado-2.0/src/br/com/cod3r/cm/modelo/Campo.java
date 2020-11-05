package br.com.cod3r.cm.modelo;

import java.util.ArrayList;
import java.util.List;

public class Campo {

	private final int linha;
	private final int coluna;

	private boolean aberto = false;
	private boolean marcado =false;
	private boolean minado = false;
	
	private List<Campo> vizinhos = new ArrayList<>(); 
	private List<CampoObservador> observadores = new ArrayList<>();
	
	Campo(int linha, int coluna) {
		this.linha = linha;
		this.coluna = coluna;
	}
	
	public int getLinha() {
		return linha;
	}

	public int getColuna() {
		return coluna;
	}

	public boolean isMarcado() {
		return marcado;
	}
	
	public boolean isAberto() {
		return aberto;
	}
	
	public boolean isFechado() {
		return !aberto;
	}
	
	public boolean isMinado() {
		return minado;
	}

	public void marcarOuDesmarcar() {
		if (!aberto) {
			marcado = !marcado;
			if (marcado) {
				notificarObersavores(CampoEvento.MARCAR);
			} else {
				notificarObersavores(CampoEvento.DESMARCAR);
			}
		}
	}

	public boolean abrir() {
		if (!aberto && !marcado) {
			if (minado) {
				notificarObersavores(CampoEvento.EXPLODIR);
				return true;
			}	
			
			setAberto(true);
			
			if (vizinhacaSegura()) {
				vizinhos.forEach(v -> v.abrir());
			}
			return true;
		} else
			return false;
	}
	
	public boolean vizinhacaSegura() {
		return vizinhos.stream().noneMatch(v -> v.minado);
	}
	
	public int minasNaVizinhaca() {
		return (int)vizinhos.stream().filter(v -> v.minado).count();
	}
	
	public void registrarObersador(CampoObservador observador) {
		observadores.add(observador);
	}
	
	boolean adicionarVizinho(Campo vizinho) {
		int deltaLinha = Math.abs(linha - vizinho.linha);
		int deltaColuna = Math.abs(coluna - vizinho.coluna);
		int deltaGeral = deltaLinha + deltaColuna;

		boolean linhaDiferente = linha != vizinho.linha;
		boolean colunaDiferente = coluna != vizinho.coluna;
		boolean diagonal = linhaDiferente && colunaDiferente;
		
		if (deltaGeral==1) {
			vizinhos.add(vizinho);
			return true; 
		} else if (deltaGeral==2 & diagonal) {
			vizinhos.add(vizinho);
			return true;
		} else
			return false;
	}
	
	void minar() {
		minado = true;
	}
	
	boolean objeticoAlcancado() {
		boolean desvendado = !minado && aberto;
		boolean protegido = minado && marcado;
		return desvendado || protegido;
	}
	
	void reiniciar() {
		aberto = false;
		marcado = false;
		minado = false;
		notificarObersavores(CampoEvento.REINICIAR);
	}
	
	void setAberto(boolean aberto) {
		this.aberto = aberto;
		
		if (aberto)
			notificarObersavores(CampoEvento.ABRIR);
	}

	private void notificarObersavores (CampoEvento evento) {
		observadores.stream().forEach(o -> o.eventoOcorreu(this, evento));
	}
}

