import java.awt.CardLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import javax.swing.JButton;
import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import javax.swing.SwingConstants;
import net.miginfocom.swing.MigLayout;
import javax.swing.JTextPane;
import java.awt.Color;
import java.awt.SystemColor;

public class MainFrame extends JFrame {

	/* START CONFIGURATIONS */
	protected final static int FRAME_WIDTH = 400;
	protected final static int FRAME_HEIGHT = 400;
	/* END CONFIGURATIONS */
	
	protected CardLayout cLayout;
	protected JPanel topPanel;
	protected JPanel cardPanel;
	protected JPanel buttonPanel;
	protected JPanel startPanel;
	protected JPanel gamePanel;

	protected JButton startButton;
	protected JButton highButton;
	protected JButton lowButton;
	protected JButton rollButton;
	protected JButton restartButton;
	
	protected Integer gameStep;
	protected Integer computerNum;
	protected Integer playerNum;

	private Integer score;
	private Integer savedScore;
	private Integer highScore;

	private JLabel highScoreText;
	private JLabel computerText;
	private JLabel computerRoll;
	private JLabel playerText;
	private JLabel playerRoll;
	private JLabel playerSubText;
	private JLabel resultText;
	private JLabel resultSubText;
	private JLabel currentScore;
	
	private JTextPane gameTitle;
	
	private String gameResults;
	
	Random random;
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) { 
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		highScore = 0;
		savedScore = 0;
		gameStep = 0;
		score = 0;
		random = new Random(); 
		
		// add savedScore from SQL method here, currently set to 69
		
        setTitle("High or Low");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		setBounds(800, 400, FRAME_WIDTH, FRAME_HEIGHT);
		
		
		cLayout = new CardLayout();
		topPanel = new JPanel();
		cardPanel = new JPanel();
		startPanel = new JPanel();
		gamePanel = new JPanel();
		buttonPanel = new JPanel();
		
		cardPanel.setLayout(cLayout);
		
		cardPanel.add(startPanel);
		cardPanel.add(gamePanel); 
		
		startButton = new JButton("Start");
		startButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg) {

				gameStep = 0;
				updateGame();
			}
		});
		
		highButton = new JButton("High");
		highButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg) {

				switch (gameStep)
				{
				    case 2: 

				    	playerText.setEnabled(false);
				    	lowButton.setEnabled(true);
				    	highButton.setEnabled(false);
				    	rollButton.setEnabled(true);
				    	playerSubText.setEnabled(true);
				    	playerSubText.setText("You say HIGH, press ROLL to see or change to LOW");
				    	playerSubText.setVisible(true);
				    	return;
				    	
				    default:
				        System.out.println("ERROR >>> unknown case at highButton, gameStep is at "+gameStep);
				}
				
			}
		});
		
		lowButton = new JButton("Low");
		lowButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg) {

				switch (gameStep)
				{
				    case 2: 

				    	playerText.setEnabled(false);
				    	lowButton.setEnabled(false);
				    	highButton.setEnabled(true);
				    	rollButton.setEnabled(true);
				    	playerSubText.setEnabled(true);
				    	playerSubText.setText("You say LOW, press ROLL to see or change to HIGH");
				    	playerSubText.setVisible(rootPaneCheckingEnabled);
				    	playerSubText.setVisible(true);
				    	return;
				    	
				    default:
				        System.out.println("ERROR >>> unknown case at lowButton, gameStep is at "+gameStep);
				}
				
			}
		});
		
		rollButton = new JButton("Roll");
		rollButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg) {
				
				switch (gameStep)
				{
			    	case 0: 

			    	updateGame();
			    	return;
			    	
				    case 1: 

				    	updateGame();
				    	return;
				    	
				    case 2: 

				    	updateGame(); 
				    	return; 
				    	
				    default:
				        System.out.println("ERROR >>> unknown case at rollButton, gameStep is at "+gameStep);
				    	return; 
				}
				
			}
		});
		
		restartButton = new JButton("Restart");
		restartButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg) {

				gameStep = 0;
	    		setupGame();
	    		gameStep++;
				
			}
		});
		
		buttonPanel.add(startButton);
		buttonPanel.add(highButton);
		buttonPanel.add(lowButton);
		buttonPanel.add(rollButton);
		buttonPanel.add(restartButton);
		startPanel.setLayout(new MigLayout());
		
		
		
		gameTitle = new JTextPane();
		gameTitle.setBackground(SystemColor.menu);
		gameTitle.setEditable(false); 
		gameTitle.setText("High or Low\r\nThe Guessing Game\r\n\r\nby Roberto Chavez");
		startPanel.add(gameTitle, "cell 0 0,push, align center");
		
		StyledDocument doc = gameTitle.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		
		highScoreText = new JLabel();
		highScoreText.setHorizontalAlignment(SwingConstants.CENTER);
		highScoreText.setHorizontalTextPosition(SwingConstants.CENTER);
		topPanel.add(highScoreText);
		highScoreText.setText("Highscore : "+savedScore);
		highScoreText.setAlignmentX(CENTER_ALIGNMENT);

		currentScore = new JLabel();
		currentScore.setHorizontalAlignment(SwingConstants.CENTER);
		currentScore.setHorizontalTextPosition(SwingConstants.CENTER);
		topPanel.add(currentScore);
		currentScore.setText("Current Score : 0");
		currentScore.setAlignmentX(CENTER_ALIGNMENT);
		
		computerText = new JLabel(); 
		gamePanel.add(computerText);
		//computerText.setText("Computer rolled : ");
		
		computerRoll = new JLabel();
		gamePanel.add(computerRoll);
		//computerRoll.setText("x");
		
		playerText = new JLabel();
		gamePanel.add(playerText);
		//playerText.setText("You rolled : ");

		playerSubText = new JLabel();
		gamePanel.add(playerSubText);
		
		playerRoll = new JLabel();
		gamePanel.add(playerRoll);
		//playerRoll.setText("x");
		
		resultText = new JLabel();
		gamePanel.add(resultText);
		
		resultSubText = new JLabel();
		gamePanel.add(resultSubText);
		
		rollButton.setVisible(false);
		highButton.setVisible(false);
		lowButton.setVisible(false);
		restartButton.setVisible(false);
		
		computerRoll.setVisible(false);
		computerText.setVisible(false);
		playerRoll.setVisible(false);
		playerText.setVisible(false);
		currentScore.setVisible(false);

		computerRoll.setVisible(false);
		computerText.setVisible(false);
		playerRoll.setVisible(false);
		playerText.setVisible(false);

        getContentPane().add(topPanel, BorderLayout.NORTH);
        getContentPane().add(cardPanel, BorderLayout.CENTER);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH); 
		saveScore("READ");
	}
	
	protected void setupGame() {
		
			//savedScore = 0;
			//gameStep = 1;
			//score = 0;
			//random = new Random();
			saveScore("READ");
		
		
			startButton.setEnabled(false);
			startButton.setVisible(false);
			
			rollButton.setVisible(true);
			rollButton.setEnabled(true);
			
			highButton.setVisible(true);
			lowButton.setVisible(true);
			
			restartButton.setVisible(true);
			restartButton.setEnabled(false);
			
			highScoreText.setVisible(false);
			currentScore.setVisible(true);
			computerRoll.setVisible(true);
			computerText.setVisible(true);
			playerRoll.setVisible(true);
			playerText.setVisible(true);
			

			currentScore.setText("Current Score : "+score);
			computerText.setEnabled(true);
			computerText.setVisible(true);
			computerRoll.setVisible(false);
			playerText.setVisible(false);
			playerSubText.setVisible(false);
			playerRoll.setVisible(false);
			resultText.setVisible(false);
			resultSubText.setVisible(false);
			
		
	}
	
	private void updateGame() {

    	System.out.println("Performing gameStep("+gameStep+")!");
    	
		switch (gameStep)
		{
		
		    case 0:	// Pressing Roll for computer number

		    	highButton.setEnabled(false);
		    	lowButton.setEnabled(false);
		    	restartButton.setEnabled(false);
		    	
				cLayout.last(cardPanel);
				setupGame();
				computerText.setText("It is the computer's turn to roll, press ROLL");
				computerText.setVisible(true); 
				
				break; 
	    	
		    case 1:	// Pressing Start when starting game

		    	computerNum = doRoll();
		    	computerRoll.setText("Computer's roll is : "+computerNum);
		    	computerRoll.setVisible(true);
		    	playerText.setText("It is you, the player's turn, to guess, HIGH or LOW?");
		    	playerText.setVisible(true);
		    	playerText.setEnabled(true);
		    	
				highButton.setEnabled(true);
				lowButton.setEnabled(true);
				rollButton.setEnabled(false);
				restartButton.setEnabled(false);
		    	computerText.setEnabled(false);
		    	
				break;
				
				
		    case 2:	// Pressing Roll for computer number

		    	rollButton.setEnabled(false);
		    	playerNum = doRoll();
		    	playerText.setEnabled(false);
		    	playerSubText.setEnabled(false);
		    	playerRoll.setText("Player's roll is : "+playerNum);
		    	playerRoll.setVisible(true);

		    	if (!lowButton.isEnabled()) {	// if player chose low for roll
		    		
		    		highButton.setEnabled(false);
		    		
		    		if (playerNum < computerNum) {
		    			
		    			gameResults("WINNER");
		    		}
		    		else if (playerNum == computerNum) {

		    			gameResults("DRAW");
		    		}
		    		else if (playerNum > computerNum) {

		    			gameResults("LOSER");
		    		}
		    		else 
		    		{
		    			System.out.println("ERROR >>> Unknown results at updateGame case2");
		    		}
		    	}
		    	
		    	else if (!highButton.isEnabled()) {	// else if player chose high for roll
		    		
		    		lowButton.setEnabled(false);
		    		
		    		if (playerNum > computerNum) {
		    			
		    			gameResults("WINNER");
		    		}
		    		else if (playerNum == computerNum) {

		    			gameResults("DRAW");
		    		}
		    		else if (playerNum < computerNum) {

		    			gameResults("LOSER");
		    		}
		    		else
		    		{
		    			System.out.println("ERROR >>> Unknown results at updateGame case2");
		    		}
		    	}
		    	
		    	else
		    	{

	    			System.out.println("ERROR >>> Unknown results at updateGame case2");
		    	}
				return; 
		    	
		    case 3:

		    	resultSubText.setText("Your highscore is currently "+highScore+", press RESTART to keep playing!");
		    	resultSubText.setVisible(true);
		    	restartButton.setEnabled(true);
		    	break;
			
		    default:
		    	
		        System.out.println("ERROR >>> unknown case at updateButtons, gameStep is at "+gameStep);
				return; 
		}
		
    	gameStep++;
		
	}

	protected int doRoll() {
		
		int randomRoll = random.nextInt(101);
		System.out.println("DEBUG >>> roll was "+randomRoll);
		
		return randomRoll;
	}

	private void gameResults(String x) {



    	gameStep++;
		switch (x)
		{
		    case "WINNER": 

		    	score++;
		    	
		    	if (score > savedScore) {	// Update highscore to current score if needed
		    		
		    		savedScore = score;
		    		saveScore("WRITE");
		    		resultText.setText("WINNER! You have beaten your highscore! It is now "+score+"!");
		    		
		    	}
		    	else
		    	{
		    		
			    	resultText.setText("You WON! Your winstreak is now "+score);
		    	}
				currentScore.setText("Current Score : "+score);
		    	break;
		    	
		    case "LOSER": 

		    	score = 0;
	    		resultText.setText("LOSER! You have lost and your current score has resetted to 0!");
		    	break;
		    	
		    case "DRAW": 

	    		resultText.setText("DRAW! Both are same! This is rare and might never happen again!");
		    	break;	
		    	
		    default:
		        System.out.println("ERROR >>> unknown case at rollButton, gameStep is at "+gameStep);
		    	return;
		}

		resultText.setVisible(true);
    	updateGame();
	}
	
	private void saveScore(String x) {
		
		File savedScoreFile = new File("hlsave.txt");
		
		switch (x) {
		
		case "WRITE":
			
			highScore = score;
			
			if (savedScoreFile.exists()) {
				
		        try {
					 BufferedReader readScore = new BufferedReader(new FileReader(savedScoreFile));
					 String temp = readScore.readLine();
					 readScore.close();
					 
					 FileWriter writeScore = new FileWriter(savedScoreFile);

					 System.out.println("[PAST]:"+temp);   
					 writeScore.write(String.valueOf(highScore)); 
					 System.out.println("debug1 = "+highScore);
					 System.out.println("debug2 = "+String.valueOf(highScore));
					 writeScore.close();
					 System.out.println("[CURRENT]:"+highScore);  
					 
					 
					 
				} catch (IOException e) {
					
					e.printStackTrace();
				}
			
			}
			else
			{

			    try {

			      if (savedScoreFile.createNewFile()) {
			    	  
			        FileWriter myWriter = new FileWriter(savedScoreFile);

					System.out.println("[PAST]: n/a");   
			        myWriter.write(String.valueOf(highScore));
			        myWriter.close();
					System.out.println("[CURRENT]:"+highScore);  
			        System.out.println("File created: " + savedScoreFile.getName()); 
			        
			      } else {
			    	  
			        System.out.println("File already exists."); 
			      }
			      
			    } catch (IOException e) {

			      e.printStackTrace();
			    }
				System.out.println("Does not have an old save, so we created one!");
			}
	        highScoreText.setText("Highscore : "+highScore);
			break;

		case "READ":

			if (savedScoreFile.exists()) {
				
		        try {
					 BufferedReader readScore = new BufferedReader(new FileReader(savedScoreFile));
					 String temp = readScore.readLine();
					 
					 System.out.println(temp);  
					 readScore.close();
					 highScoreText.setText("Highscore : "+temp);
					 
					} catch (IOException e) {
						
						e.printStackTrace();
					}
				
				}
				else
				{

					System.out.println("Does not have an old save, so we are setting to 0!");
	
				}
				break;
				
		default:
			break;
			
		}
		
	}
	
	
	
	
	
	
	
}
