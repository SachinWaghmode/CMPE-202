# CMPE-202
-PersonalProjectUMLparser


UMLparser is a parser which converts Java Source Code into a UML Class Diagram. 

Input: Directory path where all Java classes are stored.
Output: Image of Class Diagram.

Step1: Scan all the .java files from the input directory
Step2: parse the individual java file to fetch details(classname,attributes,methods) and write it to intermediate code
Step3: Use PlantUML digram generator to generate diagram using intermediate code and output directory.

Test case 1: Parse class name, attributes, dependency between Classes, Multiplicity.


      
