compile-all: SpaceGame.java

SpaceGame.java: 
	javac -d "bin" -cp "src" src/org/spacegame/SpaceGame.java
