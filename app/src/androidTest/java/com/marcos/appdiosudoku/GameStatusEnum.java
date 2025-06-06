package com.marcos.appdiosudoku;

public enum GameStatusEnum {
    PLAYING, // O jogo está em andamento
    COMPLETE, // O Sudoku foi resolvido com sucesso
    NEW_GAME // Status inicial ou quando um novo jogo é carregado
}