/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mandelbrot;

/**
 *
 * @author don
 */ 
enum Status {UNTOUCHED, UNDERWAY, CONVERGED, ESCAPED, MAXITERATIONS, OSCILLATES};

class IterationRecord {		// fractal record giving point location and other attributes
    
    float x;			// x coordinate
    float y;			// y coordinate
    long count;			// iteration count
    Status status;		// processing status of point (x,y)

    IterationRecord(float xCoord, float yCoord, long iterCount, Status currentStatus) {
        x = xCoord;
        y = yCoord;
        count = iterCount;
        status = currentStatus;
    }

    void print() {
        System.out.println(" x=" + x + " y=" + y + " count=" + count + " Status=" + status);
    }
}

class ComplexNumber {

    float x;
    float y;

    ComplexNumber(float xCoord, float yCoord) {
        x = xCoord;
        y = yCoord;
    }

    ComplexNumber() {
        x = 0.0f;
        y = 0.0f;
    }

    static ComplexNumber add(ComplexNumber number1, ComplexNumber number2) {
        ComplexNumber sum = new ComplexNumber();
        sum.x = number1.x + number2.x;
        sum.y = number1.y + number2.y;
        return sum;
    }

    static ComplexNumber multiply(ComplexNumber number1, ComplexNumber number2) {
        ComplexNumber product = new ComplexNumber();
        product.x = (number1.x * number2.x) - (number1.y * number2.y);
        product.y = (number1.x * number2.y) + (number1.y * number2.x);
        return product;
    }

    static double magnitude(ComplexNumber number) {
        double z;
        z = Math.sqrt(Math.pow(number.x, 2) + Math.pow(number.y, 2));
        return z;
    }

    void print() {
        System.out.println(" x = " + this.x);
        System.out.println(" y = " + this.y);
    }
}

public class Mandelbrot {

    static IterationRecord mandelbrotCalc(ComplexNumber z, ComplexNumber c) {
        int iterationMax = 10;
        Status status = Status.UNDERWAY;
        ComplexNumber iterateZ = new ComplexNumber(z.x, z.y);
        int iterationCounter = 0;
        for (iterationCounter = 1; iterationCounter <= iterationMax; iterationCounter++) {
            iterateZ = ComplexNumber.add(ComplexNumber.multiply(z, z), c);
        }
        if (iterationCounter >= 10) {
            status = Status.MAXITERATIONS;
        }
        if (ComplexNumber.magnitude(iterateZ) > 2) {
            status = Status.ESCAPED;
        }
        IterationRecord record = new IterationRecord(z.x, z.y, iterationCounter, status);
        return record;
    }

    public static void main(String[] args) {
        ComplexNumber upperleft = new ComplexNumber(-2.0f, 1.5f);
        ComplexNumber upperright = new ComplexNumber(1.0f, 1.5f);
        ComplexNumber lowerleft = new ComplexNumber(-2.0f, -1.5f);
        ComplexNumber lowerright = new ComplexNumber(1.0f, -1.5f);
        float interval = 0.25f;
        int width = 1 + Math.abs(Math.round((upperleft.x - upperright.x) / interval));
        int height = 1 + Math.abs(Math.round((upperleft.y - lowerleft.y) / interval));

        float xfirst = upperleft.x;
        float yfirst = lowerleft.y;

        float x = xfirst;
        float y = yfirst;
        ComplexNumber z = new ComplexNumber(x, y);
        ComplexNumber c = new ComplexNumber(0.0f, 0.0f);
        IterationRecord[][] records = new IterationRecord[width][height];
        System.out.println(" Size of Iterationrecord[][] = " + records.length + "," + records[0].length);
        for (int i = 0; i < width; i++) {
            x = xfirst;
            for (int j = 0; j < height; j++) {;
                records[i][j] = new IterationRecord(x, y, 0l, Status.UNTOUCHED);
                z = new ComplexNumber(x, y);
                records[i][j] = mandelbrotCalc(z, c);
//				System.out.println(" x=" + records[i][j].x + " y=" +records[i][j].y);
                x += interval;
            }
            y += interval;
        }

        for (IterationRecord[] eachRecord : records) {
            for (IterationRecord elem : eachRecord) {
                System.out.println(" x=" + elem.x + " Y=" + elem.y + " iterations=" + elem.count + " " + elem.status);
            }
        }
    }
}
