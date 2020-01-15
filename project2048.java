import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import java.util.Optional;
import java.util.ArrayList;

public class project2048 extends Application{
	private Integer score = 0;
	private Boolean winned = false;

	private FX2048 rect0 = new FX2048(0, 0, 0);
	private FX2048 rect1 = new FX2048(90, 0, 0);
	private FX2048 rect2 = new FX2048(180, 0, 0);
	private FX2048 rect3 = new FX2048(270, 0, 0);

	private FX2048 rect4 = new FX2048(0, 90, 0);
	private FX2048 rect5 = new FX2048(90, 90, 0);
	private FX2048 rect6 = new FX2048(180, 90, 0);
	private FX2048 rect7 = new FX2048(270, 90, 0);

	private FX2048 rect8 = new FX2048(0, 180, 0);
	private FX2048 rect9 = new FX2048(90, 180, 0);
	private FX2048 rect10 = new FX2048(180, 180, 0);
	private FX2048 rect11 = new FX2048(270, 180, 0);

	private FX2048 rect12 = new FX2048(0, 270, 0);
	private FX2048 rect13 = new FX2048(90, 270, 0);
	private FX2048 rect14 = new FX2048(180, 270, 0);
	private FX2048 rect15 = new FX2048(270, 270, 0);

	private FX2048 line0 [] = {rect0, this.rect4, this.rect8, this.rect12};
	private FX2048 line1 [] = {rect1, this.rect5, this.rect9, this.rect13};
	private FX2048 line2 [] = {rect2, this.rect6, this.rect10, this.rect14};
	private FX2048 line3 [] = {rect3, this.rect7, this.rect11, this.rect15};

	private FX2048 column0 [] = {rect0, this.rect1, this.rect2, this.rect3};
	private FX2048 column1 [] = {rect4, this.rect5, this.rect6, this.rect7};
	private FX2048 column2 [] = {rect8, this.rect9, this.rect10, this.rect11};
	private FX2048 column3 [] = {rect12, this.rect13, this.rect14, this.rect15};

	private FX2048 lines [][] = {line0, line1, line2, line3};
    private FX2048 columns [][] = {column0, column1, column2, column3};
    
    private ArrayList<FX2048> available = new ArrayList<FX2048>(16);
	private ArrayList<ArrayList<Integer>> undoMove = new ArrayList<ArrayList<Integer>>();
	private ArrayList<Integer> undoScore = new ArrayList<Integer>();


	public static void main(String[] args){
		launch(args);
	}
	
	@Override
  	public void start (Stage primaryStage){
		primaryStage.setTitle("2048");
		// primaryStage.getIcons().add(new Image("icon.png"));
		primaryStage.setMaxHeight(600);
		primaryStage.setMinHeight(600);
		primaryStage.setMaxWidth(600);
		primaryStage.setMinWidth(600);
		primaryStage.resizableProperty().setValue(false);

		Menu menuGame = new Menu("Game");
		MenuItem itemUndo = new MenuItem("Undo");
		MenuItem itemReset = new MenuItem("New game");
		MenuItem itemExit = new MenuItem("Exit");
		
		itemExit.setOnAction(event->{
			System.exit(0);
		});

		menuGame.getItems().addAll(itemUndo, itemReset, itemExit);
		MenuBar menu = new MenuBar(menuGame);
		menu.prefWidthProperty().bind(primaryStage.widthProperty());

		Label title = new Label("2048");
		title.setTextFill(Color.web("#383b53"));
		title.setFont(new Font(50));

		Label scoreLbl = new Label("Score : "+ String.valueOf(this.score));
		scoreLbl.setTextFill(Color.web("#383b53"));
		scoreLbl.setFont(new Font(15));

		Button undoBtn = new Button("Undo");
		undoBtn.setFocusTraversable(false);

        checkAvailable();
		while(this.available.size() > 14){
			this.available.get(rand2()).setValue(rand());
			checkAvailable();
		}

		Group root = new Group();
		Scene scene = new Scene(root, 500, 500, Color.WHITESMOKE);
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(35, 0, 0, 112));
		grid.add(title, 0, 0);

		GridPane grid2 = new GridPane();
		grid2.setPadding(new Insets(48, 0, 0, 385));
		grid2.add(scoreLbl, 1, 0);
		grid2.add(undoBtn, 1, 1);

		itemUndo.setOnAction(event->{
			undo();
			root.getChildren().clear();
			scoreLbl.setText("Score : " + String.valueOf(this.score));
			root.getChildren().addAll(grid, this.rect0.createRect(), this.rect1.createRect(), this.rect2.createRect(), this.rect3.createRect(), this.rect4.createRect(), this.rect5.createRect(), this.rect6.createRect(), this.rect7.createRect(), this.rect8.createRect(), this.rect9.createRect(), this.rect10.createRect(), this.rect11.createRect(), this.rect12.createRect(), this.rect13.createRect(), this.rect14.createRect(), this.rect15.createRect(), grid2, menu);
		});

		undoBtn.setOnAction(event->{
			undo();
			root.getChildren().clear();
			scoreLbl.setText("Score : " + String.valueOf(this.score));
			root.getChildren().addAll(grid, this.rect0.createRect(), this.rect1.createRect(), this.rect2.createRect(), this.rect3.createRect(), this.rect4.createRect(), this.rect5.createRect(), this.rect6.createRect(), this.rect7.createRect(), this.rect8.createRect(), this.rect9.createRect(), this.rect10.createRect(), this.rect11.createRect(), this.rect12.createRect(), this.rect13.createRect(), this.rect14.createRect(), this.rect15.createRect(), grid2, menu);
		});

		itemReset.setOnAction(event->{
			resetGame();
			scoreLbl.setText("Score : " + String.valueOf(this.score));
			root.getChildren().clear();
			root.getChildren().addAll(grid, this.rect0.createRect(), this.rect1.createRect(), this.rect2.createRect(), this.rect3.createRect(), this.rect4.createRect(), this.rect5.createRect(), this.rect6.createRect(), this.rect7.createRect(), this.rect8.createRect(), this.rect9.createRect(), this.rect10.createRect(), this.rect11.createRect(), this.rect12.createRect(), this.rect13.createRect(), this.rect14.createRect(), this.rect15.createRect(), grid2, menu);
		});
		
		scene.setOnKeyPressed(event->{
			switch (event.getCode()) {
				case UP:	
					//System.out.println("UP");
					if (moveUp()){
						checkAvailable();
						if (this.available.size() != 0)
							this.available.get(rand2()).setValue(rand());
						//System.out.println("Score: " + this.score);
					}
					scoreLbl.setText("Score : " + String.valueOf(this.score));
					root.getChildren().clear();
					root.getChildren().addAll(grid, this.rect0.createRect(), this.rect1.createRect(), this.rect2.createRect(), this.rect3.createRect(), this.rect4.createRect(), this.rect5.createRect(), this.rect6.createRect(), this.rect7.createRect(), this.rect8.createRect(), this.rect9.createRect(), this.rect10.createRect(), this.rect11.createRect(), this.rect12.createRect(), this.rect13.createRect(), this.rect14.createRect(), this.rect15.createRect(), grid2, menu);
					if (defeatCheck()){
						//System.out.println("Defeat");
						dialog(false);
					}
					if (!winned && winCheck()){
						//System.out.println("Win");
						dialog(true);
					}
					scoreLbl.setText("Score : " + String.valueOf(this.score));
					root.getChildren().clear();
					root.getChildren().addAll(grid, this.rect0.createRect(), this.rect1.createRect(), this.rect2.createRect(), this.rect3.createRect(), this.rect4.createRect(), this.rect5.createRect(), this.rect6.createRect(), this.rect7.createRect(), this.rect8.createRect(), this.rect9.createRect(), this.rect10.createRect(), this.rect11.createRect(), this.rect12.createRect(), this.rect13.createRect(), this.rect14.createRect(), this.rect15.createRect(), grid2, menu);
					break;

				case DOWN:	
					//System.out.println("DOWN");
					if (moveDown()){
						checkAvailable();
						if (this.available.size() != 0)
							this.available.get(rand2()).setValue(rand());
						//System.out.println("Score: " + this.score);
					}
					scoreLbl.setText("Score : " + String.valueOf(this.score));
					root.getChildren().clear();
					root.getChildren().addAll(grid, this.rect0.createRect(), this.rect1.createRect(), this.rect2.createRect(), this.rect3.createRect(), this.rect4.createRect(), this.rect5.createRect(), this.rect6.createRect(), this.rect7.createRect(), this.rect8.createRect(), this.rect9.createRect(), this.rect10.createRect(), this.rect11.createRect(), this.rect12.createRect(), this.rect13.createRect(), this.rect14.createRect(), this.rect15.createRect(), grid2, menu);
					if (defeatCheck()){
						//System.out.println("Defeat");
						dialog(false);
					}
					if (!winned && winCheck()){
						//System.out.println("Win");
						dialog(true);
					}
					scoreLbl.setText("Score : " + String.valueOf(this.score));
					root.getChildren().clear();
					root.getChildren().addAll(grid, this.rect0.createRect(), this.rect1.createRect(), this.rect2.createRect(), this.rect3.createRect(), this.rect4.createRect(), this.rect5.createRect(), this.rect6.createRect(), this.rect7.createRect(), this.rect8.createRect(), this.rect9.createRect(), this.rect10.createRect(), this.rect11.createRect(), this.rect12.createRect(), this.rect13.createRect(), this.rect14.createRect(), this.rect15.createRect(), grid2, menu);
					break;

				case RIGHT:	
					//System.out.println("RIGHT");
					if (moveRight()){
						checkAvailable();
						if (this.available.size() != 0)
							this.available.get(rand2()).setValue(rand());
						//System.out.println("Score: " + this.score);
					}
					scoreLbl.setText("Score : " + String.valueOf(this.score));
					root.getChildren().clear();
					root.getChildren().addAll(grid, this.rect0.createRect(), this.rect1.createRect(), this.rect2.createRect(), this.rect3.createRect(), this.rect4.createRect(), this.rect5.createRect(), this.rect6.createRect(), this.rect7.createRect(), this.rect8.createRect(), this.rect9.createRect(), this.rect10.createRect(), this.rect11.createRect(), this.rect12.createRect(), this.rect13.createRect(), this.rect14.createRect(), this.rect15.createRect(), grid2, menu);
					if (defeatCheck()){
						//System.out.println("Defeat");
						dialog(false);
					}
					if (!winned && winCheck()){
						//System.out.println("Win");
						dialog(true);
					}
					scoreLbl.setText("Score : " + String.valueOf(this.score));
					root.getChildren().clear();
					root.getChildren().addAll(grid, this.rect0.createRect(), this.rect1.createRect(), this.rect2.createRect(), this.rect3.createRect(), this.rect4.createRect(), this.rect5.createRect(), this.rect6.createRect(), this.rect7.createRect(), this.rect8.createRect(), this.rect9.createRect(), this.rect10.createRect(), this.rect11.createRect(), this.rect12.createRect(), this.rect13.createRect(), this.rect14.createRect(), this.rect15.createRect(), grid2, menu);
					break;
                    
				case LEFT:	
					//System.out.println("LEFT");
					if (moveLeft()){
						checkAvailable();
						if (this.available.size() != 0)
							this.available.get(rand2()).setValue(rand());
						//System.out.println("Score: " + this.score);
					}
					scoreLbl.setText("Score : " + String.valueOf(this.score));
					root.getChildren().clear();
					root.getChildren().addAll(grid, this.rect0.createRect(), this.rect1.createRect(), this.rect2.createRect(), this.rect3.createRect(), this.rect4.createRect(), this.rect5.createRect(), this.rect6.createRect(), this.rect7.createRect(), this.rect8.createRect(), this.rect9.createRect(), this.rect10.createRect(), this.rect11.createRect(), this.rect12.createRect(), this.rect13.createRect(), this.rect14.createRect(), this.rect15.createRect(), grid2, menu);
					if (defeatCheck()){
						//System.out.println("Defeat");
						dialog(false);
					}
					if (!winned && winCheck()){
						//System.out.println("Win");
						dialog(true);
					}
					scoreLbl.setText("Score : " + String.valueOf(this.score));
					root.getChildren().clear();
					root.getChildren().addAll(grid, this.rect0.createRect(), this.rect1.createRect(), this.rect2.createRect(), this.rect3.createRect(), this.rect4.createRect(), this.rect5.createRect(), this.rect6.createRect(), this.rect7.createRect(), this.rect8.createRect(), this.rect9.createRect(), this.rect10.createRect(), this.rect11.createRect(), this.rect12.createRect(), this.rect13.createRect(), this.rect14.createRect(), this.rect15.createRect(), grid2, menu);
					break;

				default:
					break;
			}
		});	

		root.getChildren().addAll(grid, this.rect0.createRect(), this.rect1.createRect(), this.rect2.createRect(), this.rect3.createRect(), this.rect4.createRect(), this.rect5.createRect(), this.rect6.createRect(), this.rect7.createRect(), this.rect8.createRect(), this.rect9.createRect(), this.rect10.createRect(), this.rect11.createRect(), this.rect12.createRect(), this.rect13.createRect(), this.rect14.createRect(), this.rect15.createRect(), grid2, menu);
		primaryStage.setScene(scene);
		primaryStage.show();
	  }
	  
	Integer rand(){
		return (int)((Math.random() * ((2 - 1) + 1)) + 1) * 2;
    }
      
	Integer rand2(){
        return (int)(Math.random() * this.available.size());
	}

	Boolean defeatCheck(){
		for(int i = 0; i < 4; ++i){
            for(int j = 0; j < 4; ++j){
                if (j < 3 && (this.lines[i][j].getValue() == this.lines[i][j + 1].getValue() || this.lines[i][j + 1].getValue() == 0))
                    return false;
                if (i < 3 && (this.lines[i][j].getValue() == this.lines[i + 1][j].getValue() || this.lines[i + 1][j].getValue() == 0))
                    return false;
                if (i > 0 && (this.lines[i][j].getValue() == this.lines[i - 1][j].getValue() || this.lines[i - 1][j].getValue() == 0))
                    return false;
                if (j > 0 && (this.lines[i][j].getValue() == this.lines[i][j - 1].getValue() || this.lines[i][j - 1].getValue() == 0))
                    return false;
            }
        }
		return true;
	}
	
	Boolean winCheck(){
		for(int i = 0; i < 4; ++i){
            for(int j = 0; j < 4; ++j){
                if (lines[i][j].getValue() ==  2048){
                    return true;
                }
            }        
		}
		return false;
	}
      
    void checkAvailable(){
        this.available.clear();
        for(int i = 0; i < 4; ++i){
            for(int j = 0; j < 4; ++j){
                if (lines[i][j].getValue() ==  0){
                    this.available.add(lines[i][j]);
                }
            }        
		}
	}

	boolean moveUp(){
		int l0 = 0, l1 = 0, l2 = 0;
		boolean g = false;
		for(int i = 0; i < 4; ++i){
			for(int j = 0; j < 3; ++j){
				if(this.line0[i].getValue() == 0){
					saveValues(!g);
					this.line0[i].setValue(this.line1[i].getValue());
					this.line1[i].setValue(this.line2[i].getValue());
					this.line2[i].setValue(this.line3[i].getValue());
					this.line3[i].setValue(0);
					if (this.line0[i].getValue() != 0 || this.line1[i].getValue() != 0 || this.line2[i].getValue() != 0)
						g = true;
					if(!g){
						this.undoMove.remove(this.undoMove.size() - 1);
						this.undoScore.remove(this.undoScore.size() - 1);
					}	
				}
				if(this.line1[i].getValue() == 0){
					saveValues(!g);
					this.line1[i].setValue(this.line2[i].getValue());
					this.line2[i].setValue(this.line3[i].getValue());
					this.line3[i].setValue(0);
					if (this.line1[i].getValue() != 0 || this.line2[i].getValue() != 0)
						g = true;
					if(!g){
						this.undoMove.remove(this.undoMove.size() - 1);
						this.undoScore.remove(this.undoScore.size() - 1);
					}	
				}
				if(this.line2[i].getValue() == 0){
					saveValues(!g);
					this.line2[i].setValue(this.line3[i].getValue());
					this.line3[i].setValue(0);
					if (this.line2[i].getValue() != 0)
						g = true;
					if(!g){
						this.undoMove.remove(this.undoMove.size() - 1);
						this.undoScore.remove(this.undoScore.size() - 1);
					}	
				}

				if(l0 == 0 && this.line0[i].getValue() != 0 && this.line1[i].getValue() != 0 && this.line0[i].getValue() == this.line1[i].getValue()){
					saveValues(!g);
					this.line0[i].setValue(this.line0[i].getValue() + this.line1[i].getValue());
					this.line1[i].setValue(this.line2[i].getValue());
					this.line2[i].setValue(this.line3[i].getValue());
					this.line3[i].setValue(0);
					this.score += this.line0[i].getValue();
					l0 = 1;
					g = true;
				}
				if(l1 == 0 && this.line1[i].getValue() != 0 && this.line2[i].getValue() != 0 && this.line1[i].getValue() == this.line2[i].getValue()){
					saveValues(!g);
					this.line1[i].setValue(this.line1[i].getValue() + this.line2[i].getValue());
					this.line2[i].setValue(this.line3[i].getValue());
					this.line3[i].setValue(0);
					this.score += this.line1[i].getValue();
					l0 = 1;
					l1 = 1;
					g = true;
				}
				if(l2 == 0 && this.line2[i].getValue() != 0 && this.line3[i].getValue() != 0 && this.line2[i].getValue() == this.line3[i].getValue()){
					saveValues(!g);
					this.line2[i].setValue(this.line2[i].getValue() + this.line3[i].getValue());
					this.line3[i].setValue(0);
					this.score += this.line2[i].getValue();
					l0 = 1;
					l1 = 1;
					l2 = 1;
					g = true;
				}
			}
			l0 = 0;
			l1 = 0;
			l2 = 0;
		}
		return g;
	}

	boolean moveDown(){
		int l3 = 0, l2 = 0, l1 = 0;
		boolean g = false;
		for(int i = 0; i < 4; ++i){
			for (int j = 0; j < 3; ++j){
				if(this.line3[i].getValue() == 0){
					saveValues(!g);
					this.line3[i].setValue(this.line2[i].getValue());
					this.line2[i].setValue(this.line1[i].getValue());
					this.line1[i].setValue(this.line0[i].getValue());
					this.line0[i].setValue(0);	
					if (this.line3[i].getValue() != 0 || this.line2[i].getValue() != 0 || this.line1[i].getValue() != 0)
						g = true;
					if(!g){
						this.undoMove.remove(this.undoMove.size() - 1);
						this.undoScore.remove(this.undoScore.size() - 1);
					}	
				}
				if(this.line2[i].getValue() == 0){
					saveValues(!g);
					this.line2[i].setValue(this.line1[i].getValue());
					this.line1[i].setValue(this.line0[i].getValue());
					this.line0[i].setValue(0);	
					if (this.line2[i].getValue() != 0 || this.line1[i].getValue() != 0)
						g = true;		
					if(!g){
						this.undoMove.remove(this.undoMove.size() - 1);
						this.undoScore.remove(this.undoScore.size() - 1);
					}	
				}
				if(this.line1[i].getValue() == 0){
					saveValues(!g);
					this.line1[i].setValue(this.line0[i].getValue());
					this.line0[i].setValue(0);
					if (this.line1[i].getValue() != 0)
						g = true;
					if(!g){
						this.undoMove.remove(this.undoMove.size() - 1);
						this.undoScore.remove(this.undoScore.size() - 1);
					}	
				}
					
				if(l3 == 0 && this.line3[i].getValue() != 0 && this.line2[i].getValue() != 0 && this.line3[i].getValue() == this.line2[i].getValue()){
					saveValues(!g);
					this.line3[i].setValue(this.line3[i].getValue() + this.line2[i].getValue());
					this.line2[i].setValue(this.line1[i].getValue());
					this.line1[i].setValue(this.line0[i].getValue());
					this.line0[i].setValue(0);
					this.score += this.line3[i].getValue();
					l3 = 1;
					g = true;
				}
				if(l2 == 0 && this.line2[i].getValue() != 0 && this.line1[i].getValue() != 0 && this.line2[i].getValue() == this.line1[i].getValue()){
					saveValues(!g);
					this.line2[i].setValue(this.line2[i].getValue() + this.line1[i].getValue());
					this.line1[i].setValue(this.line0[i].getValue());
					this.line0[i].setValue(0);
					this.score += this.line2[i].getValue();
					l3 = 1;
					l2 = 1;
					g = true;
				}
				if(l1 == 0 && this.line1[i].getValue() != 0 && this.line0[i].getValue() != 0 && this.line1[i].getValue() == this.line0[i].getValue()){
					saveValues(!g);
					this.line1[i].setValue(this.line1[i].getValue() + this.line0[i].getValue());
					this.line0[i].setValue(0);
					this.score += this.line1[i].getValue();
					l3 = 1;
					l2 = 1;
					l1 = 1;
					g = true;
				}
			}
			l3 = 0;
			l2 = 0;
			l1 = 0;
		}
		return g;
	}

	boolean moveLeft(){
		int c0 = 0, c1 = 0, c2 = 0;
		boolean g = false;
		for(int i = 0; i < 4; ++i){
			for (int j = 0; j < 3; ++j){
				if(this.column0[i].getValue() == 0){
					saveValues(!g);
					this.column0[i].setValue(this.column1[i].getValue());
					this.column1[i].setValue(this.column2[i].getValue());
					this.column2[i].setValue(this.column3[i].getValue());
					this.column3[i].setValue(0);
					if (this.column0[i].getValue() != 0 || this.column1[i].getValue() != 0 || this.column2[i].getValue() != 0)
						g = true;
					if(!g){
						this.undoMove.remove(this.undoMove.size() - 1);
						this.undoScore.remove(this.undoScore.size() - 1);
					}	
				}
				if(this.column1[i].getValue() == 0){
					saveValues(!g);
					this.column1[i].setValue(this.column2[i].getValue());
					this.column2[i].setValue(this.column3[i].getValue());
					this.column3[i].setValue(0);	
					if (this.column1[i].getValue() != 0 || this.column2[i].getValue() != 0)
						g = true;			
					if(!g){
						this.undoMove.remove(this.undoMove.size() - 1);
						this.undoScore.remove(this.undoScore.size() - 1);
					}	
				}
				if(this.column2[i].getValue() == 0){
					saveValues(!g);
					this.column2[i].setValue(this.column3[i].getValue());
					this.column3[i].setValue(0);	
					if (this.column2[i].getValue() != 0)
						g = true;			
					if(!g){
						this.undoMove.remove(this.undoMove.size() - 1);
						this.undoScore.remove(this.undoScore.size() - 1);
					}	
				}

				if(c0 == 0 && this.column0[i].getValue() != 0 && this.column1[i].getValue() != 0 && this.column0[i].getValue() == this.column1[i].getValue()){
					saveValues(!g);
					this.column0[i].setValue(this.column0[i].getValue() + this.column1[i].getValue());
					this.column1[i].setValue(this.column2[i].getValue());
					this.column2[i].setValue(this.column3[i].getValue());
					this.column3[i].setValue(0);
					this.score += this.column0[i].getValue();
					c0 = 1;
					g = true;
				}
				if(c1 == 0 && this.column1[i].getValue() != 0 && this.column2[i].getValue() != 0&& this.column1[i].getValue() == this.column2[i].getValue()){
					saveValues(!g);
					this.column1[i].setValue(this.column1[i].getValue() + this.column2[i].getValue());
					this.column2[i].setValue(this.column3[i].getValue());
					this.column3[i].setValue(0);
					this.score += this.column1[i].getValue();
					c0 = 1;
					c1 = 1;
					g = true;
				}
				if(c2 == 0 && this.column2[i].getValue() != 0 && this.column3[i].getValue() != 0 && this.column2[i].getValue() == this.column3[i].getValue()){
					saveValues(!g);
					this.column2[i].setValue(this.column2[i].getValue() + this.column3[i].getValue());
					this.column3[i].setValue(0);
					this.score += this.column2[i].getValue();
					c0 = 1;
					c1 = 1;
					c2 = 1;
					g = true;
				}
			}
			c0 = 0;
			c1 = 0;
			c2 = 0;
		}
		return g;
	}

	boolean moveRight(){
		int c3 = 0, c2 = 0, c1 = 0;
		boolean g = false;
		for(int i = 0; i < 4; ++i){
			for (int j = 0; j < 3; ++j){
				if(this.column3[i].getValue() == 0){
					saveValues(!g);
					this.column3[i].setValue(this.column2[i].getValue());
					this.column2[i].setValue(this.column1[i].getValue());
					this.column1[i].setValue(this.column0[i].getValue());
					this.column0[i].setValue(0);
					if (this.column3[i].getValue() != 0 || this.column2[i].getValue() != 0 || this.column1[i].getValue() != 0)
						g = true;		
					if(!g){
						this.undoMove.remove(this.undoMove.size() - 1);
						this.undoScore.remove(this.undoScore.size() - 1);
					}	
				}
				if(this.column2[i].getValue() == 0){
					saveValues(!g);
					this.column2[i].setValue(this.column1[i].getValue());
					this.column1[i].setValue(this.column0[i].getValue());
					this.column0[i].setValue(0);
					if (this.column2[i].getValue() != 0 || this.column1[i].getValue() != 0)
						g = true;			
					if(!g){
						this.undoMove.remove(this.undoMove.size() - 1);
						this.undoScore.remove(this.undoScore.size() - 1);
					}	
				}
				if(this.column1[i].getValue() == 0){
					saveValues(!g);
					this.column1[i].setValue(this.column0[i].getValue());
					this.column0[i].setValue(0);	
					if (this.column1[i].getValue() != 0)
						g = true;				
					if(!g){
						this.undoMove.remove(this.undoMove.size() - 1);
						this.undoScore.remove(this.undoScore.size() - 1);
					}	
				}

				if(c3 == 0 && this.column3[i].getValue() != 0 && this.column2[i].getValue() != 0 && this.column3[i].getValue() == this.column2[i].getValue()){
					saveValues(!g);
					this.column3[i].setValue(this.column3[i].getValue() + this.column2[i].getValue());
					this.column2[i].setValue(this.column1[i].getValue());
					this.column1[i].setValue(this.column0[i].getValue());
					this.column0[i].setValue(0);
					this.score += this.column3[i].getValue();
					c3 = 1;
					g = true;
				}
				if(c2 == 0 && this.column2[i].getValue() != 0 && this.column1[i].getValue() != 0 && this.column2[i].getValue() == this.column1[i].getValue()){
					saveValues(!g);
					this.column2[i].setValue(this.column2[i].getValue() + this.column1[i].getValue());
					this.column1[i].setValue(this.column0[i].getValue());
					this.column0[i].setValue(0);
					this.score += this.column2[i].getValue();
					c3 = 1;
					c2 = 1;
					g = true;
				}
				if(c1 == 0 && this.column1[i].getValue() != 0 && this.column0[i].getValue() != 0 && this.column1[i].getValue() == this.column0[i].getValue()){
					saveValues(!g);
					this.column1[i].setValue(this.column1[i].getValue() + this.column0[i].getValue());
					this.column0[i].setValue(0);
					this.score += this.column1[i].getValue();
					c3 = 1;
					c2 = 1;
					c1 = 1;
					g = true;
				}
			}
			c3 = 0;
			c2 = 0;
			c1 = 0;
		}
		return g;
	}

	void resetGame(){
		this.score = 0;
		this.winned = false;
		for(int i = 0; i < 4; ++i){
			for(int j = 0; j < 4; ++j){
				this.lines[i][j].setValue(0);
			}
		}
		checkAvailable();
		this.available.get(rand2()).setValue(rand());
		this.available.get(rand2()).setValue(rand());
		checkAvailable();
		this.undoMove.clear();
		this.undoScore.clear();
		saveValues(true);
	}

	void undo(){
		if (this.undoMove.size() > 0){
			this.score = this.undoScore.get(this.undoScore.size() - 1);
			this.undoScore.remove(this.undoScore.size() - 1);
			int k = 0;
			for(int i = 0; i < 4; ++i){
				for(int j = 0; j < 4; ++j, ++k){
					this.lines[i][j].setValue(this.undoMove.get(this.undoMove.size() - 1).get(k));
				}
			}
			this.undoMove.remove(this.undoMove.size() - 1);
		}
		if (winned && !winCheck()){
			this.winned = false;
		}
	}

	void saveValues(boolean save){
		if(save){
			int k = 0;
			ArrayList<Integer> newUndo = new ArrayList<Integer>(16);
			for(int i = 0; i < 4; ++i){
				for(int j = 0; j < 4; ++j, ++k){
					newUndo.add(k, this.lines[i][j].getValue());
				}
			}
			this.undoMove.add(newUndo);
			this.undoScore.add(this.score);
		}
	}

	void dialog(boolean win){
		Alert alert = new Alert(AlertType.NONE);
		alert.setTitle("2048");
		ButtonType newGameBtn = new ButtonType("New Game");
		if (win){
			alert.setContentText("You win !");
			//alert.setContentText("What do you want to do ?");

			ButtonType ctnBtn = new ButtonType("Continue");

			alert.getButtonTypes().setAll(ctnBtn, newGameBtn);
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == newGameBtn){
				resetGame();
			}else{
				this.winned = true;
				return;
			}
		}else{
			alert.setContentText("Game Over !");
			//alert.setContentText("What do you want to do ?");

			ButtonType undoBtn = new ButtonType("Undo");

			alert.getButtonTypes().setAll(undoBtn, newGameBtn);
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == newGameBtn){
				resetGame();
			}else{
				undo();
			}
		}

	}
}
