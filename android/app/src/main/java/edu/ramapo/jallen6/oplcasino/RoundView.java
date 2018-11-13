package edu.ramapo.jallen6.oplcasino;

import java.util.Vector;

public class RoundView extends BaseView {
    private Round model;

    private HandView tableView;
    private PlayerView[] playerViews;
    private DeckView deckView;


    final int humanID = PlayerID.humanPlayer.ordinal();
    final int compID = PlayerID.computerPlayer.ordinal();


    RoundView(Round master){
        model = master;
        initRound();
    }

    private  void initRound(){
        playerViews = new PlayerView[2];
        playerViews[humanID] = new PlayerView(model.getPlayers()[humanID]);
        playerViews[compID] = new PlayerView(model.getPlayers()[compID]);
        tableView = new HandView(model.getTable(), false);
        deckView = new DeckView(model.getDeck());
    }

    public DeckView getDeckViewHandler(){return deckView;}
    public HandView getHumanHandHandler(){
        return playerViews[0].getHand();
    }

    public HandView getComputerHandHandler(){
        return playerViews[1].getHand();
    }

    public HandView getTableHandHandler(){
        return tableView;
    }

    public void updateViews(){
        playerViews[0].getHand().createViewsFromModel();
        playerViews[1].getHand().createViewsFromModel();
        deckView.createViewsFromModel();
    }

    public void updatePileViews(){
        playerViews[0].getPile().createViewsFromModel();
        playerViews[1].getPile().createViewsFromModel();
    }

    public HandView getHumanPileHandler(){
        return playerViews[0].getPile();
    }
    public HandView getComputerPileHandler(){return playerViews[1].getPile();}




}
