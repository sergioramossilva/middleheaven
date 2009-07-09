
package org.middleheaven.aas.old;

/**
 *
 *  Resource permission level
 *
 */
public enum PermissionLevel {

	NONE(0), // 00000000
	VIEW(2), // 00000010
	READ(6), // 00000110
	EDIT(14), // 00001110
	ADD(18),// 00010010
	REMOVE(34),// 00100010
	ALL(Integer.MAX_VALUE);

    static boolean levelIncludes(PermissionLevel baselevel, PermissionLevel level){
        return (level.level & baselevel.level)>=level.level;
    }

    private static PermissionLevel fromLevel(int level){
    	if (level<=0){
    		return NONE;
    	}
    	for (PermissionLevel pl : PermissionLevel.values()){
        	if (pl.level == level){
        		return pl;
        	}
        }
    	return NONE;
    }
    
    public static PermissionLevel fromString(String levelstr){
        levelstr = levelstr.toLowerCase();
        int n=0;
        for (PermissionLevel pl : PermissionLevel.values()){
        	n = n | (pl.toString().indexOf(levelstr)>=0? pl.level : 0);
        }
        return fromLevel(n);
    }

    public static PermissionLevel fromString(String[] levels){
        int n=0;
        for (int i=0;i<levels.length;i++){
            String levelstr = levels[i].toLowerCase();
            for (PermissionLevel pl : PermissionLevel.values()){
            	n = n | (pl.toString().indexOf(levelstr)>=0? pl.level : 0);
            }
        }
        return fromLevel(n);
    }

    final int level;


    PermissionLevel(int level){
        this.level = level;
    }

    boolean includes(PermissionLevel levelOther){
        return levelIncludes(this, levelOther);
    }

}
