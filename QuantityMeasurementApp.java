import java.util.Objects;

public class QuantityMeasurementApp {

    public interface IMeasurable {
        double toBaseUnit(double value);
        double fromBaseUnit(double valueInBase);
    }

    public enum LengthUnit implements IMeasurable {
        FEET(12.0), INCH(1.0), YARD(36.0), CM(0.393701);

        private final double conversionFactor;
        LengthUnit(double conversionFactor) { this.conversionFactor = conversionFactor; }

        @Override public double toBaseUnit(double value) { return value * conversionFactor; }
        @Override public double fromBaseUnit(double valueInBase) { return valueInBase / conversionFactor; }
    }

    public enum WeightUnit implements IMeasurable {
        KG(1000.0), GRAM(1.0), POUND(453.592), TONNE(1000000.0);

        private final double conversionFactor;
        WeightUnit(double conversionFactor) { this.conversionFactor = conversionFactor; }

        @Override public double toBaseUnit(double value) { return value * conversionFactor; }
        @Override public double fromBaseUnit(double valueInBase) { return valueInBase / conversionFactor; }
    }

    public enum VolumeUnit implements IMeasurable {
        GALLON(3.78), LITER(1.0), ML(0.001);

        private final double conversionFactor;
        VolumeUnit(double conversionFactor) { this.conversionFactor = conversionFactor; }

        @Override public double toBaseUnit(double value) { return value * conversionFactor; }
        @Override public double fromBaseUnit(double valueInBase) { return valueInBase / conversionFactor; }
    }

    public static class Quantity<U extends IMeasurable> {
        private final double value;
        private final U unit;

        public Quantity(double value, U unit) {
            if (unit == null) throw new IllegalArgumentException("Unit cannot be null");
            if (!Double.isFinite(value)) throw new IllegalArgumentException("Value must be finite");
            this.value = Math.round(value * 100.0) / 100.0;
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

        @Override
        public String toString() {
            return value + " " + unit;
        }
    }

    public static void main(String[] args) {
        System.out.println("Starting UC10: Generic Quantity Class with Unit Interface for Multi-Category Support");

        // Volume Tests
        Quantity<VolumeUnit> oneGallon = new Quantity<>(1.0, VolumeUnit.GALLON);
        Quantity<VolumeUnit> threePointSevenEightLiters = new Quantity<>(3.78, VolumeUnit.LITER);
        System.out.println("1 gallon == 3.78 liters: " + oneGallon.equals(threePointSevenEightLiters));

        Quantity<VolumeUnit> oneLiter = new Quantity<>(1.0, VolumeUnit.LITER);
        Quantity<VolumeUnit> thousandMl = new Quantity<>(1000.0, VolumeUnit.ML);
        System.out.println("1 liter == 1000 ml: " + oneLiter.equals(thousandMl));

        // Weight Tests
        Quantity<WeightUnit> oneTonne = new Quantity<>(1.0, WeightUnit.TONNE);
        Quantity<WeightUnit> thousandKg = new Quantity<>(1000.0, WeightUnit.KG);
        System.out.println("1 tonne == 1000 kg: " + oneTonne.equals(thousandKg));

        // Mixed category addition (Compile time error if we used proper variables, 
        // but let's check runtime or just assume type safety works)
        // oneGallon.add(thousandKg); // This would not compile in a real IDE.

        System.out.println("UC10 Verification Complete.");
    }
}
