/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package hungarianalgorithm;

/**
 *
 * @author bryan
 */
import java.util.Arrays;
import java.util.Random;
import matrix.Matrix;
import linkedlist.LinkedList;

public class HungarianAlgorithm {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) 
    {
        int size = 10;
        int[][] costMatrixInt = new int[size][size];
        Random rnd = new Random();
        for (int i = 0; i < size; i++)
        {
            for (int j = 0; j < size; j++)
            {
                costMatrixInt[i][j] = rnd.nextInt(99)+1;
            }
        }
//        costMatrixInt = new int[][]{{4,70,81,45,95},{10,4,47,75,78},{22,67,66,34,64},{72,4,94,64,99},{99,99,39,35,79}};
        Matrix costMatrix = new Matrix(costMatrixInt);
        System.out.println(costMatrix.toString() + "\n");
        HungarianAlgorithm ha = new HungarianAlgorithm();
        int[] assignmentArray = ha.Assignment(costMatrix);
        int sum = 0;
        for (int i = 0; i < costMatrix.columnLength(); i++)
        {
            sum += costMatrix.get(i, assignmentArray[i]);
        }
        System.out.println(sum);
    }
    
    private Matrix costMatrix;
    private boolean[] colLines,rowLines;
    private int[] starInColumn, starInRow, primeInRow;
    private int n;
    private int linesToCover=0;
    
    public int[] Assignment(Matrix _matrix)
    {
        costMatrix = new Matrix(Arrays.stream(_matrix.getMatrix()).map(int[]::clone).toArray(int[][]::new));
        if (costMatrix.rowLength()!=costMatrix.columnLength())
        {
            throw new IllegalArgumentException("Cost Matrix is not Square!");
        }
        n = _matrix.rowLength();
        colLines = new boolean[n];
        rowLines = new boolean[n];
        starInColumn = new int[n];
        starInRow = new int[n];
        primeInRow = new int[n];
        
        //row and column reduction
        reduceMatrixRC();
        do
        {
            Arrays.fill(starInColumn, -1);
            Arrays.fill(starInRow, -1);
            Arrays.fill(colLines,false);
            Arrays.fill(rowLines,false);
            Arrays.fill(primeInRow, -1);
            
            //star independent zeroes
            starZeroes();
            //cover columns with a starred zero
            coverStarredColumns();
            //prime uncovered zeroes
            primeZeroes();
            
            //count lines required to cover
            linesToCover=0;
            for (int b = 0; b < n; b++)
            {
                if (colLines[b])
                {
                    linesToCover++;
                }
                if (rowLines[b])
                {
                    linesToCover++;
                }
            }
            //reduce and repeat if not enough lines
            if (linesToCover<n)
            {
                coverReduction();
            }
        } while(linesToCover < n);
        for (int i = 0; i < starInRow.length; i++)
        {
            if (_matrix.get(i, starInRow[i]) == Integer.MAX_VALUE)
            {
                starInRow[i] = -1;
            }
        }
        return starInRow;
    }
    
    private void primeZeroes()
    {
        //WHILE ALL ZEROES NOT COVERED
        boolean isFlagged = true;
        while(isFlagged)
        {
            isFlagged = false;
            //go through all rows and columns to find uncovered zeros
            for (int i = 0; i < n; i++)
            {
                if (!rowLines[i])
                {
                    for (int j = 0; j < n; j++)
                    {
                        if(costMatrix.get(i,j) == 0 && !colLines[j])//looking for uncovered 0
                        {
                            isFlagged = true;
                            if (starInRow[i] != -1)//if there is a starred zero in the row
                            {
                                primeInRow[i] = j;//mark a prime at this position
                                rowLines[i] = true;//cover the corresponding column
                                colLines[starInRow[i]] = false;//uncover the corresponding column of the starred zero
                                j = n;
                            }
                            else//no starred zero in this row
                            {
                                if (starInColumn[j] != -1)
                                {
                                    drawPath(i,j);
                                    //remove all cover lines and primed zeroes
                                    Arrays.fill(colLines,false);
                                    Arrays.fill(rowLines,false);
                                    Arrays.fill(primeInRow, -1);
                                    coverStarredColumns();
                                    i = n;
                                    j = n;
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    private void drawPath(int si, int sj)
    {
        LinkedList<Integer[]> stars = new LinkedList<>();
        LinkedList<Integer[]> primes = new LinkedList<>();
        int i = si;
        int j = sj;
        //starting zero
        primes.append(new Integer[]{i,j});
        //look for star in column
        while(starInColumn[j] != -1)
        {
            //switch to star in column and add
            i = starInColumn[j];
            stars.append(new Integer[]{i,j});
            //look for prime in row and add
            j = primeInRow[i];
            primes.append(new Integer[]{i,j});
        }
        //star primed zeroes on the path and prime starred zeroes
        for (int z = 0; z < stars.length(); z++)
        {
            starInColumn[stars.get(z)[1]] = -1;
            starInRow[stars.get(z)[0]] = -1;
            primeInRow[stars.get(z)[0]] = stars.get(z)[1];
        }
        
        for(int z = 0; z < primes.length(); z++)
        {
            if (primeInRow[primes.get(z)[0]] == primes.get(z)[1])
            {
                primeInRow[primes.get(z)[0]] = -1;
            }
            starInColumn[primes.get(z)[1]] = primes.get(z)[0];
            starInRow[primes.get(z)[0]] = primes.get(z)[1];
        }
    }
    
    private void coverStarredColumns()
    {
        //cover all columns with assignments
        for (int i = 0; i < n; i++)
        {
            colLines[i] = starInColumn[i] != -1;
        }
    }
    
    private void starZeroes()
    {
        //assign as many jobs as possible
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n; j++)
            {
                if (costMatrix.get(i,j) == 0 && starInColumn[j] == -1 && starInRow[i] == -1)
                {
                    starInColumn[j] = i;
                    starInRow[i] = j;
                }
            }
        }
    }
    
    private void reduceMatrixRC()
    {
        //reduce rows
        for (int i = 0; i < costMatrix.rowLength(); i++)
        {
            int[] row = costMatrix.getRow(i);
            int minVal = row[minIndex(row)];
            for (int j = 0; j < row.length; j++)
            {
                row[j] -= minVal;
                costMatrix.set(i, j, row[j]);
            }
        }
        //do the same for columns
        for (int i = 0; i < costMatrix.columnLength(); i++)
        {
            int[] column = costMatrix.getColumn(i);
            int minVal = column[minIndex(column)];
            for (int j = 0; j < column.length; j++)
            {
                column[j] -= minVal;
                costMatrix.set(j,i,column[j]);
            }
        }
    }
    
    private void coverReduction()
    {
        int minVal = Integer.MAX_VALUE;
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n; j++)
            {
                if (!rowLines[i] && !colLines[j])
                {
                    if (costMatrix.get(i, j) < minVal)
                    {
                        minVal = costMatrix.get(i,j);
                    }
                }
            }
        }
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n; j++)
            {
                if (!rowLines[i] && !colLines[j])
                {
                    costMatrix.set(i,j,costMatrix.get(i, j)-minVal);
                }
                else if (rowLines[i] && colLines[j])
                {
                    costMatrix.set(i,j,costMatrix.get(i,j)+minVal);
                }
            }
        }
    }

    private static int minIndex(int[] array)
    {
        int minIndex = 0;
        int minVal = array[0];
        for (int i = 0; i < array.length; i++)
        {
            if (array[i] < minVal)
            {
                minIndex = i;
                minVal = array[i];
            }
        }
        return minIndex;
    }
    
    private static int minVal(LinkedList<Integer> list)
    {
        if (list.length() == 0)
        {
            return 0;
        }
        int minVal = list.get(0);
        for (int i = 0; i < list.length(); i++)
        {
            if (list.get(i) < minVal)
            {
                minVal = list.get(i);
            }
        }
        return minVal;
    }
}
