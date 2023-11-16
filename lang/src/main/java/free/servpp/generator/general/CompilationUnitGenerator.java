package free.servpp.generator.general;

import free.servpp.generator.models.ClassChecker;

import java.io.File;
import java.util.Stack;

/**
 * @author lidong@date 2023-11-01@version 1.0
 */
public class CompilationUnitGenerator extends BaseGenerator  {
    private Stack stack = new Stack();
    protected File domainPath;

    private ClassChecker globalReferenceChecker = new ClassChecker();
    private File sppFile;

    public void setSppFile(File sppFile) {
        this.sppFile = sppFile;
    }

    @Override
    public File getSppFile() {
        return sppFile;
    }

    @Override
    public ClassChecker getClassChecker() {
        return globalReferenceChecker;
    }

    @Override
    public void push(Object obj) {
        stack.push(obj);
    }

    @Override
    public Object pop() {
//        new Exception().printStackTrace();
        return stack.pop();
    }

    @Override
    public Object peek() {
        return stack.peek();
    }

    @Override
    public int stackSize() {
        return stack.size();
    }

    @Override
    public File getDomainPath() {
        return domainPath;
    }
}
