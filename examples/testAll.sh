#! /bin/bash

GREEN=$(tput setaf 2)
RED=$(tput setaf 1)
CYAN=$(tput setaf 6)

printf "${CYAN}Testing Quiz...\n"

cd ../src

mv aux/CQuiz.g4 aux/CQuiz.old
mv aux/CQuizMain.class aux/CQuizMain.old

printf "\n${CYAN}Starting syntactic tests...\n"

for f in ../examples/*.quest; do
	#printf "\t${GREEN}Testing ${f:12}\n${RED}"
	printf "\t${GREEN}Testing ${f:12}\n${RED}"
	cat $f | antlr4-test
done


printf "\n${CYAN}Starting semantic tests...\n"

#for f in ../examples/*_sem.quest; do
for f in ../examples/*.quest; do
	printf "\t${GREEN}Testing ${f:12}\n${RED}"
	cat $f | antlr4-run
done

mv aux/CQuiz.old aux/CQuiz.g4
mv aux/CQuizMain.old aux/CQuizMain.class

mv main/Quiz.g4 main/Quiz.old
mv main/QuizMain.class main/QuizMain.old

printf "\n${CYAN}Starting syntactic tests...\n"

for f in ../examples/*.qdb; do
	printf "\t${GREEN}Testing ${f:12}\n${RED}"
	cat $f | antlr4-test 
done


printf "\n${CYAN}Starting semantic tests...\n"

for f in ../examples/*.qdb; do
	printf "\t${GREEN}Testing ${f:12}\n${RED}"
	cat $f | antlr4-run
done

mv main/Quiz.old main/Quiz.g4
mv main/QuizMain.old main/QuizMain.class
