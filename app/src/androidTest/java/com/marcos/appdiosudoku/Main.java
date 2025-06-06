package com.marcos.appdiosudoku;


/**
 * Classe principal para iniciar o jogo de Sudoku.
 */
public class Main {
    public static void main(String[] args) {
        // Concatena os argumentos passados para a aplicação JavaFX
        StringBuilder argsString = new StringBuilder();
        for (String arg : args) {
            argsString.append(arg).append(" ");
        }

        // Lança a aplicação JavaFX, passando os argumentos para o construtor da View
        // Certifique-se de que o JavaFX SDK esteja configurado no seu ambiente de desenvolvimento.
        // Se estiver usando o IntelliJ IDEA, adicione a VM Option:
        // --module-path %PATH_TO_FX% --add-modules javafx.controls,javafx.fxml
        // Onde %PATH_TO_FX% é o caminho para a pasta 'lib' do seu JavaFX SDK.
        Application.launch(SudokuView.class, argsString.toString().trim());
    }
}
