/*
 * Created on 2006/09/07
 *
 */
package org.middleheaven.web.container;

import javax.servlet.ServletContext;

import org.middleheaven.io.repository.ManagedFile;



public class StandardJBossContainer extends AbstractJBossContainer {

    public StandardJBossContainer(ServletContext context, ManagedFile root) {
        super(context, root);
    }

}
