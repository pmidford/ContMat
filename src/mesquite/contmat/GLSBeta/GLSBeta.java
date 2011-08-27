/*
 * EthOntos - a tool for comparative methods using ontologies
 * Copyright 2004-2005 Peter E. Midford
 * 
 * Created on Dec 11, 2008
 * Last updated on Dec 11, 2008
 * 
 */
package mesquite.contmat.GLSBeta;

import Jama.Matrix;
import mesquite.lib.Arguments;
import mesquite.lib.MesquiteDouble;
import mesquite.lib.MesquiteMessage;
import mesquite.lib.MesquiteNumber;
import mesquite.lib.MesquiteString;
import mesquite.lib.Parser;
import mesquite.lib.Taxa;
import mesquite.lib.Tree;
import mesquite.lib.characters.CharacterData;
import mesquite.lib.characters.CharacterDistribution;
import mesquite.lib.duties.NumberFor2CharAndTree;
import mesquite.categ.lib.CategoricalDistribution;
import mesquite.cont.lib.ContinuousDistribution;
import mesquite.contmat.lib.*;
import mesquite.contmat.lib.ContMatCalc.CVMatrix;
import mesquite.distance.SharedHistoryDistance.SharedHistoryDistance;
import mesquite.distance.lib.IncTaxaDistanceSource;

public class GLSBeta extends NumberFor2CharAndTree {
    
    private ContinuousDistribution observedStates1;
    private ContinuousDistribution observedStates2;
    private SharedHistoryDistance CVmatSource; 


    public boolean startJob(String arguments, Object condition, boolean hiredByName) {
        Taxa taxa;
        CharacterData data = null;
        return true;
    }

    /**
     * Grabbed from mesquite.correl.Pagel94
     * @param tree
     * @param N
     * @param countRoot
     * @return
     */
    private boolean hasZeroOrNegLengthBranches(Tree tree, int N, boolean countRoot) {
        if (tree.getBranchLength(N) <= 0.0 && (countRoot || tree.getRoot() != N))
            return true;
        if (tree.nodeIsInternal(N)){
            for (int d = tree.firstDaughterOfNode(N); tree.nodeExists(d); d = tree.nextSisterOfNode(d))
                if (hasZeroOrNegLengthBranches(tree, d, countRoot))
                    return true;
        }
        return false;
    }

    
    public void calculateNumber(Tree tree, CharacterDistribution charStates1,CharacterDistribution charStates2, MesquiteNumber result, MesquiteString resultString) {
        clearResultAndLastResult(result);

        if (!(charStates1 instanceof ContinuousDistribution ||
                charStates2 instanceof ContinuousDistribution)) {
            result.setValue(MesquiteDouble.unassigned);  
            if (resultString != null)
                resultString.setValue("Character not continuous; inappropriate for GLS calculations");
            return;
        }

        observedStates1 = (ContinuousDistribution)charStates1;
        observedStates2 = (ContinuousDistribution)charStates2;
        Taxa taxa = tree.getTaxa();
        
        ContMatCalc calculator = new ContMatCalc();
        
        CVMatrix cv = new ContMatCalc.CVMatrix(taxa,tree);
        
        Matrix c;
        double [] y;
        Matrix x;
        
        c = new Matrix(cv.getMatrix());
        final int cDim = c.getColumnDimension();
        if (cDim != observedStates1.getNumItems()){
            MesquiteMessage.warnProgrammer("Matrix size " + cDim + " does not match size of first character set " + observedStates1.getNumItems());
            return;
        }
        if (c.getColumnDimension() != observedStates2.getNumItems()){
            MesquiteMessage.warnProgrammer("Matrix size " + cDim + " does not match size of second character set " + observedStates2.getNumItems());
            return;            
        }
           
        y = new double[cDim];
        y[0]=1.0;
        y[1]=1.5;
        y[2]=2.0;
        x = new Matrix(cDim,2);
        x.set(0, 0, 3.0);
        x.set(1, 0, 4.0);
        x.set(2, 0, 5.0);
        for(int i=0;i<cDim-1;i++)
            x.set(i, 1, 1.0);
        calculator.doCalculations(c,y,x);
        
        calculator.getBetaEst();
        
        if (result != null)
            result.setValue(calculator.getBetaEst());
        if (resultString != null)
            resultString.setValue("Beta =  " + result);

    }

    public void initialize(Tree tree, CharacterDistribution charStates1,  CharacterDistribution charStates2) {
        if (charStates1 == null || charStates2 == null)
            return;
        if (!(charStates1 instanceof ContinuousDistribution && charStates2 instanceof ContinuousDistribution)) {
               if (!(charStates1 instanceof ContinuousDistribution))
                   MesquiteMessage.warnProgrammer("Quitting because the first character is not Continuous");
               else
                   MesquiteMessage.warnProgrammer("Quitting because the second character is not Continuous");
               iQuit();
               return;
           }
           observedStates1 = (ContinuousDistribution)charStates1;
           observedStates2 = (ContinuousDistribution)charStates2;
           

    }

    public String getName() {
        // TODO Auto-generated method stub
        return "GLS Beta (slope)";
    }


}
