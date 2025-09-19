package com.example.proburok.animation;

import javafx.animation.TranslateTransition;
import javafx.util.Duration;
import javafx.scene.Node;


public class Shake {
    private TranslateTransition tt;
    public Shake(Node node){
        tt = new TranslateTransition(Duration.millis(70),  node);
        tt.setFromX(0f); // смешение по икус
        tt.setByX(10f); // передвинесья на новую позицуию
        tt.setCycleCount(5); // сколько раз будет делать
        tt.setAutoReverse(true); //что бы вернулась обратно
    }
    public void playAnim(){
        tt.playFromStart();// что бы запустилась
    }
}
