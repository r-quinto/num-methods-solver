package nummethods;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.List;
import java.util.Stack;
import java.util.Arrays;

public class Gui extends JFrame {

    private JButton bisectionButton;
    private JButton falsePositionButton;
    private JButton newtonRaphsonButton;
    private JButton secantButton;
    private JButton fixedPointButton;
    private JButton cramerButton;
    private JButton matrixMultiplicationButton;
    private JTextArea outputArea;
    private Image backgroundImage;

    public Gui() {
        setTitle("NumeriSolve");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        Dimension buttonSize = new Dimension(180, 48);
        Font buttonFont = new Font("Arial", Font.BOLD, 15);

        bisectionButton = createGradientButton("BISECTION", buttonFont, buttonSize);
        falsePositionButton = createGradientButton("FALSE POSITION", buttonFont, buttonSize);
        newtonRaphsonButton = createGradientButton("NEWTON RAPHSON", buttonFont, buttonSize);
        secantButton = createGradientButton("SECANT", buttonFont, buttonSize);
        fixedPointButton = createGradientButton("FIXED POINT", buttonFont, buttonSize);
        cramerButton = createGradientButton("CRAMER'S", buttonFont, buttonSize);
        matrixMultiplicationButton = createGradientButton(
            "<html><center>MATRIX<br>MULTIPLICATION</center></html>", buttonFont, buttonSize);

        // Output area
        outputArea = new JTextArea(50, 50);
        outputArea.setEditable(false);
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        outputArea.setOpaque(false);

        backgroundImage = new ImageIcon(getClass().getResource("/nummethods/pixels_bg.png")).getImage();

        JPanel transparentPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setColor(new Color(255, 255, 255, 180));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 24, 24);
                g2d.dispose();
            }
        };
        transparentPanel.setOpaque(false);
        transparentPanel.add(outputArea, BorderLayout.CENTER);
        transparentPanel.setPreferredSize(new Dimension(400, 160));

        JScrollPane scrollPane = new JScrollPane(transparentPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        scrollPane.setPreferredSize(new Dimension(1200, 400));

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        bottomPanel.setOpaque(false);
        bottomPanel.add(scrollPane);

        JPanel outerPanel = new JPanel(new BorderLayout());
        outerPanel.setOpaque(false);

        JPanel centeringPanel = new JPanel(new GridBagLayout());
        centeringPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.weighty = 0.2; 
        centeringPanel.add(Box.createVerticalStrut(60), gbc); 

        gbc.gridy = 1;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel buttonGrid = new JPanel(new GridLayout(2, 4, 20, 18));
        buttonGrid.setOpaque(false);
        buttonGrid.add(bisectionButton);
        buttonGrid.add(falsePositionButton);
        buttonGrid.add(newtonRaphsonButton);
        buttonGrid.add(secantButton);
        buttonGrid.add(fixedPointButton);
        buttonGrid.add(cramerButton);
        buttonGrid.add(matrixMultiplicationButton);
        buttonGrid.add(new JLabel("")); 

        centeringPanel.add(buttonGrid, gbc);

        JPanel titlePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                String mainTitle = "NumeriSolve";
                String subtitle = "Interactive Numerical Methods Toolkit";
                Font mainFont = new Font("Arial", Font.BOLD, 56); // Large main title
                Font subFont = new Font("Arial", Font.PLAIN, 18);  // Subtitle: readable but not too small

                int panelWidth = getWidth();

                Color top = new Color(235, 242, 250);
                Color bottom = new Color(255, 255, 255);
                GradientPaint titlePaint = new GradientPaint(0, 20, top, 0, 100, bottom);
               
                Color blueShadow = new Color(20, 60, 110, 110);
                Color yellow = new Color(255, 206, 50);

                g2d.setFont(mainFont);
                FontMetrics fm = g2d.getFontMetrics();
                int titleWidth = fm.stringWidth(mainTitle);
                int xTitle = (panelWidth - titleWidth) / 2;
                int yTitle = 72; 
                g2d.setColor(blueShadow);
                g2d.drawString(mainTitle, xTitle + 3, yTitle + 3);

                g2d.setPaint(titlePaint);
                g2d.drawString(mainTitle, xTitle, yTitle);

                g2d.setColor(new Color(60, 120, 180, 130));
                g2d.setStroke(new BasicStroke(3));
                int lineY = yTitle + 22;
                g2d.drawLine(panelWidth / 2 - 170, lineY, panelWidth / 2 + 170, lineY);

                g2d.setFont(subFont);
                FontMetrics subfm = g2d.getFontMetrics();
                int subWidth = subfm.stringWidth(subtitle);
                int xSub = (panelWidth - subWidth) / 2;
                int ySub = lineY + 32;
                g2d.setColor(yellow);
                g2d.drawString(subtitle, xSub, ySub);

                g2d.dispose();
            }

            @Override
            public Dimension getPreferredSize() {
                return new Dimension(800, 130); 
            }
        };
        titlePanel.setOpaque(false);

        outerPanel.add(titlePanel, BorderLayout.NORTH);
        outerPanel.add(centeringPanel, BorderLayout.CENTER);

        JPanel contentPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        contentPanel.setLayout(new BorderLayout(10, 10));
        contentPanel.add(outerPanel, BorderLayout.CENTER);
        contentPanel.add(bottomPanel, BorderLayout.SOUTH);
        contentPanel.setOpaque(false);

        setContentPane(contentPanel);

        bisectionButton.addActionListener(e -> runBisection());
        falsePositionButton.addActionListener(e -> runFalsePosition());
        newtonRaphsonButton.addActionListener(e -> runNewtonRaphson());
        secantButton.addActionListener(e -> runSecant());
        fixedPointButton.addActionListener(e -> runFixedPoint());
        cramerButton.addActionListener(e -> runCramersRule());
        matrixMultiplicationButton.addActionListener(e -> runMatrixMultiplication());
    }

    private JButton createGradientButton(String text, Font font, Dimension size) {
        JButton button = new JButton(text) {
            private boolean hover = false;

            {
                setContentAreaFilled(false);
                setFocusPainted(false);
                setOpaque(false);
                setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
                setForeground(new Color(35, 35, 35));
                setFont(font);

                addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        hover = true;
                        repaint();
                    }
                    @Override
                    public void mouseExited(MouseEvent e) {
                        hover = false;
                        repaint();
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int arc = 20;
                int w = getWidth();
                int h = getHeight();

                Color top = new Color(235, 242, 250);
                Color bottom = new Color(255, 255, 255);
                Color hoverTop = new Color(210, 222, 240);
                Color hoverBottom = new Color(240, 240, 255);

                GradientPaint paint = hover
                        ? new GradientPaint(0, 0, hoverTop, 0, h, hoverBottom)
                        : new GradientPaint(0, 0, top, 0, h, bottom);

                g2.setPaint(paint);
                g2.fillRoundRect(0, 0, w, h, arc, arc);

                g2.setColor(new Color(180, 180, 180));
                g2.setStroke(new BasicStroke(2));
                g2.drawRoundRect(0, 0, w - 1, h - 1, arc, arc);

                g2.setColor(getForeground());
                g2.setFont(getFont());
                super.paintComponent(g);

                g2.dispose();
            }

            @Override
            protected void paintBorder(Graphics g) {
            }
        };
        button.setPreferredSize(size);
        return button;
    }

    private String stripHtml(String html) {
        return html.replaceAll("<[^>]+>", "").replace("&nbsp;", " ");
    }

    
    private void runMatrixMultiplication() {
    try {
        String columns = JOptionPane.showInputDialog(this, "Enter columns (space-separated):", "");
        if (columns == null) return;
        String rows = JOptionPane.showInputDialog(this, "Enter rows (space-separated):", "");
        if (rows == null) return;

        String[] listcol = columns.trim().split("\\s+");
        String[] listrow = rows.trim().split("\\s+");
        int[] arrcol = new int[listcol.length];
        int[] arrrow = new int[listrow.length];

        for (int i = 0; i < listcol.length; i++) arrcol[i] = Integer.parseInt(listcol[i]);
        for (int i = 0; i < listrow.length; i++) arrrow[i] = Integer.parseInt(listrow[i]);

        int[][] final_matrix = new int[arrrow.length][arrcol.length];
        for (int y = 0; y < arrrow.length; y++) {
            for (int x = 0; x < arrcol.length; x++) {
                final_matrix[y][x] = arrrow[y] * arrcol[x];
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Matrix Multiplication Result:\n\n");
        for (int[] row : final_matrix) {
            sb.append(Arrays.toString(row)).append("\n");
        }
        outputArea.setText(sb.toString());
    } catch (Exception ex) {
        outputArea.setText("Invalid Input: " + ex.getMessage());
    }
}

    // Bisection Method

private void runBisection() {
    try {
        String func = JOptionPane.showInputDialog(this, "Enter the equation f(x):", "");
        if (func == null) return;

        java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("([a-zA-Z])").matcher(func);
        String variable = "x";
        if (matcher.find()) {
            variable = matcher.group(1);
        }

        String[] parts = func.split("=");
        if (parts.length == 2) {
            func = "(" + parts[0].trim() + ") - (" + parts[1].trim() + ")";
        } else if (parts.length > 2) {
            JOptionPane.showMessageDialog(this, "Invalid equation format. Too many '=' symbols.");
            return;
        }

        double a = promptForDouble("Enter the lower bound (" + variable + "L):");
        double b = promptForDouble("Enter the upper bound (" + variable + "R):");
        int decimals = promptForInt("Enter the number of decimal places for tolerance (e.g., 4 for 1e-4):", 0, 10);
        double tolerance = Math.pow(10, -decimals);

        List<BisectionMethod.IterationData> iterations = BisectionMethod.bisectionMethod(func, variable, a, b, tolerance);

        StringBuilder sb = new StringBuilder();
        sb.append("Bisection Method\n");
        sb.append("\nIteration Table:\n");
        sb.append("Iter\t   " + variable + "L\t\t   " + variable + "R\t\t   " + variable + "C\t\t   f(" + variable + "C)\n");
        sb.append("----\t----------\t----------\t----------\t----------\n");
        String format = "%4d\t%" + (decimals + 7) + "." + decimals + "f\t%" +
                (decimals + 7) + "." + decimals + "f\t%" +
                (decimals + 7) + "." + decimals + "f\t%" +
                (decimals + 7) + "." + decimals + "f\n";
        for (BisectionMethod.IterationData data : iterations) {
            sb.append(String.format(format, data.iteration, data.a, data.b, data.c, data.fc));
        }
        sb.append("\nSolving Steps:\n");
        String stepFormat = "Iter %d: " + variable + "L = %." + decimals + "f, " + variable + "R = %." + decimals + "f, " + variable + "C = %." + decimals +
                "f, f(" + variable + "C) = %." + decimals + "f\n";
        for (BisectionMethod.IterationData data : iterations) {
            sb.append(String.format(stepFormat, data.iteration, data.a, data.b, data.c, data.fc));
        }
        if (!iterations.isEmpty()) {
            BisectionMethod.IterationData last = iterations.get(iterations.size() - 1);
            sb.append(String.format("%nFinal solution: Root ≈ %." + decimals + "f (after %d iterations)%n",
                    last.c, last.iteration));
        }
        outputArea.setText(sb.toString());
    } catch (Exception ex) {
        outputArea.setText("Error: " + ex.getMessage());
    }
}

    // False Position Method
    private void runFalsePosition() {
    try {
        String func = JOptionPane.showInputDialog(this, "Enter the equation f(x):", "");
        if (func == null) return;

        java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("([a-zA-Z])").matcher(func);
        String variable = "x";
        if (matcher.find()) {
            variable = matcher.group(1);
        }

        String expressionString;
        String[] parts = func.split("=");
        if (parts.length == 2) {
            expressionString = "(" + parts[0].trim() + ") - (" + parts[1].trim() + ")";
        } else if (parts.length > 2) {
            JOptionPane.showMessageDialog(this, "Invalid equation format. Too many '=' symbols.");
            return;
        } else {
            expressionString = func;
        }

        double xL = promptForDouble("Enter initial guess " + variable + "0:");
        double xR = promptForDouble("Enter initial guess " + variable + "1:");
        int decimals = promptForInt("Enter number of decimal places for tolerance (e.g., 4 for 1e-4):", 1, 10);
        double epsilon = Math.pow(10, -decimals);

        List<FalsePosition.FalsePositionIteration> results = FalsePosition.falsePosition(expressionString, variable, xL, xR, epsilon);

        if (results.isEmpty()) {
            outputArea.setText("No valid iterations. Make sure that f(" + variable + "0) and f(" + variable + "1) have opposite signs.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("False Position Method\n");
        sb.append("\nIteration Table:\n");
        sb.append("Iter\t" + variable + "L\t\t" + variable + "R\t\t" + variable + "New\t\tf(" + variable + "New)\n");
        sb.append("---------------------------------------------------------\n");
        String format = "%4d\t%" + (decimals + 6) + "." + decimals + "f\t%" + (decimals + 6) + "." + decimals + "f\t%" +
                (decimals + 6) + "." + decimals + "f\t%" + (decimals + 6) + "." + decimals + "f\n";
        for (FalsePosition.FalsePositionIteration it : results) {
            sb.append(String.format(format, it.iteration, it.xL, it.xR, it.xNew, it.fXNew));
        }

        sb.append("\nSolving Steps:\n");
        String formatStep = variable + "%d = " + variable + "L - f(" + variable + "L)(" + variable + "R - " + variable + "L)/(f(" + variable + "R) - f(" + variable + "L)) = %." + decimals + "f - (%." + decimals +
                "f)(%." + decimals + "f - %." + decimals + "f) / (%." + decimals + "f - %." + decimals +
                "f) = %." + decimals + "f\n";
        for (FalsePosition.FalsePositionIteration it : results) {
            sb.append(String.format(formatStep, it.iteration, it.xL, it.fXL, it.xR, it.xL, it.fXR, it.fXL, it.xNew));
        }
        if (!results.isEmpty()) {
            FalsePosition.FalsePositionIteration last = results.get(results.size() - 1);
            sb.append(String.format("%nFinal solution: Root = %." + decimals + "f%n", last.xNew));
        }
        outputArea.setText(sb.toString());
    } catch (Exception ex) {
        outputArea.setText("Error: " + ex.getMessage());
    }
}

    // Newton-Raphson Method
    private void runNewtonRaphson() {
        try {
            String func = JOptionPane.showInputDialog(this, "Enter the equation f(x):", "");
            if (func == null) return;

            java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("([a-zA-Z])").matcher(func);
            String variable = "x";
            if (matcher.find()) {
                variable = matcher.group(1);
            }

            String function;
            String[] parts = func.split("=");
            if (parts.length == 2) {
                function = "(" + parts[0].trim() + ") - (" + parts[1].trim() + ")";
            } else if (parts.length > 2) {
                JOptionPane.showMessageDialog(this, "Invalid equation format. Too many '=' symbols.");
                return;
            } else {
                function = func;
            }

            double x0 = promptForDouble("Enter the initial guess (" + variable + "0):");
            int decimalPlaces = promptForInt("Enter the number of decimal places for tolerance (e.g., 4 for 1e-4):", 0, 10);
            double tolerance = Math.pow(10, -decimalPlaces);

            List<NewtonRaphson.IterationData> iterations = NewtonRaphson.newtonRaphson(function, variable, x0, tolerance);

            StringBuilder sb = new StringBuilder();
            sb.append("Newton-Raphson Method\n");

            sb.append("\nIteration Table:\n");
            sb.append("Iter\t   " + variable + "\t\t   f(" + variable + ")\n");
            sb.append("----\t----------\t----------\n");
            String formatString = "%4d\t%" + (decimalPlaces + 7) + "." + decimalPlaces + "f\t%" + (decimalPlaces + 7) + "." + decimalPlaces + "f\n";
            for (NewtonRaphson.IterationData data : iterations) {
                sb.append(String.format(formatString, data.iteration, data.x, data.fx));
            }

            sb.append("\nSolving Steps:\n");
            String formatStep = variable + "%d = " + variable + " - f(" + variable + ")/f'(" + variable + ") = %." + decimalPlaces + "f - (%." + decimalPlaces + "f) / (%." + decimalPlaces + "f) = %." + decimalPlaces + "f\n";
            for (NewtonRaphson.IterationData data : iterations) {
                sb.append(String.format(formatStep, data.iteration, data.x, data.fx, data.dfx, data.xNew));
            }
            NewtonRaphson.IterationData last = iterations.get(iterations.size() - 1);
            sb.append(String.format("%nFinal solution: Root = %." + decimalPlaces + "f%n", last.xNew));
            outputArea.setText(sb.toString());
        } catch (Exception ex) {
            outputArea.setText("Error: " + ex.getMessage());
        }
    }
    
    // Secant Method
    private void runSecant() {
    try {
        String equation = JOptionPane.showInputDialog(this, "Enter the equation f(x):", "");
        if (equation == null) return;

        java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("([a-zA-Z])").matcher(equation);
        String variable = "x";
        if (matcher.find()) {
            variable = matcher.group(1);
        }

        String[] parts = equation.split("=");
        if (parts.length == 2) {
            equation = "(" + parts[0].trim() + ") - (" + parts[1].trim() + ")";
        } else if (parts.length > 2) {
            JOptionPane.showMessageDialog(this, "Invalid equation format. Too many '=' symbols.");
            return;
        }

        double x0 = promptForDouble("Enter first initial guess (" + variable + "0):");
        double x1 = promptForDouble("Enter second initial guess (" + variable + "1):");
        int decimalPlaces = promptForInt("Enter number of decimal places for tolerance (e.g., 4 for 1e-4):", 1, 10);
        double tol = Math.pow(10, -decimalPlaces);

        Stack<Double> approximations = new Stack<>();
        approximations.push(x0);
        approximations.push(x1);

        StringBuilder solvingSteps = new StringBuilder();

        Secant.runSecant(approximations, equation, variable, tol, solvingSteps);

        StringBuilder sb = new StringBuilder();
        sb.append("Secant Method\n\n");
        sb.append("Iteration Table:\n");
        sb.append(String.format("%-6s %-12s %-12s\n", "Iter", variable, "f(" + variable + ")"));
        sb.append("------ ------------ ------------\n");
        for (int i = 0; i < approximations.size(); i++) {
            double x = approximations.get(i);
            double fx = Secant.evaluateFunction(equation, variable, x);
            sb.append(String.format("%-6d %-12." + decimalPlaces + "f %-12." + decimalPlaces + "f\n", i + 1, x, fx));
        }
        sb.append("\nSolving Steps:\n");
        sb.append(solvingSteps.toString());
        sb.append(String.format("\nFinal solution: Root = %." + decimalPlaces + "f\n", approximations.peek()));

        outputArea.setText(sb.toString());
    } catch (Exception ex) {
        outputArea.setText("Error: " + ex.getMessage());
    }
}
    
    private void runFixedPoint() {
    try {
        String gx = JOptionPane.showInputDialog(this, "Enter the function g(x):", "");
        if (gx == null) return;

        gx = FixedPoint.class.getDeclaredMethod("convertExpNotation", String.class)
                .invoke(null, gx).toString();

        double x0 = promptForDouble("Enter initial guess x0:");
        int decimalPlaces = promptForInt("Enter number of decimal places for tolerance (e.g., 4 for 1e-4):", 1, 10);
        double epsilon = Math.pow(10, -decimalPlaces);
        int maxIter = 100;

        List<Double> results = new java.util.ArrayList<>();
        results.add(x0);

        int iteration = 0;
        double xPrev = x0;

        while (iteration < maxIter) {
            double xNext = FixedPoint.evaluateG(gx, xPrev);
            results.add(xNext);
            if (Math.abs(xNext - xPrev) < epsilon) {
                break;
            }
            xPrev = xNext;
            iteration++;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Fixed Point Method\n\n");

        // Iteration Table
        sb.append("Iteration Table:\n");
        sb.append(String.format("%-6s %-12s %-12s\n", "Iter", "x", "g(x)"));
        sb.append("------ ------------ ------------\n");
        for (int i = 0; i < results.size() - 1; i++) {
            double x = results.get(i);
            double gxVal = FixedPoint.evaluateG(gx, x);
            sb.append(String.format("%-6d %-12." + decimalPlaces + "f %-12." + decimalPlaces + "f\n", i + 1, x, gxVal));
        }

        // Solving Steps
        sb.append("\nSolving Steps:\n");
        for (int i = 1; i < results.size(); i++) {
            double prev = results.get(i - 1);
            double curr = results.get(i);
            sb.append(String.format("x%d = g(x%d-1) = g(%." + decimalPlaces + "f) = %." + decimalPlaces + "f\n", i, i, prev, curr));
        }

        sb.append("\nFinal solution: Root ≈ ")
          .append(String.format("%." + decimalPlaces + "f", results.get(results.size() - 1)));

        outputArea.setText(sb.toString());
    } catch (Exception ex) {
        outputArea.setText("Error: " + ex.getMessage());
    }
}
    private void runCramersRule() {
    try {
        String[] equations = new String[3];
        equations[0] = JOptionPane.showInputDialog(this, "Enter equation 1 (ex., 2x + 3y - 1z = 5):", "");
        if (equations[0] == null) return;
        equations[1] = JOptionPane.showInputDialog(this, "Enter equation 2 (ex., 4x - 2y + 3z = 6):", "");
        if (equations[1] == null) return;
        equations[2] = JOptionPane.showInputDialog(this, "Enter equation 3 (ex., 3x + 5y + 2z = 8):", "");
        if (equations[2] == null) return;

        java.util.Set<String> varSet = new java.util.LinkedHashSet<>();
        for (String eq : equations) {
            java.util.regex.Matcher m = java.util.regex.Pattern.compile("([a-zA-Z]\\w*)").matcher(eq);
            while (m.find()) {
                varSet.add(m.group(1));
            }
        }
        java.util.List<String> variables = new java.util.ArrayList<>(varSet);

        if (variables.size() != 3) {
            outputArea.setText("Please enter equations with exactly 3 unique variables.");
            return;
        }

        double[][] matrix = new double[3][3];
        double[] constants = new double[3];

        for (int i = 0; i < 3; i++) {
            String eq = equations[i].replaceAll("-", "+-");
            String[] sides = eq.split("=");
            if (sides.length != 2) {
                outputArea.setText("Equation format error in: " + eq);
                return;
            }
            String lhs = sides[0];
            String rhs = sides[1];

            for (int v = 0; v < 3; v++) {
                String var = variables.get(v);
                java.util.regex.Matcher m = java.util.regex.Pattern.compile("([+-]?\\d*\\.?\\d*)\\s*" + var).matcher(lhs);
                double coef = 0;
                while (m.find()) {
                    String num = m.group(1);
                    if (num == null || num.trim().isEmpty() || num.equals("+")) num = "1";
                    if (num.equals("-")) num = "-1";
                    coef += Double.parseDouble(num.trim());
                }
                matrix[i][v] = coef;
            }
            constants[i] = Double.parseDouble(rhs.trim());
        }

        double detA = determinant3x3(matrix);

        StringBuilder sb = new StringBuilder();

        sb.append("System of equations:\n");
        for (String eq : equations) sb.append("  ").append(eq).append("\n");
        sb.append("\nVariables: ").append(variables).append("\n\n");

        sb.append("Coefficient matrix:\n");
        for (int i = 0; i < 3; i++) {
            sb.append("[ ");
            for (int j = 0; j < 3; j++) {
                sb.append(String.format("%6.2f ", matrix[i][j]));
            }
            sb.append("]\n");
        }

        sb.append(String.format("|A| = %.2f\n\n", detA));

        for (int v = 0; v < 3; v++) {
            double[][] temp = new double[3][3];
            for (int i = 0; i < 3; i++)
                for (int j = 0; j < 3; j++)
                    temp[i][j] = (j == v) ? constants[i] : matrix[i][j];

            sb.append("Matrix: " + variables.get(v) + "\n");
            for (int i = 0; i < 3; i++) {
                sb.append("[ ");
                for (int j = 0; j < 3; j++) {
                    sb.append(String.format("%6.2f ", temp[i][j]));
                }
                sb.append("]\n");
            }
            double detAi = determinant3x3(temp);
            sb.append(String.format("|A%s| = %.2f\n", variables.get(v), detAi));
            double val = detA != 0 ? detAi / detA : 0;
            if (Math.abs(val) < 1e-9) val = 0.0;
            sb.append(String.format("%s = |A%s| / |A| = %.2f / %.2f = %.2f\n\n",
                    variables.get(v), variables.get(v), detAi, detA, val));
        }

        sb.append("Final answers:\n");
        for (int v = 0; v < 3; v++) {
            double[][] temp = new double[3][3];
            for (int i = 0; i < 3; i++)
                for (int j = 0; j < 3; j++)
                    temp[i][j] = (j == v) ? constants[i] : matrix[i][j];
            double val = detA != 0 ? determinant3x3(temp) / detA : 0;
            if (Math.abs(val) < 1e-9) val = 0.0;
            sb.append(variables.get(v)).append(" = ").append(String.format("%.2f", val)).append("\n");
        }

        outputArea.setText(sb.toString());
    } catch (Exception ex) {
        outputArea.setText("Error: " + ex.getMessage());
    }
}

private double determinant3x3(double[][] m) {
    return m[0][0]*(m[1][1]*m[2][2] - m[1][2]*m[2][1])
         - m[0][1]*(m[1][0]*m[2][2] - m[1][2]*m[2][0])
         + m[0][2]*(m[1][0]*m[2][1] - m[1][1]*m[2][0]);
}

   private double promptForDouble(String prompt) {
        while (true) {
            String input = JOptionPane.showInputDialog(this, prompt, "");
            if (input == null) throw new RuntimeException("Operation cancelled.");
            try {
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid number, please try again.");
            }
        }
    }

    private int promptForInt(String prompt, int min, int max) {
        while (true) {
            String input = JOptionPane.showInputDialog(this, prompt, "");
            if (input == null) throw new RuntimeException("Operation cancelled.");
            try {
                int val = Integer.parseInt(input);
                if (val < min || val > max) {
                    JOptionPane.showMessageDialog(this, "Please enter an integer between " + min + " and " + max + ".");
                } else {
                    return val;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Invalid input, please enter an integer.");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Gui numeriSolve = new Gui();
            numeriSolve.setVisible(true);
        });
    }
}
