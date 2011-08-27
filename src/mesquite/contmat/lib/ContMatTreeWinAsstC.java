/* Mesquite module ~~ Copyright 1997-2005 W. & D. Maddison*/
/* ContMat ~~ Copyright 2007 Peter E. Midford */
package mesquite.contmat.lib;

import mesquite.lib.Tree;
import mesquite.lib.duties.TreeWindowAssistantC;

public abstract class ContMatTreeWinAsstC extends TreeWindowAssistantC {

    public abstract void setTree(Tree tree); 

    public String getName() {
        return "Provides chart for ContMap";
    }


}
