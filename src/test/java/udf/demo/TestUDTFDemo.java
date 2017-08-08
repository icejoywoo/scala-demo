package udf.demo;

import java.util.ArrayList;

import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.junit.Assert;
import org.junit.Test;

public class TestUDTFDemo {

    @Test
    public void testUDTFOneSpace() {

        // set up the models we need
        NameParserGenericUDTF example = new NameParserGenericUDTF();

        ObjectInspector[] inputOI = {PrimitiveObjectInspectorFactory.javaStringObjectInspector};

        // create the actual UDF arguments
        String name = "John Smith";

        // the value exists
        try{
            example.initialize(inputOI);
        }catch(Exception ignored){

        }

        ArrayList<Object[]> results = example.processInputRecord(name);
        Assert.assertEquals(1, results.size());
        Assert.assertEquals("John", results.get(0)[0]);
        Assert.assertEquals("Smith", results.get(0)[1]);
    }

    @Test
    public void testUDTFAnd() {

        // set up the models we need
        NameParserGenericUDTF example = new NameParserGenericUDTF();

        ObjectInspector[] inputOI = {PrimitiveObjectInspectorFactory.javaStringObjectInspector};

        // create the actual UDF arguments
        String name = "John and Ann White";

        // the value exists
        try{
            example.initialize(inputOI);
        }catch(Exception ignored){

        }

        ArrayList<Object[]> results = example.processInputRecord(name);
        Assert.assertEquals(2, results.size());
        Assert.assertEquals("John", results.get(0)[0]);
        Assert.assertEquals("White", results.get(0)[1]);
        Assert.assertEquals("Ann", results.get(1)[0]);
        Assert.assertEquals("White", results.get(1)[1]);
    }

}
