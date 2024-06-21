package gamestates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import entities.EnemyManager;
import entities.Player;
import levels.LevelManager;
import main.Game;
import ui.GameOverOverlay;
import ui.PauseOverlay;
import utils.LoadSave;
import static utils.Constants.Environment.*;

public class Playing extends State implements Statemethods {
    
    private Player player;
    private LevelManager levelManager;
    private EnemyManager enemyManager;
    private PauseOverlay pauseOverlay;
    private GameOverOverlay gameOverOverlay;
    private boolean paused = false;

    private int xLvlOffset;
    private int leftBorder =  (int)(0.3 * Game.GAME_WIDTH);
    private int rightBorder = (int)(0.6 * Game.GAME_WIDTH);
    private int lvlTilesWide = LoadSave.GetLevelData()[0].length;
    private int maxTilesOffset = lvlTilesWide - Game.TILES_IN_WIDTH;
    private int maxLvlOffsetX = maxTilesOffset * Game.TILES_SIZE;

    private BufferedImage backgroundImg, bigCloud, medCloud, smallCloud, grassLand, trees;
    // private int[] grassLandPos;
    // private Random rnd = new Random();

    private boolean gameOver;

	public Playing(Game game) {
        super(game);
        initClasses();

        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.SKY);

        bigCloud = LoadSave.GetSpriteAtlas(LoadSave.BIG_CLOUD);
        medCloud = LoadSave.GetSpriteAtlas(LoadSave.MED_CLOUD);
        smallCloud = LoadSave.GetSpriteAtlas(LoadSave.SMALL_CLOUD);

        grassLand = LoadSave.GetSpriteAtlas(LoadSave.LAND);
        trees = LoadSave.GetSpriteAtlas(LoadSave.TREES);

        // grassLandPos = new int[8];
        // for(int i = 0; i < grassLandPos.length; i++)
        //     grassLandPos[i] = (int)(90 * Game.SCALE) + rnd.nextInt((int)(100 * Game.SCALE));
    }

    private void initClasses() {
		levelManager = new LevelManager(game);
        enemyManager = new EnemyManager(this);
		player = new Player(200, 200, (int) (52 * Game.SCALE), (int) (48 * Game.SCALE), this);
		player.loadLvlData(levelManager.getCurrentLevel().getLevelData());
        pauseOverlay = new PauseOverlay(this);
        gameOverOverlay = new GameOverOverlay(this);
	}

    @Override
    public void update() {
        // Freeze when paused
        if(!paused && !gameOver) {
            levelManager.update();
            player.update();
            enemyManager.update(levelManager.getCurrentLevel().getLevelData(), player);
            checkCloseToBorder();
        } else {
            pauseOverlay.update();
        }
    }

    private void checkCloseToBorder() {
        int playerX = (int) player.getHitbox().x;
        int diff = playerX - xLvlOffset;

        if(diff > rightBorder)
            xLvlOffset += diff - rightBorder;
        else if(diff < leftBorder)
            xLvlOffset += diff - leftBorder;

        if(xLvlOffset > maxLvlOffsetX)
            xLvlOffset = maxLvlOffsetX;
        else if(xLvlOffset < 0)
            xLvlOffset = 0;
    }

    @Override
    public void draw(Graphics g) {
		g.drawImage(backgroundImg, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);

		drawBackground(g);

		levelManager.draw(g, xLvlOffset);
		player.render(g, xLvlOffset);
		enemyManager.draw(g, xLvlOffset);

		if (paused) {
			g.setColor(new Color(0, 0, 0, 150));
			g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
			pauseOverlay.draw(g);
		} else if (gameOver)
			gameOverOverlay.draw(g);
	}

    private void drawBackground(Graphics g) {
        // CLOUDS
        for(int i = 0; i < 3; i++) {
            g.drawImage(bigCloud, i * BIG_CLOUD_WIDTH - (int)(xLvlOffset * 0.1), (int)(28 * Game.SCALE), BIG_CLOUD_WIDTH, BIG_CLOUD_HEIGHT, null);
        }
        for(int i = 0; i < 6; i++) {
            g.drawImage(medCloud, i * MED_CLOUD_WIDTH - (int)(xLvlOffset * 0.15), (int)(28 * Game.SCALE), MED_CLOUD_WIDTH, MED_CLOUD_HEIGHT, null);
        }
        for(int i = 0; i < 9; i++) {
            g.drawImage(smallCloud, i * SMALL_CLOUD_WIDTH - (int)(xLvlOffset * 0.2), (int)(28 * Game.SCALE), SMALL_CLOUD_WIDTH, SMALL_CLOUD_HEIGHT, null);
        }

        // LAND
        for(int i = 0; i < 9; i++) {
            g.drawImage(grassLand, i * LAND_WIDTH - (int)(xLvlOffset * 0.3), (int)(90 * Game.SCALE), LAND_WIDTH, LAND_HEIGHT, null);
        }
        for(int i = 0; i < 9; i++) {
            g.drawImage(trees, i * TREE_WIDTH - (int)(xLvlOffset * 0.55), (int)(124 * Game.SCALE), TREE_WIDTH, TREE_HEIGHT, null);
        }
    }

    public void resetAll() {
		gameOver = false;
		paused = false;
		player.resetAll();
		enemyManager.resetAllEnemies();
	}

    public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}

	public void checkEnemyHit(Rectangle2D.Float attackBox) {
		enemyManager.checkEnemyHit(attackBox);
	}

    @Override
    public void mouseClicked(MouseEvent e) {
        if(!gameOver)
            if (e.getButton() == MouseEvent.BUTTON1)
                player.setAttacking(true);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(!gameOver)
            if(paused)
                pauseOverlay.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(!gameOver)
            if(paused)
                pauseOverlay.mouseReleased(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(!gameOver)
            if(paused)
                pauseOverlay.mouseMoved(e);
    }

    public void mouseDragged(MouseEvent e) {
        if(!gameOver)
            if(paused)
                pauseOverlay.mouseDragged(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(gameOver)
            gameOverOverlay.keyPressed(e);

        else
            switch (e.getKeyCode()) {
            case KeyEvent.VK_A:
                player.setLeft(true);
                break;
            case KeyEvent.VK_D:
                player.setRight(true);
                break;
            case KeyEvent.VK_SPACE:
                player.setJump(true);
                break;
            case KeyEvent.VK_ESCAPE:
                paused = !paused;
                break;
            }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (!gameOver)
			switch (e.getKeyCode()) {
			case KeyEvent.VK_A:
				player.setLeft(false);
				break;
			case KeyEvent.VK_D:
				player.setRight(false);
				break;
			case KeyEvent.VK_SPACE:
				player.setJump(false);
				break;
			}
    }

    public void unpauseGame() {
        paused = false;
    }

    public void windowFocusLost() {
		player.resetDirBooleans();
	}

	public Player getPlayer() {
		return player;
	}
}
