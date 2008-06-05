/*
 * Created on 2006/11/25
 *
 */
package org.middleheaven.dependency;

public interface InstallableDependencyMatcher<I extends Installable, D extends Dependency> {

    public boolean isMatchable(Dependency dependency);
    
    /**
     * Matches a dependency with an Installable. Return true is the 
     * Installable satisfies the dependency
     * @param inst installable to test
     * @param d dependency to satisfy
     * @return boolean value indicating if the installable matches the dependency
     */
    public boolean match (I installable, D dependency);

    
}
