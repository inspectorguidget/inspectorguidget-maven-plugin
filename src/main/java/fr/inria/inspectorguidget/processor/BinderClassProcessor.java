package fr.inria.inspectorguidget.processor;

import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtClass;

import java.util.ArrayList;
import java.util.List;

public class BinderClassProcessor extends AbstractProcessor<CtClass> {

    private List<String> classNames = new ArrayList<>();

    @Override
    public boolean isToBeProcessed(CtClass candidate){
        return true;
    }

    @Override
    public void process(CtClass clazz) {
        classNames.add(clazz.getSimpleName());
    }

    public List<String> getClassNames(){
        return classNames;
    }
}
