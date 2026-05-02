public class QuantityMeasurementApp {

    public static class Feet {
        private final double value;

        public Feet(double value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Feet feet = (Feet) obj;
            return Double.compare(feet.value, value) == 0;
        }

        @Override
        public String toString() {
            return value + " ft";
        }
    }

    public static void main(String[] args) {
        System.out.println("Starting UC1: Feet measurement equality");

        Feet f1 = new Feet(1.0);
        Feet f2 = new Feet(1.0);
        Feet f3 = new Feet(2.0);

        System.out.println("Test 1.0 ft == 1.0 ft: " + f1.equals(f2));
        System.out.println("Test 1.0 ft != 2.0 ft: " + !f1.equals(f3));
        System.out.println("Test Null Comparison: " + !f1.equals(null));
        System.out.println("Test Type Safety: " + !f1.equals("1.0"));

        System.out.println("UC1 Verification Complete.");
    }
}
