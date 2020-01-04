# A simplified enigma machine in Java

In winter 2015, I visited [Bletchley Park](https://en.wikipedia.org/wiki/Bletchley_Park),
where I saw real [Enigma machines](https://en.wikipedia.org/wiki/Enigma_machine).

I came home and decided to build a software one in Java, and make it an exercise with 
my second year computer science students.

Note that it was not a cryptographic exercice, but a software engineering one. It was tested
against virtual enigma machines found on the internet (like [this one](https://www.101computing.net/enigma-machine-emulator/)) on sample texts with basic settings.

The students got fun. Me too.

I was able to find many information on the internet (e.g. the mapping of each rotor and reflector).

The GUI is basic: you type a letter on the keyboard, and a letter is highlighted on the GUI
(the letter is also stored in a textfield). There is a known bug in the GUI: the rottors are "eaten" 
by the machine (i.e. they do not appear again in the rotor box when replaced by a new one).

I make it available for education purpose: I am happy to get feedback on that design.
Note that some comments are still in French.

The design is simplified: the rotors cannot be shifted, and there is no plugboard.

