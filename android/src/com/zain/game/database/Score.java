package com.zain.game.database;

public class Score implements Comparable<Score> {
    int _id;
    String _name;
    int _score;
    public Score(){   }
    public Score(int id, String name, int score){
        this._id = id;
        this._name = name;
        this._score = score;
    }

    public Score(String name, int score){
        this._name = name;
        this._score = score;
    }
    public int getID(){ return this._id; }

    public void setID(int id){
        this._id = id;
    }

    public String getName(){ return this._name; }

    public void setName(String name){ this._name = name; }

    public int getScore(){ return this._score; }

    public void setScore(int score){
        this._score = score;
    }

    @Override
    public int compareTo(Score score) {
        return score.getScore() - this.getScore(); // descending order sorting
    }
}
