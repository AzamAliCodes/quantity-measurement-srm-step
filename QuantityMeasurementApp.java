import java.util.Objects;

public class QuantityMeasurementApp {

    public interface IMeasurable {
        double toBaseUnit(double value);
        double fromBaseUnit(double valueInBase);
        double getConversionFactor();
        String getUnitName();
    }

    public enum LengthUnit implements IMeasurable {
        FEET(12.0), INCH(1.0), YARD(36.0), CM(0.393701);

        private final double conversionFactor;
        LengthUnit(double conversionFactor) { this.conversionFactor = conversionFactor; }

        @Override public double toBaseUnit(double value) { return value * conversionFactor; }
        @Override public double fromBaseUnit(double valueInBase) { return valueInBase / conversionFactor; }
        @Override public double getConversionFactor() { return conversionFactor; }
        @Override public String getUnitName() { return name(); }
    }

    public enum WeightUnit implements IMeasurable {
        KG(1000.0), GRAM(1.0), POUND(453.592), TONNE(1000000.0);

        private final double conversionFactor;
        WeightUnit(double conversionFactor) { this.conversionFactor = conversionFactor; }

        @Override public double toBaseUnit(double value) { return value * conversionFactor; }
        @Override public double fromBaseUnit(double valueInBase) { return valueInBase / conversionFactor; }
        @Override public double getConversionFactor() { return conversionFactor; }
        @Override public String getUnitName() { return name(); }
    }

    public enum VolumeUnit implements IMeasurable {
        GALLON(3.78541), LITER(1.0), ML(0.001);

        private final double conversionFactor;
        VolumeUnit(double conversionFactor) { this.conversionFactor = conversionFactor; }

        @Override public double toBaseUnit(double value) { return value * conversionFactor; }
        @Override public double fromBaseUnit(double valueInBase) { return valueInBase / conversionFactor; }
        @Override public double getConversionFactor() { return conversionFactor; }
        @Override public String getUnitName() { return name(); }
    }

    public static class Quantity<U extends IMeasurable> {
        private final double value;
        private final U unit;

        public Quantity(double value, U unit) {
            if (unit == null) throw new IllegalArgumentException("Unit cannot be null");
            if (!Double.isFinite(value)) throw new IllegalArgumentException("Value must be finite");
            this.value = value;
            this.unit = unit;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Quantity<?> that = (Quantity<?>) obj;
            if (this.unit.getClass() != that.unit.getClass()) return false;
            return Math.abs(this.unit.toBaseUnit(this.value) - 
                            ((IMeasurable)that.unit).toBaseUnit(that.value)) < 1e-6;
        }

        @Override
        public int hashCode() {
            return Objects.hash(value, unit);
        }

        public Quantity<U> convertTo(U targetUnit) {
            double valueInBase = this.unit.toBaseUnit(this.value);
            double convertedValue = targetUnit.fromBaseUnit(valueInBase);
            return new Quantity<>(convertedValue, targetUnit);
        }

        public Quantity<U> add(Quantity<U> other) {
            return add(other, this.unit);
        }

        public Quantity<U> add(Quantity<U> other, U targetUnit) {
            if (other == null) throw new IllegalArgumentException("Operand cannot be null");
            double sumInBase = this.unit.toBaseUnit(this.value) + other.unit.toBaseUnit(other.value);
            double sumInTargetUnit = targetUnit.fromBaseUnit(sumInBase);
            return new Quantity<>(sumInTargetUnit, targetUnit);
        }

        public Quantity<U> subtract(Quantity<U> other) {
            return subtract(other, this.unit);
        }

        public Quantity<U> subtract(Quantity<U> other, U targetUnit) {
            if (other == null) throw new IllegalArgumentException("Operand cannot be null");
            double diffInBase = this.unit.toBaseUnit(this.value) - other.unit.toBaseUnit(other.value);
            double diffInTargetUnit = targetUnit.fromBaseUnit(diffInBase);
            return new Quantity<>(diffInTargetUnit, targetUnit);
        }

        public double divide(Quantity<U> other) {
            if (other == null) throw new IllegalArgumentException("Operand cannot be null");
            if (other.value == 0) throw new ArithmeticException("Division by zero");
            return this.unit.toBaseUnit(this.value) / other.unit.toBaseUnit(other.value);
        }

        @Override
        public String toString() {
            return String.format("%.2f %s", value, unit.getUnitName());
        }
    }

    public static void main(String[] args) {
        System.out.println("Starting UC12: Subtraction and Division Operations on Quantity Measurements");

        Quantity<LengthUnit> oneFoot = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> sixInches = new Quantity<>(6.0, LengthUnit.INCH);

        System.out.println("1 foot - 6 inches = " + oneFoot.subtract(sixInches));
        System.out.println("Test 1 foot / 6 inches: " + oneFoot.divide(sixInches));

        Quantity<VolumeUnit> oneGallon = new Quantity<>(1.0, VolumeUnit.GALLON);
        Quantity<VolumeUnit> oneLiter = new Quantity<>(1.0, VolumeUnit.LITER);
        System.out.println("1 gallon - 1 liter = " + oneGallon.subtract(oneLiter));

        System.out.println("UC12 Verification Complete.");
    }
}
