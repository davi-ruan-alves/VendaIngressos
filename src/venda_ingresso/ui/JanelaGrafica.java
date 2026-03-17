package venda_ingresso.ui;

import venda_ingresso.entities.Ingresso;
import java.util.ArrayList;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

class JanelaGrafica extends JDialog {

    private JPanel painelFundo;
    private JTable tabelaIngressos;
    private DefaultTableModel modelo;

    public JanelaGrafica(TelaInicial telaInicial, boolean isModal) {
        super(telaInicial, isModal);
        modelo = new DefaultTableModel();
        criarTabela();
        criarComponentes();
    }

    public JanelaGrafica() {
        modelo = new DefaultTableModel();
        criarTabela();
        criarComponentes();
    }

    private void criarTabela() {
        tabelaIngressos = new JTable(modelo);
        modelo.addColumn("Código");
        modelo.addColumn("Nome");
        modelo.addColumn("Setor");
        modelo.addColumn("Qtd");
        modelo.addColumn("Valor");
        modelo.addColumn("Total");
        modelo.addColumn("Data e Hora");

        // Ajuste de largura das colunas (opcional)
        tabelaIngressos.getColumnModel().getColumn(0).setPreferredWidth(5);
        tabelaIngressos.getColumnModel().getColumn(1).setPreferredWidth(70);
        tabelaIngressos.getColumnModel().getColumn(2).setPreferredWidth(20);
        tabelaIngressos.getColumnModel().getColumn(3).setPreferredWidth(1);
        tabelaIngressos.getColumnModel().getColumn(4).setPreferredWidth(15);
        tabelaIngressos.getColumnModel().getColumn(5).setPreferredWidth(15);
        tabelaIngressos.getColumnModel().getColumn(6).setPreferredWidth(70);
    }

    private void criarComponentes() {
        painelFundo = new JPanel();
        painelFundo.add(new JScrollPane(tabelaIngressos));
        add(painelFundo);

        setTitle("Ingressos");
        setSize(600, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    public void imprimirRelatorio(ArrayList<Ingresso> ingressos) {
        modelo.setNumRows(0); // limpa a tabela
        for (Ingresso ing : ingressos) {
            modelo.addRow(new Object[]{
                    ing.getCodigo(),
                    ing.getNome(),
                    ing.getSetor(),
                    ing.getQuantidade(),
                    ing.getValor(),
                    ing.getValorTotal(),
                    ing.getDataHora()
            });
        }
    }
}