/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package venda_ingresso.services;

import java.util.ArrayList;
import venda_ingresso.entities.Ingresso;
import venda_ingresso.enums.SetorEnum;
import venda_ingresso.exceptions.QuantidadeInvalidaException;
import venda_ingresso.exceptions.SetorEsgotadoException;

/**
 *
 * @author Junior
 */
public class GerenciadorIngresso {
    
    private ArrayList<Ingresso> ingressos;
    private int prox = 0;

    public GerenciadorIngresso() {
        
        ingressos = new ArrayList<>();
    }
    
    public synchronized boolean comprarIngresso(Ingresso ingresso) {


        int totalVendidoSetor = 0;
        if (ingresso == null) {
            return false;
        }

        if (ingresso.getQuantidade() <= 0) {
            throw new QuantidadeInvalidaException("Erro: Quantidade Inválida");
        }

        for (Ingresso ingressoExistente : ingressos) {
            if (ingressoExistente.getSetor().equals(ingresso.getSetor())) {
                totalVendidoSetor += ingressoExistente.getQuantidade();
            }
        }
        // Obtém o limite máximo do setor usando o enum
        SetorEnum setorEnum = SetorEnum.valueOf(ingresso.getSetor().toUpperCase());
        int limiteMaximoSetor = setorEnum.getLimiteMaximoIngressos();
        
        if (totalVendidoSetor + ingresso.getQuantidade() > limiteMaximoSetor) {
            throw new SetorEsgotadoException("Erro: Setor Esgotado");
        }

        ingresso.setCodigo(++prox);
        
        // Registra o nome da thread que processou o ingresso
        ingresso.setThreadOrigem(Thread.currentThread().getName());

        ingressos.add(ingresso);

        return true;


    }
    
    //Retorna os ingressos adquiridos
    public synchronized ArrayList<Ingresso> getIngressos() {
        return ingressos;
    }
    
}

    
    
    
    

