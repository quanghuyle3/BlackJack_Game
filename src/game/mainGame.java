package game;

import java.util.ArrayList
import java.util.Random;
import javax.swing.JOptionPane;

/**
 * This program stimulates BlackJack game
 * @author QuangLe
 */

public class mainGame {

	public static void main(String[] args) 
	{
		int newRound = 0; // A flag for continuing new game with JOptionPane.showConfirmDialog later
		
		// Run the game except the player choose to stop at the end of each round
		do 
		{
			// This ArrayList will contain 52 deck cards
			ArrayList<String> cardDecks = new ArrayList<String>();
			String[] ranks = {"Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
			String[] suits = {"-spades", "-clubs", "-diamonds" , "-hearts"};
			
			// Create 52-card deck 
			makeADeck(cardDecks, ranks, suits);
			
			// This ArrayList will contain player's cards
			ArrayList<String> playerCards = new ArrayList<>();
			
			// This ArrayList will contain computer's cards
			ArrayList<String> computerCards = new ArrayList<>();
			
			
			// Player gets 2 cards
			playerCards.add(getCard(cardDecks));
			playerCards.add(getCard(cardDecks));
			
			// Computer gets 2 cards
			computerCards.add(getCard(cardDecks));
			computerCards.add(getCard(cardDecks));
			
			
			// Display 2 cards of player
			JOptionPane.showMessageDialog(null, "Your cards: " + playerCards.get(0) + ", " + playerCards.get(1));
			
			// Check BlackJack
			int blackjack; // A variable to determine the one has BLACKJACK (-1: no one, 0: tie,  1: player has, 2: computer has)
			blackjack = checkBlackJack(playerCards, computerCards);
			
			// Announce BlackJack if anyone has an Ace and 10-point card
			announceBlackJack(blackjack);
			
			
			// If no one has BlackJack, deal card until player select "no"		
			if (blackjack == -1)
			{	
				boolean playerBurst = false; // A flag for BURST (>21)
				int yourPoints = checkPoints(playerCards, "player");
				
				// 0: Yes, 1: No, 2: Cancel
				int next = JOptionPane.showConfirmDialog(null, "Do you want to deal a card? ('Yes' to deal, 'No' to stop)");
				
				// Let player deal card as long as player choose 'yes' and under 21 points
				while (next == 0 && yourPoints < 21)
				{
					
					playerCards.add(getCard(cardDecks));
					
					String onHand = playerCards.get(0);
					for (int i =1; i < playerCards.size(); i++)
						onHand += ", " + playerCards.get(i);
					
					JOptionPane.showMessageDialog(null, "Your cards: " + onHand);
					yourPoints = checkPoints(playerCards, "player");
					
					// Check point after dealing a card, if burst, set playerBurst to true
					if (yourPoints <= 21)
						next = JOptionPane.showConfirmDialog(null, "Do you want to deal another card?");
					else
					{
						playerBurst = true;
						JOptionPane.showMessageDialog(null,"You burst. DEALER WINS THIS ROUND");
					}
				}
				
				// Continue checking point for computer if the player has not burst yet
				if (!(playerBurst))
				{
					
					boolean computerBurst = false; // A flag to mark computer burst
					int computerPoints = checkPoints(computerCards, "computer");
					JOptionPane.showMessageDialog(null, "Dealer has " + computerPoints + " points with " + computerCards);
					
					// Deal another card as long as computer has under 17 points
					while (computerPoints < 17)
					{
						JOptionPane.showMessageDialog(null, "Dealer deals another card");
						computerCards.add(getCard(cardDecks));
						computerPoints = checkPoints(computerCards, "computer");
						JOptionPane.showMessageDialog(null, "Dealer has " + computerPoints + "  points with " + computerCards);
						 
					}
					
					// Check if computer burst, otherwise, compare points
					if (computerPoints > 21)
					{
						JOptionPane.showMessageDialog(null, "Dealer bursts " + computerPoints + " points with " + computerCards + "\n PLAYER WINS THIS ROUND");
					}
					else
					{
						if (yourPoints > computerPoints)
							JOptionPane.showMessageDialog(null, "Player has " + yourPoints + " points, higher than dealer with " + computerPoints + " points.\n PLAYER WINS THIS ROUND");
						else if (yourPoints < computerPoints)
							JOptionPane.showMessageDialog(null, "Player has only " + yourPoints + " points, lower than dealer with " + computerPoints + " points.\n DEALER WINS THIS ROUND");
						else
							JOptionPane.showMessageDialog(null, "Player and dealer have the same " + yourPoints + " points. \n THIS ROUND TIE");
					}
				}
			}
			
			
			// Ask if player want to play another round
			newRound = JOptionPane.showConfirmDialog(null, "Do you want to play another round? ('Yes' to play, 'No' to stop)");
		} while (newRound == 0);

		System.exit(0);
	}
			
	
	/**
	 * The makeADeck method make a deck of 52 cards for a new round
	 * @param list An array list to contains 52 cards
	 * @param array1 The array of ranks
	 * @param array2 The array of suits
	 */
	public static void makeADeck(ArrayList<String> list, String[] array1, String[] array2)
	{
		for (String r : array1)
			for (String s : array2)
			{
				// Create 4 cards for each rank with 4 suits 
				list.add(r + s);
			}
	}
	
	
	/**
	 * The getCard method get a random card and remove it from deck
	 * @param deck The ArrayList contains 52 cards
	 * @return
	 */
	public static String getCard(ArrayList<String> deck)
	{
		Random rand = new Random();
		// Random a number
		int num = rand.nextInt(deck.size());
		// Get the card having the index of that random number
		String card =  deck.get(num);
		// Remove that card from the deck
		deck.remove(num);
		
		return card;
	}
	
	/**
	 * The checkBlackJack method checks if anyone has BlackJack - An Ace card with 10-point card
	 * @param player The ArrayList of player's cards
	 * @param computer The ArrayList of computer's cards
	 * @return 0 if both have BlackJack (BJ), 1 if player has BJ, 2 if computer has BJ, -1 if no one has
	 */
	public static Integer checkBlackJack(ArrayList<String> player, ArrayList<String> computer)
	{
		boolean playerBJ = false; // A flag to mark player has BJ
		boolean computerBJ = false; // A flag to mark computer has BJ
		
		// Check player
		if (((player.get(0)).charAt(0) == 'A') && (tenPointsCard(player.get(1))))
			playerBJ = true;
		else if (((player.get(1)).charAt(0) == 'A') && (tenPointsCard(player.get(0))))
			playerBJ = true;
		
		// Check computer
		if (((computer.get(0)).charAt(0) == 'A') && (tenPointsCard(computer.get(1))))
			computerBJ = true;
		else if (((computer.get(1)).charAt(0) == 'A') && (tenPointsCard(computer.get(0))))
			computerBJ = true;
		

		if (playerBJ && computerBJ)
			return 0;
		else if (playerBJ)
			return 1;
		else if (computerBJ)
			return 2;
		else
			return -1;
			
	}
	
	/** 
	 * The tenPointsCard method checks to see if that card is a 10-point card
	 * @param s The string of that card
	 * @return true if it's 10-point card, false if it's not
	 */
	public static Boolean tenPointsCard(String s)
	{
		char c = s.charAt(0);
		
		if (c == '1' || c == 'J' || c == 'Q' || c =='K')
			return true;
		else 
			return false;
		
	}
	
	/**
	 * The announceBlackJack method announces the one has BJ
	 * @param num The number indicates the one has BJ
	 */
	public static void announceBlackJack(Integer num)
	{
		if (num == 0)
			JOptionPane.showMessageDialog(null, "TIE. Dealer and player have BLACKJACK");
		else if (num == 1)
			JOptionPane.showMessageDialog(null, "Player has BLACKJACK. \n PLAYER WINS");
		else if (num == 2)
			JOptionPane.showMessageDialog(null, "Dealer has BLACKJACK. \n COMPUTER WINS");
		
	}
	
	/**
	 * The checkPoints method checks points for each card in the ArrayList
	 * @param array The ArrayList needs to be checked
	 * @param forWhom The variable indicates for 'player' or 'computer'
	 * @return The total points
	 */
	public static int checkPoints(ArrayList<String> array, String forWhom)
	{
		int points = 0;
		for (String card : array)
		{
			switch (card.charAt(0))
			{
			case 'A':
			{
				// This case for player
				if (forWhom.equals("player"))
				{
					String acePoint =JOptionPane.showInputDialog("You have " + array + ". You want to count Ace as 1 or 11? Enter 1 or 11");
					while (!(acePoint.equalsIgnoreCase("1") || acePoint.equalsIgnoreCase("11")))
					{
						acePoint =JOptionPane.showInputDialog("Error! Enter 1 or 11 for this " + card + " card agian.");
					}
					if (acePoint.equals("1"))
						points += 1;
					else if (acePoint.equals("11"))
						points += 11;
					
				}
				
				// This case for computer
				if (forWhom.equals("computer"))
				{
					points += 11;
				}
				break;
				
			}
			case '2':
			{
				points += 2;
				break;
			}
			case '3':
			{
				points += 3;
				break;
			}
			case '4':
			{
				points += 4;
				break;
			}
			case '5':
			{
				points += 5;
				break;
			}
			case '6':
			{
				points += 6;
				break;
			}
			case '7':
			{
				points += 7;
				break;
			}
			case '8':
			{
				points += 8;
				break;
			}
			case '9':
			{
				points += 9;
				break;
			}
			case '1':
			case 'J':
			case 'Q':
			case 'K':
			{
				points += 10;
				break;
			}
				
			}
			
		}
		
	// Display points if that ArrayList is for player
	if (forWhom == "player")	
		JOptionPane.showMessageDialog(null, "You have "+ points + " points now.");
	
	// If computer has over 21 points, change Ace to 1 points by minus 10 (if computer has Ace)
	if (forWhom == "computer" && points > 21)
	{
		for (String card : array)
		{
			if (card.charAt(0) == 'A')
			{
				points -= 10;
			}
		}
	}
	
	return points;
	
	}
}
