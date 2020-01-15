import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.layout.StackPane;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class FX2048 extends Rectangle{
	private Integer size_ = 80;
	private Integer X, Y;
	private Integer value_;
	private Text txt_;
	private Rectangle rect_;

	public FX2048(){}
	public FX2048(int nX, int nY, int val){
		this.X = nX;
		this.Y = nY;
		this.value_ = val;
	}

	public void setValue(int x){
		this.value_ = x;
		if (this.value_ == 0){
			this.txt_ = new Text(" ");
		}else{
			this.txt_ = new Text(String.valueOf(this.value_));
		}
		this.rect_ = new Rectangle(this.size_, this.size_, Color.web(setColor(this.value_)));
	}

	public int getValue(){
		return this.value_;
	}

	private String setColor(int value){
		switch (value) {
			case 0:
				return "#bdbdc8";
			case 2:
				return "#5d6289";
			case 4:
				return "#535779";
			case 8:
				return "#484c6a";
			case 16:
				return "#383b53";
			case 32:
				return "#34364c";
			case 64:
				return "#292c3d";
			case 128:
				return "#33293d";
			case 256:
				return "#40334d";
			case 512:
				return "#4d3d5c";
			case 1024:
				return "#66527a";
			case 2048:
				return "#735c8a";
			default:
				return "#895587";
		}
	}

	public StackPane createRect(){
		if (this.value_ == 0){
			this.txt_ = new Text(" ");
		}else{
			this.txt_ = new Text(String.valueOf(this.value_));
		}
		this.rect_ = new Rectangle(this.size_, this.size_, Color.web(setColor(this.value_)));
		this.txt_.setFill(Color.WHITE);
		this.txt_.setFont(new Font(35));

		StackPane stack = new StackPane();
		stack.setPadding(new Insets(115 + this.X, 10, 10, 115 + this.Y));
		stack.getChildren().addAll(this.rect_, this.txt_);

		return stack;
	}
}
