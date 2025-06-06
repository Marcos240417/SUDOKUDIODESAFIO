package com.marcos.appdiosudoku;

/**
 * Representa o tabuleiro do Sudoku e contém a lógica do jogo.
 */
public class Board {
    private Space[][] spaces; // Matriz 9x9 de objetos Space
    public static final int SIZE = 9; // Tamanho do tabuleiro (9x9)

    /**
     * Construtor que inicializa um tabuleiro de Sudoku vazio.
     */
    public Board() {
        spaces = new Space[SIZE][SIZE];
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                spaces[row][col] = new Space(0, false); // Inicializa com valor 0 e não fixo
            }
        }
    }

    /**
     * Define o valor de um espaço específico no tabuleiro.
     * Realiza validação para garantir que o movimento é válido e que o espaço não é fixo.
     * @param row A linha do espaço (0-8).
     * @param col A coluna do espaço (0-8).
     * @param value O valor a ser definido (1-9), ou 0 para limpar o espaço.
     * @return Verdadeiro se o valor foi definido com sucesso e é válido, falso caso contrário.
     */
    public boolean setValue(int row, int col, int value) {
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) {
            System.err.println("Coordenadas fora do tabuleiro: (" + row + ", " + col + ")");
            return false;
        }

        Space space = spaces[row][col];
        if (space.isFixed()) {
            // Não permite alterar valores fixos
            return false;
        }

        if (value < 0 || value > 9) {
            System.err.println("Valor inválido: " + value + ". Deve ser entre 0 e 9.");
            return false;
        }

        // Se o valor for 0, limpa o espaço sem validação
        if (value == 0) {
            space.setValue(0);
            return true;
        }

        // Temporariamente define o valor para validar
        int oldValue = space.getValue();
        space.setValue(value);

        if (isValid(row, col, value)) {
            return true;
        } else {
            // Se o valor não for válido, reverte para o valor antigo
            space.setValue(oldValue);
            return false;
        }
    }

    /**
     * Verifica se um determinado valor é válido na posição (row, col) de acordo com as regras do Sudoku.
     * Esta função assume que o valor já foi temporariamente colocado na célula.
     * @param row A linha do espaço.
     * @param col A coluna do espaço.
     * @param value O valor a ser verificado.
     * @return Verdadeiro se o valor é válido na posição, falso caso contrário.
     */
    private boolean isValid(int row, int col, int value) {
        // Verifica a linha
        for (int c = 0; c < SIZE; c++) {
            if (c != col && spaces[row][c].getValue() == value) {
                return false;
            }
        }

        // Verifica a coluna
        for (int r = 0; r < SIZE; r++) {
            if (r != row && spaces[r][col].getValue() == value) {
                return false;
            }
        }

        // Verifica o bloco 3x3
        int startRow = row - row % 3;
        int startCol = col - col % 3;
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (startRow + r != row || startCol + c != col) { // Garante que não verifica a própria célula
                    if (spaces[startRow + r][startCol + c].getValue() == value) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Retorna o objeto Space em uma determinada coordenada.
     * @param row A linha.
     * @param col A coluna.
     * @return O objeto Space.
     */
    public Space getSpace(int row, int col) {
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) {
            return null; // Ou lançar uma exceção, dependendo da necessidade
        }
        return spaces[row][col];
    }

    /**
     * Verifica se o tabuleiro está completo (todos os espaços preenchidos) e se todos os valores são válidos.
     * @return Verdadeiro se o Sudoku está resolvido, falso caso contrário.
     */
    public boolean checkWin() {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                Space space = spaces[row][col];
                if (space.getValue() == 0 || !isValid(row, col, space.getValue())) {
                    return false; // Se houver espaço vazio ou valor inválido, não está resolvido
                }
            }
        }
        return true; // Se todas as células estiverem preenchidas e válidas
    }

    /**
     * Cria e retorna uma cópia profunda deste tabuleiro.
     * @return Uma nova instância de Board com os mesmos valores e estados fixos.
     */
    public Board copyBoard() {
        Board newBoard = new Board();
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                Space originalSpace = this.spaces[r][c];
                // Cria um novo Space com o mesmo valor e estado fixo
                newBoard.spaces[r][c] = new Space(originalSpace.getValue(), originalSpace.isFixed());
            }
        }
        return newBoard;
    }

    /**
     * Método auxiliar para carregar o tabuleiro a partir de um array de Strings.
     * Usado principalmente para inicialização e testes.
     * @param rawBoard Uma matriz 9x9 de strings, onde cada string é o valor da célula.
     */
    public void loadBoard(String[][] rawBoard) {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                int value = Integer.parseInt(rawBoard[r][c]);
                boolean isFixed = (value != 0); // Valores iniciais não-zero são fixos
                this.spaces[r][c] = new Space(value, isFixed);
            }
        }
    }
}
