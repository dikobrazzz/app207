package com.dikobrazzz;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class MainClass extends ApplicationAdapter {
	float dSizeY;
	float dSizeX;
	float sizeCircle1;
	float sizeCircle2;
	private SpriteBatch batch;
	private Circle circle1;
	private Circle circle2;
	private boolean collision;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		dSizeY = Gdx.graphics.getHeight();
		dSizeX = Gdx.graphics.getWidth();
		collision = false;
		sizeCircle1 = 300.0f;
		sizeCircle2 = 200.0f;
		circle1 = new Circle(MathUtils.random(0, dSizeX - sizeCircle1), MathUtils.random(0, dSizeY - sizeCircle1), sizeCircle1, 300.0f, 300.0f);
		circle2 = new Circle(MathUtils.random(0, dSizeX - sizeCircle2), MathUtils.random(0, dSizeY - sizeCircle2), sizeCircle2, 400.0f, 500.0f);
	}

	public void update (float dt){
		if (Intersector.overlaps(new com.badlogic.gdx.math.Circle(circle1.getPosition().x, circle1.getPosition().y, sizeCircle1/2),
				new com.badlogic.gdx.math.Circle(circle2.getPosition().x, circle2.getPosition().y,sizeCircle2/2))){
			collision = true;
		}
		circle1.update(dt);
		circle2.update(dt);
	}

	@Override
	public void render () {
		float dt = Gdx.graphics.getDeltaTime();
		update(dt);
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		circle1.render(batch);
		circle2.render(batch);
		batch.end();
	}

	class Circle {
		private Texture texture;
		private Vector2 position;
		private Vector2 velosity;
		private boolean turnX;
		private boolean turnY;
		private float size;
		private float speedX;
		private float speedY;

		public Circle (float x, float y, float sizze, float speeedX, float speeedY) {
			texture = new Texture("circle.png");
			position = new Vector2(x, y);
			speedX = speeedX;
			speedY = speeedY;
			velosity = new Vector2(speedX, speedY);
			size = sizze;
			turnX = false;
		}

		public void render (SpriteBatch batch){
			batch.draw(texture, position.x, position.y, size, size);
		}

		public void update (float dt){
			collision(dt);
		}

		void collision (float dt){
			if (!turnX){
				position.x += velosity.x * dt;
			} else {
				position.x -= velosity.x * dt;
			}
			if (position.x > dSizeX - size){
				turnX = true;
			}
			if (position.x < 0){
				turnX = false;
			}
			if (!turnY){
				if (!collision){
					position.y += velosity.y * dt;
				} else position.y -= velosity.y * dt;
			} else {
				if(!collision){
					position.y -= velosity.y * dt;
				} else {
					position.y += velosity.y * dt;
				}
			}
			if (position.y < 0){
				turnY = false;
				collision = false;
			}
			if (position.y > dSizeY - size){
				turnY = true;
				collision = false;
			}

		}

		Vector2 getPosition(){
			return position;
		}

	}
}
