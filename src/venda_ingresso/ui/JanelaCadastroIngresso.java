package venda_ingresso.ui;

import venda_ingresso.entities.Ingresso;
import venda_ingresso.enums.SetorEnum;
import venda_ingresso.exceptions.QuantidadeInvalidaException;
import venda_ingresso.exceptions.SetorEsgotadoException;
import venda_ingresso.services.GerenciadorIngresso;
import venda_ingresso.services.GerenciadorArquivo;

import java.time.LocalDateTime;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class JanelaCadastroIngresso extends JDialog {

    private JPanel painelFundo;
    private JButton btnSalvar;
    private JButton btnVoltar;
    private JLabel lblNome, lblQtde;
    private JTextField txtNome, txtQtde;
    private JComboBox<String> cbxSetores;
    private JComboBox<String> cbxTipoTorcedor;

    private final String[] setores = {"Amarelo", "Azul", "Branco", "Verde"};
    private final String[] tiposTorcedor = {"Inteira", "Meia"};

    private GerenciadorIngresso gerenciador;
    private GerenciadorArquivo arquivo = new GerenciadorArquivo();

    public JanelaCadastroIngresso(GerenciadorIngresso gerenciador) {
        this.gerenciador = gerenciador;
        criarComponentesInsercao();

        // Listener para serializar ao fechar a janela pelo "X"
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                arquivo.serializar(gerenciador.getIngressos(), "ingressos.ser");
            }
        });
    }

    private void limpar() {
        txtNome.setText("");
        txtQtde.setText("");
    }

    private void criarComponentesInsercao() {
        btnSalvar = new JButton("Salvar");
        btnVoltar = new JButton("Voltar");

        btnSalvar.addActionListener(e -> comprarIngresso());

        btnVoltar.addActionListener(e -> {
            // Serializa e fecha a janela, sem criar uma nova TelaInicial
            arquivo.serializar(gerenciador.getIngressos(), "ingressos.ser");
            dispose();
        });

        lblNome = new JLabel("Nome:");
        txtNome = new JTextField(10);
        lblQtde = new JLabel("Quantidade:");
        txtQtde = new JTextField(5);

        cbxTipoTorcedor = new JComboBox<>(tiposTorcedor);
        cbxSetores = new JComboBox<>(setores);

        painelFundo = new JPanel();
        painelFundo.add(lblNome);
        painelFundo.add(txtNome);
        painelFundo.add(cbxTipoTorcedor);
        painelFundo.add(lblQtde);
        painelFundo.add(txtQtde);
        painelFundo.add(cbxSetores);
        painelFundo.add(btnSalvar);
        painelFundo.add(btnVoltar);

        add(painelFundo);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        pack();
        setVisible(true);
    }

    private void comprarIngresso() {
        Ingresso ingresso = new Ingresso();
        double valorIngresso;

        String nome = txtNome.getText().trim();
        if (nome.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Informe o nome do comprador.");
            return;
        }
        ingresso.setNome(nome);

        String setorSelecionado = cbxSetores.getSelectedItem().toString();
        String tipoTorcedor = cbxTipoTorcedor.getSelectedItem().toString();

        int quantidade;
        try {
            quantidade = Integer.parseInt(txtQtde.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Digite uma quantidade válida (número inteiro).");
            return;
        }

        ingresso.setQuantidade(quantidade);
        ingresso.setSetor(setorSelecionado);

        // Obtém o preço do enum
        SetorEnum setorEnum = SetorEnum.valueOf(setorSelecionado.toUpperCase());
        valorIngresso = setorEnum.getPreco();

        if (tipoTorcedor.equalsIgnoreCase("Meia")) {
            valorIngresso = valorIngresso / 2;
        }

        ingresso.setValor(valorIngresso);
        ingresso.setValorTotal(valorIngresso * quantidade);
        ingresso.setDataHora(LocalDateTime.now());

        try {
            if (gerenciador.comprarIngresso(ingresso)) {
                limpar();
                JOptionPane.showMessageDialog(this, "Ingresso comprado com sucesso!");
            }
        } catch (QuantidadeInvalidaException | SetorEsgotadoException e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }
}