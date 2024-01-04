// Name: Joel Fu and Braden Chau
// Date: 1/14/2023
// File name: study++.java
/* Description: The software will provide the user with a study method recommendation for core courses
& subjects delivered at STL. This will be based on what course they have to study for, the
nature of that course and research based on what works best for that course/ subject. It will provide study methods
for the user to pick from: Flash Card , Matching and Note Taking. Finally it will schedule breaks to prevent burnout for effiecent
studying.
*/

import java.util.*;
import java.io.*;
import java.util.Timer;
import java.util.TimerTask;
// import everything we need

// general class for a card
class card{
	// declare varibles
	String front, back;
	// getter functions to access the class varibles
	public String getFront(){
		return front;
	}
	public String getBack(){
		return back;
	}
}

// use inheritance to inheritance the cards template from cards
class flashCards extends card{
	// contructor takes in front and back of card
	public flashCards(String front, String back){
		this.front = front;
		this.back = back;
	}
	
	// function to flip the flashcard (swap front and back)
	public void flip(){
		String temp;
		temp = front;
		front = back;
		back = temp;
	}
}
// class to handle the timer
class timer{
	// declare varibles
	static Timer clock = new Timer();
  	static int time = 0;
	static int cycle;
	
	// contructor gets the desired study rythem
	public timer(int type){
		cycle = type;
	}
	
	// anonymous class (which is declared and intialized at same time)
  	static TimerTask task = new TimerTask(){
    	public void run(){// Polymorphism overwrote the run function to schedule in the breaks
        	time++; // time keeps track of the number of seconds

			// some math to show how I got the seconds
			// 25 min x 60s/ min = 1500
			// 30 min x 60s/min = 1800
			// if each cycle is 30 min --> check if remainder is 25 min --> to achive the 25 min work and 5 min break
			
			// 90 min x 60s/ min = 5400
			// 120 min x 60s/min = 7200
			// if each cycle is 120 min --> check if remainder is 90 min --> to achive the 90 min work and 30 min break
			
			if(cycle==1&&time% 1800 == 1500){
				System.out.println("");
				System.out.println("Take a 5 min break");
				System.out.println("");
			}else if(cycle ==2 && time %7200 == 5400){
				System.out.println("");
				System.out.println("Take a 30 min break");
				System.out.println("");
			}
    	}
  	};
	
	// function to start the timer
	public void runTimer(){
    	clock.schedule(task, 0, 1000);
    }
	
	// function to stop the timer
    public void stopTimer(){
       clock.cancel(); 
    }
}

class Main {
	// declare buffered reader object
	static BufferedReader br = null;
	
	// declare a string tokenizer object
	static StringTokenizer st;
	// declare a print writer object
	static PrintWriter pw = null;

	// declare 2 arrayList to store data from the user and textfile 
	static ArrayList<flashCards> cards = new ArrayList<flashCards>();
	static ArrayList<String> notes = new ArrayList<String>();

	// create an instance of the timer class
	static timer tut;
    
	// function to get the next token
	static String next() throws IOException {
		while (st == null || !st.hasMoreTokens()) {
			st = new StringTokenizer(br.readLine().trim());
		}
		return st.nextToken();
	}

	// function to read a integer in a set range and parse it to int with built in error handling
	static int readInt(int low, int high) throws IOException {
		try {
			int ret;
			ret = Integer.parseInt(next());
			
			//check if the input is in the valid range
			while(ret<low||ret>high){
				System.out.println("Invalid input, Try Again");
				ret = readInt(low, high);
			}
			return ret;
		} catch (Exception err) {
			// if there is an error tell user and try again
			System.out.println("Invalid Input, Try Again");
			return readInt(low, high);
		}
	}
    
	// overload the function so I can choose to read numbers without any restriction on the number. Also has error handling built in
	static int readInt() throws IOException {
		try {
			return Integer.parseInt(next());
		} catch (Exception err) {
			// if there is an error tell user and try again
			System.out.println("Invalid Input, Try Again");
			return readInt();
		}
	}
	
    // function to read a line
    static String readLine () throws IOException {
        return br.readLine();
    }

	// function to read all of the cards from the text file to the arrayList
	static void readCards() throws IOException {
		// declare varibles
		String line, line2;
		flashCards temp;
		
		// set buffered reader to read from cards file and get the stored data
		br = new BufferedReader(new FileReader("cards.txt"));

		// read the lines from the text file
        line= readLine();
        while(line!= null){
			line2 = readLine();
			temp = new flashCards(line, line2);
            cards.add(temp);
            line = readLine();
        }
	}
	// function to read all of the notes from the text file to the arrayList
	static void readNotes() throws IOException {
		// declare varibles
		String line;
		
		// set buffered reader to read from the Notes file and get the stored data
		br = new BufferedReader(new FileReader("notes.txt"));

		// read the lines from the text file
		line= readLine();
        while(line!= null){
            notes.add(line);
            line = readLine();
        }
	}
	// function to make new flashcards
	static void makeCards() throws IOException{
		// declare varibles
		int num;
		String front, back;

		// ask user how many cards they want to make
		System.out.print("How many cards would you like to make? ");
		num = readInt();

		// for each card, read in the front and back and then make a new card
		// then add the card to the arrayList that stores all of the cards
		for(int i = 1; i<=num; i++){
			System.out.print("Enter the front of the card: ");
			front = readLine();
			System.out.print("Enter the back of the card: ");
			back = readLine();
			flashCards newCard = new flashCards(front, back);
			cards.add(newCard);
		}
	}
    
	static void runFlashcards() throws IOException{
		// declare varibles
		int cur, choice;
		cur = 0;

		// allow the user to scroll though the flashcards and flip them as they wish
		while(cur<cards.size()){
			System.out.print("The current card front is: ");
			System.out.println(cards.get(cur).getFront());
			System.out.println("Enter 1 to go to the next card and 2 to flip the current card and 3 to exit");
			choice = readInt(1, 3);
			if(choice==1){
				cur++;
			}else if(choice==2){
				cards.get(cur).flip();
			}else{
				return;
			}
		}
		
		// once you get to end tell user that there are no more
		System.out.println("There are no more flashcards");
	}

	// function to allow the user to match card fronts to card backs
	static void runMatching() throws IOException{
		// declare varibles
		int back;

		// check that they are enough cards to use this mode
		if(cards.size()<10){
			System.out.println("You do not have enough cards to use this mode");
			return;
		}
		
		// randomly select 10 flashcards by mixing them up and picking first 10
		Collections.shuffle(cards);
		System.out.println("Here are all of the card fronts");
		for(int i =0; i<10; i++){
			System.out.println((i+1) + ". " + cards.get(i).getFront());
		}
		
		// get order to print backs
		ArrayList<Integer> order = new ArrayList<Integer>();
        for (int i=0; i<10; i++) order.add(i);
        Collections.shuffle(order);
		
		// order.get(i) = j --> the i+1 front goes with the j+1 back
		// since 0<=i, j<10 so pairings are distinct 

		// print out the card backs
		System.out.println("Here are all of the card backs");
		for(int i = 0; i<10; i++){
			System.out.println((i+1) + ". " + cards.get(order.get(i)).getBack());
		}

		// ask the user to match cards
		for(int i = 0; i<10; i++){
			System.out.println("Enter the card back that card front #" + (i+1) + " corresponds too");
			back = readInt(1, 10);
			if(i!= order.get(back-1)){
				// if they get it wrong tell them and end the matching
				System.out.println("You got it wrong");
				return;
			}
		}
		
		// tell user they got everything correct if they make it to the end
		System.out.println("You got everything correct");
	}

	// function to allow the user to add to the notes
	static void makeNotes() throws IOException{
		// declare varibles
		int num;
		String line;

		// ask user how many lines of notes they want to add
		System.out.print("How many lines of notes would you like to make? ");
		num = readInt();
		for(int i = 1; i<=num; i++){
			// for each line add it to the notes
			System.out.println("Enter line #" + i + " of notes: ");
			line = readLine();
			notes.add(line);
		}
	}
	// function to recommend a study method
    static void recommendMethod() throws IOException{
		// ask user what subject they are studying
        UI.subjectList();

		// read user input for what subject they are using study++ to study
        System.out.print("\nWhat subject are we studying today?: ");
		int subject = readInt(1, 6);
        
		// recommend the corresponding study method
		if(subject == 1){
            clearConsole();
			UI.mathDescription();
		}else if(subject == 2){
            clearConsole();
            UI.scienceDescription();
		}else if(subject == 3){
            clearConsole();
            UI.englishDescription();
		}else if(subject == 4){
            clearConsole();
            UI.historyGeoDescription();
		}else if(subject == 5){
            clearConsole();
            UI.comsciDescription();
		}else{
            clearConsole();
            UI.religionDescription();
		}
    }
	// auxiliary function to clear console when it gets too messy
    static void clearConsole(){
        System.out.print("\033\143");
    }
	// function to displace the welcome screen
    static void welcomeUI(){
        UI.welcomeText();
    }
	// function to start the timer by using the timer class we implmented
    static void studyTimer() throws IOException{
		// declare varible
        int time;
		
        // ask user that work rhythm to use
        UI.workRhythm();
		time = readInt(1, 2);

		// allocate space to the instance of the timer class
		tut = new timer(time);
		
		// start the timer
        tut.runTimer();
    }
    // function to write all of the notes to the file to save it
	static void saveNotes() throws IOException{
		// set printwriter to write to that file
		pw = new PrintWriter(new BufferedWriter(new FileWriter("notes.txt")));
		
		// write everything to that file
        for(int i = 0; i<notes.size(); i++){
            pw.println(notes.get(i));
        }
		
		// close the print writer so it flushes and actually writes everything there
        pw.close();
	}
    // function to write all of the flash cards to the file to save it
	static void saveCards() throws IOException{
		// set printwriter to write to that file
        pw = new PrintWriter(new BufferedWriter(new FileWriter("cards.txt")));
		
		// write everything to that file
        for(int i = 0; i<cards.size(); i++){
            pw.println(cards.get(i).getFront());
			pw.println(cards.get(i).getBack());
        }
		
		// close the print writer so it flushes and actually writes everything there
        pw.close();
	}
	
	// function to display notes to reader
	static void showNotes(){
		System.out.println("Here are your notes: ");
		for(int i = 0; i<notes.size(); i++){
            System.out.println(notes.get(i));
        }
	}
	
    public static void main(String[] args) throws IOException {
		// declare varibles
		int choice;
		boolean cont;
		String temp;
		// first read the stored data by calling our readCards and readNotes functions
		readCards();
		readNotes();

		// set the buffer reader to read from the terminal
		br = new BufferedReader(new InputStreamReader(System.in));

		// show welcome UI
        welcomeUI();

		// recommend a study method
        recommendMethod();

		// start study timer
		studyTimer();

		cont = true;
		// while the user still wants to continue, keep running the code
		while(cont){
			clearConsole();
			// ask the user what they would like to do and do the appropriate action
			UI.studyMethod();
			choice = readInt(1, 6);
			if(choice==1){
				makeCards();
			}else if(choice==2){
				runFlashcards();
			}else if(choice==3){
				runMatching();
			}else if(choice==4){
				makeNotes();
			}else if(choice==5){
				showNotes();
			}else{
				cont=false;
			}
			System.out.println("Press any key to continue");
			temp = readLine();
		}
		
		// show end screen
        clearConsole();
		UI.endScreen();

		// stop timer
		tut.stopTimer();

		// save the data we aquired to the corresponding file
		saveCards();
		saveNotes();
    }
}