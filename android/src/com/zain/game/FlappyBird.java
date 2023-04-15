package com.zain.game;

import android.util.Log;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.zain.game.database.DatabaseHandler;
import com.zain.game.database.Score;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class FlappyBird extends ApplicationAdapter {
    DatabaseHandler db;
    int currentTopScore;

    Object mutex = new Object();

    SpriteBatch batch;
    Texture background;
    // ShapeRenderer shapeRenderer;
    Texture gameover;

    Texture[] birds;
    Texture[] upsideDownBirds;
    int flapState = 0;
    float birdY = 0;

    float upsideDownBirdY = 0;
    float velocity = 0;
    float upsideDownVelocity = 0;
    Circle birdCircle;

    Circle upsideDownBirdCircle;
    int score = 0;
    int scoringTube = 0;
    BitmapFont font;


    int gameState = 0;
    float gravity = 1;
    Texture topTube;
    Texture bottomTube;
    float gap = 800;
    float maxTubeOffset;
    Random randomGenerator;

    float tubeVelocity = 4;

    int numberOfTubes = 4;
    float[] tubeX = new float[numberOfTubes];
    float[] tubeOffset = new float[numberOfTubes];
    float distanceBetweenTheTube;
    Rectangle[] topTubeRectangles;
    Rectangle[] bottomTubeRectangles;

    AtomicInteger jumps = new AtomicInteger(0);
    AtomicInteger scoreAtomic = new AtomicInteger(0);

    AtomicInteger upsideDownJumps = new AtomicInteger(0);
    AtomicInteger upsideDownScoreAtomic = new AtomicInteger(0);

    BirdThread birdThread = new BirdThread(1, jumps, scoreAtomic, false);
    BirdThread upsideDownBirdThread = new BirdThread(2, upsideDownJumps, upsideDownScoreAtomic, false);

    Thread thread = new Thread(birdThread);
    Thread upsideDownThread = new Thread(upsideDownBirdThread);


    public FlappyBird(DatabaseHandler input_db) {
        db = input_db;
//        db.deleteAllRows();
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        background = new Texture("bg.png");
        gameover = new Texture("gameover.png");
        //shapeRenderer = new ShapeRenderer();
        birdCircle = new Circle();
        upsideDownBirdCircle = new Circle();

        birds = new Texture[2];
        birds[0] = new Texture("bird.png");
        birds[1] = new Texture("bird2.png");

        upsideDownBirds = new Texture[2];
        upsideDownBirds[0] = new Texture("bird.png");
        upsideDownBirds[1] = new Texture("bird2.png");

        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(10);

        topTube = new Texture("toptube.png");
        bottomTube = new Texture("bottomtube.png");

        maxTubeOffset = Gdx.graphics.getHeight() / 2 - gap / 2 - 100;
        randomGenerator = new Random();
        distanceBetweenTheTube = Gdx.graphics.getWidth() * 7 / 8;
        topTubeRectangles = new Rectangle[numberOfTubes];
        bottomTubeRectangles = new Rectangle[numberOfTubes];

        startGame();


    }

    public void startGame()
    {
        currentTopScore = db.getTopScore();

        birdY = Gdx.graphics.getHeight() / 2 - birds[flapState].getHeight() / 2;
        upsideDownBirdY = Gdx.graphics.getHeight() / 2 - birds[flapState].getHeight() / 2;
        for (int i = 0; i < numberOfTubes; i++) {
            tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 400);

            tubeX[i] = Gdx.graphics.getWidth() / 2 - topTube.getWidth() / 2 + Gdx.graphics.getWidth() + i * distanceBetweenTheTube;

            topTubeRectangles[i] = new Rectangle();
            bottomTubeRectangles[i] = new Rectangle();

        }
    }

    public void updateScoreDatabase() {
        db.addScore(new Score("placeholder_name", score));
        db.deleteExceptTopScores();

        db.logAllScores();
    }

    @Override
    public void render() {

        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        if (gameState == 1) {

            if (tubeX[scoringTube] < Gdx.graphics.getWidth() / 2) {
                Gdx.app.log("Score", String.valueOf(score));

                synchronized(mutex) {
                    scoreAtomic.getAndAdd(1);
                    upsideDownScoreAtomic.getAndAdd(1);
                    score += 2;
                }
                if (scoringTube < numberOfTubes - 1) {
                    scoringTube++;
                } else {
                    scoringTube = 0;
                }
            }

            if (Gdx.input.justTouched()) {
                System.out.println(Gdx.input.getY());

                jumps.getAndAdd(1);
                upsideDownJumps.getAndAdd(1);

                if (Gdx.input.getY() > Gdx.graphics.getHeight() / 2) {
                    velocity = -30;
                } else {
                    upsideDownVelocity = -30;
                }

//                velocity = -30;
//                upsideDownVelocity = -30;
            }
            for (int i = 0; i < numberOfTubes; i++) {

                if (tubeX[i] < -topTube.getWidth()) {

                    tubeX[i] += numberOfTubes * distanceBetweenTheTube;
                    tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
                } else {

                    tubeX[i] = tubeX[i] - tubeVelocity;

                }
                batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
                batch.draw(bottomTube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i]);

                topTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i], topTube.getWidth(), topTube.getHeight());
                bottomTubeRectangles[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i], bottomTube.getWidth(), bottomTube.getHeight());
            }

            if (upsideDownBirdY < Gdx.graphics.getHeight()) {
                upsideDownVelocity = upsideDownVelocity + gravity;
                upsideDownBirdY += upsideDownVelocity;
            }else{
                gameState = 2; //2 is the game over state
            }


            if (birdY > 0) {
                velocity = velocity + gravity;
                birdY -= velocity;
            }else{
                gameState = 2; //2 is the game over state
            }

        } else if(gameState==0){

            if (Gdx.input.justTouched())
            {
                gameState = 1;
            }

        }else if(gameState==2)
        {
            batch.draw(gameover ,Gdx.graphics.getWidth()/2 - gameover.getWidth()/2,Gdx.graphics.getHeight()/2 -gameover.getHeight());

            if (Gdx.input.justTouched())
            {
                updateScoreDatabase();

                gameState = 1;
                startGame();
                synchronized(mutex) {
                    score = 0;
                }
                scoringTube=0;
                velocity = 0;
                upsideDownVelocity = 0;

                birdThread.resetJumps();
                upsideDownBirdThread.resetJumps();
                birdThread.resetScore();
                upsideDownBirdThread.resetScore();

            }

        }

        if (flapState == 0) {
            flapState = 1;
        } else {
            flapState = 0;
        }


        batch.draw(birds[flapState], Gdx.graphics.getWidth() / 2 - birds[flapState].getWidth() / 2, birdY);
        font.draw(batch , String.valueOf(score) , 100 , 200);

        // Render top score so far
        font.draw(batch , String.valueOf(currentTopScore) , 100 , Gdx.graphics.getHeight() - 100);

        batch.draw(upsideDownBirds[flapState], Gdx.graphics.getWidth() / 2 - upsideDownBirds[flapState].getWidth() / 2, upsideDownBirdY);


        batch.end();

        birdCircle.set(Gdx.graphics.getWidth() / 2, birdY + birds[flapState].getHeight() / 2, birds[flapState].getWidth() / 2);
        upsideDownBirdCircle.set(Gdx.graphics.getWidth() / 2, upsideDownBirdY + upsideDownBirds[flapState].getHeight() / 2, upsideDownBirds[flapState].getWidth() / 2);

//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//        shapeRenderer.setColor(Color.RED);
//        shapeRenderer.circle(birdCircle.x ,birdCircle.y , birdCircle.radius);

        for (int i = 0; i < numberOfTubes; i++) {

            //shapeRenderer.rect(tubeX[i],Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i],topTube.getWidth(),topTube.getHeight());
            //shapeRenderer.rect(tubeX[i],Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i], bottomTube.getWidth(),bottomTube.getHeight());

            if (Intersector.overlaps(birdCircle, topTubeRectangles[i]) || Intersector.overlaps(birdCircle, bottomTubeRectangles[i])) {

                gameState = 2;
            }

            if (Intersector.overlaps(upsideDownBirdCircle, topTubeRectangles[i]) || Intersector.overlaps(upsideDownBirdCircle, bottomTubeRectangles[i])) {

                gameState = 2;
            }
        }


        //shapeRenderer.end();

        try {
            thread.join();
            upsideDownThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
