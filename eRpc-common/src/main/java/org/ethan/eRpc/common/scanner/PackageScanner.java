package org.ethan.eRpc.common.scanner;

import java.io.IOException;
import java.util.List;

public interface PackageScanner {
	 public List<String> getFullyQualifiedClassNameList() throws IOException;
}
