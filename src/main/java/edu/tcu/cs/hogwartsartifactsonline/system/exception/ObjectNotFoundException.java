package edu.tcu.cs.hogwartsartifactsonline.system.exception;

public class ObjectNotFoundException extends RuntimeException{
    public ObjectNotFoundException(String objName, String id) {
        super("Could not find " + objName + " with Id " + id + " :(");
    }

    public ObjectNotFoundException(String objName, Integer id) {
        super("Could not find " + objName + " with Id " + id + " :(");
    }
}
