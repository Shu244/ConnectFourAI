import java.util.Scanner;

public class ConnectFourAI
{
	public static int numOfRows = 8, 
					  numOfColumns = 8, 
					  numToWin = 4, 
					  difficulty = 8; //difficulty can be between 1 to infinity. 
	
	//-----------------------------------------------------------------------------	
	public static double a = 8.486797251568142, b = 0.7830424806703618;
	public static int[][] board = new int[numOfRows][numOfColumns]; //1 is human, 2 is computer.
	public static void main(String[] args)
	{
		printBoard(board);
		while(true)
		{
			checkingForTie();
			humanTurn();
			printBoard(board);
			
			System.out.println("-------Computer--------");
			
			checkingForTie();
			computerTurn();
			printBoard(board);
		}
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
	public static void checkingForTie()
	{
		for(int c = 0; c < numOfColumns; c++)
			if(validRow(c, board) != -1)
				return; 
		System.out.println("TIE!");
		System.exit(0);
	}
	public static void humanTurn()
	{
		Scanner input = new Scanner(System.in);
		System.out.println("Column:");
		int c = input.nextInt();
		int r = validRow(c, board);
		while(r == -1)
		{
			System.out.println("Invalid Column, input another column");
			c = input.nextInt();
			r = validRow(c, board);
		}
		placePiece(r, c, 1, board);
	}
	public static double tempScore;
	//Timing is important, a win early on should weigh more than a ton of wins many games onwards. Make connect four play against itself.
	public static void computerTurn()
	{
		int columnWithLargest = -1;
		double largestScore = Integer.MIN_VALUE;
		for(int c = 0; c < numOfColumns; c++)
		{
			int r = validRow(c, board);
			if(r == -1)
				continue;
			if(win(r, c, 2, board))
			{
				columnWithLargest = c;
				break;
			}
			tempScore = 0;	
			int[][] boardCopy = copy(board);		
			boardCopy[r][c] = 2;		
			tryAll(boardCopy, difficulty-1, true);
			//System.out.println(tempScore);
			if(tempScore > largestScore)
			{
				largestScore = tempScore;
				columnWithLargest = c;
			}
			
		}
		placePiece(validRow(columnWithLargest, board), columnWithLargest, 2, board);
	}
	public static void tryAll(int[][] board, int tempDifficulty, boolean human)
	{
		if(tempDifficulty == 0)
			return;
		for(int c = 0; c < numOfColumns; c++)
		{
			int[][] boardCopy = copy(board);
			int r = validRow(c, boardCopy);
			if(r == -1)
				continue;
			else
			{
				if(human)
				{
					boardCopy[r][c] = 1;
					if(win(r, c, 1, boardCopy))
					{
						tempScore -= Math.pow(a, -b*(difficulty - tempDifficulty));
						if(tempDifficulty == difficulty-1)
							continue;
						else
							return;
					}				
				}
				else
				{
					boardCopy[r][c] = 2;
					if(win(r, c, 2, boardCopy))
					{
						tempScore += Math.pow(a, -b*(difficulty - tempDifficulty));			
						if(tempDifficulty == difficulty-1)
							continue;
						else
							return;
					}
				}
				
			}
			tryAll(boardCopy, tempDifficulty-1, !human);
		}
	}
	public static int[][] copy(int[][] origianl)
	{
		int [][] copy1 = new int[origianl.length][];
		for(int i = 0; i < origianl.length; i++)
			copy1[i] = origianl[i].clone();
		return copy1;
	}
	//returns -1 if no valid rows
	public static int validRow(int column, int[][] board)
	{
		for(int r = numOfRows-1; r >= 0; r--)
			if(board[r][column] == 0)
				return r;
		return -1;
	}
	public static void placePiece(int r, int column, int player, int[][] board)
	{
		if(player == 1)
		{
			board[r][column] = 1;  
			if(win(r, column, 1, board))
			{
					printBoard(board);
					System.out.println("Eyyyy you win.");
					System.exit(0); 
			}
		}
		else
		{
			board[r][column] = 2;  
			if(win(r, column, 2, board))
			{
					printBoard(board);
					System.out.println("The computer wins.");
					System.exit(0);
			}
		}
		
	}
	public static boolean win(int row, int column, int player, int[][] board)
	{
		if(winDiagonally(row, column, player, board))
			return true;
		if(winHorizontally(row, column, player, board))
			return true;
		if(winVertically(row, column, player, board))
			return true;
		return false; 
		
	}
	public static boolean winDiagonally(int row, int column, int player, int[][] board)
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
	public static boolean winHorizontally(int row, int column, int player, int[][] board)
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
	public static boolean winVertically(int row, int column, int player, int[][] board)
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
}







