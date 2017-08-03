package com.looi.looi;

import java.util.ArrayList;

/**
 * This class contains useful calculations. Most of the static methods are pure
 * functions. These methods may be accessed through a LooiObject or through 
 * static method calls. This class is uninstantiable except by members of this 
 * package.
 * @author peter_000
 */
public interface Calculations
    {
        /**
         * This class is uninstantiable except by members of this package
         */
        
        
        /**
         * Converts any angle to the same angle between 0 and 360 degrees
         * @param angle The angle to convert
         * @return The resulting angle
         */
        public static double simplifyAngle(double angle)
            {
                if(angle == -Double.POSITIVE_INFINITY || angle == Double.POSITIVE_INFINITY)
                    {
                        //WHy the heck is your angle infinty...... I'm just being SAFE!
                        return 0;
                    }
                while(angle < 0)
                    {
                        angle += 360;
                    }
                angle = angle % 360;
                return angle;
            }
        /**
         * Finds the true positive difference between two angles
         * @param a1 One angle
         * @param a2 The other angle
         * @return The difference
         */
        public static double angleDifference(double a1, double a2)
            {
                a1 = simplifyAngle(a1);
                a2 = simplifyAngle(a2);
                if(Math.abs(a1 - a2) > 180)
                    {
                        return 360 - Math.abs(a1 - a2);
                    }
                return Math.abs(a1 - a2);
            }
        /**
         * Finds the sign of a number
         * @param d The number to find the sign of
         * @return The sign in the form of an int
         */
        public static int sign(double d)
            {
                if(d > 0)
                    {
                        return 1;
                    }
                else if(d < 0)
                    {
                        return -1;
                    }
                else
                    {
                        return 1;
                    }
            }
        /**
         * Finds the sin of theta 
         * @param theta The angle in degrees
         * @return The sin of theta
         */
        public static double sin(double theta)//works in degrees no table
            {
                return Math.sin(Math.toRadians(theta));
            }
        /**
         * Finds the cos of theta
         * @param theta The angle in degrees
         * @return The cos of theta
         */
        public static double cos(double theta)//works in degrees No table
            {
                return Math.cos(Math.toRadians(theta));
            }
        /**
         * Finds the tan of theta
         * @param theta The angle in degrees
         * @return The tan of theta
         */
        public static double tan(double theta)
            {
                return Math.tan(Math.toRadians(theta));
            }
        /**
         * Finds the arctan of the ratio
         * @param ratio The ratio
         * @return The resulting angle in degrees
         */
        public static double arctan(double ratio)
            {
                return Math.toDegrees(Math.atan(ratio));
            }
        /**
         * Finds the arcsin of the ratio
         * @param ratio The ratio
         * @return The resulting angle in degrees
         */
        public static double arcsin(double ratio)
            {
                return Math.toDegrees(Math.asin(ratio));
            }
        /**
         * Finds the arcsin of the ratio
         * @param ratio The ratio
         * @return The resulting angle in degrees
         */
        public static double arccos(double ratio)
            {
                return Math.toDegrees(Math.acos(ratio));
            }
        
        
        /**
         * Returns the quotient of a and b, but in the context of 3D scaling
         * @param a The dividend
         * @param b The divisor
         * @return The quotient
         */
        public static double divide3D(double a, double b)//divides a by b, and then takes absolute value
            {
                
                if(a > .1 && b > .1)
                    {
                        return a/b;
                    }
                else
                    {
                        return 7500;
                    }
            }
        
        /**
         * Merge sorts a double[] from least to greatest
         * @param messedUpArray
         * @return Sorted array
         */
        public static double[] mergeSort(double[] messedUpArray)
            {
                double[] firstHalf = new double[messedUpArray.length/2];
                for(int i = 0; i < messedUpArray.length/2; i++)
                    {
                        firstHalf[i] = messedUpArray[i];
                    }
                double[] lastHalf = new double[messedUpArray.length - messedUpArray.length/2];
                for(int i = messedUpArray.length/2; i < messedUpArray.length; i++)
                    {
                        lastHalf[i-messedUpArray.length/2] = messedUpArray[i];
                    }
                if(firstHalf.length < 2 && lastHalf.length < 2)
                    {
                        return MergeSortAlgorithms.mergeSort(firstHalf,lastHalf);
                    }
                return MergeSortAlgorithms.mergeSort(mergeSort(firstHalf),mergeSort(lastHalf));
                
            }
        
        
        /**
         * Merge sorts a LooiObject[] from least to greatest
         * @param messedUpArray
         * @return Sorted array
         */
        public static LooiObject[] mergeSort(LooiObject[] messedUpArray)//sorts from least to greatest
            {
                LooiObject[] firstHalf = new LooiObject[messedUpArray.length/2];
                for(int i = 0; i < messedUpArray.length/2; i++)
                    {
                        firstHalf[i] = messedUpArray[i];
                    }
                LooiObject[] lastHalf = new LooiObject[messedUpArray.length - messedUpArray.length/2];
                for(int i = messedUpArray.length/2; i < messedUpArray.length; i++)
                    {
                        lastHalf[i-messedUpArray.length/2] = messedUpArray[i];
                    }
                if(firstHalf.length < 2 && lastHalf.length < 2)
                    {
                        return MergeSortAlgorithms.mergeSort(firstHalf,lastHalf);
                    }
                return MergeSortAlgorithms.mergeSort(mergeSort(firstHalf),mergeSort(lastHalf));
            }
        
        /**
         * Merge sorts an ArrayList<LooiObject> from least to greatest
         * @param messedUpArray
         * @return Sorted array
         */
        public static ArrayList<LooiObject> mergeSort(ArrayList<LooiObject> messedUpArray)//sorts from least to greatest
            {
                ArrayList<LooiObject> firstHalf = new ArrayList<LooiObject>(messedUpArray.size()/2);
                for(int i = 0; i < messedUpArray.size()/2; i++)
                    {
                        firstHalf.add(i,messedUpArray.get(i));
                    }
                ArrayList<LooiObject> lastHalf = new ArrayList<LooiObject>(messedUpArray.size() - messedUpArray.size()/2);
                for(int i = messedUpArray.size()/2; i < messedUpArray.size(); i++)
                    {
                        lastHalf.add(i-messedUpArray.size()/2,messedUpArray.get(i));
                    }
                if(firstHalf.size() < 2 && lastHalf.size() < 2)
                    {
                        return MergeSortAlgorithms.mergeSort(firstHalf,lastHalf);
                    }
                return MergeSortAlgorithms.mergeSort(mergeSort(firstHalf),mergeSort(lastHalf));
            }
        
        class MergeSortAlgorithms
            {
                private static ArrayList<LooiObject> mergeSort(ArrayList<LooiObject> sortedArray, ArrayList<LooiObject> sortedArray2)//This one only works with pre-sorted arrays given by the other mergeSort method.
                    {
                        ArrayList<LooiObject> build1 = new ArrayList<LooiObject>(sortedArray.size() + sortedArray2.size());
                        int nextAvailibleIndex = 0;
                        int sAIndex = 0;
                        int sA2Index = 0;
                        while(sAIndex <= sortedArray.size() - 1 || sA2Index <= sortedArray2.size() - 1)
                            {
                                if(sAIndex == sortedArray.size())
                                    {
                                        build1.add(nextAvailibleIndex,sortedArray2.get(sA2Index));
                                        nextAvailibleIndex++;
                                        sA2Index++;
                                    }
                                else if(sA2Index == sortedArray2.size())
                                    {
                                        build1.add(nextAvailibleIndex,sortedArray.get(sAIndex));
                                        nextAvailibleIndex++;
                                        sAIndex++;
                                    }
                                else if(sortedArray.get(sAIndex).getLayer() < sortedArray2.get(sA2Index).getLayer())
                                    {
                                        build1.add(nextAvailibleIndex,sortedArray.get(sAIndex));
                                        nextAvailibleIndex++;
                                        sAIndex++;
                                    }
                                else
                                    {
                                        build1.add(nextAvailibleIndex,sortedArray2.get(sA2Index));
                                        nextAvailibleIndex++;
                                        sA2Index++;
                                    }
                            }
                        return build1;
                    }
                private static LooiObject[] mergeSort(LooiObject[] sortedArray, LooiObject[] sortedArray2)//This one only works with pre-sorted arrays given by the other mergeSort method.
                    {
                        LooiObject[] build1 = new LooiObject[sortedArray.length + sortedArray2.length];
                        int nextAvailibleIndex = 0;
                        int sAIndex = 0;
                        int sA2Index = 0;
                        while(sAIndex <= sortedArray.length - 1 || sA2Index <= sortedArray2.length - 1)
                            {
                                if(sAIndex == sortedArray.length)
                                    {
                                        build1[nextAvailibleIndex] = sortedArray2[sA2Index];
                                        nextAvailibleIndex++;
                                        sA2Index++;
                                    }
                                else if(sA2Index == sortedArray2.length)
                                    {
                                        build1[nextAvailibleIndex] = sortedArray[sAIndex];
                                        nextAvailibleIndex++;
                                        sAIndex++;
                                    }
                                else if(sortedArray[sAIndex].getLayer() < sortedArray2[sA2Index].getLayer())
                                    {
                                        build1[nextAvailibleIndex] = sortedArray[sAIndex];
                                        nextAvailibleIndex++;
                                        sAIndex++;
                                    }
                                else
                                    {
                                        build1[nextAvailibleIndex] = sortedArray2[sA2Index];
                                        nextAvailibleIndex++;
                                        sA2Index++;
                                    }
                            }
                        return build1;
                    }
                private static double[] mergeSort(double[] sortedArray, double[] sortedArray2)//This one only works with two ALREADY SORTED ARRAYS- CAREFUL
                    {
                        //int[] returnMe = new int[sortedArray.length + sortedArray2.length];
                        ArrayList<Double> build1 = new ArrayList<Double>();
                        int sAIndex = 0;
                        int sA2Index = 0;
                        while(sAIndex <= sortedArray.length - 1 || sA2Index <= sortedArray2.length - 1)
                            {
                                if(sAIndex == sortedArray.length)
                                    {
                                        build1.add(sortedArray2[sA2Index]);
                                        sA2Index++;
                                        //System.out.println("A");
                                    }
                                else if(sA2Index == sortedArray2.length)
                                    {
                                        build1.add(sortedArray[sAIndex]);
                                        sAIndex++;
                                        //System.out.println("B");
                                    }
                                else if(sortedArray[sAIndex] < sortedArray2[sA2Index])
                                    {
                                        build1.add(sortedArray[sAIndex]);
                                        sAIndex++;
                                        //System.out.println("C");
                                    }
                                else
                                    {
                                        build1.add(sortedArray2[sA2Index]);
                                        sA2Index++;
                                        //System.out.println("D");
                                    }
                            }
                        double[] returnMe = new double[sortedArray.length + sortedArray2.length];
                        for(int i = 0; i < build1.size(); i++)
                            {
                                returnMe[i] = build1.get(i);
                            }
                        return returnMe;
                    }
            }
    }