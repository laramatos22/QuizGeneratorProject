grammar CQuiz;

@header{package aux;}

main: q*;

//   ID        QUESTION           GROUPS or DEPENDENCIES 
q: 	 id=Id ':' question=Word* '{' (l1=list ';')? (l2=list ';')? type=Word ';' (possible=tuple ';')? correct=tuple? '}'	#MultMatch
	|id=Id ':' question=Word* '{' (l1=list ';')? (l2=list ';')? type=Word ';' validator=tuple? '}'						#Short
	|id=Id ':' question=Word* '{' (l1=list ';')? (l2=list ';')? type=Word ';' validator=list? '}'						#Numeric
	|id=Id ':' question=Word* '{' (l1=list ';')? (l2=list ';')? type=Word ';' validator=Word? '}'						#Long
	;

tuple: '{' (tuplelem ';')* tuplelem? '}';

list: (lstelem ',')* lstelem ','?;

tuplelem: lstelem 	#TupElem
		| list		#TupLst
		;

lstelem: Word 		#LstWrd
		| Number 	#LstNum
		| tuple		#LstTup
		;

Word: '"' .*? '"';
Number: /*'-'?*/ [0-9]+ ('.'[0-9]*)?;
Id: [a-zA-Z] [a-zA-Z0-9_]*;

Comment: '#' .*? '\n' -> skip;
WS: [ \t\r\n]+ -> skip;
