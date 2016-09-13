##Goal:
To demonstrate knowledge of both exhaustive search of a problem space and lookup search through the implementation of a password checker.

##Background:
Let's assume that you work for a company that requires employee passwords to be exactly 5 characters long, 1-3 of which must be letters (lowercase "a"-"z", no capitals), 1-2 of which must be numbers, and 1-2 of which must be symbols (specifically "!", "@", "$", "^", "_", or "*").
You have been tasked with creating a program to check that employee passwords are "good" passwords within this space.
A password is considered to be good if it does not contain any of the 500 most used English words (words from dictionary.txt), or any of these words with one or more numbers substituted for letters (i.e., "7" for "t", "4" for "a", "0" for "o", "3" for "e", "1" for "i", "1" for "l", or "$" for "s").
Your program will have two parts.
First, it will enumerate all good passwords, second it will check if enetered passwords are good.
If the entered password is not good, your program will recommend 10 alternative good passwords that share the longest possible prefix with the entered password.

##Specifications:
* You must implement a De La Briandais (DLB) trie data structure (as described in lecture) to use throughout your project.
* Your main program should be called from the command line one of two ways, either without any command line arguments (e.g., "java pw_check"), or with a single command line argument "-g" (e.g., "java pw_check -g").  Your program should first be run with the "-g" argument before being run without the "-g" argument.
* When called with the "-g" argument, your program should generate the list of good passwords, write out results to two separate files, and terminate.
	* You are provided a list of dictionary words to check in dictionary.txt.  Use this file to populate a DLB trie with strings that cannot be contained within user passwords based on the rules discussed in the Background section.
		* You must write also this full list of strings that you add to this DLB trie to a text file called "my_dictionary.txt".
	* Use exhaustive search and pruning rules to find all good passwords.  Be sure to carefully choose your pruning rules!  Your search needs to be as efficient as you can make it.
		* You must write the list of good words out to a text file called "good_passwords.txt".
* When called without any command line arguments, your program should prompt the user to enter passwords until they wish to stop.  You should check that a list of good passwords has already been generated (i.e., your program has already been run with the "-g" option and "good_passwords.txt" exists).
	* For each entered password, there are two options:  it is either a good password, or it is not.
		* If it is a good password, congratulate the user for their wise choice.
		* If it is not a good password, find 10 good passwords that share the longest prefix with the entered password and present them to the user as alternatives.
	* To facilitate these checks, you should read good passwords in from disk and store them in a DLB.
