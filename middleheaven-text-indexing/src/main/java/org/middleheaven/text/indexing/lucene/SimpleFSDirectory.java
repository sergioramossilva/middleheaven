package org.middleheaven.text.indexing.lucene;

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.IOException;

import org.apache.lucene.store.BufferedIndexInput;
import org.apache.lucene.store.BufferedIndexOutput;
import org.apache.lucene.store.IndexInput;
import org.apache.lucene.store.IndexOutput;
import org.apache.lucene.store.MMapDirectory;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.store.NativeFSLockFactory;
import org.middleheaven.io.repository.ManagedFile;
import org.middleheaven.io.repository.ManagedRandomAccessFile;

/** A straightforward implementation of {@link ManagedFileDirectory}
 *  using java.io.RandomAccessFile.  However, this class has
 *  poor concurrent performance (multiple threads will
 *  bottleneck) as it synchronizes when multiple threads
 *  read from the same file.  It's usually better to use
 *  {@link NIOFSDirectory} or {@link MMapDirectory} instead. */
class SimpleFSDirectory extends ManagedFileDirectory {


	/** Create a new SimpleFSDirectory for the named location and {@link NativeFSLockFactory}.
	 *
	 * @param path the path of the directory
	 * @throws IOException
	 */
	public SimpleFSDirectory(ManagedFile path) throws IOException {
		super(path);
	}

	/** Creates an IndexOutput for the file with the given name. */
	@Override
	public IndexOutput createOutput(String name) throws IOException {
		initOutput(name);
		return new SimpleFSIndexOutput(directory.retrive(name));
	}

	/** Creates an IndexInput for the file with the given name. */
	@Override
	public IndexInput openInput(String name, int bufferSize) throws IOException {
		ensureOpen();
		return new SimpleFSIndexInput(directory.retrive(name), bufferSize, getReadChunkSize());
	}

	protected static class SimpleFSIndexInput extends BufferedIndexInput {

		protected static class Descriptor extends ManagedRandomAccessFile {
			// remember if the file is open, so that we don't try to close it
			// more than once
			protected volatile boolean isOpen;
			long position;
			final long length;

			public Descriptor(ManagedFile file, String mode) throws IOException {
				super(file, mode);
				isOpen=true;
				length=length();
			}

			@Override
			public void close() throws IOException {
				if (isOpen) {
					isOpen=false;
					super.close();
				}
			}
		}

		protected final Descriptor file;
		boolean isClone;
		//  LUCENE-1566 - maximum read length on a 32bit JVM to prevent incorrect OOM 
		protected final int chunkSize;

		public SimpleFSIndexInput(ManagedFile path, int bufferSize, int chunkSize) throws IOException {
			super(bufferSize);
			file = new Descriptor(path, "r");
			this.chunkSize = chunkSize;
		}

		/** IndexInput methods */
		@Override
		protected void readInternal(byte[] b, int offset, int len)
				throws IOException {
			synchronized (file) {
				long position = getFilePointer();
				if (position != file.position) {
					file.seek(position);
					file.position = position;
				}
				int total = 0;

				try {
					do {
						final int readLength;
						if (total + chunkSize > len) {
							readLength = len - total;
						} else {
							// LUCENE-1566 - work around JVM Bug by breaking very large reads into chunks
							readLength = chunkSize;
						}
						final int i = file.read(b, offset + total, readLength);
						if (i == -1) {
							throw new IOException("read past EOF");
						}
						file.position += i;
						total += i;
					} while (total < len);
				} catch (OutOfMemoryError e) {
					// propagate OOM up and add a hint for 32bit VM Users hitting the bug
					// with a large chunk size in the fast path.
					final OutOfMemoryError outOfMemoryError = new OutOfMemoryError(
							"OutOfMemoryError likely caused by the Sun VM Bug described in "
									+ "https://issues.apache.org/jira/browse/LUCENE-1566; try calling FSDirectory.setReadChunkSize "
									+ "with a a value smaller than the current chunks size (" + chunkSize + ")");
					outOfMemoryError.initCause(e);
					throw outOfMemoryError;
				}
			}
		}

		@Override
		public void close() throws IOException {
			// only close the file if this is not a clone
			if (!isClone) file.close();
		}

		@Override
		protected void seekInternal(long position) {
		}

		@Override
		public long length() {
			return file.length;
		}

		@Override
		public Object clone() {
			SimpleFSIndexInput clone = (SimpleFSIndexInput)super.clone();
			clone.isClone = true;
			return clone;
		}

	}

	protected static class SimpleFSIndexOutput extends BufferedIndexOutput {

		ManagedRandomAccessFile file = null;

		// remember if the file is open, so that we don't try to close it
		// more than once
		private volatile boolean isOpen;

		public SimpleFSIndexOutput(ManagedFile path) throws IOException {
			file = new ManagedRandomAccessFile(path, "rw");
			isOpen = true;
		}

		/** output methods: */
		@Override
		public void flushBuffer(byte[] b, int offset, int size) throws IOException {
			file.write(b, offset, size);
		}
		@Override
		public void close() throws IOException {
			// only close the file if it has not been closed yet
			if (isOpen) {
				boolean success = false;
				try {
					super.close();
					success = true;
				} finally {
					isOpen = false;
					if (!success) {
						try {
							file.close();
						} catch (IOException t) {
							// Suppress so we don't mask original exception
						}
					} else{
						file.close();
					} 
				}
			}
		}

		/** Random-access methods */
		@Override
		public void seek(long pos) throws IOException {
			super.seek(pos);
			file.seek(pos);
		}

		@Override
		public long length() throws IOException {
			return file.length();
		}
		@Override
		public void setLength(long length) throws IOException {
			file.setLength(length);
		}
	}
}