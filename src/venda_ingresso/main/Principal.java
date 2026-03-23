/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package venda_ingresso.main;

import java.util.ArrayList;
import venda_ingresso.entities.Ingresso;
import venda_ingresso.enums.SetorEnum;
import venda_ingresso.services.CompradorRunnable;
import venda_ingresso.services.GerenciadorArquivo;
import venda_ingresso.services.GerenciadorIngresso;

/**
 *
 * @author Junior
 */
public class Principal {
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Cria instância compartilhada do gerenciador
        GerenciadorIngresso gerenciador = new GerenciadorIngresso();
        GerenciadorArquivo arquivo = new GerenciadorArquivo();
        
        // Cria thread daemon de salvamento automático
        Thread daemonSalvamento = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    arquivo.serializar(gerenciador.getIngressos(), "ingressos_auto.ser");
                    System.out.println("[Daemon-Salvamento] Ingressos salvos automaticamente");
                    Thread.sleep(500); // Espera 500ms
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println("[Daemon-Salvamento] Thread interrompida, encerrando salvamento automático");
                    break;
                } catch (Exception e) {
                    System.out.println("[Daemon-Salvamento] Erro ao salvar: " + e.getMessage());
                }
            }
        }, "Daemon-Salvamento");
        
        // Configura como thread daemon
        daemonSalvamento.setDaemon(true);
        daemonSalvamento.start();
        
        // Cria 4 threads de compradores diferentes
        Thread thread1 = new Thread(new CompradorRunnable("João", SetorEnum.AMARELO, 2, gerenciador), "João-Thread");
        Thread thread2 = new Thread(new CompradorRunnable("Maria", SetorEnum.AZUL, 3, gerenciador), "Maria-Thread");
        Thread thread3 = new Thread(new CompradorRunnable("Pedro", SetorEnum.VERDE, 1, gerenciador), "Pedro-Thread");
        Thread thread4 = new Thread(new CompradorRunnable("Ana", SetorEnum.BRANCO, 4, gerenciador), "Ana-Thread");
        
        System.out.println("=== INICIANDO VENDAS CONCORRENTES ===");
        
        // Inicia todas as threads
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        
        try {
            // Aguarda todas as threads terminarem
            thread1.join();
            thread2.join();
            thread3.join();
            thread4.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Thread principal interrompida");
        }
        
        // Interrompe a thread daemon após todas as compras terminarem
        daemonSalvamento.interrupt();
        
        System.out.println("\n=== RELATÓRIO FINAL DE VENDAS ===");
        
        // Exibe relatório final
        for (Ingresso ingresso : gerenciador.getIngressos()) {
            System.out.println("Código: " + ingresso.getCodigo() + 
                " | Comprador: " + ingresso.getNome() + 
                " | Setor: " + ingresso.getSetor() + 
                " | Quantidade: " + ingresso.getQuantidade() + 
                " | Valor: R$ " + String.format("%.2f", ingresso.getValor()) + 
                " | Total: R$ " + String.format("%.2f", ingresso.getValorTotal()) +
                " | Thread: " + ingresso.getThreadOrigem() +
                " | Data/Hora: " + ingresso.getDataHora());
        }
        
        System.out.println("\nTotal de ingressos vendidos: " + gerenciador.getIngressos().size());
        
        // Salva final antes de encerrar
        arquivo.serializar(gerenciador.getIngressos(), "ingressos_final.ser");
        
        System.out.println("\n=== TESTE DE DESSERIALIZAÇÃO ===");
        
        // Desserializa os ingressos salvos
        ArrayList<Ingresso> ingressosDesserializados = arquivo.desserializar("ingressos_final.ser");
        
        System.out.println("Ingressos recuperados do arquivo: " + ingressosDesserializados.size());
        
        // Exibe o campo threadOrigem de cada ingresso desserializado
        for (Ingresso ingresso : ingressosDesserializados) {
            System.out.println("Código: " + ingresso.getCodigo() + 
                " | Thread Origem: " + ingresso.getThreadOrigem());
        }
        
        /*
         * EXPLICAÇÃO: O campo threadOrigem fica null após desserialização porque:
         * 1. O campo threadOrigem não foi marcado como 'transient' na classe Ingresso
         * 2. Durante a serialização, o nome da thread foi salvo no arquivo
         * 3. Porém, ao desserializar, o valor é recuperado mas a referência da thread
         *    original não existe mais (as threads já terminaram)
         * 4. O campo armazena apenas o nome (String) da thread, não a thread em si
         * 5. Se quiséssemos evitar salvar este campo, deveríamos marcá-lo como 'transient'
         * 
         * Solução: Marcar threadOrigem como 'transient' se não for necessário persistir
         * ou manter como está se o nome da thread for útil para auditoria
         */
    }
    
}
