package tile;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import main.GamePanel;

public class TileManager {
	
	GamePanel gp;
	Tile[] tiles;
	int mapTileIndex[][];
	
	public TileManager(GamePanel gp) {
		
		this.gp = gp;
		this.tiles = new Tile[10];
		this.mapTileIndex = new int[gp.maxScreenCol][gp.maxScreenRow];
		this.getTileImage();
		this.loadMap("/maps/tile-map.txt");
	}
	
	public void getTileImage() {
		try {
			
			this.tiles[0] = new Tile();
			this.tiles[0].image = ImageIO.read(getClass().getResourceAsStream("/tiles/grass.png"));
			
			this.tiles[1] = new Tile();
			this.tiles[1].image = ImageIO.read(getClass().getResourceAsStream("/tiles/wall.png"));
			
			this.tiles[2] = new Tile();
			this.tiles[2].image = ImageIO.read(getClass().getResourceAsStream("/tiles/water.png"));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void loadMap(String mapFilePath) {
		try {
			InputStream is = this.getClass().getResourceAsStream(mapFilePath);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			
			int col = 0;
			int row = 0;
			
			while (row < GamePanel.maxScreenRow) {
				String line = br.readLine();
				
				while (col < GamePanel.maxScreenCol) {
					String[] numbers = line.split(" ");
					
					int number = Integer.parseInt(numbers[col]);
					
					this.mapTileIndex[col][row] = number;
					col++;
				}
				
				row++;
				col = 0;
			}
			
			br.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void draw(Graphics2D g2) {
		
		int col = 0;
		int row = 0;
		int x = 0;
		int y = 0;
		
		while (col < GamePanel.maxScreenCol && row < GamePanel.maxScreenRow) {
			
			int tileNum = this.mapTileIndex[col][row];
			
			g2.drawImage(this.tiles[tileNum].image, x, y, GamePanel.tileSize, GamePanel.tileSize, null);
			
			x += GamePanel.tileSize;
			if (x == GamePanel.screenWidth) {
				x = 0;
				y += GamePanel.tileSize;
				row++;
				col = 0;
				continue;
			}
			
			col++;
		}
	}
}
