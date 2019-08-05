package com.dikobrazzz;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import sun.rmi.runtime.Log;

public class MainClass extends ApplicationAdapter {
	float dSizeY;
	float dSizeX;
	float sizeCircle1;
	float sizeCircle2;
	float weightCircle1;
	float weightCircle2;
	float speedCircle1;
	float speedCircle2;
	private SpriteBatch batch;
	private Circle circle1;
	private Circle circle2;
	private boolean collision;

	@Override
	public void create () {
		batch = new SpriteBatch();
		dSizeY = Gdx.graphics.getHeight();
		dSizeX = Gdx.graphics.getWidth();
		sizeCircle1 = 400.0f;
		sizeCircle2 = 200.0f;
		speedCircle1 = 500;
		speedCircle2 = -300;
		weightCircle1 = 6;
		weightCircle2 = 2;
		collision = false;
		circle1 = new Circle(MathUtils.random(0, dSizeX - sizeCircle1), MathUtils.random(0, dSizeY - sizeCircle1), sizeCircle1, speedCircle1);
		circle2 = new Circle(MathUtils.random(0, dSizeX - sizeCircle2), MathUtils.random(0, dSizeY - sizeCircle2), sizeCircle2, speedCircle2);
	}

	public void update (float dt){
		circle1.setCircleShape(circle1.getPosition().x + sizeCircle1/2, circle1.getPosition().y + sizeCircle1/2, sizeCircle1/2);
		circle2.setCircleShape(circle2.getPosition().x+ sizeCircle2/2, circle2.getPosition().y+ sizeCircle2/2,sizeCircle2/2);

		if (Intersector.overlaps(circle1.getCircleShape(),circle2.getCircleShape())){
			collision = true;
			circle1.speed = getSpeed(speedCircle1, speedCircle2, weightCircle1, weightCircle2);
			circle2.speed = getSpeed(speedCircle2, speedCircle1, weightCircle2, weightCircle1);
            circle1.angle = getAngle(circle1.position, circle2.position, circle1.velocity, circle2.velocity);
            circle2.angle = getAngle(circle2.position, circle1.position, circle2.velocity, circle1.velocity);
		} else {
			collision = false;
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
	private float getSpeed(float v1, float v2, float m1, float m2) {
		return ((m1-m2)*v1 + 2*m2*v2)/(m1+m2);
	}
	private float getAngle(Vector2 pos1, Vector2 pos2, Vector2 vel1, Vector2 vel2){
		float term1X = pos1.x;
		float term1Y = pos1.y;
		float term2X = pos2.x;
		float term2Y = pos2.y;
		double angleDest = Math.atan2(vel1.y, vel1.x)/Math.PI *180;
		float term = pos1.sub(vel1).nor().dot(pos2.sub(vel2).nor());
		pos1.x = term1X;
		pos1.y = term1Y;
		pos2.x = term2X;
		pos2.y = term2Y;
		float angle = (float) angleDest*term;
		return angle;
	}

	class Circle {
		private Texture texture;
		private Vector2 position;
		private Vector2 velocity;
		private float size;
		float speed;
		float angle;
		private com.badlogic.gdx.math.Circle circleShape;

		public com.badlogic.gdx.math.Circle getCircleShape() {
			return circleShape;
		}

		public void setCircleShape(float x, float y, float radius) {
			circleShape.set(x, y, radius);
		}

		public Circle (float x, float y, float sizze, float speeed) {
			texture = new Texture("circle.png");
			position = new Vector2(x, y);
			speed = speeed;
			velocity = new Vector2(speed, speed);
			size = sizze;
			angle = (float) (Math.random()*90);
			this.circleShape = new com.badlogic.gdx.math.Circle(x + sizze/2, y + sizze/2, sizze/2);
		}

		public void render (SpriteBatch batch){
			batch.draw(texture, position.x, position.y, size, size);
		}

		public void update (float dt){
			collision(dt);
		}

		void collision (float dt){
			if (collision){
				velocity.x = (float)Math.cos(angle)*speed;
				velocity.y = (float)Math.sin(angle)*speed;
			}
			if (velocity.x > 0 && position.x > dSizeX - size || velocity.x < 0 && position.x < 0){
				velocity.x *= -1;
			}
			if (velocity.y > 0 && position.y > dSizeY - size || velocity.y < 0 && position.y < 0){
				velocity.y *= -1;
			}
			position.x += velocity.x * dt;
			position.y += velocity.y * dt;
		}


		Vector2 getPosition(){
			return position;
		}

	}
}
