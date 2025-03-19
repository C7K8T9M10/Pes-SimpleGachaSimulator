import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GachaGame extends JFrame {
    private JTextArea resultArea;
    private JComboBox<String> packTypeComboBox;
    private JComboBox<String> drawTypeComboBox;
    private JButton drawButton;
    private int legendaryFavorCount; // 传奇心仪球员计数
    private int specialFavorCount;   // 精选心仪球员计数
    private int totalDraws;          // 总抽取次数
    private JTextField legendaryCountField; // 传奇计数显示
    private JTextField specialCountField;   // 精选计数显示
    private JTextField totalDrawsField;     // 总抽取次数显示

    public GachaGame() {
        setTitle("抽卡游戏");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());

        // 包类型选择
        controlPanel.add(new JLabel("选择包类型:"));
        packTypeComboBox = new JComboBox<>(new String[]{"传奇球员包", "精选球员包"});
        controlPanel.add(packTypeComboBox);

        // 抽取类型选择
        controlPanel.add(new JLabel("抽取方式:"));
        drawTypeComboBox = new JComboBox<>(new String[]{"单抽", "十连抽"});
        controlPanel.add(drawTypeComboBox);

        // 抽取按钮
        drawButton = new JButton("抽取");
        drawButton.addActionListener(new DrawActionListener());
        controlPanel.add(drawButton);

        // 结果显示区域
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        // 计数显示区域
        JPanel countPanel = new JPanel();
        countPanel.setLayout(new FlowLayout());
        countPanel.add(new JLabel("传奇心仪球员:"));
        legendaryCountField = new JTextField(5);
        legendaryCountField.setEditable(false);
        countPanel.add(legendaryCountField);
        countPanel.add(new JLabel("精选心仪球员:"));
        specialCountField = new JTextField(5);
        specialCountField.setEditable(false);
        countPanel.add(specialCountField);
        countPanel.add(new JLabel("总抽取次数:"));
        totalDrawsField = new JTextField(5);
        totalDrawsField.setEditable(false);
        countPanel.add(totalDrawsField);

        // 添加组件到主面板
        Container contentPane = getContentPane();
        contentPane.add(controlPanel, BorderLayout.NORTH);
        contentPane.add(scrollPane, BorderLayout.CENTER);
        contentPane.add(countPanel, BorderLayout.SOUTH);

        // 初始化计数显示
        updateCountDisplay();
    }

    private class DrawActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String packType = (String) packTypeComboBox.getSelectedItem();
            String drawType = (String) drawTypeComboBox.getSelectedItem();

            List<String> results = new ArrayList<>();

            int drawCount = drawType.equals("单抽") ? 1 : 10;

            for (int i = 0; i < drawCount; i++) {
                String player = drawPlayer(packType);
                results.add(player);
            }

            // 十连抽保证至少一个4星球员
            if (drawType.equals("十连抽")) {
                boolean has4Star = false;
                for (String result : results) {
                    if (result.contains("4星") || result.contains("3星")) {
                        has4Star = true;
                        break;
                    }
                }
                if (!has4Star) {
                    // 替换最后一个结果为4星球员
                    results.set(results.size() - 1, "4星球员");
                }
            }

            // 显示结果并更新计数
            StringBuilder resultStr = new StringBuilder();
            for (String result : results) {
                resultStr.append(result).append("\n");

                // 检查是否为心仪球员并更新计数
                if (result.contains("传奇球员") && isFavorPlayer()) {
                    legendaryFavorCount++;
                } else if (result.contains("精选球员") && isFavorPlayer()) {
                    specialFavorCount++;
                }
            }

            resultArea.setText(resultStr.toString());

            // 更新总抽取次数
            totalDraws += drawCount;

            // 更新计数显示
            updateCountDisplay();
        }
    }

    private String drawPlayer(String packType) {
        Random random = new Random();
        double randValue = random.nextDouble();

        String playerType = "";
        boolean isFavor = false;

        if (packType.equals("传奇球员包")) {
            if (randValue < 0.005) {
                playerType = "传奇球员";
                // 20%的概率是心仪球员
                isFavor = random.nextDouble() < 0.2;
            } else if (randValue < 0.05) {
                playerType = "5星球员";
            } else if (randValue < 0.4) {
                playerType = "4星球员";
            } else {
                playerType = "3星球员";
            }
        } else if (packType.equals("精选球员包")) {
            if (randValue < 0.024) {
                playerType = "精选球员";
                // 20%的概率是心仪球员
                isFavor = random.nextDouble() < 0.2;
            } else if (randValue < 0.06) {
                playerType = "5星球员";
            } else if (randValue < 0.4) {
                playerType = "4星球员";
            } else {
                playerType = "3星球员";
            }
        }

        if (isFavor) {
            return playerType + "（心仪球员）";
        } else {
            return playerType;
        }
    }

    private boolean isFavorPlayer() {
        // 20%的概率是心仪球员
        return new Random().nextDouble() < 0.2;
    }

    private void updateCountDisplay() {
        legendaryCountField.setText(String.valueOf(legendaryFavorCount));
        specialCountField.setText(String.valueOf(specialFavorCount));
        totalDrawsField.setText(String.valueOf(totalDraws));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GachaGame().setVisible(true);
            }
        });
    }
}