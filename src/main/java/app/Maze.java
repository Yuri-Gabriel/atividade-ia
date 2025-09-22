package app;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Maze {
    public char[][] map;

    public Maze() {
        String fileContent = this.parseFile();
        this.map = this.toMatrix(fileContent);
    }

    private String parseFile() {
        String file = System.getProperty("user.dir") + "/src/main/java/app/maze.txt";
        System.out.println("File -> " + file);
		String lineFile = "";
        String content = "";
		
		try {
			BufferedReader textFile = new BufferedReader(new FileReader(file));
			while((lineFile = textFile.readLine()) != null) {
				content += lineFile + "\n";
			}
		} catch (IOException err) {
			err.printStackTrace();
            return "";
		}
        return content;
    }

    private char[][] toMatrix(String mazeString) {
        int x_size = mazeString.indexOf("\n", 0);
        int y_size = (int) mazeString.chars()
                        .filter(c -> c == '\n')
                        .count();

        char[][] matrix = new char[y_size][x_size];

        int x = 0;
        int y = 0;
        for(char c : mazeString.toCharArray()) {
            if(c != '\n') {
                matrix[y][x] = c;
                x++;
                continue;
            }

            y++;
            x = 0;
        }

        return matrix;
    }

    public void show() {
        for(int i = 0; i < this.map.length; i++) {
            for(int j = 0; j < this.map[i].length; j++) {
                System.out.print(this.map[i][j]);
            }
            System.out.println();
        }
    } 
}
