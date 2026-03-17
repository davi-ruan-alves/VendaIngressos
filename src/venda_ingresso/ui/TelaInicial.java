package venda_ingresso.ui;

import venda_ingresso.entities.Ingresso;
import venda_ingresso.services.GerenciadorIngresso;
import venda_ingresso.services.GerenciadorArquivo;

import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

public class TelaInicial extends JDialog {

    private JPanel painelFundo;
    private JButton btnComprar;
    private JButton btnGerarRelatorio;

    private GerenciadorArquivo arquivo = new GerenciadorArquivo();
    private GerenciadorIngresso gerenciador = new GerenciadorIngresso();

    public TelaInicial() {
        ArrayList<Ingresso> lista = arquivo.desserializar("ingressos.ser");
        if (lista != null) {
            gerenciador.getIngressos().addAll(lista);
        }
        criarComponentesTela();

        // Salva ao fechar a janela
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                arquivo.serializar(gerenciador.getIngressos(), "ingressos.ser");
            }
        });
    }

    private void criarComponentesTela() {
        btnComprar = new JButton("Comprar Ingresso");
        btnGerarRelatorio = new JButton("Gerar Relatório");

        btnComprar.addActionListener((e) -> {
            new JanelaCadastroIngresso(gerenciador);
        });

        btnGerarRelatorio.addActionListener((e) -> {
            JanelaGrafica janelaGrafica = new JanelaGrafica(this, true);
            janelaGrafica.imprimirRelatorio(gerenciador.getIngressos());
            janelaGrafica.setVisible(true); // Agora a janela aparece com os dados
        });

        painelFundo = new JPanel();
        painelFundo.add(btnComprar);
        painelFundo.add(btnGerarRelatorio);

        add(painelFundo);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        pack();
        setSize(300, 200);
        setVisible(true);
    }
}