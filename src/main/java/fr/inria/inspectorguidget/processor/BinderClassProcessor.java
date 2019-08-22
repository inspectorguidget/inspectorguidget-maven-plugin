package fr.inria.inspectorguidget.processor;

import spoon.processing.AbstractProcessor;
import spoon.reflect.code.CtInvocation;
import spoon.reflect.declaration.CtClass;
import spoon.reflect.visitor.filter.AbstractFilter;

import java.util.ArrayList;
import java.util.List;

public class BinderClassProcessor extends AbstractProcessor<CtClass> {

    private List<CtClass> classList = new ArrayList<>();

    @Override
    public boolean isToBeProcessed(CtClass candidate){

        try{
            CtInvocation invoc = candidate.getElements(new AbstractFilter<CtInvocation>() {
                @Override
                public boolean matches(CtInvocation invocation) {
                    if(invocation.toString().endsWith("bind()"))
                        return true;

                    return false;
                }
            }).get(0);
        } catch (Exception e){
            return false;
        }

        return true;
    }

    @Override
    public void process(CtClass clazz) {
        classList.add(clazz);
    }

    public List<CtClass> getClassNames(){
        return classList;
    }
}
