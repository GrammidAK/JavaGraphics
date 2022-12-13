package laba6;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.LinkedList;
import java.awt.*;
import java.awt.event.*;

// 01 0110 1110
public class Prog {
    static MainFrame balls;

    public static void main(String[] args) throws IOException {
        balls = new MainFrame();
    }

    static class MainFrame extends Frame implements Observer, ActionListener, ItemListener {
        private LinkedList<Figure> figuresList = new LinkedList<>();
        private Color color;
        private Frame controlFrame;
        private Button startFigureButton;
        private Button changeFigureButton;
        private Choice choiceColorsTable;
        private Choice choiceSpeedX;
        private Choice choiceSpeedY;
        private TextField text;
        private TextField figureNumber;
        private Choice changefigureNumber;
        private TextField newfigureNumber;
        BufferedImage image;

        MainFrame() throws IOException {
            initializeMainFrame();
            initializeControlFrame();
            initializeColorTableOfFigure();
            initializeStartButtonOfFrame();
            initializeChangeFigureSpeeds();
            initializeChangeFigureButton();
            initializeChangeFigureNumber();

            controlFrame.setVisible(true);
            File file = new File("src/Laba6/img.png");
            image = ImageIO.read(file);
        }

        private void initializeChangeFigureSpeeds() {
            figureNumber = new TextField("Введите номер фигуры");
            controlFrame.add(figureNumber);

            choiceSpeedX = new Choice();
            choiceSpeedX.addItem("1");
            choiceSpeedX.addItem("2");
            choiceSpeedX.addItem("4");
            choiceSpeedX.addItem("6");
            choiceSpeedX.addItem("8");
            choiceSpeedX.addItemListener(this);
            controlFrame.add(choiceSpeedX);

            choiceSpeedY = new Choice();
            choiceSpeedY.addItem("1");
            choiceSpeedY.addItem("2");
            choiceSpeedY.addItem("4");
            choiceSpeedY.addItem("6");
            choiceSpeedY.addItem("8");
            choiceSpeedY.addItemListener(this);
            controlFrame.add(choiceSpeedY);
        }

        private void initializeChangeFigureButton() {
            changeFigureButton = new Button("Изменить");
            changeFigureButton.setActionCommand("CHANGE");
            changeFigureButton.addActionListener(this);
            controlFrame.add(changeFigureButton);
        }
        
        private void initializeChangeFigureNumber(){
            changefigureNumber = new Choice();
            controlFrame.add(changefigureNumber);
            newfigureNumber = new TextField("Введите новый номер фигуры");
            controlFrame.add(newfigureNumber);
        }

        private void initializeMainFrame() {
            this.addWindowListener(new WindowCloser()); // кнопка закрытия окна
            this.setSize(400, 400);
            this.setResizable(false);
            this.setVisible(true);
            this.setLocation(500, 300);
            this.setTitle("Демонстрационное окно");
        }

        private void initializeStartButtonOfFrame() {
            startFigureButton = new Button("Пуск");
            startFigureButton.setActionCommand("Start");
            startFigureButton.addActionListener(this);
            controlFrame.add(startFigureButton, new Point(20, 20));
        }

        private void initializeControlFrame() {
            controlFrame = new Frame();
            controlFrame.setResizable(true);
            controlFrame.setSize(400, 400);

            controlFrame.setTitle("Управляющее окно");
            controlFrame.setLayout(new GridLayout(5,5,3,3));
            controlFrame.addWindowListener(new WindowCloser());
            text = new TextField("Картинка");
            controlFrame.add(text);
        }

        private void initializeColorTableOfFigure() {
            choiceColorsTable = new Choice();
            choiceColorsTable.addItem("Синий");
            choiceColorsTable.addItem("Зелёный");
            choiceColorsTable.addItem("Красный");
            choiceColorsTable.addItem("Чёрный");
            choiceColorsTable.addItem("Жёлтый");
            choiceColorsTable.addItemListener(this);
            controlFrame.add(choiceColorsTable, new Point(600, 20));
        }

        @Override
        public void update(Observable o, Object arg) {
            Figure figure = (Figure) arg;
            System.out.println("x = " + figure.thr.getName() + figure.x);
            repaint();
        }

        public void paint(Graphics g) {
            if (!figuresList.isEmpty()) {
                for (Figure figure : figuresList) {
                    g.setColor(figure.color);
                    g.drawString(Integer.toString(figure.number), figure.x, figure.y);
                    if (figure.objectType == ObjectType.IMAGE) {
                        g.drawImage(image, figure.x, figure.y, 50, 50, null);
                    }
                    else {
                        g.drawString(figure.getText(), figure.x, figure.y + 25);
                    }
                }
            }
        }

        @Override
        public void actionPerformed(ActionEvent aE) { // обработка нажатий кнопок
            String str = aE.getActionCommand();
            if (str.equals("Start")) {// "Пуск"
                buttonOKIsPressed();
            }
            else if (str.equals("CHANGE")) {// "Изменить"
                try {
                    if (figuresList.isEmpty())
                        return;
                    int changeableNumber = Integer.parseInt(changefigureNumber.getSelectedItem());
                    int newNumber = getUniqueNumber(Integer.parseInt(newfigureNumber.getText()));
                    if (newNumber != -1){
                        for (Figure figure : figuresList) {
                            if (figure.number == changeableNumber){
                                color = getColorFromTable();
                                figure.setSpeedX(getChoiceSpeedXFromTable());
                                figure.setSpeedY(getChoiceSpeedYFromTable());
                                figure.setColor(color);
                                figure.setNumber(newNumber);
                                changefigureNumber.remove(Integer.toString(changeableNumber));
                                changefigureNumber.addItem(Integer.toString(newNumber));
                            }
                        }
                    }
                }
                catch (NumberFormatException ignored) {
                    return;
                }
            }
            repaint();
        }
        
        private int getUniqueNumber(int number){
            for(int i = 0; i < changefigureNumber.getItemCount(); i++){
                if (Integer.parseInt(changefigureNumber.getItem(i)) == number){
                    return -1;
                }
            }
            return number;
        }
        private int getMaxNumber(){
            int maxNumber = 0;
            for(int i = 0; i < changefigureNumber.getItemCount(); i++){
                if (Integer.parseInt(changefigureNumber.getItem(i)) > maxNumber){
                    maxNumber = Integer.parseInt(changefigureNumber.getItem(i));
                }
            }
            return maxNumber;
        }

        private int getChoiceSpeedXFromTable() {
            switch(choiceSpeedX.getSelectedIndex()) {
                case 0:
                    return 1;
                case 1:
                    return 2;
                case 2:
                    return 4;
                case 3:
                    return 6;
                case 4:
                    return 8;
            }
            return 1;
        }

        private int getChoiceSpeedYFromTable() {
            switch (choiceSpeedY.getSelectedIndex()) {
                case 0:
                    return 1;
                case 1:
                    return 2;
                case 2:
                    return 4;
                case 3:
                    return 6;
                case 4:
                    return 8;
            }
            return 1;
        }

        private void buttonOKIsPressed() {
            color = getColorFromTable();
            ObjectType objectType = getObjectType();
            Figure figure = null;
            try {
                figure = getFigureByFigureBuilder(objectType);
            }
            catch (NumberFormatException ignored) {
            }
            finally {
                if (figure == null){
                    figure = getFigureByFigureBuilderWithoutSpeeds(objectType);
                }
                figuresList.add(figure);
                figure.addObserver(this);
                changefigureNumber.addItem(Integer.toString(figure.number));
            }
        }

        private Figure getFigureByFigureBuilder(ObjectType objectType) throws NumberFormatException {
            int number = 0;
            try{
                number = getUniqueNumber(Integer.parseInt(figureNumber.getText()));
                if (number == -1){
                    number = getMaxNumber()+1;
                }
            }
            catch(NumberFormatException e){ 
                    return Figure.FigureBuilder.aFigure()
                        .withColor(color)
                        .withObjectType(objectType)
                        .withText(this.text.getText())
                        .withNumber(number)
                        .withSpeedX(getChoiceSpeedXFromTable())
                        .withSpeedY(getChoiceSpeedYFromTable()) 
                        .build();
            }
                return Figure.FigureBuilder.aFigure()
                        .withColor(color)
                        .withObjectType(objectType)
                        .withText(this.text.getText())
                        .withNumber(number)
                        .withSpeedX(getChoiceSpeedXFromTable())
                        .withSpeedY(getChoiceSpeedYFromTable()) 
                        .build();
        }

        private Figure getFigureByFigureBuilderWithoutSpeeds(ObjectType objectType) {
            return Figure.FigureBuilder.aFigure()
                    .withColor(color)
                    .withObjectType(objectType)
                    .withText(this.text.getText())
                    .withNumber(Integer.parseInt(figureNumber.getText()))
                    .build();
        }

        private ObjectType getObjectType() {
            if (text.getText().equals("Картинка")){
                return ObjectType.IMAGE;
            }
            return ObjectType.TEXT;
        }

        private Color getColorFromTable() {
            switch (choiceColorsTable.getSelectedIndex()) {
                case 0:
                    return Color.blue;
                case 1:
                    return Color.green;
                case 2:
                    return Color.red;
                case 3:
                    return Color.black;
                case 4:
                    return Color.yellow;
            }
            return Color.blue;
        }

        @Override
        public void itemStateChanged(ItemEvent e) {
        }
    }

    static class Figure extends Observable implements Runnable {
        Thread thr;
        private boolean xplus = true;
        private boolean yplus = true;
        public ObjectType objectType;
        private int speedX = 1;
        private int speedY = 1;

        final static int WIDTH = 50;
        final static int HEIGHT = 50;
        int x = 0;
        int y = 30;
        int number;
        private final String text;
        private Color color;

        public Figure(FigureBuilder builder){
            color = builder.color;
            text = builder.text;
            speedX = builder.speedX;
            speedY = builder.speedY;
            objectType = builder.objectType;
            number = builder.number;
            thr = new Thread(this, number + ":" + text + ":");
            thr.start();
        }

        public Figure(Color color, String text, ObjectType type) {
                objectType = type;
                this.text = text;
                this.color = color;
                thr = new Thread(this, number + ":" + text + ":");
                thr.start();
        }

        public int getSpeedX() {
            return speedX;
        }

        public void setSpeedX(int speedX) {
            this.speedX = speedX;
        }

        public int getSpeedY() {
            return speedY;
        }

        public void setColor(Color color) {
            this.color = color;
        }

        public void setSpeedY(int speedY) {
            this.speedY = speedY;
        }

        public String getText() {
            return text;
        }
        
        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number= number;
        }

        public void run() {
            while (true) {
                Dimension mainFrameSize = balls.getSize();
                if (x > mainFrameSize.width - WIDTH)
                    xplus = false;
                if (x < -1)
                    xplus = true;
                if (y > mainFrameSize.height - HEIGHT)
                    yplus = false;
                if (y < 31)
                    yplus = true;
                if (xplus)
                    x += speedX;
                else
                    x -= speedX;
                if (yplus)
                    y += speedY;
                else
                    y -= speedY;
                setChanged();
                notifyObservers(this);
                try {
                    Thread.sleep(10);
                }
                catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        public static class FigureBuilder {
            private ObjectType  objectType = ObjectType.IMAGE;
            private int speedX = 1;
            private int speedY = 1;
            private String text = "default text";
            private Color color = Color.black;
            private int number = 0;

            private FigureBuilder(){
            }

            public static FigureBuilder aFigure() {
                return new FigureBuilder();
            }

            public FigureBuilder withColor(Color color){
                this.color = color;
                return this;
            }

            public FigureBuilder withText(String text) {
                this.text = text;
                return this;
            }

            public FigureBuilder withSpeedX(int speedX) {
                this.speedX = speedX;
                return this;
            }

            public FigureBuilder withSpeedY(int speedY) {
                this.speedY = speedY;
                return this;
            }

            public FigureBuilder withObjectType(ObjectType type) {
                this.objectType = type;
                return this;
            }
            
            public FigureBuilder withNumber(int number) {
                this.number = number;
                return this;
            }

            public Figure build() {
                return new Figure(this);
            }
        }
    }

    static class WindowCloser extends WindowAdapter {
        @Override
        public void windowClosing (WindowEvent wE) {
            System.exit(0);
        }
    }

    static enum ObjectType {
        IMAGE("Картинка"),
        TEXT("Текст");

        private final String type;

        ObjectType(String type){
            this.type = type;
        }

        private String getType(){
            return type;
        }
    }
}
