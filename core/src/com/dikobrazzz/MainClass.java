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
		sizeCircle1 = 300.0f;
		sizeCircle2 = 200.0f;
		collision = false;
		circle1 = new Circle(MathUtils.random(0, dSizeX - sizeCircle1), MathUtils.random(0, dSizeY - sizeCircle1), sizeCircle1, 600.0f, 600.0f);
		circle2 = new Circle(MathUtils.random(0, dSizeX - sizeCircle2), MathUtils.random(0, dSizeY - sizeCircle2), sizeCircle2, 700.0f, 800.0f);
	}

	public void update (float dt){
		circle1.setCircleShape(circle1.getPosition().x + sizeCircle1/2, circle1.getPosition().y + sizeCircle1/2, sizeCircle1/2);
		circle2.setCircleShape(circle2.getPosition().x+ sizeCircle2/2, circle2.getPosition().y+ sizeCircle2/2,sizeCircle2/2);
        if (Intersector.overlaps(circle1.getCircleShape(),circle2.getCircleShape())){
            collision = true;
        } else {
        	collision = false;
		}
		circle1.update(dt, circle1.position, circle2.position);
		circle2.update(dt, circle2.position, circle1.position);
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
		private com.badlogic.gdx.math.Circle circleShape;

		public com.badlogic.gdx.math.Circle getCircleShape() {
			return circleShape;
		}

		public void setCircleShape(float x, float y, float radius) {
			circleShape.set(x, y, radius);
		}

		public Circle (float x, float y, float sizze, float speeedX, float speeedY) {
			texture = new Texture("circle.png");
			position = new Vector2(x, y);
			speedX = speeedX;
			speedY = speeedY;
			velosity = new Vector2(speedX, speedY);
			size = sizze;
			turnX = false;
			turnY = false;
			this.circleShape = new com.badlogic.gdx.math.Circle(x + sizze/2, y + sizze/2, sizze/2);
		}

		public void render (SpriteBatch batch){
			batch.draw(texture, position.x, position.y, size, size);
		}

		public void update (float dt, Vector2 myPos, Vector2 otherPos){
			collision(dt, myPos, otherPos);
		}

		void collision (float dt, Vector2 myPos, Vector2 otherPos){
			if (!turnX){
				position.x += velosity.x * dt;
			} else {
				position.x -= velosity.x * dt;
			}
			if (position.x > dSizeX - size || (collision && myPos.x < otherPos.x)){
				turnX = true;
			}
			if (position.x < 0 || (collision && myPos.x > otherPos.x)){
				turnX = false;
			}
			if (!turnY){
				position.y += velosity.y * dt;
			} else {
				position.y -= velosity.y * dt;
			}
			if (position.y < 0 || (collision && myPos.y > otherPos.y)){
				turnY = false;
			}
			if (position.y > dSizeY - size || (collision && myPos.y < otherPos.y)){
				turnY = true;
			}
		}

		Vector2 getPosition(){
			return position;
		}

	}
}
