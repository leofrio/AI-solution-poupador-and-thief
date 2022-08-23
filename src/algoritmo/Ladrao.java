package algoritmo;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Ladrao extends ProgramaLadrao {

	private final int PARADO = 0;
	private final int CIMA = 1;
	private final int BAIXO = 2;
	private final int DIREITA = 3;
	private final int ESQUERDA = 4;
	private Point posAnterior;
	
	private final int VISAOCIMA = 7;
	private final int VISAOBAIXO = 16;
	private final int VISAODIREITA = 12;
	private final int VISAOESQUERDA = 11;
	
	private boolean roubou;
	private int numMoedas;
	
	private int[][] matriz;

	Ladrao(){
		this.roubou = false;
		this.numMoedas = 0;
		this.matriz = new int[30][30];
		this.posAnterior = new Point();
	}


	public int acao() {
		
		confirmaRoubo();
		Point posicaoAtual = sensor.getPosicao();
		this.matriz[(int) posicaoAtual.getY()][(int) posicaoAtual.getX()]++;

		List<Sucessor> sucessores = getSucessor(posicaoAtual);
		
		persepcao();
		
//		printaMatriz();
		
		int retorno = tomarDecisao(sucessores);
		
		this.posAnterior = sensor.getPosicao();
		
		return retorno;
	}


	private List<Sucessor> getSucessor(Point posicaoAtual){

		List<Sucessor> sucessores = new ArrayList<Sucessor>();

		int x = (int) (posicaoAtual.getX() +1);

		if(x < 30){
			sucessores.add(new Sucessor(new Point(x,(int) posicaoAtual.getY()), DIREITA));
		}

		x = (int) posicaoAtual.getX() - 1;

		if(x > 0){
			sucessores.add(new Sucessor(new Point(x,(int) posicaoAtual.getY()),ESQUERDA));
		}

		int y = (int) posicaoAtual.getY() +1;

		if( y < 30 ){
			sucessores.add(new Sucessor(new Point((int) posicaoAtual.getX(), y),BAIXO));
		}

		y = (int) posicaoAtual.getY() - 1;

		if(y > 0){
			sucessores.add(new Sucessor(new Point((int) posicaoAtual.getX(), y), CIMA));
		}

		return sucessores;
	}
	
	
	private void persepcao() {
		int[] visao = sensor.getVisaoIdentificacao();
		Point posicaoAtual = sensor.getPosicao();
		int z = 0;
		
		for (int j = 0; j < 5; j++) {
			for(int i = 0; i < 5; i++) {
				if(i != 2 || j != 2) {
					
					if(visao[z] == 1 || visao[z] == 3) {
						this.matriz[(j-2)+posicaoAtual.y][(i-2)+posicaoAtual.x] += 10;
					}
					
					z++;
				}
			}
		}
	}
	
	
	private int tomarDecisao(List<Sucessor> sucessores) {
		
		int menorEsforco = Integer.MAX_VALUE;
		
		ArrayList<Integer> decisoesPossiveis = new ArrayList<Integer>();
		int decisao = PARADO;
		if(verPoupador() == PARADO || this.roubou) {
			
			if(verPoupador() == PARADO) {
				this.roubou = false;
			}			

				for (Sucessor s : sucessores) {
					
					int esforco = utilidade(s);
					if(esforco < menorEsforco) {
						decisoesPossiveis = new ArrayList<Integer>();
						menorEsforco = esforco;
						decisoesPossiveis.add(s.acaoGeradora);
					}
					else if(esforco == menorEsforco) {
						decisoesPossiveis.add(s.acaoGeradora);
					}
				}
				
				decisao = this.desempata(decisoesPossiveis.toArray(new Integer[decisoesPossiveis.size()]));
			
		} else {
			decisao = verPoupador();
		}
		
		
		return decisao;
	}
	
	
	private void printaMatriz() {
		System.out.println("MATRIZ UTILIDADE ------------------------------------");
		
		for(int i = 0; i < 30; i++) {
			for(int j = 0; j < 30; j++) {
				System.out.print("["+this.matriz[i][j]+"]");
			}
			System.out.println("");
		}
	}
	
	private int utilidade(Sucessor sucessor) {
		int visao[] = sensor.getVisaoIdentificacao();
		int v[] = {0, VISAOCIMA, VISAOBAIXO, VISAODIREITA, VISAOESQUERDA};
		
		if(visao[ v[sucessor.acaoGeradora] ] == 4 
				|| visao[ v[sucessor.acaoGeradora] ] == 5 
				|| visao[ v[sucessor.acaoGeradora] ] > 100) {
			return (this.matriz[sucessor.posicao.y][sucessor.posicao.x]) + 10;
		} else {
			return (this.matriz[sucessor.posicao.y][sucessor.posicao.x]);
		}
		
	}
	
	private int desempata(Integer[] opcoes) {
		Random gerador = new Random();
		return opcoes[gerador.nextInt(opcoes.length)];
	}
	
	// Checa existencia de um poupador no campo de visão
	public int verPoupador() {
		int[] visao = this.sensor.getVisaoIdentificacao();
		
		for(int i = 0; i < 24; i++) {
			
			if(visao[i] >= 100 && visao[i] < 200) {
				if((i >= 0) && (i <= 9)) {
					
					if(verObstaculos(VISAOCIMA) == true) {
						return CIMA;
					} else {
						
						switch(i) {
						case 0:
							if(((verObstaculos(5) == true) || (verObstaculos(6) == true)) && (verObstaculos(VISAOESQUERDA) == true)) {
								return ESQUERDA;
							}
							if(((verObstaculos(8) == true) || (verObstaculos(9) == true)) && (verObstaculos(VISAODIREITA) == true)) {
								return DIREITA;
							}
							break;
						case 1:
							if(((verObstaculos(5) == true) || (verObstaculos(6) == true)) && (verObstaculos(VISAOESQUERDA) == true)) {
								return ESQUERDA;
							}
							if(((verObstaculos(8) == true) || (verObstaculos(9) == true)) && (verObstaculos(VISAODIREITA) == true)) {
								return DIREITA;
							}
							break;
						case 5:
							if(((verObstaculos(5) == true) || (verObstaculos(6) == true)) && (verObstaculos(VISAOESQUERDA) == true)) {
								return ESQUERDA;
							}
							if(((verObstaculos(8) == true) || (verObstaculos(9) == true)) && (verObstaculos(VISAODIREITA) == true)) {
								return DIREITA;
							}
							break;
						case 6:
							if(((verObstaculos(5) == true) || (verObstaculos(6) == true)) && (verObstaculos(VISAOESQUERDA) == true)) {
								return ESQUERDA;
							}
							if(((verObstaculos(8) == true) || (verObstaculos(9) == true)) && (verObstaculos(VISAODIREITA) == true)) {
								return DIREITA;
							}
							break;
						case 3:
							if(((verObstaculos(8) == true) || (verObstaculos(9) == true)) && (verObstaculos(VISAODIREITA) == true)) {
								return DIREITA;
							}
							if(((verObstaculos(5) == true) || (verObstaculos(6) == true)) && (verObstaculos(VISAOESQUERDA) == true)) {
								return ESQUERDA;
							}
							break;
						case 4:
							if(((verObstaculos(8) == true) || (verObstaculos(9) == true)) && (verObstaculos(VISAODIREITA) == true)) {
								return DIREITA;
							}
							if(((verObstaculos(5) == true) || (verObstaculos(6) == true)) && (verObstaculos(VISAOESQUERDA) == true)) {
								return ESQUERDA;
							}
							break;
						case 8:
							if(((verObstaculos(8) == true) || (verObstaculos(9) == true)) && (verObstaculos(VISAODIREITA) == true)) {
								return DIREITA;
							}
							if(((verObstaculos(5) == true) || (verObstaculos(6) == true)) && (verObstaculos(VISAOESQUERDA) == true)) {
								return ESQUERDA;
							}
							break;
						case 9:
							if(((verObstaculos(8) == true) || (verObstaculos(9) == true)) && (verObstaculos(VISAODIREITA) == true)) {
								return DIREITA;
							}
							if(((verObstaculos(5) == true) || (verObstaculos(6) == true)) && (verObstaculos(VISAOESQUERDA) == true)) {
								return ESQUERDA;
							}
							break;
						default:
							if(((verObstaculos(5) == true) || (verObstaculos(6) == true)) && (verObstaculos(VISAOESQUERDA) == true)) {
								return ESQUERDA;
							}
							
							if(((verObstaculos(8) == true) || (verObstaculos(9) == true)) && (verObstaculos(VISAODIREITA) == true)) {
								return DIREITA;
							}
						}	
						return PARADO;
					}
				} else {
					if((i >= 14) && (i <= 23)) {
							
							if(verObstaculos(VISAOBAIXO) == true) {
							return BAIXO;
						} else {
							
							switch(i) {
							case 14:
								if(((verObstaculos(14) == true) || (verObstaculos(15) == true)) && (verObstaculos(VISAOESQUERDA) == true)) {
									return ESQUERDA;
								}
								if(((verObstaculos(17) == true) || (verObstaculos(18) == true)) && (verObstaculos(VISAODIREITA) == true)) {
									return DIREITA;
								}
								break;
							case 15:
								if(((verObstaculos(14) == true) || (verObstaculos(15) == true)) && (verObstaculos(VISAOESQUERDA) == true)) {
									return ESQUERDA;
								}
								if(((verObstaculos(17) == true) || (verObstaculos(18) == true)) && (verObstaculos(VISAODIREITA) == true)) {
									return DIREITA;
								}
								break;
							case 19:
								if(((verObstaculos(14) == true) || (verObstaculos(15) == true)) && (verObstaculos(VISAOESQUERDA) == true)) {
									return ESQUERDA;
								}
								if(((verObstaculos(17) == true) || (verObstaculos(18) == true)) && (verObstaculos(VISAODIREITA) == true)) {
									return DIREITA;
								}
								break;
							case 20:
								if(((verObstaculos(14) == true) || (verObstaculos(15) == true)) && (verObstaculos(VISAOESQUERDA) == true)) {
									return ESQUERDA;
								}
								if(((verObstaculos(17) == true) || (verObstaculos(18) == true)) && (verObstaculos(VISAODIREITA) == true)) {
									return DIREITA;
								}
								break;
							case 17:
								if(((verObstaculos(17) == true) || (verObstaculos(18) == true)) && (verObstaculos(VISAODIREITA) == true)) {
									return DIREITA;
								}
								if(((verObstaculos(14) == true) || (verObstaculos(15) == true)) && (verObstaculos(VISAOESQUERDA) == true)) {
									return ESQUERDA;
								}
								break;
							case 18:
								if(((verObstaculos(17) == true) || (verObstaculos(18) == true)) && (verObstaculos(VISAODIREITA) == true)) {
									return DIREITA;
								}
								if(((verObstaculos(14) == true) || (verObstaculos(15) == true)) && (verObstaculos(VISAOESQUERDA) == true)) {
									return ESQUERDA;
								}
								break;
							case 22:
								if(((verObstaculos(17) == true) || (verObstaculos(18) == true)) && (verObstaculos(VISAODIREITA) == true)) {
									return DIREITA;
								}
								if(((verObstaculos(14) == true) || (verObstaculos(15) == true)) && (verObstaculos(VISAOESQUERDA) == true)) {
									return ESQUERDA;
								}
								break;
							case 23:
								if(((verObstaculos(17) == true) || (verObstaculos(18) == true)) && (verObstaculos(VISAODIREITA) == true)) {
									return DIREITA;
								}
								if(((verObstaculos(14) == true) || (verObstaculos(15) == true)) && (verObstaculos(VISAOESQUERDA) == true)) {
									return ESQUERDA;
								}
								break;
							default:
								if(((verObstaculos(14) == true) || (verObstaculos(15) == true)) && (verObstaculos(VISAOESQUERDA) == true)) {
									return ESQUERDA;
								}
								
								if(((verObstaculos(17) == true) || (verObstaculos(18) == true)) && (verObstaculos(VISAODIREITA) == true)) {
									return DIREITA;
								}
							}	
							return PARADO;
							
						}
				
					} else {
						if((i == 10) || (i == 11)) {
							
							if(verObstaculos(11) == true) {
								return ESQUERDA;
							} else {
								if(((verObstaculos(1) == true) || (verObstaculos(6) == true))  && (verObstaculos(VISAOCIMA) == true)) {
									return CIMA;
								}
								
								if(((verObstaculos(15) == true) || (verObstaculos(20) == true))  && (verObstaculos(VISAOBAIXO) == true)) {
									return BAIXO;
								}
								return PARADO;
							}
						} else {
							if((i == 12) || (i == 13)) {
								
								if(verObstaculos(12) == true) {
									return DIREITA;
								} else {
									if(((verObstaculos(3) == true) || (verObstaculos(8) == true))  && (verObstaculos(VISAOCIMA) == true)) {
										return CIMA;
									}
									
									if(((verObstaculos(17) == true) || (verObstaculos(22) == true))  && (verObstaculos(VISAOBAIXO) == true)) {
										return BAIXO;
									}
									return PARADO;
								}
							}
						}
					}
				}	
			}
			
		} 
		return PARADO;
	}
	
	// Checa se determinada célula possui parede, banco, moeda ou pastilha do poder
	public boolean verObstaculos(int celula) {
		
		int[] visao = this.sensor.getVisaoIdentificacao();
		
		if((visao[celula] == 1) || (visao[celula] == 3) || (visao[celula] == 4) || (visao[celula] == 5)) {
			return false;
		}
		
		return true;
	}
	
	// Checa se há marca olfativa de um poupador no campo de olfato
	public int cheirarPoupador() {

		int posicaoParaIr = 0;

		int odorPosicao = 0;

		int[] olfatoPoupador = this.sensor.getAmbienteOlfatoPoupador();

		// Verificar qual posiÃ§Ã£o do vetor tem o menor nÃºmero diferente de zero, ou seja, qual odor foi gerado a menos unidades de tempo

		for (int i = 0; i < olfatoPoupador.length; i++) {

			if(olfatoPoupador[i]!=0) { // Se sentir Odor de Poupador				

				if((odorPosicao!=0)&&(odorPosicao>olfatoPoupador[i])) { // Se Odor da CÃ©lula for Menor que Odor jÃ¡ Sentido

					posicaoParaIr = i;

					odorPosicao = olfatoPoupador[i];

				} else { //Primeiro Odor Sentido

					posicaoParaIr = i;

					odorPosicao = olfatoPoupador[i];

				}
			}
		}
		if(odorPosicao == -1) {
			return PARADO;
		}
		if ((posicaoParaIr==1)||(posicaoParaIr==2)) {
			return CIMA;			
		}
		if ((posicaoParaIr==5)||(posicaoParaIr==6)||(posicaoParaIr==7)) {
			return BAIXO;
		}
		if (posicaoParaIr==3) {
			return ESQUERDA;
		}
		if (posicaoParaIr==4) {
			return DIREITA;
		} else {
			return PARADO;
		}
	}
	
	private void confirmaRoubo() {
		if( sensor.getNumeroDeMoedas() > this.numMoedas || this.posAnterior.equals(sensor.getPosicao())) {
			this.numMoedas = sensor.getNumeroDeMoedas();
			this.roubou = true;
		}
	}
	
	private class Sucessor{
		
		public Point posicao;
		
		public int acaoGeradora;
		
		Sucessor(Point posicao, int acaoGeradora){
			this.posicao = posicao;
			this.acaoGeradora = acaoGeradora;
		}
		
	}

}
