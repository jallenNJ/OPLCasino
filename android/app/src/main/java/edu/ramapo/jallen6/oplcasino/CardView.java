package edu.ramapo.jallen6.oplcasino;

import android.graphics.drawable.Drawable;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class CardView extends BaseView {
    private Card model;

    CardView(){
        model = new Card();
    }

    CardView(Card master){
        model = master;
    }

    CardView(CardView copy){
        model = new Card(copy.model);
    }

    public CardSuit getModelSuit(){
        return  model.getSuit();
    }

    public int getModelValue(){
        return model.getValue();
    }

    private int cardToGraphic(){
        CardSuit suit = model.getSuit();
        switch(suit){
            case club:
                return resolveClubSuit();
            case diamond:
                return resolveDiamondSuit();
            case heart:
                return resolveHeartSuit();
            case spade:
                return resolveSpadeSuit();
            default:
                return R.drawable.testcard;
        }
    }

    private int resolveClubSuit(){
        int cardVal = model.getValue();
        switch (cardVal){
            case 2:
                return R.drawable.c2;
            case 3:
                return R.drawable.c3;
            case 4:
                return R.drawable.c4;
            case 5:
                return R.drawable.c5;
            case 6:
                return R.drawable.c6;
            case 7:
                return R.drawable.c7;
            case 8:
                return R.drawable.c8;
            case 9:
                return R.drawable.c9;
            case 10:
                return R.drawable.cx;
            case 11:
                return R.drawable.cj;
            case 12:
                return R.drawable.cq;
            case 13:
                return R.drawable.ck;
            default:
                return R.drawable.ca;
        }
    }



    private  int resolveDiamondSuit(){
        int cardVal = model.getValue();
        switch (cardVal){
            case 2:
                return R.drawable.d2;
            case 3:
                return R.drawable.d3;
            case 4:
                return R.drawable.d4;
            case 5:
                return R.drawable.d5;
            case 6:
                return R.drawable.d6;
            case 7:
                return R.drawable.d7;
            case 8:
                return R.drawable.d8;
            case 9:
                return R.drawable.d9;
            case 10:
                return R.drawable.dx;
            case 11:
                return R.drawable.dj;
            case 12:
                return R.drawable.dq;
            case 13:
                return R.drawable.dk;
            default:
                return R.drawable.da;
        }
    }

    private  int resolveHeartSuit(){
        int cardVal = model.getValue();
        switch (cardVal){
            case 2:
                return R.drawable.h2;
            case 3:
                return R.drawable.h3;
            case 4:
                return R.drawable.h4;
            case 5:
                return R.drawable.h5;
            case 6:
                return R.drawable.h6;
            case 7:
                return R.drawable.h7;
            case 8:
                return R.drawable.h8;
            case 9:
                return R.drawable.h9;
            case 10:
                return R.drawable.hx;
            case 11:
                return R.drawable.hj;
            case 12:
                return R.drawable.hq;
            case 13:
                return R.drawable.hk;
            default:
                return R.drawable.ha;
        }
    }

    private  int resolveSpadeSuit(){
        int cardVal = model.getValue();
        switch (cardVal){
            case 2:
                return R.drawable.s2;
            case 3:
                return R.drawable.s3;
            case 4:
                return R.drawable.s4;
            case 5:
                return R.drawable.s5;
            case 6:
                return R.drawable.s6;
            case 7:
                return R.drawable.s7;
            case 8:
                return R.drawable.s8;
            case 9:
                return R.drawable.s9;
            case 10:
                return R.drawable.sx;
            case 11:
                return R.drawable.sj;
            case 12:
                return R.drawable.sq;
            case 13:
                return R.drawable.sk;
            default:
                return R.drawable.sa;
        }
    }
    void setButton (ImageButton ref){
        if(ref != null){
                ref.setImageResource(cardToGraphic());

        }

    }
}
