package com.marcos.appdio.desafiodio;

public class DevViewModel {

    private Dev dev;

    public DevViewModel(String nome) {
        this.dev = new Dev();
        this.dev.setNome(nome);
    }

    public void inscreverEmBootcamp(Bootcamp bootcamp) {
        dev.inscreverBootcamp(bootcamp);
    }

    public void progredir() {
        dev.progredir();
    }

    public double getXpTotal() {
        return dev.calcularTotalXp();
    }

    public Dev getDev() {
        return dev;
    }
}
