grammar Quiz;

@header{package main;}

main: (qdb ';')* stat* EOF;

stat:
	  question			# StatQuestionStat
	| varDecl ';'		# StatVariableDecl
	| mcall ';'?		# StatMcall
	| propertyAcess ';'	# StatPropertyStat
	| fluxCtrl			# StatFluxControl
	| exp ';'			# StatExp
	;

qdb : 'qdb' ID String;

question:
	'{'
		('id:' id=ID)?
		('q:' q=String ';')?
		'type:' type=ID ';'
		('groups:' gp=list ';')?
		('depend:' dep=list ';')?
		('ans:' ans=qans ';')?
		('correct:' corr=qcorrect ';'?)?
	'}';

varDecl: (type=ID)? name=propertyAcess '=' (question | exp | time);

mcall: ID exp?;

qans: list;

qcorrect: list? # CorNotLong | (ID | String)? # CorLong;

propertyAcess: (ID '.')* ID;

fluxCtrl: 	if_ | while_ | dowhile | for_ | foreach;

if_: 		'if' exp loopStat elsif* else_?;

elsif: 		('else if' | 'elif') exp loopStat;

else_: 		'else' loopStat;

while_: 	'while' exp loopStat;

dowhile: 	'do' loopStat 'while' exp;

for_: 		'for' '(' (init=exp)? ';' (loop=exp)? ';' (fin=exp)? ')' loopStat;

foreach: ('foreach' | 'for each') iterator=ID 'in' (
		list
		| tuple
		| propertyAcess
		| String
		| Num
		| ID
	) loopStat;

loopStat: '{' stat* '}' # Multi | stat # Single;

exp:
	exp op = ('==' | '<' | '>' | '<=' | '>=') exp	# ExpComp
	| exp op = ('&&' | '||') exp					# ExpLogicAndOr
	| exp op = ('++' | '--')						# ExpPostInc
	| op = ('++' | '--') exp						# ExpPreInc
	| op = ('+' | '-') exp							# ExpUnary
	| exp op = ('+' | '-') exp						# ExpAddSub
	| exp op = ('*' | '/') exp						# ExpMulDiv
	| '(' exp ')'									# ExpParenthesis
	| varDecl										# ExpDecl
	| propertyAcess									# ExpPAccess
	| mcall											# ExpMcall
	| exp listIndex									# ExpIndex
	| Num											# ExpNum
	| ID											# ExpVarAccess
	| String										# ExpString
	| (list | tuple)								# ExpListTuple;


// no use of exp allows for simpler syntax, long exps would make a list barelly readable
list: (lstElem ',')* lstElem ','?;

tuple: '{' (exp ',')* exp? '}';

lstElem: tuple	#LstTuple
       | Num	#LstNum
       | ID		#LstVar
       | String	#LstStr
		;

listIndex: '[' i=exp ']'										#SingleIndex
		|	'[' (low=exp)? ':' (high=exp)? ']'					#RangeIndex
		|	'[' (low=exp)? ':' (high=exp)? ':' (step=exp)? ']'	#StepIndex
		;

time: exp mod=.;

Num: [0-9]+ ('.'[0-9]*)?;
String: '"' .*? '"';
ID: [a-zA-Z] [a-zA-Z0-9_]*;

Comment: '#' .*? '\n' -> skip;
WS: [ \t\r\n]+ -> skip;
