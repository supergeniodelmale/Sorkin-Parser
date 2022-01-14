import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Script{
	
	private ArrayList<Scene> scenes;
	private ArrayList<Character> chars;
	
	public Script(String file) {
		scenes = new ArrayList<Scene>();
		try {
			Scanner reader = new Scanner(new File(file));
			if(parseScript(reader)) {
				System.out.println("Script parsing was succesful!");
			} else {
				System.out.println("Parsing error!");
			}
		} catch(FileNotFoundException e) {
			System.out.println("Raw script file not found!");
		}
	}
	private boolean parseScript(Scanner file) {
		try {
			while(file.hasNextLine()) {
				String heading = file.nextLine();
				String[] headingSplit = heading.split("\\s+");
				if(isHeading(headingSplit)){
					scenes.add(parseScene(file, heading));
				}
			}
			return true;
		} catch(Exception e) {
			System.out.println(e);
		}
		return false;
	}
	private Scene parseScene(Scanner file, String heading) {
		Scene scene = new Scene(heading);
			while(file.hasNextLine()) {
				String line = file.nextLine();
				if(isTransition(line)) {
					scene.addTransition(line);
					return scene;
				} else {
					scene.appendAction(line);
				}
			}
		return null;
	}
	
	private static boolean isHeading(String[] text) {
		return (text[0].equals("INT.")||text[0].equals("EXT.")||text[0].equals("I/E."));
	}
	private static boolean isTransition(String text) {
		try {
			Scanner reader = new Scanner(new File("config.txt"));
			String[] transitionList = reader.nextLine().split(",");
			for(String i : transitionList) {
				if(text.equals(i)) {
					return true;
				}
			}
		}
		catch(Exception e) {
			System.out.println(e);
		}
		return false;
	}
	private static boolean isDialogue() {
		return false;
	}
	
	public void printAll() {
		for(Scene i : scenes) {
			i.printScene();
		}
	}
	public void printTransitions() {
		for(Scene i : scenes) {
			System.out.println(i.trans);
		}
	}
	public void printHeadings() {
		for(Scene i : scenes) {
			System.out.println(i.head);
		}
	}
	
	class Scene{
		
		private String head;
		private ArrayList<SceneToken> timeline;
		private String trans;
		
		private Scene(String h) {
			head = h;
			timeline = new ArrayList<SceneToken>();
			trans = null;
		}
		private void appendAction(String x) {
			timeline.add(new SceneToken("Action", new Action(x)));
		}
		private void appendDialogue(Persona pers, String text) {
			timeline.add(new SceneToken("Dialogue", new Dialogue(pers, text)));
		}
		private void addTransition(String text) {
			trans = text;
		}
		private boolean isValid() {
			return (!trans.equals(null)&&!head.equals(null));
		}

		private void printScene() {
			System.out.println(head);
			for(SceneToken i : timeline) {
				i.print();
			}
			System.out.println(trans);
		}
		
		class SceneToken{
			private Tuple sToken;
			
			public SceneToken(String type, Object x) {
				sToken = new Tuple(type, x);
			}
			public boolean isAction() {
				return (sToken.getKey().equals("Action"));
			}
			public boolean isDialogue() {
				return (sToken.getKey().equals("Dialogue"));
			}
			public void print() {
				if(isAction()) {
					System.out.println(sToken.getValue());
				}
				if(isDialogue()) {
					System.out.println(sToken.getKey());
					System.out.println(sToken.getValue());
				} 
			}
		}
		class Action{
			private String actToken;
			
			public Action(String a) {
				actToken = a;
			}
			public String toString() {
				return actToken;
			}
		}
		class Dialogue{
			private Tuple dialToken;
			
			public Dialogue(Persona pers, String text) {
				dialToken = new Tuple(pers, text);
			}
			public String getText() {
				return (String) dialToken.getValue();
			}
		}
		
	}
	class Persona {
		
	}
}

class Tuple{
	private Object key;
	private Object value;
	
	public Tuple(Object k, Object v) {
		key = k;
		value = v;
	}
	public boolean isEmpty() {
		return (key.equals(null)&&value.equals(null));
	}
	public Object getValue() {
		return value;
	}
	public Object getKey() {
		return key;
	}

}
