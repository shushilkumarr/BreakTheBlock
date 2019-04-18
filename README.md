# BreakTheBlock
A tetris like game.

This game programitically is totally based on matrix manipulation. The game board is reprosented as a matrix. According to the player's input, this matrix is manipulated and the board is redrawn.

The first step involves scanning the matrix and dividing it to blocks for easier matrix manipulation in later stages
```java
public void analyse(int[][] matrix) {
        indices.removeAll(indices);
        for (int i = 1; i <= 20; i++)
            indices.add(new ArrayList<int[]>());

        for (int i = 0; i < 10; i++)
            for (int j = 0; j < 6; j++) {
                try {

                    indices.get(matrix[i][j] - 1).add(new int[]{i, j});
                } catch (IndexOutOfBoundsException e) {
                }
            }

    }
```

Then, get the user's input and remove the elements from the matrix, chech if the matrix "now" is adjustable?. if yes, then adjust it until it is no longer adjustable. then redraw the image using the new matrix.
```java
public int[][] remove(int[][] mat, int n) {
        for (int i = 0; i < indices.get(n - 1).size(); i++)
            mat[indices.get(n - 1).get(i)[0]][indices.get(n - 1).get(i)[1]] = 0;
        while (adjustable(mat)) {
        }
        analyse(mat);
        return mat;

}
    
private boolean adjustable(int[][] mat) {
        analyse(mat);
        boolean adjustable;
        for (int n = 1; n <= 20; n++) {
            adjustable = true;
            for (int i = indices.get(n - 1).size() - 1; i >= 0; i--) {
                try {
                    if (indices.get(n - 1).size() > 0 && !(mat[indices.get(n - 1).get(i)[0] + 1][indices.get(n - 1).get(i)[1]] == 0 || mat[indices.get(n - 1).get(i)[0] + 1][indices.get(n - 1).get(i)[1]] == n)) {
                        adjustable = false;
                        continue;
                    }
                } catch (Exception e) {
                    adjustable = false;
                }

                if (adjustable) {
                    adjust(mat, n);
                    return true;
                }
            }
        }


        return false;
}

private int[][] adjust(int[][] mat, int num) {
        ArrayList<int[]> newPos = new ArrayList<>();
        for (int i = indices.get(num - 1).size() - 1; i >= 0; i--) {
            try {
                mat[indices.get(num - 1).get(i)[0] + 1][indices.get(num - 1).get(i)[1]] = mat[indices.get(num - 1).get(i)[0]][indices.get(num - 1).get(i)[1]];
                mat[indices.get(num - 1).get(i)[0]][indices.get(num - 1).get(i)[1]] = 0;
                newPos.add(new int[]{indices.get(num - 1).get(i)[0] + 1, indices.get(num - 1).get(i)[1]});
            } catch (ArrayIndexOutOfBoundsException e) {
            }
        }
        for (int i = 0; i < newPos.size(); i++) {
            mat[newPos.get(i)[0]][newPos.get(i)[1]] = num;
        }
        return mat;


    }
}
```

Now, to the Drawing the board part..
```java

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
```
These Methods are called each time the user gives the input inside the onclick of the button.
```java
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
```
