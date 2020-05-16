package com.example.dotnboxex;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class LevelView extends View {
    private Cell[][] cells;
    private  int COLS,ROWS,players,count=1;
    private float cellsize,hmargin,vmargin;
    private Paint circlepaint,player1,player2,player3,box1,box2,box3,text1,text2,text3;
    private  final float WALL_THICKNESS = 10;
    public List<Float> fromx = new ArrayList<Float>();
    public List<Float> fromy = new ArrayList<Float>();
    public List<Float> tox = new ArrayList<Float>();
    public List<Float> toy = new ArrayList<Float>();
    public List<Float> boxx = new ArrayList<Float>();
    public List<Float> boxy = new ArrayList<Float>();
    public List<Integer> linecount = new ArrayList<Integer>();
    public List<Integer> boxcount = new ArrayList<Integer>();
    public LevelView(Context context) {
        super(context);
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
        player3.setARGB(500,0,0,255);
        player3.setStrokeWidth(WALL_THICKNESS);
        //
        text1 = new Paint();
        text1.setARGB(500, 102, 147,219 );
        text1.setTextSize(70);
        //
        text2 = new Paint();
        text2.setARGB(500, 255, 0,0 );
        text2.setTextSize(70);
        //
        text3 = new Paint();
        text3.setARGB(500, 0, 255,0 );
        text3.setTextSize(70);
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
        SharedPreferences numofplayers = getContext().getSharedPreferences("players",MODE_PRIVATE);
        players = numofplayers.getInt("players",2);
        //
        SharedPreferences gridsize = getContext().getSharedPreferences("gridsize",MODE_PRIVATE);
        COLS = gridsize.getInt("gridsize",2);
        ROWS = COLS;
        //

        createmaze();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
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
        int plyaer1score = 0;
        int player2score = 0;

        //
        if(boxcount.size()==ROWS*COLS){
            for(int i=0,temp=0;i<boxcount.size();i++,temp++) {
                if (boxcount.get(temp) == 1) {
                    plyaer1score++;
                } else {
                    player2score++;
                }
            }
            String ps1 = String.valueOf(plyaer1score);
            String ps2 = String.valueOf(player2score);
            canvas.drawText("PLAYER 1",0,-vmargin+120,text1);
            canvas.drawText("PLAYER 2",(width-2*hmargin)-300,-vmargin+120,text1);
            canvas.drawText(ps1,120,-vmargin+200,text2);
            canvas.drawText(ps2,(width-2*hmargin)-180,-vmargin+200,text3);
            if(plyaer1score>player2score){
                canvas.drawText("PLAYER 1 WINS !!",170,300,text1);
            }
            if(player2score>plyaer1score) {
                canvas.drawText("PLAYER 2 WINS !!",170,300,text1);
            }
            if(player2score==plyaer1score) {
                canvas.drawText("Draw !!",240,300,text1);
            }
        }
        else {
            //
            for(int i=0,temp=0;i<boxcount.size();i++,temp++){
                float startx =boxx.get(i);
                float starty =boxy.get(i);
                float stopx =(startx/cellsize+1)*cellsize;
                float stopy =(starty/cellsize+1)*cellsize;
                if(boxcount.get(temp)==1){
                    canvas.drawRect(startx,starty,stopx,stopy,box1);
                    plyaer1score++;
                }
                if(boxcount.get(temp)==2){
                    canvas.drawRect(startx,starty,stopx,stopy,box2);
                    player2score++;
                }
                if(boxcount.get(temp)==3){
                    canvas.drawRect(startx,starty,stopx,stopy,box3);
                }
            }
            //
            for(int i=0,temp=0;i<fromx.size();i++,temp++){
                float startx =fromx.get(i);
                float starty =fromy.get(i);
                float stopx =tox.get(i);
                float stopy =toy.get(i);
                if(linecount.get(temp)==1){
                    canvas.drawLine(startx,starty,stopx,stopy,player1);
                }
                if(linecount.get(temp)==2){
                    canvas.drawLine(startx,starty,stopx,stopy,player2);
                }
                if(linecount.get(temp)==3){
                    canvas.drawLine(startx,starty,stopx,stopy,player3);
                }
            }
            //
            for(int x=0;x<COLS+1;x++) {
                for (int y = 0; y < ROWS + 1; y++) {
                    canvas.drawCircle(x * cellsize, y * cellsize, 20, circlepaint);
                }
            }
            String ps1 = String.valueOf(plyaer1score);
            String ps2 = String.valueOf(player2score);
            canvas.drawText("PLAYER 1",0,-vmargin+120,text1);
            canvas.drawText("PLAYER 2",(width-2*hmargin)-300,-vmargin+120,text1);
            canvas.drawText(ps1,120,-vmargin+200,text2);
            canvas.drawText(ps2,(width-2*hmargin)-180,-vmargin+200,text3);

        }

    }

    private void createmaze(){
        cells = new LevelView.Cell[COLS][ROWS];
        for(int x=0;x<COLS;x++){
            for(int y=0;y<ROWS;y++){
                cells[x][y] = new LevelView.Cell(x,y);
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
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            //
            float a = event.getX();
            float b = event.getY();
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
                            Boolean make = true;
                            if(!repeat){
                                //checking condition to create box
                                createboxinx(x,y);
                                //passing the values to arraylist
                                fromx.add((x*cellsize));
                                tox.add(((x+1)*cellsize));
                                fromy.add((y*cellsize));
                                toy.add((y*cellsize));
                                addlinecolor();
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
                            Boolean make = true;
                            if(!repeat){
                                //checking condition to create box
                                createboxiny(x,y);
                                //passing the values to arraylist
                                fromy.add((y*cellsize));
                                toy.add(((y+1)*cellsize));
                                fromx.add((x*cellsize));
                                tox.add((x*cellsize));
//                                if(linecount.size()==0){
////                                    linecount.add(1);
////                                }
////                                else {
////                                    int actionpoint =  linecount.size()-1;
////                                    if(linecount.get(actionpoint)==1){
////                                        linecount.add(2);
////                                    }
////                                    else {
////                                        linecount.add(1);
////                                    }
////                                }
                                addlinecolor();
                            }
                        }
                    }
                }
            }
            count++;
            invalidate();
        }
        return true;
    }
    public void addlinecolor(){
        if(count%players==players-1){
            linecount.add(1);
        }
        if(count%players==players-2){
            linecount.add(2);
        }
        if(count%players==players-3){
            linecount.add(3);
        }
    }

    public void createboxinx(int x,int y){
        boolean case1=false,case2=false,case3=false,col1=false,col2=false,col3=false;
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
                    for(int k=0; k<boxcount.size();k++){
                        if(x*cellsize-boxx.get(k)==0 && (y-1)*cellsize-boxy.get(k)==0){
                            dew=false;
                        }
                    }
                    if(dew){
                        boxx.add(x*cellsize);
                        boxy.add((y-1)*cellsize);
                        saveboxcolor();
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
                    for(int k=0; k<boxcount.size();k++){
                        if(x*cellsize-boxx.get(k)==0 && (y)*cellsize-boxy.get(k)==0){
                            dew=false;
                        }
                    }
                    if(dew){
                        boxx.add(x*cellsize);
                        boxy.add((y)*cellsize);
                        saveboxcolor();
                    }
                }
            }
        }
    }
    public void createboxiny(int x, int y){
        boolean case1=false,case2=false,case3=false,col1=false,col2=false,col3=false;
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
                    for(int k=0;k<boxcount.size();k++){
                        if((x-1)*cellsize-boxx.get(k)==0 && y*cellsize-boxy.get(k)==0){
                            dew=false;
                        }
                    }
                    if(dew){
                        boxx.add((x-1)*cellsize);
                        boxy.add((y)*cellsize);
                        saveboxcolor();
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
                    for(int k=0;k<boxcount.size();k++){
                        if((x)*cellsize-boxx.get(k)==0 && y*cellsize-boxy.get(k)==0){
                            dew=false;
                        }
                    }
                    if(dew){
                        boxx.add((x)*cellsize);
                        boxy.add((y)*cellsize);
                        saveboxcolor();
                    }
                }
            }
        }
    }
    public void saveboxcolor(){
        if(count%players==players-1){
            boxcount.add(1);
        }
        if(count%players==players-2){
            boxcount.add(2);
        }
        if(count%players==players-3){
            boxcount.add(3);
        }
    }

}
