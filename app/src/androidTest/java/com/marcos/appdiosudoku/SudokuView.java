package com.marcos.appdiosudoku;

import android.app.Application;
import android.graphics.Insets;
import android.transition.Scene;

import androidx.test.runner.lifecycle.Stage;

/**
 * A View do jogo de Sudoku.
 * Esta classe é responsável por exibir a interface do usuário e interagir com o ViewModel.
 */
public class SudokuView extends Application {

    private SudokuViewModel viewModel; // Instância do ViewModel
    private TextField[][] cellTextFields; // Matriz de TextFields para as células do Sudoku
    private Label statusLabel; // Label para exibir o status do jogo
    private String initialArgs; // Argumentos iniciais para o tabuleiro

    /**
     * Construtor da View.
     * @param args Os argumentos de linha de comando para o tabuleiro inicial.
     */
    public SudokuView(String args) {
        this.initialArgs = args;
        this.viewModel = new SudokuViewModel();
    }

    // Construtor padrão necessário para o Application.launch() se não houver argumentos
    public SudokuView() {
        this.initialArgs = null; // Será definido ao iniciar o jogo
        this.viewModel = new SudokuViewModel();
    }

    /**
     * Método start é o ponto de entrada principal para todos os aplicativos JavaFX.
     * @param primaryStage O palco principal desta aplicação.
     */
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Jogo de Sudoku - DIO");

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        // Cria o GridPane para o tabuleiro do Sudoku
        GridPane sudokuGrid = createSudokuGrid();
        root.setCenter(sudokuGrid);

        // Cria a área de controle (botões e status)
        HBox controlBox = createControlBox();
        root.setBottom(controlBox);

        // Configura as ligações do ViewModel com a View
        setupViewModelBindings();

        Scene scene = new Scene(root, 450, 550); // Largura, Altura
        primaryStage.setScene(scene);
        primaryStage.show();

        // Inicia um novo jogo com os argumentos iniciais ou um tabuleiro vazio
        viewModel.newGame(initialArgs);
    }

    /**
     * Cria e configura o GridPane que representa o tabuleiro do Sudoku.
     * @return O GridPane configurado.
     */
    private GridPane createSudokuGrid() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(2); // Espaço horizontal entre as células
        grid.setVgap(2); // Espaço vertical entre as células
        grid.setPadding(new Insets(10));
        grid.setStyle("-fx-background-color: #333; -fx-border-color: #666; -fx-border-width: 2px; -fx-border-radius: 5px;");

        cellTextFields = new TextField[Board.SIZE][Board.SIZE];

        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                TextField textField = new TextField();
                textField.setPrefSize(40, 40); // Tamanho preferencial da célula
                textField.setAlignment(Pos.CENTER);
                textField.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #000; -fx-background-color: #FFF; -fx-border-color: #CCC; -fx-border-radius: 3px;");

                // Adiciona um estilo diferente para os blocos 3x3
                if ((row / 3 + col / 3) % 2 == 0) {
                    textField.setStyle(textField.getStyle() + "-fx-background-color: #F0F0F0;"); // Cor mais clara para blocos alternados
                }

                final int r = row;
                final int c = col;

                // Listener para entrada de texto
                textField.textProperty().addListener((obs, oldVal, newVal) -> {
                    if (newVal.matches("[1-9]?")) { // Permite 0-9 para limpar, ou 1-9
                        int value = newVal.isEmpty() ? 0 : Integer.parseInt(newVal);
                        boolean isValid = viewModel.setCellValue(r, c, value);
                        if (!isValid && value != 0) {
                            // Se o valor não for válido, reverte o texto para o valor antigo
                            // Ou mostra um feedback visual de erro
                            textField.setText(oldVal);
                            highlightCell(textField, Color.RED); // Destaca em vermelho
                            // Automaticamente remove o destaque após um curto período
                            new Thread(() -> {
                                try {
                                    Thread.sleep(500); // 0.5 segundos
                                    Platform.runLater(() -> highlightCell(textField, Color.BLACK)); // Volta para o estilo original
                                } catch (InterruptedException e) {
                                    Thread.currentThread().interrupt();
                                }
                            }).start();
                        }
                    } else {
                        textField.setText(oldVal); // Reverte se não for número válido
                    }
                });

                cellTextFields[row][col] = textField;
                grid.add(textField, col, row);
            }
        }
        return grid;
    }

    /**
     * Cria e configura a caixa de controle na parte inferior da View.
     * Contém o botão "Novo Jogo" e o status do jogo.
     * @return O HBox configurado.
     */
    private HBox createControlBox() {
        HBox controlBox = new HBox(10); // Espaço de 10px entre os elementos
        controlBox.setAlignment(Pos.CENTER);
        controlBox.setPadding(new Insets(10, 0, 0, 0));

        Button newGameButton = new Button("Novo Jogo");
        newGameButton.setStyle("-fx-font-size: 14px; -fx-background-color: #4CAF50; -fx-text-fill: white; -fx-background-radius: 5;");
        newGameButton.setOnAction(e -> {
            // Reinicia o jogo com os argumentos iniciais ou um tabuleiro vazio
            // (Para um novo jogo aleatório, seria necessário uma lógica de geração de tabuleiro)
            viewModel.newGame(initialArgs);
        });

        statusLabel = new Label("Status: Novo Jogo");
        statusLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #333;");

        controlBox.getChildren().addAll(newGameButton, statusLabel);
        return controlBox;
    }

    /**
     * Configura as ligações (bindings) entre o ViewModel e os elementos da View.
     * Isso garante que a View seja atualizada automaticamente quando os dados no ViewModel mudam.
     */
    private void setupViewModelBindings() {
        // Liga o status do jogo
        viewModel.gameStatusProperty().addListener((obs, oldStatus, newStatus) -> {
            Platform.runLater(() -> {
                statusLabel.setText("Status: " + newStatus.toString());
                if (newStatus == GameStatusEnum.COMPLETE) {
                    statusLabel.setTextFill(Color.GREEN);
                } else {
                    statusLabel.setTextFill(Color.BLACK);
                }
            });
        });

        // Liga os valores das células do tabuleiro
        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                final int r = row;
                final int c = col;
                int index = r * Board.SIZE + c;

                // Liga o texto do TextField à propriedade da célula no ViewModel
                cellTextFields[r][c].textProperty().bindBidirectional(viewModel.getBoardCells().get(index));

                // Configura o estilo para células fixas (não editáveis)
                Platform.runLater(() -> { // Executa na thread da UI
                    if (viewModel.isCellFixed(r, c)) {
                        cellTextFields[r][c].setEditable(false);
                        cellTextFields[r][c].setStyle(cellTextFields[r][c].getStyle() + "-fx-background-color: #E0E0E0; -fx-text-fill: #555;");
                    } else {
                        cellTextFields[r][c].setEditable(true);
                        cellTextFields[r][c].setStyle(cellTextFields[r][c].getStyle() + "-fx-text-fill: #000;"); // Garante cor preta para editáveis
                    }
                });
            }
        }

        // Observa as propriedades de cada célula para atualizar o estilo de células fixas/editáveis
        // Isso é importante após o carregamento de um novo tabuleiro via newGame
        // Poderíamos ter um ObservableList de propriedades mais complexas (como SpaceProperty)
        // para encapsular valor e fixed, mas para simplicidade, re-verificamos aqui.
        viewModel.getBoardCells().forEach(stringProperty -> {
            stringProperty.addListener((obs, oldVal, newVal) -> {
                int index = viewModel.getBoardCells().indexOf(stringProperty);
                int r = index / Board.SIZE;
                int c = index % Board.SIZE;
                Platform.runLater(() -> {
                    if (viewModel.isCellFixed(r, c)) {
                        cellTextFields[r][c].setEditable(false);
                        cellTextFields[r][c].setStyle(cellTextFields[r][c].getStyle() + "-fx-background-color: #E0E0E0; -fx-text-fill: #555;");
                    } else {
                        cellTextFields[r][c].setEditable(true);
                        // Garante que o fundo volte ao normal se o valor foi corrigido
                        if ((r / 3 + c / 3) % 2 == 0) {
                            cellTextFields[r][c].setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #000; -fx-background-color: #F0F0F0; -fx-border-color: #CCC; -fx-border-radius: 3px;");
                        } else {
                            cellTextFields[r][c].setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #000; -fx-background-color: #FFF; -fx-border-color: #CCC; -fx-border-radius: 3px;");
                        }
                    }
                });
            });
        });
    }

    /**
     * Função auxiliar para destacar uma célula temporariamente.
     * @param textField O TextField a ser destacado.
     * @param color A cor para o destaque.
     */
    private void highlightCell(TextField textField, Color color) {
        if (color == Color.RED) {
            textField.setStyle(textField.getStyle() + "-fx-text-fill: " + toHexString(color) + "; -fx-border-color: " + toHexString(color) + ";");
        } else { // Voltar ao normal (usar cor preta para texto)
            textField.setStyle(textField.getStyle().replace("-fx-text-fill: " + toHexString(Color.RED) + ";", "-fx-text-fill: " + toHexString(Color.BLACK) + ";"));
            textField.setStyle(textField.getStyle().replace("-fx-border-color: " + toHexString(Color.RED) + ";", "-fx-border-color: #CCC;"));
        }
    }

    /**
     * Converte um objeto Color JavaFX para uma String hexadecimal.
     * @param color A cor JavaFX.
     * @return A representação hexadecimal da cor.
     */
    private String toHexString(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }
}