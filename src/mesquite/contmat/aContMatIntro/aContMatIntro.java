/* ContMat ~~ Copyright 2011 Peter E. Midford */
package mesquite.contmat.aContMatIntro;

import mesquite.lib.duties.PackageIntro;


public class aContMatIntro extends PackageIntro{

    public boolean startJob(String arguments, Object condition, boolean hiredByName) {
        return true;
    }

    public Class getDutyClass(){
        return aContMatIntro.class;
     }
    /*.................................................................................................................*/
    public String getExplanation() {
        return "Serves as an introduction to the ContMat package.";
     }

    /*.................................................................................................................*/
    public String getName() {
        return "ContMat Package Introduction";
    }
    /*.................................................................................................................*/
    /** Returns the name of the package of modules (e.g., "ContMat Package")*/
    public String getPackageName(){
        return "ContMat Package";
    }
    
    /*.................................................................................................................*/
    /** Returns a citation for the package of modules */
    public String getPackageCitation(){
        return "Midford, P. E. 2007. ContMat Package.";
    }
    
    /** Returns full(er) names of the authors of the package */
    public String getPackageAuthors() {
        return "Peter E. Midford" ;
    }
    
    /** Returns a version string for the package */
    public String getPackageVersion() {
        return "0.01";
    }
    
    /*.................................................................................................................*/
    /** Returns version for a package of modules as an integer*/
    public int getPackageVersionInt(){
        return 1;
    }

    /*.................................................................................................................*/
    /** Returns whether there is a splash banner*/
    public boolean hasSplash(){
        return true; 
    }

}
