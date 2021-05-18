//Matematicas Computacionales 
//Hector Alvarez A01636166

import java.util.*;

public class GrammarAnalyzer {

	public static final String RED    = "\u001B[31m";
	public static final String GREEN  = "\u001B[32m";
	public static final String CYAN   = "\u001B[36m";
	public static final String RESET  = "\u001B[0m";

	//Se crea el arbol con la gramatica aritemtica
	//y se retorna el nodo raiz del mismo
	public static Node makeTree(String chain){
		String[] str = transform(chain).split("");
		Stack<Node> stack = new Stack<Node>();

		Node node;

		//se recorre la entrada y si se encuentra un numero
		//se agrega un nodo con el valor, de lo contrario
		//de hace pop a los vecinos y se agrega el caracter
		for(int i = 0; i<str.length ; i++) {
			String val = str[i];
			if( val.equals("0") ||  val.equals("1") || val.equals("2") ||  val.equals("3") || val.equals("4") ||  val.equals("5") || val.equals("6") ||  val.equals("7") || val.equals("8") ||  val.equals("9") ) {
				node = new Node<String>(val);
				stack.push(node);
				
			}else {
				node = new Node<String>(val);			
				node.right = stack.pop();
				node.left = stack.pop();
				stack.push(node);
			}

		}
		
		return stack.pop();
	}
	
	//Se evalua que la gramatica sea valida 
	//tomando en cuenta el balanceo de parentesis
	public static Boolean accepted(String chain) {
		
		String[] operation = chain.split("");
		HashSet<String> operators = new HashSet<String>();
		HashSet<String> numbers = new HashSet<String>();
		Stack<String> stack = new Stack<String>();
		registerOperator(operators, numbers);
		
		for (int i = 1; i < operation.length-1; i++) {
			
			String symbol = operation[i];
			
			//Se evalua si el simbolo es un operador aritmetico
			if(operators.contains(symbol)) {
				
				boolean left = numbers.contains(operation[i-1]) || operation[i-1].equals(")");
				boolean right = numbers.contains(operation[i+1]) || operation[i+1].equals("(");
				
				if(left && right == false) {
					return false;
					}
				
			//Se evalua si el simbolo es un parentesis de apertura
			}else if(symbol.equals("(")){
				
				boolean left = operators.contains(operation[i-1]) || operation[i-1].equals("(");
				boolean right = numbers.contains(operation[i+1]) || operation[i+1].equals("(");
				
				if(stack.isEmpty() || stack.peek().equals("(")) {
					stack.add("(");
				}else {
					return false;
				}
				
				
				if(left && right == false) {
					return false;
					}
			
			//Se evalua si el simbolo es un parentesis de cierre
			}else if(symbol.equals(")")){
				
				boolean left = numbers.contains(operation[i-1]) || operation[i-1].equals(")");
				boolean right = operators.contains(operation[i+1]) || operation[i+1].equals(")");
				
				if(stack.isEmpty() ) {
					return false;
				}else if(stack.peek().equals("(")) {
					stack.pop();
				}
				
				
				if(left && right == false) {
					return false;
					}
				
				
			//Se evalua si el simbolo es un numero
			}else if(numbers.contains(symbol)){
				
				boolean left = numbers.contains(operation[i-1]) || operation[i-1].equals("(") || operators.contains(operation[i-1]);
				boolean right = numbers.contains(operation[i+1]) || operation[i+1].equals(")") || operators.contains(operation[i+1]);
				
				if(left && right == false) {
					return false;
					}
			
			//No es un simbolo
			}else {return false;}	
			
		}
		
		if(operators.contains(operation[0]) || operation[0].equals(")")) {return false;}
		if(operators.contains(operation[operation.length-1]) || operation[operation.length-1].equals("(")) return false;
		if(!stack.isEmpty()){return false;}
		
		return true;
	} 

	//Se evalua la expresion postfija
	//se agreagan los operadores a su pila y se obtiene el resultado de 
	//la expresion
	public static double evaluate(String postfix) {
		String[] operation = postfix.split("");
		Stack<Integer> stack = new Stack<Integer>();
		String operator;
		int x = 0;
		int y = 0;
		int z = 0;
		double ans;
		
		for (int i = 0; i < operation.length; i++) {
			
			try {
				stack.add(Integer.parseInt(operation[i]));
			}catch (NumberFormatException e) {
				operator = operation[i];
				x = (int) stack.pop();
				y = (int) stack.pop();
				
				//Se obtiene el resultado de la operacion 
				if (operator.equals("+")) {
					z = y + x;
				}else if(operator.equals("-")) {
					z = y - x;
				}else if(operator.equals("*")) {
					z = y * x;
				}else if(operator.equals("/")) {
					z = y / x;
				}
				
				stack.add(z);
			}
			continue;
		}
		ans = stack.pop();
		return ans;
	}
	

	//Se tranforma de infija a postfija
	//y se agregan los valores al stack de operadores
	public static String transform(String infix) {
			
			String postfix = "";
			String[] operation = infix.split("");
			Stack<String> stack = new Stack<String>();
			boolean turn = true;
			
			for (int i = 0; i < operation.length; i++) {
				
				try {
					postfix += Integer.parseInt(operation[i]) + "";
					continue;
				}catch(NumberFormatException e){ 
					turn = true;
					while(turn == true) {
						
					if(stack.isEmpty()) {
						stack.add(operation[i]);
						turn = false;
						continue;
					}else { //En caso de que la pila tenga elementos, se compara por prioridad

						if(operation[i].equals("(")) { //Parentesis registrado
							stack.add(operation[i]);
							turn = false;
							continue;
							
						}else if(operation[i].equals("+") || operation[i].equals("-")) {//Operador registrado
							
							if(stack.lastElement().equals("(")) {
								stack.add(operation[i]);
								turn = false;
								continue;
								
							}else if(stack.lastElement().equals("+") || stack.lastElement().equals("-")) {
								postfix += stack.pop() + "";
								turn = true;
								
							}else if(stack.lastElement().equals("*") || stack.lastElement().equals("/")) {
								postfix += stack.pop() + "";
								turn = true;
								
							}else {
								postfix += stack.pop() + "";
								turn = true;
							}
							
						}else if(operation[i].equals("*") || operation[i].equals("/")) { //Operador registrado
							if(stack.lastElement().equals("(")) {
								stack.add(operation[i]);
								turn = false;
								continue;
								
							}else if(stack.lastElement().equals("+") || stack.lastElement().equals("-")) {
								stack.add(operation[i]);
								turn = false;
								continue;
								
							}else if(stack.lastElement().equals("*") || stack.lastElement().equals("/")) {
								postfix += stack.pop() + " ";
								turn = true;
								
							}else {
								//"^"
								postfix += stack.pop() + "";
								turn = true;
							}
							
						}else if(operation[i].equals(")")) { //En caso de arentesis de cierrer

							while(!stack.lastElement().equals("(")){
								postfix+= stack.pop() + "";
							}
							stack.pop();
							turn= false;
							continue;
						}			
					}
				}
		
				}			
			}
			while(!stack.isEmpty()){
				postfix+= stack.pop() + "";
			}
			
			return postfix;
			
		}
	

	//Se agregan los simbolos permitidos a un Hashset
	private static void registerOperator(HashSet<String> operators, HashSet<String> numbers) {
		operators.add("+");
		operators.add("-");
		operators.add("*");
		operators.add("/");
		for (int i = 0; i < 10; i++) {
			numbers.add( Integer.toString(i) );
		}
	}
	
	//Funcion encargada de realizar el setup del programa
	//y correrlo
	public static void run(String chain) {
		
		boolean accepted = accepted(chain);
		
		if(accepted) {
            System.out.println(CYAN + "Your input: " + RESET + chain + GREEN + " is valid" + RESET);
			String postfix = transform(chain);
			System.out.println(CYAN + "The result is: " + RESET + evaluate(postfix));
			Node node = makeTree(postfix);
			
			System.out.println(CYAN + "\nThis is the resultant derivation tree:" + GREEN);
			BTreePrinter.printNode(node);
			System.out.println(RESET); 
		}else {
            System.out.println(CYAN + "Your input: " + RESET + chain + RED + " is NOT valid" + RESET);
			System.out.println(RED + "\n* NOT A VALID INPUT, RESTRUCTURE AND TRY AGAIN *" + RESET);
		}
		
		
	}

	public static void main(String[] args) {		
		
		System.out.println("WELCOME! \nPlease, enter a gramatic expression: ");

		Scanner scanner = new Scanner(System.in);
        String chain = scanner.nextLine();
		scanner.close();

		run(chain);
		
	}

}