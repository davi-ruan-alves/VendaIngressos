/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package venda_ingresso.services;

import venda_ingresso.entities.Ingresso;
import venda_ingresso.exceptions.QuantidadeInvalidaException;
import venda_ingresso.exceptions.SetorEsgotadoException;

import java.util.ArrayList;

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
    
    public boolean comprarIngresso(Ingresso ingresso) {


        int totalVendidoSetor = 0;
        int contadorSetor = 0;
        if (ingresso == null) {
            return false;
        }

        if (ingresso.getQuantidade() <= 0) {
            throw new QuantidadeInvalidaException("Erro: Quantidade Inválida");
        }

        for (Ingresso ingressoExistente : ingressos) {

            if (ingressoExistente.getSetor().equals(ingresso.getSetor())) {
                contadorSetor++;
            }

        }
        for (Ingresso ingressoExistente : ingressos) {
            if (ingressoExistente.getSetor().equals(ingresso.getSetor())) {
                totalVendidoSetor += ingressoExistente.getQuantidade();
            }
        }
        if (totalVendidoSetor + ingresso.getQuantidade() > 10) {
            throw new SetorEsgotadoException("Erro: Setor Esgotado");
        }

        ingresso.setCodigo(++prox);

        ingressos.add(ingresso);

        return true;


    }
    
    //Retorna os ingressos adquiridos
    public ArrayList<Ingresso> getIngressos() {
        return ingressos;
    }
    
}

    
    
    
    

