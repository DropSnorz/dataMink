
# dataMink

Stratified datalog evaluation and SQL translation engine. Deductive database school project.

Based on mif37-dex (University Lyon 1) and using [Spring-shell](https://projects.spring.io/spring-shell/).


## Run

### Default engine

You can run all the engine by passing a file as parameter to the datamink binary.

```
java -jar datamink.jar yourFile
```
This will compute program type, stratification if needed, evaluation and sql translation.

### DataMink shell

DataMink provide a powerfull CLI runining on all good shells. To start the shell simply run datamink without any parameters.

```
java -jar datamink.jar yourFile
```

Once the shell is started, you have access to several commands.

- `help`: Show all available commands
- `dm load --file yourFile`: Pre-load a file on datamink engine
- `dm parse [file]`: Parse the given or pre-loaded datalog file and prints the result
- `dm prgm-type [file]`: Compute program type for the given or pre-loaded datalog file and prints the result
- `dm stratify [file]`: Stratify the given or pre-loaded datalog file and prints all stratums
- `dm eval [file]`: Compute evaluation for the given or pre-loaded datalog file and prints inferred facts
- `dm tosql [file]`: Translate the given or pre-loaded datalog file to SQL and prints the result
- `exit`: Leave the shell



## Datalog files

As datamink is based on mif37-dex, it handles datalog programs written as the following example.

```
EDB
link(Charpennes,Perrache) 
link(PartDieu,Charpennes) 
link(Debourg,PartDieu)
link(PartDieu,Debourg)

IDB
metro($x)
reachable($x,$y)
unreachable($x,$y)

MAPPING
link($x,$y) -> metro($x).
link($x,$y) -> metro($y).
link($x,$y) -> reachable($x,$y).
link($x,$z), reachable($z,$y) -> reachable($x,$y).
metro($x), metro($y), NEG reachable($x,$y) -> unreachable($x,$y).
```


```
START : := "EDB" SCHEMA "IDB" SCHEMA "MAPPING" TGDS
SCHEMA : := RELATION SCHEMA | RELATION
RELATION : := NAME " ( " ATTS " ) "
ATTS : := NAME " , " ATTS | NAME
TGDS : := TGD | TGDS TGD
TGD : := QUERY "−>" ATOM " . "
LITERAL : := "NEG" ATOM | ATOM
QUERY : := LITERAL " , " QUERY | LITERAL
ATOM : := NAME " ( " ARGS " ) "
ARGS : := VALUE " , " ARGS | VALUE
VALUE : := VARIABLE | CONSTANT
VARIABLE : := " $ " NAME
NAME : := LETTER | LETTER NAME2
NAME2 : := LETTER_OR_DIGIT | LETTER_OR_DIGIT NAME2
CONSTANT : := DIGITS
DIGITS : := DIGIT DIGITS | DIGIT
LETTER_OR_DIGIT : := LETTER | DIGIT
LETTER : := " a " | "b" | " c " | "d" | " e " | " f " | " g " | "h" | " i " | " j "
| " k " | " l " | "m" | "n" | " o " | "p" | " q " | " r " | " s " | " t "
| "u" | " v " | "w" | " x " | " y " | " z "
| "A" | "B" | "C" | "D" | "E" | "F" | "G" | "H" | " I " | " J "
| "K" | "L" | "M" | "N" | "O" | "P" | "Q" | "R" | "S" | "T"
| "U" | "V" | "W" | "X" | "Y" | "Z"
| "_"
DIGIT : := " 0 " | " 1 " | " 2 " | " 3 " | " 4 " | " 5 " | " 6 " | " 7 " | " 8 " | " 9 "
```



