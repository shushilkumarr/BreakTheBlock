package com.example.breaktheblock;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    Paint paint;
    int[] colors;
    int[] colorCode;
    ImageView canvas;
    EditText input;
    Button submit;
    int userInput;
    MatrixAdjuster adjuster;
    int[][] matrix;
    private boolean isEmpty;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adjuster = new MatrixAdjuster();

        canvas = findViewById(R.id.canvas);
        input = findViewById(R.id.number);
        submit = findViewById(R.id.submit);

        //initialize the matrix
        matrix = new int[][]{{0, 1, 1, 0, 0, 0},
                {0, 1, 2, 2, 0, 0},
                {0, 1, 1, 2, 3, 0},
                {17, 0, 2, 2, 4, 0},
                {17, 5, 5, 5, 6, 0},
                {17, 0, 0, 8, 7, 0},
                {17, 9, 8, 8, 10, 10},
                {17, 11, 12, 12, 12, 13},
                {17, 15, 12, 14, 0, 16},
                {17, 12, 12, 12, 18, 19}};

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    userInput = Integer.parseInt(input.getText().toString());// parse input
                } catch (Exception e) {// unable to parse
                    Toast.makeText(getApplicationContext(), R.string.invalid_input, Toast.LENGTH_SHORT).show();
                    return;
                }
                input.setText("",null);//clear EditText

                //analyse matrix,remove element and adjust matrix
                adjuster.analyse(matrix);
                matrix = adjuster.remove(matrix, userInput);
                isEmpty = draw(matrix);
                if (isEmpty) //if matrix contains only 0's
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage(R.string.finished)
                            .setPositiveButton(R.string.Exit, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    finish();// Exit Game..
                                }
                            });
                    builder.create().show();
                }


            }
        });


        paint = new Paint();

        // initailize color resources
        colors = new int[]{
                ResourcesCompat.getColor(getResources(), R.color.white, null),
                ResourcesCompat.getColor(getResources(), R.color.color1, null),
                ResourcesCompat.getColor(getResources(), R.color.color2, null),
                ResourcesCompat.getColor(getResources(), R.color.color3, null),
                ResourcesCompat.getColor(getResources(), R.color.color4, null),
                ResourcesCompat.getColor(getResources(), R.color.color5, null),
                ResourcesCompat.getColor(getResources(), R.color.color6, null)};
        colorCode = new int[20];
        Random r = new Random();
        colorCode[0] = 0; //assign 0 color of white

        // assign each number a random color of the remaining 6 colors
        for (int i = 1; i < 20; i++) {
            colorCode[i] = r.nextInt(6) + 1;
        }
        //Draw the image on the canvas and update Imageview
        draw(matrix);
    }

    private boolean draw(int[][] matrix) {
        boolean empty = true;
        Bitmap bmp = Bitmap.createBitmap(150, 250, Bitmap.Config.ARGB_8888);


        Canvas artist = new Canvas(bmp);

        //draw the picture using array of rectangles and texts
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 6; j++) {
                if (matrix[i][j] != 0) {
                    empty = false;//matrix not empty
                }

                paint.setColor(colors[colorCode[matrix[i][j]]]);
                artist.drawRect(j * 25, i * 25, j * 25 + 25, i * 25 + 25, paint);
                paint.setColor(colors[0]);
                artist.drawText("" + matrix[i][j], j * 25 + 12, i * 25 + 12, paint);
            }
        }
        //update imageView
        canvas.setImageBitmap(bmp);
        return empty;//return whether the matrix was empty

    }
}
