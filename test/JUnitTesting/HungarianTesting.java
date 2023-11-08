/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package JUnitTesting;

import hungarianalgorithm.HungarianAlgorithm;
import matrix.Matrix;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author bryan
 */
public class HungarianTesting {
    
    HungarianAlgorithm hAlg;
    
    @Before
    public void setUp() 
    {
        hAlg = new HungarianAlgorithm();
    }
    
    @Test
    public void test_A()
    {
        Matrix mat = new Matrix(new int[][]
        {
            {63,51,22},
            {14,13,29},
            {40,88,84}
        });
        int[] assignmentArray = hAlg.Assignment(mat);
        int cost = 0;
        for (int i = 0; i < assignmentArray.length; i++)
        {
            cost += mat.get(i, assignmentArray[i]);
        }
        assertEquals(75, cost);
    }
    
    @Test
    public void test_B()
    {
        Matrix mat = new Matrix(new int[][]
        {
            {71,46,44},
            {79,99,54},
            {42,8,54}
        });
        int[] assignmentArray = hAlg.Assignment(mat);
        int cost = 0;
        for (int i = 0; i < assignmentArray.length; i++)
        {
            cost += mat.get(i, assignmentArray[i]);
        }
        assertEquals(131, cost);
    }
    
    @Test
    public void test_C()
    {
        Matrix mat = new Matrix(new int[][]
        {
            {8,69,71,17},
            {65,47,18,91},
            {8,52,90,84},
            {70,45,71,67}
        });
        int[] assignmentArray = hAlg.Assignment(mat);
        int cost = 0;
        for (int i = 0; i < assignmentArray.length; i++)
        {
            cost += mat.get(i, assignmentArray[i]);
        }
        assertEquals(88, cost);
    }
    
    @Test
    public void test_D()
    {
        Matrix mat = new Matrix(new int[][]
        {
            {64,97,73,89},
            {55,13,8,68},
            {25,34,43,54},
            {27,78,90,5}
        });
        int[] assignmentArray = hAlg.Assignment(mat);
        int cost = 0;
        for (int i = 0; i < assignmentArray.length; i++)
        {
            cost += mat.get(i, assignmentArray[i]);
        }
        assertEquals(111, cost);
    }
    
    @Test
    public void test_E()
    {
        Matrix mat = new Matrix(new int[][]
        {
            {66,53,41,42,31},
            {15,82,15,96,91},
            {89,82,93,52,12},
            {71,28,32,89,57},
            {78,90,58,15,94}
        });
        int[] assignmentArray = hAlg.Assignment(mat);
        int cost = 0;
        for (int i = 0; i < assignmentArray.length; i++)
        {
            cost += mat.get(i, assignmentArray[i]);
        }
        assertEquals(111, cost);
    }
    
    @Test
    public void test_F()
    {
        Matrix mat = new Matrix(new int[][]
        {
            {9,53,98,33,72},
            {9,35,76,3,29},
            {84,91,70,63,78},
            {71,68,25,59,76},
            {93,81,76,86,42}
        });
        int[] assignmentArray = hAlg.Assignment(mat);
        int cost = 0;
        for (int i = 0; i < assignmentArray.length; i++)
        {
            cost += mat.get(i, assignmentArray[i]);
        }
        assertEquals(170, cost);
    }
    
    @Test
    public void test_G()
    {
        Matrix mat = new Matrix(new int[][]
        {
            {30,57,32,66,52,74},
            {70,84,91,63,11,83},
            {44,73,9,42,56,84},
            {87,32,15,82,76,5},
            {78,25,2,16,55,56},
            {89,42,86,28,24,3}
        });
        int[] assignmentArray = hAlg.Assignment(mat);
        int cost = 0;
        for (int i = 0; i < assignmentArray.length; i++)
        {
            cost += mat.get(i, assignmentArray[i]);
        }
        assertEquals(101, cost);
    }
    
    @Test
    public void test_H()
    {
        Matrix mat = new Matrix(new int[][]
        {
            {29,39,2,14,50,20},
            {5,66,63,73,69,39},
            {54,30,62,83,45,83},
            {84,75,85,89,60,38},
            {72,26,43,19,39,14},
            {31,60,79,24,66,82}
        });
        int[] assignmentArray = hAlg.Assignment(mat);
        int cost = 0;
        for (int i = 0; i < assignmentArray.length; i++)
        {
            cost += mat.get(i, assignmentArray[i]);
        }
        assertEquals(135, cost);
    }
    
    @Test
    public void test_I()
    {
        Matrix mat = new Matrix(new int[][]
        {
            {28,58,24,53,92,53,80},
            {81,81,53,25,63,37,84},
            {40,92,82,80,48,59,16},
            {67,23,90,41,24,68,24},
            {96,87,16,97,89,9,12},
            {67,9,96,32,67,24,4},
            {83,15,69,35,57,75,28}
        });
        int[] assignmentArray = hAlg.Assignment(mat);
        int cost = 0;
        for (int i = 0; i < assignmentArray.length; i++)
        {
            cost += mat.get(i, assignmentArray[i]);
        }
        assertEquals(141, cost);
    }
    
    @Test
    public void test_J()
    {
        Matrix mat = new Matrix(new int[][]
        {
            {58,39,65,80,32,91,57},
            {90,71,71,57,65,25,73},
            {34,16,25,74,98,77,14},
            {37,94,78,4,39,28,96},
            {66,34,6,79,46,19,4},
            {91,20,72,93,68,12,10},
            {24,6,25,62,94,86,75}
        });
        int[] assignmentArray = hAlg.Assignment(mat);
        int cost = 0;
        for (int i = 0; i < assignmentArray.length; i++)
        {
            cost += mat.get(i, assignmentArray[i]);
        }
        assertEquals(117, cost);
    }
    
    @Test
    public void test_K()
    {
        Matrix mat = new Matrix(new int[][]
        {
            {90,83,64,77,55,72,30,52},
            {53,58,74,51,16,51,57,23},
            {64,27,6,70,60,24,13,5},
            {56,47,52,58,87,3,79,13},
            {53,27,20,63,51,24,97,42},
            {32,82,21,9,31,47,5,25},
            {12,64,39,57,22,41,98,39},
            {51,1,86,55,32,74,21,30}
        });
        int[] assignmentArray = hAlg.Assignment(mat);
        int cost = 0;
        for (int i = 0; i < assignmentArray.length; i++)
        {
            cost += mat.get(i, assignmentArray[i]);
        }
        assertEquals(96, cost);
    }
    
    @Test
    public void test_L()
    {
        Matrix mat = new Matrix(new int[][]
        {
            {11,37,22,42,93,48,91,92},
            {67,56,63,34,45,23,8,40},
            {64,49,86,81,70,59,16,35},
            {52,59,71,7,10,12,88,55},
            {40,5,68,8,74,22,64,56},
            {6,93,16,24,63,25,75,82},
            {37,49,65,28,70,55,17,15},
            {55,49,58,47,83,9,80,68}
        });
        int[] assignmentArray = hAlg.Assignment(mat);
        int cost = 0;
        for (int i = 0; i < assignmentArray.length; i++)
        {
            cost += mat.get(i, assignmentArray[i]);
        }
        assertEquals(116, cost);
    }
    
    @Test
    public void test_M()
    {
        Matrix mat = new Matrix(new int[][]
        {
            {53,46,39,11,69,94,15,44,89},
            {16,39,65,95,64,32,25,59,45},
            {92,76,96,20,44,63,18,42,40},
            {34,8,70,42,79,64,79,19,17},
            {88,62,26,17,58,29,18,99,33},
            {68,27,20,74,89,26,80,8,47},
            {29,76,80,74,55,48,4,3,28},
            {47,81,60,33,13,84,14,24,61},
            {67,81,40,93,21,40,12,60,83}
        });
        int[] assignmentArray = hAlg.Assignment(mat);
        int cost = 0;
        for (int i = 0; i < assignmentArray.length; i++)
        {
            cost += mat.get(i, assignmentArray[i]);
        }
        assertEquals(152, cost);
    }
    
    @Test
    public void test_N()
    {
        Matrix mat = new Matrix(new int[][]
        {
            {97,9,53,80,2,58,97,19,30},
            {70,93,3,4,31,56,50,12,37},
            {87,11,42,44,8,60,31,44,73},
            {24,24,61,7,64,99,64,72,7},
            {63,70,26,40,22,47,67,14,43},
            {71,30,37,3,49,85,23,4,31},
            {50,16,60,55,16,89,50,71,25},
            {88,59,34,94,21,88,94,66,45},
            {99,95,94,75,96,77,35,41,99}
        });
        int[] assignmentArray = hAlg.Assignment(mat);
        int cost = 0;
        for (int i = 0; i < assignmentArray.length; i++)
        {
            cost += mat.get(i, assignmentArray[i]);
        }
        assertEquals(186, cost);
    }
    
    @Test
    public void test_O()
    {
        Matrix mat = new Matrix(new int[][]
        {
            {20,11,47,55,47,55,55,26,17,31},
            {19,17,17,1,80,79,5,17,65,50},
            {37,24,17,57,96,12,67,15,32,46},
            {72,53,33,8,6,63,73,38,82,2},
            {20,80,27,3,23,23,24,35,38,50},
            {27,89,49,86,91,64,8,23,53,90},
            {53,90,89,61,12,55,17,28,47,6},
            {57,65,16,57,92,71,42,99,69,45},
            {49,17,6,64,45,90,79,95,18,7},
            {1,36,53,68,86,95,47,23,87,79}
        });
        int[] assignmentArray = hAlg.Assignment(mat);
        int cost = 0;
        for (int i = 0; i < assignmentArray.length; i++)
        {
            cost += mat.get(i, assignmentArray[i]);
        }
        assertEquals(98, cost);
    }
    
    @Test
    public void test_P()
    {
        Matrix mat = new Matrix(new int[][]
        {
            {47,71,69,76,6,76,17,13,36,30},
            {29,45,34,83,65,62,42,64,6,71},
            {49,34,83,56,67,57,86,74,68,4},
            {47,25,72,43,47,1,45,71,81,76},
            {22,99,20,63,78,49,6,73,4,47},
            {82,2,56,34,69,56,66,15,77,72},
            {72,60,38,15,7,70,50,32,42,97},
            {62,44,7,26,95,29,54,67,79,60},
            {25,70,73,99,83,4,99,53,57,62},
            {83,87,77,38,4,37,2,5,96,90}
        });
        int[] assignmentArray = hAlg.Assignment(mat);
        int cost = 0;
        for (int i = 0; i < assignmentArray.length; i++)
        {
            cost += mat.get(i, assignmentArray[i]);
        }
        assertEquals(77, cost);
    }
}
