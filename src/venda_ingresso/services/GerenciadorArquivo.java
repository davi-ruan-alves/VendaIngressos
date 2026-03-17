package venda_ingresso.services;

import venda_ingresso.entities.Ingresso;

import java.io.*;
import java.util.ArrayList;

public class GerenciadorArquivo {

    public void serializar(ArrayList<Ingresso> ingressos, String path) {

        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
            oos.writeObject(ingressos);
            oos.close();

        } catch (IOException e) {
            System.out.println("[ERRO] Falha ao serializar: " + e.getMessage());

        } finally {
            System.out.println("[LOG] Operação de serialização concluída.");
        }
    }

    public ArrayList<Ingresso> desserializar(String path) {

        ArrayList<Ingresso> ingressos = new ArrayList<>();

        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
            ingressos = (ArrayList<Ingresso>) ois.readObject();
            ois.close();

        } catch (IOException e) {
            System.out.println("[ERRO] Falha ao desserializar: " + e.getMessage());

        } catch (ClassNotFoundException e) {
            System.out.println("[ERRO] Classe não encontrada: " + e.getMessage());

        } finally {
            System.out.println("[LOG] Operação de desserialização concluída.");
        }

        return ingressos;
    }

    public void exportTxt(ArrayList<Ingresso> ingressos, String path) {

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));

            for (Ingresso ingresso : ingressos) {

                writer.write(
                        "Código: " + ingresso.getCodigo() +
                                " | Nome: " + ingresso.getNome() +
                                " | Setor: " + ingresso.getSetor() +
                                " | Quantidade: " + ingresso.getQuantidade() +
                                " | Valor Total: " + ingresso.getValorTotal()
                );

                writer.newLine();
            }

            writer.close();

        } catch (IOException e) {
            System.out.println("[ERRO] Falha ao gerar TXT: " + e.getMessage());

        } finally {
            System.out.println("[LOG] Exportação de relatório concluída.");
        }
    }
}