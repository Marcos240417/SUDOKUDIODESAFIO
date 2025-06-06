package com.marcos.appdio.desafiodio;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        Curso curso1 = new Curso();
        curso1.setTitulo("Curso Java");
        curso1.setDescricao("Aprenda Java do zero.");
        curso1.setCargaHoraria(8);

        Curso curso2 = new Curso();
        curso2.setTitulo("Curso Kotlin");
        curso2.setDescricao("Kotlin moderno.");
        curso2.setCargaHoraria(4);

        Mentoria mentoria = new Mentoria();
        mentoria.setTitulo("Mentoria Java Expert");
        mentoria.setDescricao("Com um dev sênior.");
        mentoria.setData(LocalDate.now());

        Bootcamp bootcamp = new Bootcamp();
        bootcamp.setNome("Bootcamp Java Developer");
        bootcamp.setDescricao("Descrição do bootcamp Java Developer");
        bootcamp.getConteudos().add(curso1);
        bootcamp.getConteudos().add(curso2);
        bootcamp.getConteudos().add(mentoria);

        DevViewModel camilaVM = new DevViewModel("Camila");
        camilaVM.inscreverEmBootcamp(bootcamp);
        camilaVM.progredir();
        camilaVM.progredir();

        System.out.println("Conteúdos Inscritos Camila: " + camilaVM.getDev().getConteudosInscritos());
        System.out.println("Conteúdos Concluídos Camila: " + camilaVM.getDev().getConteudosConcluidos());
        System.out.println("XP Camila: " + camilaVM.getXpTotal());

        DevViewModel joaoVM = new DevViewModel("João");
        joaoVM.inscreverEmBootcamp(bootcamp);
        joaoVM.progredir();

        System.out.println("\nConteúdos Inscritos João: " + joaoVM.getDev().getConteudosInscritos());
        System.out.println("Conteúdos Concluídos João: " + joaoVM.getDev().getConteudosConcluidos());
        System.out.println("XP João: " + joaoVM.getXpTotal());
    }
}

