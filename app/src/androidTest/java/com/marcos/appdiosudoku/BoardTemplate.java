package com.marcos.appdiosudoku;

import br.com.dio.model.Board;
import br.com.dio.model.Space;

/**
 * Utilitário para carregar um tabuleiro de Sudoku a partir de uma string de argumentos.
 */
public class BoardTemplate {

    /**
     * Converte uma string de argumentos em um tabuleiro de Sudoku.
     * Formato esperado da string: "row,col;value,isFixed row,col;value,isFixed ..."
     * Ex: "0,0;4,false 1,0;7,false 2,0;9,true ..."
     * @param args A string de argumentos.
     * @return Um objeto Board inicializado com os dados fornecidos.
     * @throws IllegalArgumentException se a string de argumentos estiver mal formatada.
     */
    public static Board getStartingBoard(String args) {
        Board board = new Board();
        if (args == null || args.isEmpty()) {
            return board; // Retorna um tabuleiro vazio se não houver argumentos
        }

        String[] parts = args.split(" ");
        for (String part : parts) {
            String[] cellData = part.split(";");
            if (cellData.length != 2) {
                throw new IllegalArgumentException("Formato de célula inválido: " + part);
            }
            String[] coords = cellData[0].split(",");
            if (coords.length != 2) {
                throw new IllegalArgumentException("Formato de coordenada inválido: " + cellData[0]);
            }
            String[] valueFixed = cellData[1].split(",");
            if (valueFixed.length != 2) {
                throw new IllegalArgumentException("Formato de valor/fixo inválido: " + cellData[1]);
            }

            try {
                int row = Integer.parseInt(coords[0]);
                int col = Integer.parseInt(coords[1]);
                int value = Integer.parseInt(valueFixed[0]);
                boolean isFixed = Boolean.parseBoolean(valueFixed[1]);

                // Define o espaço no tabuleiro
                Space space = board.getSpace(row, col);
                if (space != null) {
                    space.setValue(value);
                    space.setFixed(isFixed);
                }
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Erro de formato numérico ou booleano em: " + part, e);
            }
        }
        return board;
    }
}

