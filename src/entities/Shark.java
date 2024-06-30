package entities;

import static utils.Constants.EnemyConstants.*;
import static utils.Constants.EnemyConstants.GetSpriteAmount;
import static utils.HelpMethods.IsFloor;

import gamestates.Playing;

public class Shark extends Enemy {

	public Shark(float x, float y) {
		super(x, y, SHARK_WIDTH, SHARK_HEIGHT, SHARK);
		initHitbox(24, 31);
		initAttackBox(54, 31, 20);
	}

	public void update(int[][] lvlData, Playing playing) {
		updateBehavior(lvlData, playing);
		updateAnimationTick();
		updateAttackBoxFlip();
	}

	private void updateBehavior(int[][] lvlData, Playing playing) {
		if (firstUpdate)
			firstUpdateCheck(lvlData);

		if (inAir)
			inAirChecks(lvlData, playing);
		else {
			switch (state) {
			case IDLE:
				if (IsFloor(hitbox, lvlData))
					newState(RUNNING);
				else
					inAir = true;
				break;
			case RUNNING:
				if (canSeePlayer(lvlData, playing.getPlayer())) {
					turnTowardsPlayer(playing.getPlayer());
					if (isPlayerCloseForAttack(playing.getPlayer()))
						newState(ATTACK);
				}

				move(lvlData);
				break;
			case ATTACK:
				if (aniIndex == 0)
					attackChecked = false;
				if (aniIndex == 3 && !attackChecked)
					checkPlayerHit(attackBox, playing.getPlayer());
				break;
	 		case HIT:
				if (aniIndex <= GetSpriteAmount(enemyType, state) - 2)
					pushBack(pushBackDir, lvlData, 2f);
				updatePushBackDrawOffset();
				break;
			}
		}
	}

	// protected void attackMove(int[][] lvlData, Playing playing) {
	// 	float xSpeed = 0;

	// 	if (walkDir == LEFT)
	// 		xSpeed = -walkSpeed;
	// 	else
	// 		xSpeed = walkSpeed;

	// 	if (CanMoveHere(hitbox.x + xSpeed * 4, hitbox.y, hitbox.width, hitbox.height, lvlData))
	// 		if (IsFloor(hitbox, xSpeed * 4, lvlData)) {
	// 			hitbox.x += xSpeed * 4;
	// 			return;
	// 		}
	// 	newState(IDLE);
	// 	playing.addDialogue((int) hitbox.x, (int) hitbox.y, EXCLAMATION);
	// }
}