
package org.middleheaven.logging;

import java.io.IOException;

public class LogWritingIOExcepiton extends LogWritingException {

	private static final long serialVersionUID = 1L;

	public LogWritingIOExcepiton(IOException e) {
        super(e);
    }

}
