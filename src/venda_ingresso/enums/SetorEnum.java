package venda_ingresso.enums;

public enum SetorEnum {

    AMARELO("Amarelo", 180, 150),
    AZUL("Azul", 100, 200),
    BRANCO("Branco", 60, 300),
    VERDE("Verde", 350, 100);

    private String nome;
    private double preco;
    private int limiteMaximoIngressos;

    SetorEnum(String nome, double preco, int limiteMaximoIngressos){
        this.nome = nome;
        this.preco = preco;
        this.limiteMaximoIngressos = limiteMaximoIngressos;
    }

    public String getNome() {
        return nome;
    }

    public double getPreco() {
        return preco;
    }

    public int getLimiteMaximoIngressos() {
        return limiteMaximoIngressos;
    }
}
