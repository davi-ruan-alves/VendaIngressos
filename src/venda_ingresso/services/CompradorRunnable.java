package venda_ingresso.services;

import java.time.LocalDateTime;
import venda_ingresso.entities.Ingresso;
import venda_ingresso.enums.SetorEnum;
import venda_ingresso.exceptions.QuantidadeInvalidaException;
import venda_ingresso.exceptions.SetorEsgotadoException;

public class CompradorRunnable implements Runnable {
    
    private String nomeComprador;
    private SetorEnum setor;
    private int quantidade;
    private GerenciadorIngresso gerenciador;
    
    public CompradorRunnable(String nomeComprador, SetorEnum setor, int quantidade, GerenciadorIngresso gerenciador) {
        this.nomeComprador = nomeComprador;
        this.setor = setor;
        this.quantidade = quantidade;
        this.gerenciador = gerenciador;
    }
    
    @Override
    public void run() {
        try {
            // Simula latência de rede
            Thread.sleep(50);
            
            // Cria um novo ingresso com os dados do comprador
            Ingresso ingresso = new Ingresso();
            ingresso.setNome(nomeComprador);
            ingresso.setSetor(setor.getNome());
            ingresso.setQuantidade(quantidade);
            ingresso.setValor(setor.getPreco());
            ingresso.setValorTotal(setor.getPreco() * quantidade);
            ingresso.setDataHora(LocalDateTime.now());
            
            // Tenta comprar o ingresso
            boolean sucesso = gerenciador.comprarIngresso(ingresso);
            
            if (sucesso) {
                System.out.println("[" + Thread.currentThread().getName() + "] " + 
                    nomeComprador + " comprou " + quantidade + " ingresso(s) no setor " + 
                    setor.getNome() + " com sucesso!");
            } else {
                System.out.println("[" + Thread.currentThread().getName() + "] " + 
                    nomeComprador + " falhou ao comprar ingresso no setor " + setor.getNome());
            }
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("[" + Thread.currentThread().getName() + "] " + 
                nomeComprador + " - Thread interrompida durante latência de rede");
            return;
        } catch (QuantidadeInvalidaException e) {
            System.out.println("[" + Thread.currentThread().getName() + "] " + 
                nomeComprador + " - Erro: " + e.getMessage());
        } catch (SetorEsgotadoException e) {
            System.out.println("[" + Thread.currentThread().getName() + "] " + 
                nomeComprador + " - Erro: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("[" + Thread.currentThread().getName() + "] " + 
                nomeComprador + " - Erro inesperado: " + e.getMessage());
        }
    }
}
