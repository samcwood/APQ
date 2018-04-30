import java.io.File;

import javafx.application.Application;
import javafx.collections.MapChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Playlist extends Application {
	public static void main(String[] args) {
		launch();
	}
	
	Queue Playlist = new Queue(10);
	MediaPlayer Song[] = new MediaPlayer[10];
	MediaPlayer currentSong;
	int count = 0;

	public void start(Stage primaryStage) throws Exception {
		
		FileChooser fileChooser = new FileChooser();
		
		//Create and position buttons, images, and labels
		Button btnAdd = new Button("Add");
		Button btnDequeue = new Button("Skip");
		Image PlaySymbol = new Image("http://cdn.onlinewebfonts.com/svg/img_404212.png");
		Image PauseSymbol = new Image("https://upload.wikimedia.org/wikipedia/commons/thumb/e/e3/Ic_pause_circle_outline_48px.svg/1024px-Ic_pause_circle_outline_48px.svg.png");
		ImageView play = new ImageView(PlaySymbol);
		ImageView pause = new ImageView(PauseSymbol);
		Label album = new Label("Album:");
		Label artist = new Label("Artist:");
		Label title = new Label("Title:");
		Rectangle r1 = new Rectangle();
		Rectangle r2 = new Rectangle();
		
		//Set sizes and position of buttons and images
		btnAdd.setPrefSize(40, 30);
		btnAdd.setLayoutX(5);
		btnAdd.setLayoutY(225);
		btnDequeue.setPrefSize(50, 30);
		btnDequeue.setLayoutX(55);
		btnDequeue.setLayoutY(225);
		btnDequeue.setDisable(true);
		play.setPreserveRatio(true);
		play.setDisable(true);
		play.setFitHeight(70);
		play.setLayoutX(105);
		play.setLayoutY(40);
		pause.setPreserveRatio(true);
		pause.setFitHeight(90);
		pause.setLayoutX(95);
		pause.setLayoutY(30);
		title.setLayoutX(5);
		title.setLayoutY(150);
		album.setLayoutX(5);
		album.setLayoutY(170);
		artist.setLayoutX(5);
		artist.setLayoutY(190);
		r1.setHeight(10);
		r1.setWidth(300);
		r1.setLayoutX(0);
		r1.setLayoutY(210);
		r1.setFill(Color.LIGHTCORAL);
		r1.setStroke(Color.PALEGOLDENROD);
		r2.setHeight(10);
		r2.setWidth(300);
		r2.setLayoutX(0);
		r2.setLayoutY(137);
		r2.setFill(Color.LIGHTCORAL);
		r2.setStroke(Color.PALEGOLDENROD);
		
		//Create pane and add buttons and images to pane
		Pane pane = new Pane();
		pane.getChildren().addAll(btnAdd, btnDequeue, play, album, artist, title, r1, r2);
		
		//Create scene and add it to stage
		Scene s = new Scene(pane);
		primaryStage.setScene(s);
		primaryStage.setTitle("Audio Playlist Queue");
		primaryStage.setHeight(290);
		primaryStage.setWidth(300);
		primaryStage.setResizable(false);
		primaryStage.show();
		
		//Create btnAdd event
		btnAdd.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent e) {
				
				try {
					File file = fileChooser.showOpenDialog(primaryStage);
					Media media = new Media(file.toURI().toString());
					
					//Fetches metadata from songs
					media.getMetadata().addListener((MapChangeListener<String, Object>) change -> {
		                title.setText("Title: " + (String)media.getMetadata().get("title"));
		                artist.setText("Artist: " + (String)media.getMetadata().get("artist"));
		                album.setText("Album: " + (String)media.getMetadata().get("album"));
					});
					
					if (Playlist.length >= 1) {
						if (Playlist.length < 10) {
			                Playlist.enqueue(file);
			                Playlist.print();
			                Song[Playlist.rear] = new MediaPlayer(media);
			                btnAdd.setDisable(false);
						} else
							System.out.println("Playlist is full. Cannot add songs.");
					}
					
					else {
		                Playlist.enqueue(file);
		                Playlist.print();
		                currentSong = new MediaPlayer(media);
		           		play.setDisable(false);
		           		btnDequeue.setDisable(false);
		           		}
			} 
				catch (NullPointerException ex) {
					System.out.println("No file selected.");
				}
				
				catch (MediaException ex) {
					System.out.println("File type not supported.");
				}
			}
		});
		
		//Create btnDequeue event
		btnDequeue.setOnAction(new EventHandler<ActionEvent>() {

			public void handle(ActionEvent e) {
				currentSong.stop();
				Playlist.dequeue();
				Playlist.print();
				
				if(Playlist.length > 0 && pane.getChildren().contains(play)) {
					count = 1;
					pane.getChildren().remove(play);
					pane.getChildren().add(pause);
					btnDequeue.setDisable(true);
					currentSong.setOnStopped(new Runnable() {
	
						public void run() {
							currentSong = Song[Playlist.front];
							currentSong.play();
						}
					});
				}
				
				if (Playlist.isEmpty()) {
					count = 0;
					play.setDisable(true);
					btnDequeue.setDisable(true);
					title.setText("Title:");
					album.setText("Album:");
					artist.setText("Artist:");
				}
			}
		});
		
		//Plays currently playing song
		play.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
			currentSong.play();
			btnDequeue.setDisable(true);
			
			if (count % 2 == 0) {
				count++;
				pane.getChildren().remove(play);
				pane.getChildren().add(pause);
				
				if (Playlist.length > 1) {
					currentSong.setOnEndOfMedia(new Runnable() {

						public void run() {
							Playlist.dequeue();
							currentSong = Song[Playlist.front];
							currentSong.play();
							}
						}
					);
				}
			}
		});
		
		//Pauses currently playing song
		pause.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
			btnDequeue.setDisable(false);
			
			if (count % 2 != 0) {
				count--;
				pane.getChildren().remove(pause);
				pane.getChildren().add(play);
				currentSong.pause();
			}
		});
	}	
}
