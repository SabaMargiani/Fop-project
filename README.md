Project Description
  
This project is a implementation of a simple BASIC subset interpreter. It is capable of parsing and executing newer-style BASIC code, supporting operations like variable assignment, arithmetic operations, printing, if-else chains and conditional loops. The interpreter is designed to help run simple programs and algorithms written in BASIC without needing to use advanced constructs like recursion or arrays.


  Team Members and Roles
  
Saba Margiani - CEO of everything


  Clone the repository:
  
git clone https://github.com/your-username/basic-interpreter.git
cd basic-interpreter

  Compile the code:
  
To compile this code you need to run main.java with respective folder open, so other classes are visible as well.
It will prompt you for a file path. ./test files/algorithms.bas is already in place if you want to test.

  Features
  
!!! BASIC code is case-INsensitive !!!
do keep in mind:
1. no unary minus: need to write (0 - x) for -x 
2. "=" and "<>" instead of "==" and "!="
difference between assignment and equality is determined from context
  
  Supported Commands
 
LET: variable assignment .
SYNTAX: LET [variableName] = [expression]
variables can hold number or boolean values. examples:
let primitiveBool = true
let primitiveNum = 0  

PRINT: Output the value of a variable, expression, string literal or a list of these.
SYNTAX: PRINT [variableName/"string"/number] (; [variableName/"string"/number])*
examples:
PRINT "hello, world"
PRINT "value of primitiveNum is "; primitiveNum; "\n"

WHILE: self-explanatory.
SYNTAX:
WHILE [boolean expression]
	...
	body
	...
WEND

FOR: self-explanatory.
SYNTAX:
FOR LET [loop variable name] = [startValue] TO [endValue]
	...
	body
	...
NEXT [loop variable name]
1. startValue, endValue must evaluate to a number.
2. loop variable must be consistent in declaration and after NEXT keyword.

IF + ELSE/ELSE-IF: self-explanatory
SYNTAX:
IF [boolean expression] THEN
	...
	then-body
	...
ELSE IF [boolean expression] THEN
	...
	else-(then-body)
	...
ELSE	
	...
	else-(else-body)
	...
END IF <- required for IF to close. ELSE/ELSE-IF branches can be omitted

Exit: exists the program with an error as soon as its executed (bypasses lex and parse)
on-hand runtime exception basically. it's a feature bro.


  Built-in functions:
MOD: modulo operator
SYNTAX: [num1] MOD [num2]
return remainder from dividing num1 by num2

SQR: square root but scaled to a whole number because we don't support floats
SYNTAX SQR [num]
