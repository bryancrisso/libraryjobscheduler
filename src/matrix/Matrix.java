/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package matrix;

import java.util.Arrays;

/**
 *
 * @author bryan
 */
public class Matrix
{
    
    /**
     * @param args the command line arguments
     */
    
    
    private final int[][] matrix;
    
    public Matrix(int[][] _matrix)
    {
        matrix = Arrays.stream(_matrix).map(int[]::clone).toArray(int[][]::new);
    }
    
    public Matrix(int rows, int columns)
    {
        matrix = new int[rows][columns];
    }
    
    public void set(int row, int column, int val)
    {
        matrix[row][column] = val;
    }
    
    public int get(int row, int column)
    {
        return matrix[row][column];
    }
    
    public int rowLength()
    {
        return matrix.length;
    }
    
    public int columnLength()
    {
        return matrix[0].length;
    }
    
    public int[] getRow(int row)
    {
        return matrix[row];
    }
    
    public int[] getColumn(int column)
    {
        int[] returnval = new int[matrix[0].length];
        for (int i = 0; i < matrix.length; i++)
        {
            returnval[i] = matrix[i][column];
        }
        return returnval;
    }
    
    
    public int[][] getMatrix()
    {
        return matrix;
    }
    
    
    @Override
    public String toString()
    {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < matrix.length-1; i++)
        {
            str.append(Arrays.toString(matrix[i]));
            str.append("\n");
        }
        str.append(Arrays.toString(matrix[matrix.length-1]));
        return str.toString();
    }
}
