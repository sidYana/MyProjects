package com.game.flappyBird;

import processing.core.PApplet;

public class Pipe {

	public float top;
	public float bottom;
	public float pipeWidth = 20f;

	public float posX;
	public float width;
	public float height;

	public float speed = 3f;

	public boolean highlight = false;
	public boolean isOffScreen = false;

	public float pipeSeparation = 100;
	
	public Pipe(PApplet context, float width, float height) {

		this.width = width;
		this.height = height;

		this.bottom = context.random(0, height - pipeSeparation);
		this.top = height - bottom - pipeSeparation;
		this.posX = width;

	}

	public void show(PApplet context) {
		if(!highlight) {
			context.fill(255);
		}else {
			context.fill(255,0,0);
		}
		context.rect(posX, 0, pipeWidth, top);
		context.rect(posX, height - bottom, pipeWidth, bottom);
	}

	public boolean hits(PlayerCreature player) {

		if (player.posY < top || player.posY > height - bottom) {
			if (player.posX > posX && player.posX < posX + pipeWidth+5) {
				highlight = true;
				return true;
			}
		}
		return false;
	}

	public void update() {

		posX -= speed;

		if (posX < -pipeWidth) {
			isOffScreen = true;
		}

	}

	public void showClosestPipe(PApplet context) {
		context.fill(0,255,0);
		context.rect(posX, 0, pipeWidth, top);
		context.rect(posX, height - bottom, pipeWidth, bottom);
		context.fill(255,0,0);
	}
	
}
