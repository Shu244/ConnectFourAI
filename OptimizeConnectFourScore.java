import java.util.ArrayList;

public class OptimizeConnectFourScore
{
	public int numOfRows = 8, 
			   numOfColumns = 8, 
			   numToWin = 4, 
			   difficulty = 8,  //difficulty can be between 1 to infinity.
			   winner; 
	public boolean continuePlaying = true; 
	public int[][] board = new int[numOfRows][numOfColumns]; //1 is human, 2 is computer.
	class Gene
	{
		public double a, b;
		Gene(double a, double b)
		{
			this.a = a;
			this.b = b;
		}
	}
	public int play(Gene one, Gene two) //returns 1 if computer 1 wins and 2 if computer 2 wins. 0 if tie. 
	{
		while(continuePlaying)
		{
			if(checkingForTie())
				return 0;
			computerTurn(1, one);				
			if(checkingForTie())
				return 0;
			computerTurn(2, two);
		}
		return winner; 
	}
	public boolean checkingForTie()
	{
		for(int c = 0; c < numOfColumns; c++)
			if(validRow(c, board) != -1)
				return false; 
		return true;
	}
	public double tempScore; 
	public void computerTurn(int player, Gene g)
	{
		int columnWithLargest = -1;
		double largestScore = Integer.MIN_VALUE;
		for(int c = 0; c < numOfColumns; c++)
		{
			int r = validRow(c, board);
			if(r == -1)
				continue;
			if(win(r, c, player, board))
			{
				columnWithLargest = c;
				break;
			}
			tempScore = 0;	
			int[][] boardCopy = copy(board);		
			boardCopy[r][c] = player;	
			tryAll(boardCopy, difficulty-1, player == 1? false : true, player, g);
			if(tempScore > largestScore)
			{
				largestScore = tempScore;
				columnWithLargest = c;
			}
			
		}
		placePiece(validRow(columnWithLargest, board), columnWithLargest, player, board);
	}
	//y = a^(-bx) is the equation for the distribution of scores. Consider a^(bx+c) as a distributor function.
	public void tryAll(int[][] board, int tempDifficulty, boolean computer1, int player, Gene g)
	{
		if(tempDifficulty == 0)
			return;
		for(int c = 0; c < numOfColumns; c++) //fix
		{
			int[][] boardCopy = copy(board);
			int r = validRow(c, boardCopy);
			if(r == -1)
				continue;
			else
			{
				boardCopy[r][c] = computer1 ? 1 : 2;
				if(win(r, c, board[r][c], boardCopy))
				{
					if(computer1 && player == 1)
						tempScore += Math.pow(g.a, -g.b*(difficulty - tempDifficulty));	
					else
						tempScore -= Math.pow(g.a, -g.b*(difficulty - tempDifficulty));	
					if(tempDifficulty == difficulty-1)
						continue;
					else
						return;
				}
				
			}
			tryAll(boardCopy, tempDifficulty-1, !computer1, player, g);
		}
	}
	public int[][] copy(int[][] origianl)
	{
		int [][] copy1 = new int[origianl.length][];
		for(int i = 0; i < origianl.length; i++)
			copy1[i] = origianl[i].clone();
		return copy1;
	}
	//returns -1 if no valid rows
	public int validRow(int column, int[][] board)
	{
		for(int r = numOfRows-1; r >= 0; r--)
			if(board[r][column] == 0)
				return r;
		return -1;
	}
	public void placePiece(int r, int column, int player, int[][] board)
	{
		if(player == 1)
		{
			board[r][column] = 1;  
			if(win(r, column, 1, board))
			{
					continuePlaying = false; 
					winner = 1; 
			}
		}
		else
		{
			board[r][column] = 2;  
			if(win(r, column, 2, board))
			{
					continuePlaying = false;
					winner = 2;
			}
		}
		
	}
	public boolean win(int row, int column, int player, int[][] board)
	{
		if(winDiagonally(row, column, player, board))
			return true;
		if(winHorizontally(row, column, player, board))
			return true;
		if(winVertically(row, column, player, board))
			return true;
		return false; 
		
	}
	public boolean winDiagonally(int row, int column, int player, int[][] board)
	{
		int consecutive = 1;
		//positive slope diagonal 
		for(int r = row - 1, c = column + 1; r >= 0 && c < numOfColumns; r--, c++)
		{
			if(board[r][c] == player)
				consecutive++;
			else
				break;
		}
		for(int r = row + 1, c = column - 1; r < numOfRows && c >= 0; r++, c--)
		{
			if(board[r][c] == player)
				consecutive++;
			else
				break;
		}
		
		if(consecutive >= numToWin)
			return true;
		consecutive = 1;
		
		//Negative slope diagonal
		for(int r = row - 1, c = column - 1; r >= 0 && c >= 0; r--, c--)
		{
			if(board[r][c] == player)
				consecutive++;
			else
				break;
		}
		for(int r = row + 1, c = column + 1; r < numOfRows && c < numOfColumns; r++, c++)
		{
			if(board[r][c] == player)
				consecutive++;
			else
				break;
		}
		return consecutive >= numToWin;
		
	}
	public boolean winHorizontally(int row, int column, int player, int[][] board)
	{
		int consecutive = 1;
		for(int c = column-1; c >= 0; c--)
		{
			if(board[row][c] == player)
				consecutive++; 
			else
				break;
		}
		for(int c = column+1; c < numOfColumns; c++)
		{
			if(board[row][c] == player)
				consecutive++; 
			else
				break;
		}
		return consecutive >= numToWin;
			
	}
	public boolean winVertically(int row, int column, int player, int[][] board)
	{
		int consecutive = 1;
		for(int r = row-1; r >= 0; r--)
		{
			if(board[r][column] == player)
				consecutive++; 
			else
				break;
		}
		for(int r = row+1; r < numOfRows; r++)
		{
			if(board[r][column] == player)
				consecutive++; 
			else
				break;
		}
		return consecutive >= numToWin;
	}
	
	
	
	public static void printBoard(int[][] board)
	{
		System.out.println();
		for(int r = 0; r < board.length; r++)
		{
			for(int c = 0; c < board[0].length; c++)
				System.out.print(board[r][c] + " ");
			System.out.println();
		}
		System.out.println();
		for(int i = 0; i < board[0].length; i++)
			System.out.print(i + " ");
		System.out.println("\n");
	}
	
	//---------------------Starting Genetic Algorithm------------------------
	
	public final int popSize = 500; 
	public final double mutationRate = 0.05; //1.0 means 100% mutation rate. 
	public final int numOfGenerations = 100000;
	public ArrayList<Gene> population = new ArrayList<Gene>();
	
	public void startPopulation()
	{
		for(int i = 0; i < popSize; i++)
		{
			double a = Math.random()*9 + 1;
			double b = Math.random()*10;
			population.add(new Gene(a, b));
		}
	}
	/*
	 * Indexes are for the location of competing Gene. 
	 */
	public int compete(int index1, int index2)
	{
		return play(population.get(index1), population.get(index2));
	}
	public Gene crossOver(Gene one, Gene two)
	{
		double a = Math.random() > 0.5? one.a : two.a;
		double b = Math.random() > 0.5? one.b : two.b;
		if(Math.random() <= mutationRate)
			a = Math.random()*9+1; 
		if(Math.random() <= mutationRate)
			b = Math.random()*10;
		return new Gene(a, b);
	}
	public void newPopulation()
	{
		ArrayList<Gene> winners = Winners();
		ArrayList<Gene> children = new ArrayList<Gene>();
		while(children.size() <= popSize)
		{
			Gene one = winners.get((int)(Math.random()*winners.size()));
			Gene two = winners.get((int)(Math.random()*winners.size()));
			children.add(crossOver(one, two));
		}
		population = children;
	}
	public ArrayList<Gene> Winners()
	{
		ArrayList<Gene> newPop = new ArrayList<Gene>();
		for(int i = 0; i < popSize; i++)
		{
			int index1 = (int)(Math.random()*popSize);
			int index2 = (int)(Math.random()*popSize);
			if(index1 == index2)
			{
				i -= 1;
				continue;
			}
			else
			{
				int winner = compete(index1, index2);
				if(winner == 0)
				{
					newPop.add(population.get(index1));
					newPop.add(population.get(index2));
					i += 1;
				}
				else if(winner == 1)
					newPop.add(population.get(index1));
				else 
					newPop.add(population.get(index2));
			}
		}
		return newPop;
	}
	public Gene winnerAmongPop()
	{
		if(population.size() == 1)
			return population.get(0); 
		int indexOfWinner = compete(0, 1)-1;
		population.remove(indexOfWinner == 0? 1 : 0);
		return winnerAmongPop();	
	}
	public void evolve()
	{
		startPopulation();
		for(int i = 0; i < numOfGenerations; i++)
			newPopulation();
		Gene fittest = winnerAmongPop(); 
		System.out.println("Optimal Gene: a = " + fittest.a + " b = " + fittest.b);
	}
	public static void main(String[] args)
	{
		new OptimizeConnectFourScore().evolve();
	}
}









