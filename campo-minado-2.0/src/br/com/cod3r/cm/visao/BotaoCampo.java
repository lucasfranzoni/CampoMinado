package br.com.cod3r.cm.visao;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;

import br.com.cod3r.cm.modelo.Campo;
import br.com.cod3r.cm.modelo.CampoEvento;
import br.com.cod3r.cm.modelo.CampoObservador;

@SuppressWarnings("serial")
public class BotaoCampo extends JButton implements CampoObservador, MouseListener{
	
	private final Color BG_PADRAO = new Color(184,184,184);
	private final Color BG_MARCADO = new Color(8,179,247);
	private final Color BG_EXPLOSAO = new Color(189,66,68);
	private final Color TEXTO_VERDE = new Color(0,120,0);
	private final Color TEXTO_ROXO = new Color(99,33,99);
	private final Color TEXTO_VERMELHO = new Color(210,0,0);
	private final Color TEXTO_AZUL = new Color(0,0,200);
	
	private Campo campo;
	
	public BotaoCampo(Campo campo) {
		this.campo = campo;
		campo.registrarObersador(this);	
		setBorder(BorderFactory.createBevelBorder(0));
		setBackground(BG_PADRAO);
		addMouseListener(this);
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == 1) {
			campo.abrir();
		} else {
			campo.marcarOuDesmarcar();
		}
	}
	
	@Override
	public void eventoOcorreu(Campo campo, CampoEvento evento) {
		switch (evento) {
		case ABRIR:
			aplicarEstiloAbrir();
			break;
		case MARCAR:
			aplicarEstiloMarcar();
			break;
		case EXPLODIR:
			aplicarEstiloExplodir();
			break;
		default:
			aplicarEstiloPadrao();
			
		}
	}

	private void aplicarEstiloAbrir() {
		if(campo.isMinado()) {
			setBackground(BG_EXPLOSAO);
			return;
		}
		setBackground(BG_PADRAO);
		setBorder(BorderFactory.createLineBorder(Color.GRAY));
		switch (campo.minasNaVizinhaca()) {
		case 1: 
			setForeground(TEXTO_VERDE);
			break;
		case 2:
			setForeground(TEXTO_AZUL);
			break;
		case 3:
			setForeground(TEXTO_VERMELHO);
			break;
		case 4:
			setForeground(TEXTO_ROXO);
			break;
		case 5:
		case 6:
		default:
			setForeground(Color.PINK);
		}
		String texto = campo.vizinhacaSegura() ? "" : campo.minasNaVizinhaca() + "";
		setText(texto);
	}
	
	private void aplicarEstiloMarcar() {
		setBackground(BG_MARCADO);
		setForeground(Color.BLACK);
		setText("x");
	}
	
	private void aplicarEstiloExplodir() {
		setBackground(BG_EXPLOSAO);
		setForeground(Color.WHITE);
		setText("*");
	}
	
	private void aplicarEstiloPadrao() {
		setBackground(BG_PADRAO);
		setBorder(BorderFactory.createBevelBorder(0));
		setText("");
	}


	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseClicked(MouseEvent e) {}
}
