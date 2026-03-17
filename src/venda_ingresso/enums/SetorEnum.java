package venda_ingresso.enums;

public enum SetorEnum {

    AMARELO("Amarelo", 180),
    AZUL("Azul", 100),
    BRANCO("Branco", 60),
    VERDE("Verde", 350);

    private String nome;
    private double preco;

    SetorEnum(String nome, double preco){
        this.nome = nome;
        this.preco = preco;
    }

    public String getNome() {
        return nome;
    }

    public double getPreco() {
        return preco;
    }
}
