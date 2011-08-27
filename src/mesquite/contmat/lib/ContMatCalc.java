/* GLS/GLM package for Mesquite  copyright 2008-2009 P. Midford & W. Maddison
This package is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY.
The web site for this package is TBD

This source code and its compiled class files are free and modifiable under the terms of 
GNU Lesser General Public License.  (http://www.gnu.org/copyleft/lesser.html)
 */
package mesquite.contmat.lib;

import mesquite.distance.lib.TaxaDistance;
import mesquite.lib.Double2DArray;
import mesquite.lib.MesquiteDouble;
import mesquite.lib.Taxa;
import mesquite.lib.Tree;
import Jama.Matrix;
import Jama.SingularValueDecomposition;

public class ContMatCalc {

    private double betaEst;
    
    public ContMatCalc(){
        super();
    }
    
    public double getBetaEst(){
        return betaEst;
    }
    
    public void doCalculations(Matrix c, double[] y, Matrix x){
        final SingularValueDecomposition svd = new SingularValueDecomposition(c);
        final Matrix d = svd.getU();
        final Matrix dt = svd.getV();
        final Matrix cs = svd.getS();  // better be the transpose of d (or c is malformed)
        final Matrix yMat = new Matrix(y,y.length);
        final Matrix z = d.times(yMat);
        final Matrix u = d.times(x);
        final Matrix xt = x.transpose();
        final Matrix cInverse = c.inverse();

        final Matrix xtci = xt.times(cInverse);
        final Matrix xtcix = xtci.times(x);
        final Matrix xtcixI = xtcix.inverse();
        
        final Matrix xtciy = xtci.times(yMat);
        final Matrix betaHat = xtcixI.times(xtciy);
        betaEst = betaHat.get(0,0);
    }
    
    /** This is returns the amount of shared history between two nodes.  It is just the distance from the root to
     * the nodes' most recent common ancestor. */
    public static class CVMatrix {
        double[][] distances;
        int numTaxa;
        public CVMatrix(Taxa taxa, Tree tree){
            numTaxa = taxa.getNumTaxa();
            distances= new double[numTaxa][numTaxa];
            Double2DArray.deassignArray(distances);
            int root = tree.getRoot();
            for (int taxon1=0; taxon1<numTaxa; taxon1++) {
                int node1 = tree.nodeOfTaxonNumber(taxon1);
                if (tree.nodeExists(node1)){
                    for (int taxon2=0; taxon2<numTaxa; taxon2++) {
                        int node2 = tree.nodeOfTaxonNumber(taxon2);
                        if (tree.nodeExists(node2)){
                            int mrca = tree.mrca(node1, node2);
                            int node = mrca;
                            double sumPath = 0;
                            while (node != root && tree.nodeExists(node)){
                                sumPath += tree.getBranchLength(node, 1.0);
                                node = tree.motherOfNode(node);
                            }
                            distances[taxon1][taxon2] = sumPath;
                        }
                    }
                }
            }
        }

        public double getDistance(int taxon1, int taxon2){
            if (taxon1>=0 && taxon1<numTaxa && taxon2>=0 && taxon2<numTaxa)
                return distances[taxon1][taxon2];
            else
                return MesquiteDouble.unassigned;
            
        }
        public double[][] getMatrix(){
            return distances;
        }
        public boolean isSymmetrical(){
            return true;
        }
    
    }

    
}
