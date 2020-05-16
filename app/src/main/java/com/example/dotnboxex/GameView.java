package com.example.dotnboxex;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.VIBRATOR_SERVICE;

public class GameView extends View {
    private Vibrator vibrator;
    private Cell[][] cells;
    private boolean twobox = false;
    private int COLS,ROWS,players,count=1;
    private float cellsize,hmargin,vmargin;
    private Paint circlepaint,player1,player2,player3,box1,box2,box3,whitebox,text1,text2,text3,text4,undo;
    private static final float WALL_THICKNESS = 10;
    public List<Float> fromx = new ArrayList<Float>();
    public List<Float> fromy = new ArrayList<Float>();
    public List<Float> tox = new ArrayList<Float>();
    public List<Float> toy = new ArrayList<Float>();
    public List<Float> boxx = new ArrayList<Float>();
    public List<Float> boxy = new ArrayList<Float>();
    public List<Integer> linecount = new ArrayList<Integer>();
    public List<Integer> boxcount = new ArrayList<Integer>();
    public List<Integer> boxnumber = new ArrayList<Integer>();
    private static MediaPlayer button ;

    @SuppressLint("ServiceCast")
    public GameView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        circlepaint = new Paint();
        circlepaint.setColor(Color.WHITE);
        circlepaint.setStrokeWidth(WALL_THICKNESS);
        //
        player1 = new Paint();
        player1.setARGB(500,255,0,0);
        player1.setStrokeWidth(WALL_THICKNESS);
        //
        player2 = new Paint();
        player2.setARGB(500,0,255,0);
        player2.setStrokeWidth(WALL_THICKNESS);
        //
        player3 = new Paint();
        player3.setARGB(500,0,64,244);
        player3.setStrokeWidth(WALL_THICKNESS);
        //
        text1 = new Paint();
        text1.setARGB(500, 255,153,0);
        text1.setTextSize(70);
        text1.setFakeBoldText(true);
        //
        text2 = new Paint();
        text2.setARGB(500, 255, 0,0 );
        text2.setTextSize(70);
        text2.setFakeBoldText(true);
        //
        text3 = new Paint();
        text3.setARGB(500, 0, 255,0 );
        text3.setTextSize(70);
        text3.setFakeBoldText(true);
        //
        text4 = new Paint();
        text4.setARGB(500, 0,64,244 );
        text4.setTextSize(70);
        text4.setFakeBoldText(true);
        //
        box1 = new Paint();
        box1.setARGB(20,255,0,0);
        //
        box2 = new Paint();
        box2.setARGB(20,0,255,0);
        //
        box3 = new Paint();
        box3.setARGB(20,0,0,255);
        //
        whitebox = new Paint();
        whitebox.setARGB(500,255,153,0);
        //
        undo= new Paint();
        undo.setARGB(500, 68,68,68 );
        undo.setTextSize(70);
        undo.setFakeBoldText(true);
        //
        SharedPreferences numofplayers = getContext().getSharedPreferences("players",MODE_PRIVATE);
        players = numofplayers.getInt("players",2);
        //
        SharedPreferences gridsize = getContext().getSharedPreferences("gridsize",MODE_PRIVATE);
        COLS = gridsize.getInt("gridsize",2);
        ROWS = COLS;
        //
        button = MediaPlayer.create(context,R.raw.yellow);
        vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
        createmaze();
    }

    private void createmaze(){
        cells = new Cell[COLS][ROWS];
        for(int x=0;x<COLS;x++){
            for(int y=0;y<ROWS;y++){
                cells[x][y] = new Cell(x,y);
            }
        }
    }

    private class Cell{
        int col, row;
        public Cell(int col,int row){
            this.col =col;
            this.row =row;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {

        int width = getWidth();
        int height = getHeight()+400;

        if(width/height<COLS/ROWS){
            cellsize = width/(COLS+1);
        }
        else {
            cellsize = height/(ROWS+1);
        }
        hmargin = (width-(COLS*cellsize))/2;
        vmargin = (height-(ROWS*cellsize))/2;
        //
        canvas.drawColor(Color.DKGRAY);
        canvas.translate(hmargin,vmargin);
        int player1score = 0;
        int player2score = 0;
        int player3score = 0;
        //
        for(int i=0,temp=0;i<boxcount.size();i++,temp++) {
            if (boxcount.get(temp) == 1) {
                player1score++;
            }
            if (boxcount.get(temp) == 2) {
                player2score++;
            }
            if (boxcount.get(temp) == 3) {
                player3score++;
            }
        }
        float textsize = text1.measureText("Player 0");
        String ps1 = String.valueOf(player1score);
        String ps2 = String.valueOf(player2score);
        canvas.drawText("PLAYER 1",-20,-vmargin+120,text1);
        canvas.drawText(ps1,textsize/2-20,-vmargin+200,text2);
        canvas.drawText("PLAYER 2",(width-2*hmargin)-textsize-40,-vmargin+120,text1);
        canvas.drawText(ps2,(width-2*hmargin)-textsize/2-40,-vmargin+200,text3);
        if(players==3){
            String ps3 = String.valueOf(player3score);
            canvas.drawText("PLAYER 3",(width-2*hmargin)-textsize-40,-vmargin+320,text1);
            canvas.drawText(ps3,(width-2*hmargin)-textsize/2-40,-vmargin+400,text4);
        }
        //
        if(boxx.size()==ROWS*COLS){
            Boolean draw = true;
            float winmsg1 = text1.measureText("Player 0 WINS !!");
            float winmsg2 = text1.measureText("Player0 and player0 WINS !!");
            float winmsg3 = text1.measureText("DRAW !!");
            if(player1score>player2score && player1score>player3score){
                canvas.drawText("PLAYER 1 WINS !!",(width-2*hmargin)/2-winmsg1/2,300,text1);
                draw = false;
            }
            if(player2score>player1score && player2score>player3score) {
                canvas.drawText("PLAYER 2 WINS !!",(width-2*hmargin)/2-winmsg1/2,300,text1);
                draw = false;
            }
            if(player3score>player1score && player3score>player2score) {
                canvas.drawText("PLAYER 3 WINS !!",(width-2*hmargin)/2-winmsg1/2,300,text1);
                draw = false;
            }
            if(draw){
                if(player1score==player2score&&player2score==player3score){
                    canvas.drawText("DRAW !!",(width-2*hmargin)/2-winmsg3/2,300,text1);
                }
                else {
                    if(player1score==player2score){
                        canvas.drawText("Player1 and Player2 WINS!!",(width-2*hmargin)/2-winmsg2/2,300,text1);
                    }
                    if(player2score==player3score) {
                        canvas.drawText("Player2 and Player3 WINS!!",(width-2*hmargin)/2-winmsg2/2,300,text1);
                    }
                    if(player3score==player1score) {
                        canvas.drawText("Player1 and Player3 WINS!!",(width-2*hmargin)/2-winmsg2/2,300,text1);
                    }
                }
            }

        }
        else {
            //
            for(int i=0,temp=0;i<boxcount.size();i++,temp++){
                if(boxcount.get(temp)>0){
                    float startx =boxx.get(i);
                    float starty =boxy.get(i);
                    float stopx =(startx/cellsize+1)*cellsize;
                    float stopy =(starty/cellsize+1)*cellsize;
                    if(boxcount.get(temp)==1){
                        canvas.drawRect(startx,starty,stopx,stopy,box1);
                        player1score++;
                    }
                    if(boxcount.get(temp)==2){
                        canvas.drawRect(startx,starty,stopx,stopy,box2);
                        player2score++;
                    }
                    if(boxcount.get(temp)==3){
                        canvas.drawRect(startx,starty,stopx,stopy,box3);
                        player3score++;
                    }
                }
            }
            //
            for(int i=0,temp=0;i<fromx.size();i++,temp++){
                if(linecount.get(temp)==1){
                    canvas.drawLine(fromx.get(i),fromy.get(i),tox.get(i),toy.get(i),player1);
                }
                if(linecount.get(temp)==2){
                    canvas.drawLine(fromx.get(i),fromy.get(i),tox.get(i),toy.get(i),player2);
                }
                if(linecount.get(temp)==3){
                    canvas.drawLine(fromx.get(i),fromy.get(i),tox.get(i),toy.get(i),player3);
                }
            }
            //
            for(int x=0;x<COLS+1;x++) {
                for (int y = 0; y < ROWS + 1; y++) {
                    canvas.drawCircle(x * cellsize, y * cellsize, 20, circlepaint);
                }
            }
        }
        //
        canvas.drawRect(-20,-vmargin+270,210,-vmargin+370,whitebox);
        canvas.drawText(" UNDO ",-20,-vmargin+350,undo);

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            float a = event.getX();
            float b = event.getY();
            if(boxcount.size()!=ROWS*COLS){
                if(a>-20+hmargin && a<210+hmargin && b>(270) && b<(370)) {
                    undoaction();
                }
            }
            boolean repeat = false;
            for(int x=0;x<COLS;x++) {
                if( a>((x*cellsize)+hmargin+30) && a<(((x+1)*cellsize)+hmargin-30) ){
                    for (int y = 0; y <= ROWS; y++) {
                        if(((y*cellsize)-30+vmargin)<b && b<((y*cellsize)+30+vmargin)){
                            //checking condition to create line
                            for(int i=0;i<fromx.size();i++){
                                if((x*cellsize-fromx.get(i)==0)&&(y*cellsize-fromy.get(i)==0)&&((x+1)*cellsize-tox.get(i)==0)&&((y)*cellsize-toy.get(i)==0)){
                                    repeat = true;
                                }
                            }
                            if(!repeat){
                                //checking condition to create box
                                createboxinx(x,y);
                                //passing the values to arraylist
                                fromx.add((x*cellsize));
                                tox.add(((x+1)*cellsize));
                                fromy.add((y*cellsize));
                                toy.add((y*cellsize));
                                addlinecolor();
                                count++;
                            }
                        }
                    }
                }
            }
            for(int y=0;y<ROWS;y++){
                if( (b>((y*cellsize)+vmargin+30)) && (b<(((y+1)*cellsize)+vmargin-30)) ){
                    for (int x=0;x<=COLS;x++){
                        if(((x*cellsize)-30+hmargin)<a && a<((x*cellsize)+30+hmargin)){
                            for(int i=0;i<fromx.size();i++){
                                if((x*cellsize-fromx.get(i)==0)&&(y*cellsize-fromy.get(i)==0)&&((x)*cellsize-tox.get(i)==0)&&((y+1)*cellsize-toy.get(i)==0)){
                                    repeat = true;
                                }
                            }
                            if(!repeat){
                                //checking condition to create box
                                createboxiny(x,y);
                                //passing the values to arraylist
                                fromy.add((y*cellsize));
                                toy.add(((y+1)*cellsize));
                                fromx.add((x*cellsize));
                                tox.add((x*cellsize));
                                addlinecolor();
                                count++;
                            }
                        }
                    }
                }
            }
            invalidate();
        }
        return true;
    }
    @SuppressLint("MissingPermission")
    public void undoaction(){

        vibrator.vibrate(200);

        if(boxnumber.size()>0){
            if(twobox){
                boxx.remove(boxx.size()-1);
                boxy.remove(boxy.size()-1);
                boxcount.remove(boxcount.size()-1);
                boxx.remove(boxx.size()-1);
                boxy.remove(boxy.size()-1);
                boxcount.remove(boxcount.size()-1);
                boxnumber.remove(boxnumber.size()-1);
                twobox=false;
            }
            else {
                if(boxnumber.get(boxnumber.size()-1)>0){
                    boxx.remove(boxx.size()-1);
                    boxy.remove(boxy.size()-1);
                    boxcount.remove(boxcount.size()-1);
                }
            }
            boxnumber.remove(boxnumber.size()-1);
        }
        if(linecount.size()>0){
            fromy.remove(fromy.size()-1);
            toy.remove(toy.size()-1);
            fromx.remove(fromx.size()-1);
            tox.remove(tox.size()-1);
            linecount.remove(linecount.size()-1);
            count--;
        }

    }

    public void addlinecolor(){
        button.start();
        if(players==2){
            if(count%players==1){
                linecount.add(1);
            }
            if(count%players==0){
                linecount.add(2);
            }
        }
        else {
            if((count+3)%players==1){
                linecount.add(1);
            }
            if((count+3)%players==2){
                linecount.add(2);
            }
            if((count+3)%players==0){
                linecount.add(3);
            }
        }
    }

    public void createboxinx(int x,int y){
        boolean case1=false,case2=false,case3=false,col1=false,col2=false,col3=false,cond1=true,cond2=true;
        if((y-1)>=0){
            for(int i=0;i<fromx.size();i++){
                if((x*cellsize-fromx.get(i)==0)&&((y-1)*cellsize-fromy.get(i)==0)&&((x+1)*cellsize-tox.get(i)==0)&&((y-1)*cellsize-toy.get(i)==0)){
                    case1 = true;
                }
                if((x*cellsize-fromx.get(i)==0)&&((y-1)*cellsize-fromy.get(i)==0)&&((x)*cellsize-tox.get(i)==0)&&((y)*cellsize-toy.get(i)==0)){
                    case2 = true;
                }
                if(((x+1)*cellsize-fromx.get(i)==0)&&((y-1)*cellsize-fromy.get(i)==0)&&((x+1)*cellsize-tox.get(i)==0)&&((y)*cellsize-toy.get(i)==0)){
                    case3 = true;
                }
                if(case1==true&&case2==true&&case3==true){
                    Boolean dew =true;
                    for(int k=0; k<boxx.size();k++){
                        if(x*cellsize-boxx.get(k)==0 && (y-1)*cellsize-boxy.get(k)==0){
                            dew=false;
                        }
                    }
                    if(dew){
                        boxx.add(x*cellsize);
                        boxy.add((y-1)*cellsize);
                        saveboxcolor();
                        cond1=false;
                    }
                }
            }
        }
        if((y+1)<=ROWS){
            for(int i=0;i<fromx.size();i++){
                if((x*cellsize-fromx.get(i)==0)&&((y+1)*cellsize-fromy.get(i)==0)&&((x+1)*cellsize-tox.get(i)==0)&&((y+1)*cellsize-toy.get(i)==0)){
                    col1 = true;
                }
                if((x*cellsize-fromx.get(i)==0)&&((y)*cellsize-fromy.get(i)==0)&&((x)*cellsize-tox.get(i)==0)&&((y+1)*cellsize-toy.get(i)==0)){
                    col2 = true;
                }
                if(((x+1)*cellsize-fromx.get(i)==0)&&((y)*cellsize-fromy.get(i)==0)&&((x+1)*cellsize-tox.get(i)==0)&&((y+1)*cellsize-toy.get(i)==0)){
                    col3 = true;
                }
                if(col1==true&&col2==true&&col3==true){
                    Boolean dew =true;
                    for(int k=0; k<boxx.size();k++){
                        if(x*cellsize-boxx.get(k)==0 && (y)*cellsize-boxy.get(k)==0){
                            dew=false;
                        }
                    }
                    if(dew){
                        boxx.add(x*cellsize);
                        boxy.add((y)*cellsize);
                        saveboxcolor();
                        cond2=false;
                    }
                }
            }
        }
        if(cond1&&cond2){
            boxnumber.add(0);
        }
        if(!cond1 && !cond2){
            twobox=true;
        }
        else {
            twobox=false;
        }
    }
    public void createboxiny(int x, int y){
        boolean case1=false,case2=false,case3=false,col1=false,col2=false,col3=false,cond1=true,cond2=true;
        if((x-1)>=0){
            for(int i=0;i<fromx.size();i++){
                if(((x-1)*cellsize-fromx.get(i)==0)&&((y)*cellsize-fromy.get(i)==0)&&((x-1)*cellsize-tox.get(i)==0)&&((y+1)*cellsize-toy.get(i)==0)){
                    case1 = true;
                }
                if(((x-1)*cellsize-fromx.get(i)==0)&&((y)*cellsize-fromy.get(i)==0)&&((x)*cellsize-tox.get(i)==0)&&((y)*cellsize-toy.get(i)==0)){
                    case2 = true;
                }
                if(((x-1)*cellsize-fromx.get(i)==0)&&((y+1)*cellsize-fromy.get(i)==0)&&((x)*cellsize-tox.get(i)==0)&&((y+1)*cellsize-toy.get(i)==0)){
                    case3 = true;
                }
                if(case1==true&&case2==true&&case3==true){
                    Boolean dew = true;
                    for(int k=0;k<boxx.size();k++){
                        if((x-1)*cellsize-boxx.get(k)==0 && y*cellsize-boxy.get(k)==0){
                            dew=false;
                        }
                    }
                    if(dew){
                        boxx.add((x-1)*cellsize);
                        boxy.add((y)*cellsize);
                        saveboxcolor();
                        cond1=false;
                    }
                }
            }
        }
        if((x+1)<=COLS){
            for(int i=0;i<fromx.size();i++){
                if(((x+1)*cellsize-fromx.get(i)==0)&&((y)*cellsize-fromy.get(i)==0)&&((x+1)*cellsize-tox.get(i)==0)&&((y+1)*cellsize-toy.get(i)==0)){
                    col1 = true;
                }
                if((x*cellsize-fromx.get(i)==0)&&((y)*cellsize-fromy.get(i)==0)&&((x+1)*cellsize-tox.get(i)==0)&&((y)*cellsize-toy.get(i)==0)){
                    col2 = true;
                }
                if(((x)*cellsize-fromx.get(i)==0)&&((y+1)*cellsize-fromy.get(i)==0)&&((x+1)*cellsize-tox.get(i)==0)&&((y+1)*cellsize-toy.get(i)==0)){
                    col3 = true;
                }
                if(col1==true&&col2==true&&col3==true){
                    Boolean dew = true;
                    for(int k=0;k<boxx.size();k++){
                        if((x)*cellsize-boxx.get(k)==0 && y*cellsize-boxy.get(k)==0){
                            dew=false;
                        }
                    }
                    if(dew){
                        boxx.add((x)*cellsize);
                        boxy.add((y)*cellsize);
                        saveboxcolor();
                        cond2=false;
                    }
                }
            }
        }
        if(cond1&&cond2){
            boxnumber.add(0);
        }
        if(!cond1 && !cond2){
            twobox=true;
        }
        else {
            twobox=false;
        }
    }
    public void saveboxcolor() {
        if(players==2){
            if(count%players == 1){
                boxcount.add(1);
                boxnumber.add(1);
            }
            if(count%players == 0){
                boxcount.add(2);
                boxnumber.add(2);
            }
        }
        else {
            if ((count+3)%players == 1) {
                boxcount.add(1);
                boxnumber.add(1);
            }
            if ((count+3)%players == 2) {
                boxcount.add(2);
                boxnumber.add(2);
            }
            if ((count+3)%players == 0) {
                boxcount.add(3);
                boxnumber.add(3);
            }
        }
    }

}
