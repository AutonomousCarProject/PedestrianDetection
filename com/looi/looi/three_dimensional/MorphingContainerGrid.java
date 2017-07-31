/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.looi.looi.three_dimensional;
//NEW CLASS!
import com.looi.looi.Container;
import com.looi.looi.utilities.Supplier;
import com.looi.looi.utilities.TwoInsOneOut;
import java.awt.Color;

/**
 *
 * @author peter_000
 */
public class MorphingContainerGrid extends Container implements Visual
    {
        
        
        private Visual[][] quadrilaterals;//we assume that they're quadrilaterals, but if you really want, you can replace one or two with another subclass of Visual
        private Visual[][] farQuadrilaterals;
        private Point3D[][] points;
        private MorphingContainer[][] morphingContainers;
        private Supplier<View> view;
        private TwoInsOneOut<Integer,Integer,Color> color;
        
        public MorphingContainerGrid (Point3D p1, Point3D p2, Point3D p3, Point3D p4, double unitSize, double morphContainerSize, Supplier<Double> morphContainerActivationDistance, TwoInsOneOut<Integer,Integer,Color> color, boolean activate, Supplier<View> view)//unitSIze is actually average unit size
            {
                this.view = view;
                this.color = color;
                int p1p2Columns = (int)(p1.get3DDistance(p2)/unitSize)+1;
                int p3p4Columns = (int)(p3.get3DDistance(p4)/unitSize)+1; 
                int pointColumns = (int)((p1p2Columns + p3p4Columns)/2);
                
                int p1p4Rows = (int)(p1.get3DDistance(p4)/unitSize)+1;
                int p2p3Rows = (int)(p2.get3DDistance(p3)/unitSize)+1;
                int pointRows = (int)((p1p4Rows + p2p3Rows)/2);
                points = new Point3D[pointRows][pointColumns];
                quadrilaterals = new Visual[getQuadrilateralRows()][getQuadrilateralColumns()];
                
                int quadrilateralsPerContainer = (int)(morphContainerSize / unitSize);
                
                int morphContainerRows = getQuadrilateralRows() / quadrilateralsPerContainer + 1;
                int morphContainerColumns = getQuadrilateralColumns() / quadrilateralsPerContainer + 1;
                
                morphingContainers = new MorphingContainer[morphContainerRows][morphContainerColumns];
                for(int r = 0; r < morphingContainers.length; r++)
                    {
                        for(int c = 0; c < morphingContainers[r].length; c++)
                            {
                                morphingContainers[r][c] = new MorphingContainer(morphContainerActivationDistance,view);
                            }
                    }
                
                
                for(int r = 0; r < pointRows; r++)
                    {
                        
                        for(int c = 0; c < pointColumns; c++)//WHY DOES THIS WORK TO VERY SPECIFIC COORDS?
                            {
                                Point3D pointOnUpperSide = p1.moveTowards3D(p2,p1.get3DDistance(p2)*c*1.0/(pointColumns-1));//2D point
                                Point3D pointOnLowerSide = p4.moveTowards3D(p3,p3.get3DDistance(p4)*c*1.0/(pointColumns-1));//2D point 
                                Point3D finalPoint = pointOnUpperSide.moveTowards3D(pointOnLowerSide,pointOnUpperSide.get3DDistance(pointOnLowerSide)*r*1.0/(pointRows-1));
                                points[r][c] = finalPoint;
                            }
                    }
                for(int r = 0; r < pointRows-1; r++)
                    {
                        
                        for(int c = 0; c < pointColumns-1; c++)
                            {
                                
                                quadrilaterals[r][c] = newQuadrilateral3D(points[r][c],points[r+1][c],points[r+1][c+1],points[r][c+1],color.make(r,c),activate,view);
                                morphingContainers[r/quadrilateralsPerContainer][c/quadrilateralsPerContainer].getCloseVisual().add(quadrilaterals[r][c]);
                                
                                
                            }
                    }
                farQuadrilaterals = new Visual[morphContainerRows][morphContainerColumns];
                for(int r = 0; r < morphContainerRows; r++)
                    {
                        for(int c = 0; c < morphContainerColumns; c++)
                            {
                                int pointRow = r * quadrilateralsPerContainer;
                                int pointCol = c * quadrilateralsPerContainer;
                                int bottomPointRow = pointRow + quadrilateralsPerContainer;
                                int rightPointCol = pointCol + quadrilateralsPerContainer;
                                if(bottomPointRow >= points.length)
                                    {
                                        bottomPointRow = points.length - 1;
                                    }
                                if(rightPointCol >= points[0].length)
                                    {
                                        rightPointCol = points[0].length - 1;
                                    }
                                
                                Color quadrilateralColor = findAvgColor(pointRow,pointCol,bottomPointRow,rightPointCol);
                                farQuadrilaterals[r][c] = new Polygon3D(quadrilateralColor,view,points[pointRow][pointCol],points[pointRow][rightPointCol],points[bottomPointRow][rightPointCol],points[bottomPointRow][pointCol]);
                                morphingContainers[r][c].getFarVisual().add(farQuadrilaterals[r][c]);
                            }
                    }
                if(activate)
                    {
                        activate();
                    }
            }
        protected Color findAvgColor(int startRow, int startCol, int oneAfterEndRow, int oneAfterEndCol)
            {
                int totalR = 0;
                int totalG = 0;
                int totalB = 0;
                int totalQuadrilaterals = 0;
                for(int r = startRow; r < oneAfterEndRow; r++)
                    {
                        for(int c = startCol; c < oneAfterEndCol; c++)
                            {
                                Visual q = quadrilaterals[r][c];
                                totalR += q.getColor().getRed();
                                totalG += q.getColor().getGreen();
                                totalB += q.getColor().getBlue();
                                totalQuadrilaterals++;
                            }
                    }
                if(totalQuadrilaterals == 0)
                    {
                        return Color.BLACK;
                    }
                int avgR = totalR / totalQuadrilaterals;
                int avgG = totalG / totalQuadrilaterals;
                int avgB = totalB / totalQuadrilaterals;
                return new Color(avgR,avgG,avgB);
            }
        public void delete(Polygon3D p)
            {
                for(int r = 0; r < quadrilaterals.length; r++)
                    {
                        for(int c = 0; c < quadrilaterals[0].length; c++)
                            {
                                if(quadrilaterals[r][c] == p)
                                    {
                                        quadrilaterals[r][c].delete();
                                        quadrilaterals[r][c] = null;
                                    }
                            }
                    }
            }
        public void delete()
            {
                super.delete();
                for(int r = 0; r < quadrilaterals.length; r++)
                    {
                        for(int c = 0; c < quadrilaterals[r].length; c++)
                            {
                                quadrilaterals[r][c].delete();
                            }
                    }
            }
        /**
         * Used to interact with specific quadrilaterals
         * @return 
         */
        public Visual[][] getQuadrilaterals()
            {
                return quadrilaterals.clone();
            }
        /**
         * Used to move a portion of the grid
         * @return 
         */
        public Point3D[][] getPoints()
            {
                return points.clone();
            }
        protected Polygon3D newQuadrilateral3D(Point3D p1, Point3D p2, Point3D p3, Point3D p4, Color c, boolean activate, Supplier<View> view)
            {
                return new Polygon3D(c,activate,view,p1,p2,p3,p4);
            }
        
        public int getQuadrilateralRows()
            {
                return getPointRows()-1;
            }
        public int getQuadrilateralColumns()
            {
                return getPointColumns()-1;
            }
        public int getPointRows()
            {
                return points.length;
            }
        public int getPointColumns()
            {
                return points[0].length;
            }
        public void activate()
            {
                for(Visual[] ps : quadrilaterals)
                    {
                        for(Visual p : ps)
                            {
                                p.activate(this);
                            }
                    }
            }
        public void deactivate()
            {
                for(Visual[] ps : quadrilaterals)
                    {
                        for(Visual p : ps)
                            {
                                p.deactivate(this);
                            }
                    }
            }
        public Supplier<View> getView()
            {
                return view;
            }
        
        public Color getColor()
            {
                return findAvgColor(0,0,quadrilaterals.length,quadrilaterals[0].length);
            }
        public TwoInsOneOut<Integer,Integer,Color> getColorFormula()
            {
                return color;
            }

        @Override
        public Point3D getCenter() 
            {
                double sumX = 0;
                double sumY = 0;
                double sumZ = 0;
                for(Visual[] p : morphingContainers)
                    {
                        for(Visual v : p)
                            {
                                sumX += v.getCenter().getX();
                                sumY += v.getCenter().getY();
                                sumZ += v.getCenter().getZ();
                            }
                    }
                int totalPolygons = morphingContainers.length * morphingContainers[0].length;
                double avgX = sumX / totalPolygons;
                double avgY = sumY / totalPolygons;
                double avgZ = sumZ / totalPolygons;
                return new Point3D(avgX,avgY,avgZ);
            }
         
    }
