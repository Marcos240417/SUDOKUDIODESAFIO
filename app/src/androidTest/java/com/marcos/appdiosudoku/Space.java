package com.marcos.appdiosudoku;

public class Space {
    private int value; // O valor numérico (1-9) desta célula
    private boolean isFixed; // Indica se o valor desta célula é fixo (inicial do puzzle)

    /**
     * Construtor para um espaço.
     * @param value O valor inicial do espaço.
     * @param isFixed Verdadeiro se o valor for fixo (não pode ser alterado pelo jogador), falso caso contrário.
     */
    public Space(int value, boolean isFixed) {
        this.value = value;
        this.isFixed = isFixed;
    }

    /**
     * Retorna o valor atual do espaço.
     * @return O valor inteiro do espaço.
     */
    public int getValue() {
        return value;
    }

    /**
     * Define o valor do espaço. Se o espaço for fixo, o valor não será alterado.
     * @param value O novo valor a ser definido.
     */
    public void setValue(int value) {
        if (!isFixed) {
            this.value = value;
        }
    }

    /**
     * Verifica se o valor do espaço é fixo.
     * @return Verdadeiro se o valor é fixo, falso caso contrário.
     */
    public boolean isFixed() {
        return isFixed;
    }

    /**
     * Define se o espaço é fixo.
     * @param fixed Verdadeiro para tornar o espaço fixo, falso para torná-lo mutável.
     */
    public void setFixed(boolean fixed) {
        isFixed = fixed;
    }

    /**
     * Retorna uma representação em String do objeto Space.
     * @return A String formatada do valor do espaço.
     */
    @Override
    public String toString() {
        return String.valueOf(value);
    }
}