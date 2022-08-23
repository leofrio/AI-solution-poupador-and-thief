package algoritmo;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import controle.Constantes;

public class Poupador extends ProgramaPoupador {

	private int parado = 0;
	private int cima = 1;
	private int baixo = 2;
	private int direita = 3;
	private int esquerda = 4;
	private int[][] matrix;
	private int x;
	private int y;
	private final Point posicaoBanco;
	private boolean passedBybank;
	private boolean goingBank;
	private boolean hasFoundBank;
	private boolean lookingForBank;
	private ArrayList<Point> pathsWalked;
	private int coinsBank;
	private ArrayList<Point> pathToTake;
	private Point prev;
	private int gIndex = 1;
	private Point supposed;

	public Poupador() {
		matrix = new int[30][30];
		this.posicaoBanco = Constantes.posicaoBanco;
		this.passedBybank = false;
		this.goingBank = false;
		this.hasFoundBank = false;
		this.lookingForBank = false;
		this.pathsWalked = new ArrayList<Point>();
		this.coinsBank = sensor.getNumeroDeMoedasBanco();
		this.pathToTake = new ArrayList<Point>();
		this.prev = null;
	}

	public int acao() {
		this.x = sensor.getPosicao().x;
		this.y = sensor.getPosicao().y;
		if (this.x > 0)
			if (this.matrix[this.x - 1][this.y] < -1)
				System.out.println("STOP");
		if (this.x < 29)
			if (this.matrix[this.x + 1][this.y] < -1)
				System.out.println("STOP");
		if (this.y > 0)
			if (this.matrix[this.x][this.y - 1] < -1)
				System.out.println("STOP");
		if (this.y < 29)
			if (this.matrix[this.x][this.y + 1] < -1)
				System.out.println("STOP");
		updateMatrix();
		boolean nextToBank = checkIfNextToBank();
		if (nextToBank) {
			this.hasFoundBank = true;
			this.lookingForBank = false;
			if (!this.passedBybank) {
				this.passedBybank = true;
				int[] vis = sensor.getVisaoIdentificacao();
				this.coinsBank = sensor.getNumeroDeMoedas();
				if (vis[11] == 3) {
					this.prev = sensor.getPosicao();
					return esquerda;
				}
				if (vis[12] == 3) {
					this.prev = sensor.getPosicao();
					return direita;
				}
				if (vis[7] == 3) {
					this.prev = sensor.getPosicao();
					return cima;
				}
				if (vis[16] == 3) {
					this.prev = sensor.getPosicao();
					return baixo;
				}
			}
			this.goingBank = false;
		}
		if (sensor.getNumeroDeMoedas() > 6) {
			double chance = ((double) (Math.round(sensor.getNumeroDeMoedas() / 3))) / 10;
			double initialrd = Math.random() * 0.7;
			double res = chance + initialrd;
			if (res > 1) {
				this.goingBank = true;
			}
		}
		if (goingBank) {
			if (this.hasFoundBank) {
				if (sensor.getNumeroDeMoedas() == 0 || checkIfNextToBank()) {
					this.goingBank = false;
					this.lookingForBank = false;
					this.coinsBank = sensor.getNumeroDeMoedasBanco();
				} else {

					/*
					 * Map<String, Integer> rawPoints = getRawPoints(); if (pathToTake.isEmpty()) {
					 * this.gIndex = 1; this.pathToTake = BFS(sensor.getPosicao()); String nextPath
					 * =goToPos(this.pathToTake.get(gIndex));
					 * this.supposed=this.pathToTake.get(gIndex); rawPoints.replace(nextPath,
					 * rawPoints.get(nextPath) +100); gIndex++; String a=biggest(rawPoints); if
					 * (a.equals("left")) { this.prev = sensor.getPosicao(); return esquerda; } else
					 * if (a.equals("right")) { this.prev = sensor.getPosicao(); return direita; }
					 * else if (a.equals("up")) { this.prev = sensor.getPosicao(); return cima; }
					 * else { this.prev = sensor.getPosicao(); return baixo; } } else {
					 * if(this.supposed.x == this.x && this.supposed.y == this.y) { String nextPath
					 * =goToPos(this.pathToTake.get(gIndex));
					 * this.supposed=this.pathToTake.get(gIndex); rawPoints.replace(nextPath,
					 * rawPoints.get(nextPath) +100); String a=biggest(rawPoints); gIndex++; if
					 * (a.equals("left")) { this.prev = sensor.getPosicao(); return esquerda; } else
					 * if (a.equals("right")) { this.prev = sensor.getPosicao(); return direita; }
					 * else if (a.equals("up")) { this.prev = sensor.getPosicao(); return cima; }
					 * else { this.prev = sensor.getPosicao(); return baixo; } } else {
					 * this.pathToTake.clear(); this.gIndex=1; this.pathToTake =
					 * BFS(sensor.getPosicao()); String nextPath
					 * =goToPos(this.pathToTake.get(gIndex));
					 * this.supposed=this.pathToTake.get(gIndex); rawPoints.replace(nextPath,
					 * rawPoints.get(nextPath) +100); gIndex++; String a=biggest(rawPoints); if
					 * (a.equals("left")) { this.prev = sensor.getPosicao(); return esquerda; } else
					 * if (a.equals("right")) { this.prev = sensor.getPosicao(); return direita; }
					 * else if (a.equals("up")) { this.prev = sensor.getPosicao(); return cima; }
					 * else { this.prev = sensor.getPosicao(); return baixo; } } }
					 */

					this.lookingForBank = true;
				}
			} else {
				this.lookingForBank = true;
			}
		}
		if (this.lookingForBank) {
			this.pathsWalked.add(new Point(this.x, this.y));
			int[] visoes = sensor.getVisaoIdentificacao();
			for (int i = 0; i < visoes.length; i++) {
				if (visoes[i] == 3) {
					if (i == 11 || i == 10 || i == 14 || i == 5) {
						if (i == 11 || i == 10) {
							this.hasFoundBank = true;
							this.lookingForBank = false;
							this.pathsWalked.clear();
						}
						this.prev = sensor.getPosicao();
						return esquerda;
					} else if (i == 12 || i == 13 || i == 9 || i == 18) {
						if (i == 12 || i == 13) {
							this.hasFoundBank = true;
							this.lookingForBank = false;
							this.pathsWalked.clear();
						}
						this.prev = sensor.getPosicao();
						return direita;
					} else if (i == 7 || i == 2 || i == 1 || i == 3) {
						if (i == 7 || i == 2) {
							this.hasFoundBank = true;
							this.lookingForBank = false;
							this.pathsWalked.clear();
						}
						this.prev = sensor.getPosicao();
						return cima;
					} else if (i == 16 || i == 21 || i == 20 || i == 22) {
						if (i == 16 || i == 21) {
							this.hasFoundBank = true;
							this.lookingForBank = false;
							this.pathsWalked.clear();
						}
						this.prev = sensor.getPosicao();
						return baixo;
					}
				}
			}
			if (this.coinsBank != sensor.getNumeroDeMoedasBanco()) {
				this.coinsBank = sensor.getNumeroDeMoedas();
			}
			Map<String, Integer> a = new HashMap();
			Map<String, Integer> rawPoints = getRawPoints();
			pointsCheck5(rawPoints); 
			if(visoes[16]==1 && visoes[11] ==1) { 
				System.out.println();
			}
			boolean hasleft = false;
			boolean hasright = false;
			boolean hasup = false;
			boolean hasdown = false;
			if (this.x > 0) {
				if (this.matrix[this.x - 1][this.y] < 1100) {
					hasleft = true;
					a.put("left", getDistanceFromBank(new Point(this.x - 1, this.y)));
				}
			}

			if (this.x < 29) {
				if (this.matrix[this.x + 1][this.y] < 1100) {
					hasright = true;
					a.put("right", getDistanceFromBank(new Point(this.x + 1, this.y)));
				}
			}
			if (this.y > 0) {
				if (this.matrix[this.x][this.y - 1] < 1100) {
					hasup = true;
					a.put("up", getDistanceFromBank(new Point(this.x, this.y - 1)));
				}
			}
			if (this.y < 29) {
				if (this.matrix[this.x][this.y + 1] < 1100) {
					hasdown = true;
					a.put("down", getDistanceFromBank(new Point(this.x, this.y + 1)));
				}
			}
			// encouraging going to new places with a astraight line disntace closer to bank
			if (!a.isEmpty()) {
				String c = lowest(a);
				if (c.equals("left")) {

					rawPoints.replace("left", rawPoints.get("left") + 26);

				} else if (c.equals("right")) {

					rawPoints.replace("right", rawPoints.get("right") + 26);

				} else if (c.equals("up")) {

					rawPoints.replace("up", rawPoints.get("up") + 26);

				} else {
					rawPoints.replace("down", rawPoints.get("down") + 26);
				}
			}
			// encourage going to new places not explored
			if (hasleft) {
				if (this.matrix[this.x - 1][this.y] == 0)
					rawPoints.replace("left", rawPoints.get("left") + 25);
			}
			if (hasright) {
				if (this.matrix[this.x + 1][this.y] == 0)
					rawPoints.replace("right", rawPoints.get("right") + 25);
			}
			if (hasup) {
				if (this.matrix[this.x][this.y - 1] == 0)
					rawPoints.replace("up", rawPoints.get("up") + 25);
			}
			if (hasdown) {
				if (this.matrix[this.x][this.y + 1] == 0)
					rawPoints.replace("down", rawPoints.get("down") + 25);
			}
			// here we check to see if the poupador has passed by one of its 6 last places
			// it visited, if it has
			// we will make it go another way
			while (true) {
				String answer = biggest(rawPoints);
				if (answer.equals("left")) {
					boolean hasbeen = false;
					int i = (this.pathsWalked.size() > 5) ? this.pathsWalked.size() - 5 : 0;
					while (i < this.pathsWalked.size()) {
						if (this.x - 1 == this.pathsWalked.get(i).x && this.y == this.pathsWalked.get(i).y) {
							rawPoints.remove("left");
							hasbeen = true;
						}
						i++;
					}
					if (hasbeen) {
						continue;
					}
					this.prev = sensor.getPosicao();
					return esquerda;
				} else if (answer.equals("right")) {
					boolean hasbeen = false;
					int i = (this.pathsWalked.size() > 5) ? this.pathsWalked.size() - 5 : 0;
					while (i < this.pathsWalked.size()) {
						if (this.x + 1 == this.pathsWalked.get(i).x && this.y == this.pathsWalked.get(i).y) {
							rawPoints.remove("right");
							hasbeen = true;
						}
						i++;
					}
					if (hasbeen) {
						continue;
					}
					this.prev = sensor.getPosicao();
					return direita;
				} else if (answer.equals("up")) {
					boolean hasbeen = false;
					int i = (this.pathsWalked.size() > 5) ? this.pathsWalked.size() - 5 : 0;
					while (i < this.pathsWalked.size()) {
						if (this.x == this.pathsWalked.get(i).x && this.y - 1 == this.pathsWalked.get(i).y) {
							rawPoints.remove("up");
							hasbeen = true;
						}
						i++;
					}
					if (hasbeen) {
						continue;
					}
					this.prev = sensor.getPosicao();
					return cima;
				} else {
					boolean hasbeen = false;
					int i = (this.pathsWalked.size() > 5) ? this.pathsWalked.size() - 5 : 0;
					while (i < this.pathsWalked.size()) {
						if (this.x == this.pathsWalked.get(i).x && this.y + 1 == this.pathsWalked.get(i).y) {
							rawPoints.remove("down");
							hasbeen = true;
						}
						i++;
					}
					if (hasbeen) {
						continue;
					}
					this.prev = sensor.getPosicao();
					return baixo;
				}
			}
		}

		Map<String, Integer> rawPoints = getRawPoints();
		pointsCheck5(rawPoints);
		// repeatPoints(rawPoints);
		this.passedBybank = false;
		this.goingBank = false;
		this.lookingForBank = false;

		String a = biggest(rawPoints);
		if (a.equals("left")) {
			this.prev = sensor.getPosicao();
			return esquerda;
		} else if (a.equals("right")) {
			this.prev = sensor.getPosicao();
			return direita;
		} else if (a.equals("up")) {
			this.prev = sensor.getPosicao();
			return cima;
		} else {
			this.prev = sensor.getPosicao();
			return baixo;
		}

	}

	public String goToPos(Point pos) {
		int posx = pos.x;
		int posy = pos.y;
		if (pos.x == this.x - 1 && pos.y == this.y) {
			return "left";
		}
		if (pos.x == this.x + 1 && pos.y == this.y) {
			return "right";
		}
		if (pos.x == this.x && pos.y - 1 == this.y) {
			return "up";
		}
		if (pos.x == this.x && pos.y + 1 == this.y) {
			return "down";
		}
		return null;

	}

	public int getDistanceFromBank(Point pos) {
		int xbank = this.posicaoBanco.x;
		int ybank = this.posicaoBanco.y;
		int currentx = pos.x;
		int currenty = pos.y;
		int distancex = Math.abs(xbank - currentx);
		int distancey = Math.abs(ybank - currenty);
		int distance = distancex + distancey;
		return distance;
	}

	public Map<String, Integer> getRawPoints() {
		Map<String, Integer> points = new HashMap();
		int[] visoes = sensor.getVisaoIdentificacao();
		int[] cheiros = sensor.getAmbienteOlfatoLadrao();
		int leftpoints = 0;
		int left1 = pointsCheck1(visoes[11]);
		int left2 = pointsCheck2(visoes[10]);
		int left3 = pointsCheck3(visoes[5], visoes[6], visoes[14], visoes[15]);
		int left4 = pointsCheck4(cheiros[3], cheiros[0], cheiros[5]);
		int left5 = outerPoints(visoes[0], visoes[1], visoes[19], visoes[20]);
		leftpoints = left1 + left2 + left3 + left4 + left5;
		int rightpoints = 0;
		int right1 = pointsCheck1(visoes[12]);
		int right2 = pointsCheck2(visoes[13]);
		int right3 = pointsCheck3(visoes[8], visoes[9], visoes[17], visoes[18]);
		int right4 = pointsCheck4(cheiros[4], cheiros[2], cheiros[7]);
		int right5 = outerPoints(visoes[3], visoes[4], visoes[22], visoes[23]);
		rightpoints = right1 + right2 + right3 + right4 + right5;
		int uppoints = 0;
		int up1 = pointsCheck1(visoes[7]);
		int up2 = pointsCheck2(visoes[2]);
		int up3 = pointsCheck3(visoes[1], visoes[6], visoes[3], visoes[8]);
		int up4 = pointsCheck4(cheiros[1], cheiros[0], cheiros[2]);
		int up5 = outerPoints(visoes[0], visoes[5], visoes[4], visoes[9]);
		uppoints = up1 + up2 + up3 + up4 + up5;
		int downpoints = 0;
		int down1 = pointsCheck1(visoes[16]);
		int down2 = pointsCheck2(visoes[21]);
		int down3 = pointsCheck3(visoes[15], visoes[20], visoes[17], visoes[22]);
		int down4 = pointsCheck4(cheiros[6], cheiros[5], cheiros[7]);
		int down5 = outerPoints(visoes[14], visoes[19], visoes[18], visoes[23]);
		downpoints = down1 + down2 + down3 + down4 + down5;
		points.put("left", leftpoints);
		points.put("right", rightpoints);
		points.put("up", uppoints);
		points.put("down", downpoints);
		return points;
	}

	public boolean checkIfNextToBank() {
		int xbank = this.posicaoBanco.x;
		int ybank = this.posicaoBanco.y;
		if ((xbank - 1 == this.x && ybank == this.y) || (xbank + 1 == this.x && ybank == this.y)
				|| (xbank == this.x && ybank + 1 == this.y) || (xbank == this.x && ybank - 1 == this.y)) {
			return true;
		}
		return false;
	}

	public int pointsCheck1(int point) {
		if (point == -2) {
			return -10000;
		} else if (point == -1) {
			return -10000;
		} else if (point == 0) {
			return 0;
		} else if (point == 1) {
			return -10000;
		} else if (point == 3) {
			return -30;
		} else if (point == 4) {
			return 50;
		} else if (point == 5) {
			if (sensor.getNumeroDeMoedas() > 14)
				return 10;
			else {
				return -10;
			}
		} else if (point >= 100 && point < 199) {
			return -500;
		} else {
			return -1000;
		}
	}

	public int pointsCheck2(int point) {
		if (point == -2) {
			return -10000;
		} else if (point == -1) {
			return 0;
		} else if (point == 0) {
			return 0;
		} else if (point == 1) {
			return 0;
		} else if (point == 3) {
			return 0;
		} else if (point == 4) {
			return 25;
		} else if (point == 5) {
			if (sensor.getNumeroDeMoedas() > 14)
				return 10;
			else {
				return -10;
			}
		} else if (point >= 100 && point < 199) {
			return -500;
		} else {
			return -1000;
		}
	}

	public int pointsCheck3(int around1, int around2, int around3, int around4) {
		int[] arounds = { around1, around2, around3, around4 };
		int value = 0;
		for (int i = 0; i < arounds.length; i++) {
			if (arounds[i] >= 100 && arounds[i] < 199) {
				value += -400;
			}
			if (arounds[i] > 199) {
				value += -800;
			}
			if (arounds[i] == 4) {
				value += 3;
			}
		}
		return value;
	}

	public int pointsCheck4(int point, int around1, int around2) {
		if (point == 0 && around1 == 0 && around2 == 0) {
			return 0;
		} else if (point == 1 || around1 == 1 || around2 == 1) {
			return -400;
		} else if (point == 2 || around1 == 2 || around2 == 2) {
			return -300;
		} else if (point == 3 || around1 == 3 || around2 == 3) {
			return -100;
		} else if (point == 4 || around1 == 4 || around2 == 4) {
			return -20;
		} else if ((point == 5 || around1 == 5 || around2 == 5)) {
			return -10;
		}
		return 0;
	}

	public void pointsCheck5(Map<String, Integer> p) {
		String helper = checkLeastExplored();
		if (helper.equals("left")) {
			p.replace("left", p.get("left") + 13);
			p.replace("right", p.get("right") - 13);
			p.replace("up", p.get("up") - 13);
			p.replace("down", p.get("down") - 13);
		} else if (helper.equals("right")) {
			p.replace("right", p.get("right") + 13);
			p.replace("left", p.get("left") - 13);
			p.replace("up", p.get("up") - 13);
			p.replace("down", p.get("down") - 13);
		} else if (helper.equals("up")) {
			p.replace("up", p.get("up") + 13);
			p.replace("right", p.get("right") - 13);
			p.replace("left", p.get("left") - 13);
			p.replace("down", p.get("down") - 13);
		} else {
			p.replace("down", p.get("down") + 13);
			p.replace("right", p.get("right") - 13);
			p.replace("up", p.get("up") - 13);
			p.replace("left", p.get("left") - 13);
		}
	}

	public int outerPoints(int outer1, int outer2, int outer3, int outer4) {
		int value = 0;
		int[] a = { outer1, outer2, outer3, outer4 };
		for (int i = 0; i < a.length; i++) {
			if (a[i] > 199) {
				value += -300;
			}
			if (a[i] > 99 && a[i] < 199) {
				value += -100;
			}
		}
		return value;
	}

	public void repeatPoints(Map<String, Integer> p) {
		if (this.prev != null) {
			String ans = biggest(p);
			if (ans.equals("left")) {
				if (this.prev.x == this.x - 1 && this.prev.y == this.y)
					p.replace("left", p.get("left") - 1);
			} else if (ans.equals("right")) {
				if (this.prev.x == this.x + 1 && this.prev.y == this.y)
					p.replace("right", p.get("right") - 1);
			} else if (ans.equals("up")) {
				if (this.prev.x == this.x && this.prev.y == this.y - 1)
					p.replace("up", p.get("up") - 1);
			} else {
				if (this.prev.x == this.x && this.prev.y == this.y + 1)
					p.replace("down", p.get("down") - 1);
			}
		}
	}
	// so i figured i didnt really need a BL :0

	/*
	 * public ArrayList<Point> BFS(Point pos) { Queue<Point> line = new
	 * LinkedList<>(); ArrayList<Point> neighboors = new ArrayList<Point>();
	 * line.add(pos); int xbank = Constantes.posicaoBanco.x; int ybank =
	 * Constantes.posicaoBanco.y; ArrayList<Node> aux; ArrayList<Point> path = new
	 * ArrayList<Point>(); Point parent = null; Map<Node, Node> parents = new
	 * HashMap(); Set<Node> rep = new HashSet<Node>(); Graph graph = new Graph();
	 * Point current = new Point(0, 0); for (int i = 0; i < this.matrix.length; i++)
	 * { for (int j = 0; j < this.matrix[i].length; j++) { if (this.matrix[i][j] > 0
	 * && this.matrix[i][j] < 1100) { graph.add(new Node(new Point(i, j))); } } }
	 * graph.connect(); while (current.x != xbank && current.y != ybank) { current =
	 * line.remove(); aux = graph.getNeighboors(current); for (Node n : aux) { if
	 * (!rep.contains(n)) { parents.put(n, graph.search(current)); line.add(n.pos);
	 * rep.add(n); } } } while (current.x != pos.x && current.y != pos.y) { current
	 * = parents.get(graph.search(current)).pos; path.add(current); }
	 * Collections.reverse(path); return path;
	 * 
	 * while (true) { Point f = line.remove(); if (f.x == this.posicaoBanco.x && f.y
	 * == this.posicaoBanco.y) { Point h = new Point(f.x, f.y); while (h != null) {
	 * path.add(h); String w = turnToWord(h); h = parents.get(w); }
	 * Collections.reverse(path); break; } aux = checkNeighboors(f, parent); for
	 * (int i = 0; i < aux.size(); i++) { String s; s = turnToWord(aux.get(i));
	 * parents.put(s, f); line.add(aux.get(i)); } parent = f; } return path;
	 * 
	 * }
	 * 
	 * class Node { Point pos; ArrayList<Node> neighboors;
	 * 
	 * public Node(Point pos) { this.pos = pos; this.neighboors = new
	 * ArrayList<Node>(); } }
	 * 
	 * class Graph { int size; ArrayList<Node> nodes;
	 * 
	 * public Graph() { this.size = 0; this.nodes = new ArrayList<Node>(); }
	 * 
	 * public ArrayList<Node> getNeighboors(Point p) { Node a = search(p); if (a !=
	 * null) { return a.neighboors; } else { return null; } }
	 * 
	 * public void add(Node n) { this.size++; nodes.add(n); }
	 * 
	 * public void connect() { if (!this.nodes.isEmpty() || this.size != 0) { for
	 * (int i = 0; i < this.nodes.size(); i++) { ArrayList<Node> aux =
	 * findNeighboors(this.nodes.get(i), this); if (aux == null) { continue; } for
	 * (Node n : aux) { this.nodes.get(i).neighboors.add(n); } } } }
	 * 
	 * public Node search(Point p) { if (this.nodes.isEmpty()) { return null; } else
	 * { for (int i = 0; i < nodes.size(); i++) { Node current = nodes.get(i); int
	 * currentx = current.pos.x; int currenty = current.pos.y; if (p.x == currentx
	 * && p.y == currenty) { return nodes.get(i); } } return null; } } }
	 * 
	 * public String turnToWord(Point p) { String s = Integer.toString(p.x) + "," +
	 * Integer.toString(p.y); return s; }
	 * 
	 * public ArrayList<Node> findNeighboors(Node n, Graph g) { Point pos = n.pos;
	 * ArrayList<Node> a = new ArrayList<Node>(); if (g.search(new Point(pos.x - 1,
	 * pos.y)) != null) { a.add(g.search(new Point(pos.x - 1, pos.y))); } if
	 * (g.search(new Point(pos.x + 1, pos.y)) != null) { a.add(g.search(new
	 * Point(pos.x + 1, pos.y))); } if (g.search(new Point(pos.x, pos.y - 1)) !=
	 * null) { a.add(g.search(new Point(pos.x, pos.y - 1))); } if (g.search(new
	 * Point(pos.x, pos.y + 1)) != null) { a.add(g.search(new Point(pos.x, pos.y +
	 * 1))); } if (a.isEmpty()) { return null; } else { return a; }
	 * 
	 * ArrayList<Point> a = new ArrayList<Point>(); boolean parentIsNull = (parent
	 * == null) ? true : false; if (pos.x > 0) { if (this.matrix[pos.x - 1][pos.y] >
	 * 0 && this.matrix[pos.x - 1][pos.y] < 1100) { if (!parentIsNull) { if (!(pos.x
	 * - 1 == parent.x && pos.y == parent.y)) a.add(new Point(pos.x - 1, pos.y)); }
	 * else { a.add(new Point(pos.x - 1, pos.y)); } } } if (pos.x < 29) { if
	 * (this.matrix[pos.x + 1][pos.y] > 0 && this.matrix[pos.x + 1][pos.y] < 1100) {
	 * if (!parentIsNull) { if (!(pos.x + 1 == parent.x && pos.y == parent.y))
	 * a.add(new Point(pos.x + 1, pos.y)); } else { a.add(new Point(pos.x + 1,
	 * pos.y)); } } } if (pos.y > 0) { if (this.matrix[pos.x][pos.y - 1] > 0 &&
	 * this.matrix[pos.x][pos.y - 1] < 1100) { if (!parentIsNull) { if (!(pos.x ==
	 * parent.x && pos.y - 1 == parent.y)) a.add(new Point(pos.x, pos.y - 1)); }
	 * else { a.add(new Point(pos.x, pos.y - 1)); } } } if (pos.y < 29) { if
	 * (this.matrix[pos.x][pos.y + 1] > 0 && this.matrix[pos.x][pos.y + 1] < 1100) {
	 * if (!parentIsNull) { if (!(pos.x == parent.x && pos.y + 1 == parent.y))
	 * a.add(new Point(pos.x, pos.y + 1)); } else { a.add(new Point(pos.x, pos.y +
	 * 1)); } } } return a;
	 * 
	 * }
	 */

	public boolean checkObstacle(int pos) {
		int[] vis = sensor.getVisaoIdentificacao();
		// checking to see if direction has unwalkable structure
		if (vis[pos] == -1 || vis[pos] == 1 || vis[pos] == 5 || vis[pos] == -2) {
			return true;
		}
		return false;
	}

	public void updateMatrix() {
		this.matrix[this.x][this.y] += 1;
		if (this.x > 0) {
			if (checkObstacle(11)) {
				this.matrix[this.x - 1][this.y] = 3000;
			}
		}
		if (this.x > 1) {
			if (checkObstacle(10)) {
				this.matrix[this.x - 2][this.y] = 3000;
			}
		}
		if (this.x < 29) {
			if (checkObstacle(12)) {
				this.matrix[this.x + 1][this.y] = 3000;
			}
		}
		if (this.x < 28) {
			if (checkObstacle(13)) {
				this.matrix[this.x + 2][this.y] = 3000;
			}
		}
		if (this.y > 0) {
			if (checkObstacle(7)) {
				this.matrix[this.x][this.y - 1] = 3000;
			}
		}
		if (this.y > 1) {
			if (checkObstacle(2)) {
				this.matrix[this.x][this.y - 2] = 3000;
			}
		}
		if (this.y < 29) {
			if (checkObstacle(16)) {
				this.matrix[this.x][this.y + 1] = 3000;
			}
		}
		if (this.y < 28) {
			if (checkObstacle(21)) {
				this.matrix[this.x][this.y + 2] = 3000;
			}
		}
	}

	public String checkLeastExplored() {
		Map<String, Integer> a = new HashMap();
		int[] vis = sensor.getVisaoIdentificacao();
		if (this.x > 0 && !checkObstacle(11) && vis[11] < 5 && vis[10] < 6 && this.matrix[this.x - 1][this.y] < 1000) {
			a.put("left", this.matrix[this.x - 1][this.y]);
		}
		if (this.x < 29 && !checkObstacle(12) && vis[12] < 5 && vis[13] < 6 && this.matrix[this.x + 1][this.y] < 1000) {
			a.put("right", this.matrix[this.x + 1][this.y]);
		}
		if (this.y > 0 && !checkObstacle(7) && vis[7] < 5 && vis[2] < 6 && this.matrix[this.x][this.y - 1] < 1000) {
			a.put("up", this.matrix[this.x][this.y - 1]);
		}
		if (this.y < 29 && !checkObstacle(16) && vis[16] < 5 && vis[21] < 6 && this.matrix[this.x][this.y + 1] < 1000) {
			a.put("down", this.matrix[this.x][this.y + 1]);
		}
		String aux = lowest(a);
		return aux;
	}

	public String lowest(Map<String, Integer> p) {
		int lowest = 3000;
		String key = "";
		ArrayList<String> a = new ArrayList<String>();
		for (Map.Entry<String, Integer> entry : p.entrySet()) {
			if (entry.getValue() <= lowest) {
				if (entry.getValue() < lowest) {
					a.clear();
					a.add(entry.getKey());
				} else {
					a.add(entry.getKey());
				}
				lowest = entry.getValue();
				key = entry.getKey();
			}
		}
		if (a.size() > 1) {
			String theone = a.get((int) (Math.random() * a.size()));
			return theone;
		} else {
			return key;
		}

	}

	/*
	 * public boolean allNegative(Map<String, Integer> p) { int counter = 0; for
	 * (Map.Entry<String, Integer> entry : p.entrySet()) { if (entry.getValue() < 0)
	 * { counter++; } } if (counter == p.size()) { return true; } return false; }
	 */

	public String biggest(Map<String, Integer> p) {
		int biggest = Integer.MIN_VALUE;
		String bigKey = "";
		ArrayList<String> a = new ArrayList<String>();
		for (Map.Entry<String, Integer> entry : p.entrySet()) {
			if (entry.getValue() >= biggest) {
				if (entry.getValue() > biggest) {
					a.clear();
					a.add(entry.getKey());
				} else {
					a.add(entry.getKey());
				}
				biggest = entry.getValue();
				bigKey = entry.getKey();
			}
		}
		if (a.size() > 1) {
			String theone = a.get((int) (Math.random() * a.size()));
			return theone;
		} else {
			return bigKey;
		}
	}

	static class Next {
		Point pos;
		int direction;

		public Next(Point pos, int direction) {
			this.pos = pos;
			this.direction = direction;
		}
	}
}