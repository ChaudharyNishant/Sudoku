//Created by Nishant Chaudhary
//https://github.com/ChaudharyNishant

package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.Random;

public class Main extends Application
{
    Random rand = new Random();
    long start;
    boolean firstTime = true;
    boolean changed = false;
    int[][] a = new int[6][6];
    int[][] b = new int[6][6];
    VBox root = new VBox();
    GridPane grid = new GridPane();
    Button btn[][] = new Button[6][6];
    Button submit = new Button("Submit");
    Label result = new Label();
    Label time = new Label("Time Elapsed: 0 seconds");
    Label score = new Label("Score: 100");
    Stage newGameStage = new Stage();
    Stage quitStage = new Stage(); 
    
    public void start(Stage primaryStage) throws Exception
    {
    	HBox top = new HBox();
    	Button newGame = new Button("New Game");
        Button quit = new Button("Quit");
    	
    	root.setId("root");
        top.setId("top");
        newGame.setId("topBtn");
        quit.setId("topBtn");
        submit.setId("submit");
        time.setId("timeScore");
        score.setId("timeScore");

        newGame.setOnAction(e ->
        {
            if(/*!firstTime && */changed && !result.getText().equals("Correct") && !quitStage.isShowing() && !newGameStage.isShowing())
                NewGame();
            else if(/*firstTime || */!changed || result.getText().equals("Correct"))
            	playNewGame();

            submit.setOnAction(e2 ->
            {
                int x, y;
                for(x=0; x<6; x++)
                    for(y=0; y<6; y++)
                    {
                        if(btn[x][y].getText().equals(""))
                            b[x][y] = 0;
                        else
                            b[x][y] = Integer.parseInt(btn[x][y].getText());
                    }
                if(CheckWin(b)==0)
                    result.setText("Incomplete");
                else if(CheckWin(b) == -1)
                    result.setText("Incorrect");
                else
                    result.setText("Correct");
                result.setId(result.getText().toLowerCase());
            });

            Timeline timeline = new Timeline(new KeyFrame( Duration.millis(1000), ae ->
            {
                long now = System.currentTimeMillis();
                int sec = (int)((now-start)/1000);
                if(!result.getText().equals("Correct"))
                {
                    time.setText("Time Elapsed: " + Integer.toString(sec) + " seconds");
                    if (sec > 120 && sec <= 220)
                        score.setText("Score: " + Integer.toString(100 - (sec - 120)));
                }
            }));
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.play();
        });

        quit.setOnAction(e ->
        {
            if(/*!firstTime && */changed && !result.getText().equals("Correct") && !quitStage.isShowing() && !newGameStage.isShowing())
                Quit();
            else if(/*firstTime || */!changed || result.getText().equals("Correct"))
                System.exit(0);
        });
        
        newGame.setPrefWidth(95);//80
        HBox.setMargin(newGame, new Insets(10, 0, 5, 15));
        quit.setPrefWidth(80);
        HBox.setMargin(quit, new Insets(10, 0, 5, 43));
        top.getChildren().addAll(newGame, quit);
        grid.setPadding(new Insets(10, 30, 0, 30));
        VBox.setMargin(submit, new Insets(15, 0, 10, 0));
        result.setPrefWidth(185);
        result.setFont(new Font("System Bold", 16));
        time.setPrefWidth(205);
        score.setPrefWidth(205);
        root.setAlignment(Pos.TOP_CENTER);
        
        root.getChildren().add(top);
        primaryStage.setTitle("Sudoku");
        Scene scene = new Scene(root, 235, 380);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    void playNewGame()
    {
        changed = false;
        result.setText("");
        score.setText("Score: 100");
        int i, j, fill;
        for (i = 0; i < 6; i++)
            for (j = 0; j < 6; j++)
                a[i][j] = b[i][j] = 0;

        GenerateSudoku(a);
        FillAtleastFour(a, b);
        for (i = 0; i < 6; i++)
            for (j = 0; j < 6; j++)
            {
                fill = rand.nextInt(2);
                btn[i][j] = new Button("");
                grid.add(btn[i][j], j, i);
                btn[i][j].setPrefWidth(30);
                if (i == 1 || i == 3)
                    GridPane.setMargin(btn[i][j], new Insets(0, 0, 5, 0));
                if (j == 2 && i % 2 == 0)
                    GridPane.setMargin(btn[i][j], new Insets(0, 5, 0, 0));
                btn[i][j].setText("");
                btn[i][j].setId("empty");
                if (fill == 1)
                    b[i][j] = a[i][j];
                else if (b[i][j] == 0)
                    a[i][j] = 0;
                if (a[i][j] != 0)
                {
                    btn[i][j].setText(Integer.toString(a[i][j]));
                    btn[i][j].setId("filled");
                }
            }
        
        if (firstTime)
        {
            root.getChildren().addAll(grid, submit, result, time, score);
            firstTime = false;
        }

        start = System.currentTimeMillis();
        for (i = 0; i < 6; i++)
            for (j = 0; j < 6; j++)
            {
                int x = i, y = j;
                btn[x][y].setOnAction(e ->
                {
                	if(!newGameStage.isShowing() && !quitStage.isShowing())
                	{
	                    int i2, j2;
	                    for(i2 = 0; i2 < 6; i2++)
	                        for(j2 = 0; j2 < 6; j2++)
	                            if(a[i2][j2] == 0)
	                                btn[i2][j2].setId("empty");
	                    if(a[x][y] == 0 && !result.getText().equals("Correct"))
	                    	btn[x][y].setId("selected");
                	}
                });
                btn[x][y].setOnKeyPressed(e ->
                {
                    if (a[x][y] == 0 && !result.getText().equals("Correct") && !newGameStage.isShowing() && !quitStage.isShowing())
                    {
                        String temp = e.getCode().toString();
                        switch (temp)
                        {
                            case "DIGIT1":
                            case "NUMPAD1": btn[x][y].setText("1");
                                            changed = true;
                                            break;
                            case "DIGIT2":
                            case "NUMPAD2": btn[x][y].setText("2");
                                            changed = true;
                                            break;
                            case "DIGIT3":
                            case "NUMPAD3": btn[x][y].setText("3");
                                            changed = true;
                                            break;
                            case "DIGIT4":
                            case "NUMPAD4": btn[x][y].setText("4");
                                            changed = true;
                                            break;
                            case "DIGIT5":
                            case "NUMPAD5": btn[x][y].setText("5");
                                            changed = true;
                                            break;
                            case "DIGIT6":
                            case "NUMPAD6": btn[x][y].setText("6");
                                            changed = true;
                                            break;
                            case "BACK_SPACE": btn[x][y].setText("");
                                            break;
                        }
                        result.setText("");
                    }
                });
            }
    }

    void GenerateSudoku(int[][] a)
    {
        int i, j, k, l, m, n, c, flag, done;
        do
        {
            done=1;
            for(i=0; i<6; i++)
                for(j=0; j<6; j++)
                    a[i][j]=0;
            for(i=0; i<6; i++)
            {
                for(j=0; j<6; j++)
                {
                    c=0;
                    do
                    {
                        c++;
                        a[i][j] = 1 + rand.nextInt(6);
                        flag=1;
                        for(k=0; k<j; k++)
                            if(a[i][j]==a[i][k])
                            {
                                flag=0;
                                break;
                            }
                        if(flag==1)
                        {
                            for(k=0; k<i; k++)
                                if(a[i][j]==a[k][j])
                                {
                                    flag=0;
                                    break;
                                }
                            if(i==0 || i==1)
                                m=0;
                            else if(i==2 || i==3)
                                m=2;
                            else
                                m=4;
                            if(j<3)
                                n=0;
                            else
                                n=3;
                            if(flag==1)
                            {
                                for(k=0; k<2; k++)
                                {
                                    if(flag==1)
                                    {
                                        for(l=0; l<3; l++)
                                            if((i!=m+k || j!=n+l) && (a[i][j] == a[m+k][n+l]))
                                            {
                                                flag=0;
                                                break;
                                            }
                                    }
                                }
                            }
                        }
                    }while(flag==0 && c<20);
                    if(flag==0 && i!=0)
                    {
                        done=0;
                        break;
                    }
                }
                if(done==0)
                    break;
            }
        }while(done==0);
    }

    void FillAtleastFour(int[][] a, int[][] b)
    {
        int rcb, b1, b2, m, n, b1i, b1j, b2i, b2j, i, j;
        rcb = rand.nextInt(3);
        switch(rcb)
        {
            case 0:	m = rand.nextInt(6);
                b1 = rand.nextInt(6);
                do
                {
                    b2 = rand.nextInt(6);
                }while(b1==b2);
                for(i=0;i<6;i++)
                    if(i!=b1 && i!=b2)
                        b[m][i] = a[m][i];
                break;
            case 1:	n = rand.nextInt(6);
                b1 = rand.nextInt(6);
                do
                {
                    b2 = rand.nextInt(6);
                }while(b1==b2);
                for(i=0;i<6;i++)
                    if(i!=b1 && i!=b2)
                        b[i][n] = a[i][n];
                break;
            case 2: m = 2 * (rand.nextInt(3));
                n = 3 * (rand.nextInt(2));
                b1i = m + rand.nextInt(2);
                b1j = n + rand.nextInt(3);
                do
                {
                    b2i = m + rand.nextInt(2);
                    b2j = n + rand.nextInt(3);
                }while(b1i==b2i && b1j==b2j);
                for(i=0; i<2; i++)
                    for(j=0; j<3; j++)
                        if(!((m+i==b1i && n+j==b1j) || (m+i==b2i && n+j==b2j)))
                            b[m+i][n+j] = a[m+i][n+j];
        }
    }

    int CheckWin(int[][] b)
    {
        int i, j, k, l, m, n;
        for(i=0; i<6; i++)
            for(j=0; j<6; j++)
                if(b[i][j]==0)
                    return(0);
        for(i=0; i<6; i++)
            for(j=0; j<6; j++)
                for(k=1; k<j; k++)
                    if(b[i][j]==b[i][k])
                        return(-1);
        for(j=0; j<6; j++)
            for(i=0; i<6; i++)
                for(k=1; k<i; k++)
                    if(b[i][j]==b[k][j])
                        return(-1);
        for(j=0; j<6; j++)
            for(i=0; i<6; i++)
            {
                if(i==0 || i==1)
                    m=0;
                else if(i==2 || i==3)
                    m=2;
                else
                    m=4;
                if(j<3)
                    n=0;
                else
                    n=3;
                for(k=m;k<m+2;k++)
                    for(l=n;l<n+3;l++)
                        if((i!=k || j!=l) && (b[i][j]==b[k][l]))
                            return(-1);
            }
        return(1);
    }

    void NewGame()
    {
    	newGameStage = new Stage();
        VBox newGameVBox = new VBox();
        newGameVBox.setId("top");
        Label newGameAsk = new Label("Are you sure to load new game?");
        newGameAsk.setId("bold");
        Button newGameYes = new Button("Yes");
        newGameYes.setId("filled");
        Button newGameNo = new Button("No");
        newGameNo.setId("no");

        newGameYes.setOnAction(e1 ->
        {
        	playNewGame();
        	newGameStage.close();
        });
        
        newGameNo.setOnAction(e1 -> newGameStage.close());
        
        newGameAsk.setPadding(new Insets(10, 0, 10, 0));
        VBox.setMargin(newGameNo, new Insets(5, 0, 0, 0));
        newGameVBox.setAlignment(Pos.TOP_CENTER);
        newGameVBox.getChildren().addAll(newGameAsk, newGameYes, newGameNo);
        newGameStage.setTitle("New Game");
        Scene scene = new Scene(newGameVBox, 235, 120);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        newGameStage.setAlwaysOnTop(true);
        newGameStage.setResizable(false);
        newGameStage.setScene(scene);
        newGameStage.show();
    }
    
    void Quit()
    {
    	quitStage = new Stage();
        VBox quitVBox = new VBox();
        quitVBox.setId("top");
        Label quitAsk = new Label("Are you sure to quit?");
        quitAsk.setPadding(new Insets(10, 0, 10, 0));
        quitAsk.setId("bold");
        Button quitYes = new Button("Yes");
        quitYes.setId("filled");
        Button quitNo = new Button("No");
        VBox.setMargin(quitNo, new Insets(5, 0, 0, 0));
        quitNo.setId("no");
        quitYes.setOnAction(e1 -> System.exit(0));
        quitNo.setOnAction(e1 -> quitStage.close());
        quitVBox.setAlignment(Pos.TOP_CENTER);
        quitVBox.getChildren().addAll(quitAsk, quitYes, quitNo);
        quitStage.setTitle("Quit");
        Scene scene = new Scene(quitVBox, 235, 120);
        scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        quitStage.setAlwaysOnTop(true);
        quitStage.setResizable(false);
        quitStage.setScene(scene);
        quitStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}

//Created by Nishant Chaudhary
//https://github.com/ChaudharyNishant
