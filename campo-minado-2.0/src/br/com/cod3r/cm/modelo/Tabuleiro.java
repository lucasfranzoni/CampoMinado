package br.com.cod3r.cm.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Tabuleiro implements CampoObservador{

	private final int linhas;
	private final int colunas;
	private final int minas;
	
	private final List<Campo> campos = new ArrayList<>();
	private final List<Consumer<ResultadoEvento>> observadores = new ArrayList<>();

	public Tabuleiro(int linhas, int colunas, int minas) {
		this.linhas = linhas;
		this.colunas = colunas;
		this.minas = minas;
		
		gerarCampos();
		associarVizinhos();
		sortearMinas();
	}
	
	public void abrir(int linha, int coluna) {
		campos.stream()
		.filter(c -> c.getLinha()==linha && c.getColuna()==coluna)
		.findFirst()
		.ifPresent(c -> c.abrir());
	}
	
	public void marcar(int linha, int coluna) {
		campos.stream()
		.filter(c -> c.getLinha()==linha && c.getColuna()==coluna)
		.findFirst()
		.ifPresent(c -> c.marcarOuDesmarcar());
	}
	
	public void paraCadaCampo(Consumer<Campo> funcao) {
		campos.forEach(funcao);
	}
	
	public boolean objetivoAlcancado() {
		return campos.stream().allMatch(c -> c.objeticoAlcancado());
	}
	
	public void reiniciar() {
		campos.forEach(c -> c.reiniciar());
		sortearMinas();
	}
	
	public int getLinhas() {
		return linhas;
	}
	
	public int getColunas() {
		return colunas;
	}
	
	public void eventoOcorreu(Campo campo, CampoEvento evento) {
		if (evento == CampoEvento.EXPLODIR) {
			monstrarMinas();
			notificarObservadores(false);
		} else if (objetivoAlcancado()) {
			notificarObservadores(true);
		}
	}
	
	public void registrarObservadores(Consumer<ResultadoEvento> observador) {
		observadores.add(observador);
	}
	
	private void gerarCampos() {
		for (int linha = 1; linha <= linhas; linha++) {
			for (int coluna = 1; coluna <= colunas; coluna++) {
				Campo campo = new Campo(linha,coluna);
				campo.registrarObersador(this);
				campos.add(campo);
			}
		}
	}

	private void associarVizinhos() {
		for (Campo c1 : campos) {
			for (Campo c2 : campos) {
				c1.adicionarVizinho(c2);
			}
		}
	}

	private void sortearMinas() {
		long minasArmadas;
		do {
			int aleatorio = (int) (Math.random() * campos.size());
			campos.get(aleatorio).minar();
			minasArmadas = campos.stream()
					.filter(c -> c.isMinado())
					.count(); 
		} while (minasArmadas < minas);
	}
	
	private void monstrarMinas() {
		campos.stream()
		.filter(c -> c.isMinado())
		.forEach(c -> c.setAberto(true));
	}
	
	private void notificarObservadores (boolean resultado) {
		observadores.stream().forEach(o -> o.accept(new ResultadoEvento(resultado)));
		
	}
}

