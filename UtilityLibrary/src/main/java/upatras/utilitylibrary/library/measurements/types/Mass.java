package upatras.utilitylibrary.library.measurements.types;

/**
 * Created by Paris on 23-Jun-18.
 */
public class Mass extends AbstractGenericDouble<Mass> {

    public Mass(double kilograms) {
        super(kilograms);
    }

    public Mass() {
        super(0);
    }

    public static Mass kilograms(double kg){
        return new Mass(kg);
    }

    public static Mass grams(double g){
        return new Mass(g/1000.0);
    }

    public static Mass tons(double ton){
        return new Mass(ton*1000.0);
    }

    @Override
    protected Mass generateParentClass(double value) {
        return new Mass(value);
    }


}
