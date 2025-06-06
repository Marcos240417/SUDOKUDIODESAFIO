package com.marcos.appdiosudoku;

import br.com.dio.model.Board;
import br.com.dio.model.GameStatusEnum;
import br.com.dio.model.Space;
import br.com.dio.util.BoardTemplate;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * ViewModel para o jogo de Sudoku.
 * Gerencia a lógica do jogo e expõe dados observáveis para a View.
 */
public class SudokuViewModel {
    private Board board; // O modelo do tabuleiro de Sudoku

    // Propriedades observáveis para a View
    private ObjectProperty<GameStatusEnum> gameStatus = new SimpleObjectProperty<>(GameStatusEnum.NEW_GAME);
    private ObservableList<StringProperty> boardCells = FXCollections.observableArrayList();

    /**
     * Construtor do ViewModel.
     * Inicializa o tabuleiro e as propriedades observáveis.
     */
    public SudokuViewModel() {
        // Inicializa o tabuleiro com um estado padrão ou vazio
        this.board = new Board();
        initializeBoardCells();
    }

    /**
     * Inicializa as propriedades StringProperty para cada célula do tabuleiro.
     */
    private void initializeBoardCells() {
        for (int i = 0; i < Board.SIZE * Board.SIZE; i++) {
            boardCells.add(new SimpleStringProperty(""));
        }
    }

    /**
     * Inicia um novo jogo de Sudoku com base na string de argumentos fornecida.
     * @param args A string de argumentos que define o tabuleiro inicial.
     */
    public void newGame(String args) {
        try {
            this.board = BoardTemplate.getStartingBoard(args);
            updateViewBoardCells();
            gameStatus.set(GameStatusEnum.PLAYING);
            System.out.println("Novo jogo iniciado.");
        } catch (IllegalArgumentException e) {
            System.err.println("Erro ao carregar o tabuleiro: " + e.getMessage());
            gameStatus.set(GameStatusEnum.NEW_GAME); // Mantém o status como novo jogo em caso de erro
        }
    }

    /**
     * Limpa o tabuleiro atual e o prepara para um novo jogo.
     */
    public void resetGame() {
        this.board = new Board(); // Cria um novo tabuleiro vazio
        updateViewBoardCells();
        gameStatus.set(GameStatusEnum.NEW_GAME);
        System.out.println("Jogo resetado.");
    }

    /**
     * Tenta definir um valor em uma célula específica do tabuleiro.
     * @param row A linha da célula (0-8).
     * @param col A coluna da célula (0-8).
     * @param value O valor a ser definido (0-9).
     * @return Verdadeiro se o valor foi definido e é válido, falso caso contrário.
     */
    public boolean setCellValue(int row, int col, int value) {
        if (board.getSpace(row, col).isFixed()) {
            return false; // Não permite alterar células fixas
        }

        // Tenta definir o valor no modelo do tabuleiro
        boolean isValid = board.setValue(row, col, value);

        if (isValid) {
            // Atualiza a propriedade observável da célula na View
            int index = row * Board.SIZE + col;
            boardCells.get(index).set(value == 0 ? "" : String.valueOf(value));

            // Verifica se o jogo foi concluído após a mudança
            if (board.checkWin()) {
                gameStatus.set(GameStatusEnum.COMPLETE);
                System.out.println("Parabéns! Você resolveu o Sudoku!");
            } else {
                gameStatus.set(GameStatusEnum.PLAYING); // Garante que o status volte a ser PLAYING se não estiver completo
            }
        }
        return isValid;
    }

    /**
     * Atualiza as propriedades observáveis das células da View com base no estado atual do modelo do tabuleiro.
     */
    private void updateViewBoardCells() {
        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                Space space = board.getSpace(row, col);
                int index = row * Board.SIZE + col;
                boardCells.get(index).set(space.getValue() == 0 ? "" : String.valueOf(space.getValue()));
            }
        }
    }

    /**
     * Retorna a propriedade observável do status do jogo.
     * @return ObjectProperty<GameStatusEnum> do status do jogo.
     */
    public ObjectProperty<GameStatusEnum> gameStatusProperty() {
        return gameStatus;
    }

    /**
     * Retorna a lista observável de propriedades de String para as células do tabuleiro.
     * Cada StringProperty representa o valor de uma célula no formato de texto.
     * @return ObservableList de StringProperty.
     */
    public ObservableList<StringProperty> getBoardCells() {
        return boardCells;
    }

    /**
     * Verifica se uma célula específica é fixa (não pode ser alterada pelo jogador).
     * @param row A linha da célula.
     * @param col A coluna da célula.
     * @return Verdadeiro se a célula é fixa, falso caso contrário.
     */
    public boolean isCellFixed(int row, int col) {
        return board.getSpace(row, col).isFixed();
    }
}
