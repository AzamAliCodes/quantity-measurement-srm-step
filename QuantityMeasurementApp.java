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

    public static class Inches {
        private final double value;

        public Inches(double value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Inches inches = (Inches) obj;
            return Double.compare(inches.value, value) == 0;
        }

        @Override
        public String toString() {
            return value + " in";
        }
    }

    public static void main(String[] args) {
        System.out.println("Starting UC2: Feet and Inches measurement equality");

        Feet f1 = new Feet(1.0);
        Feet f2 = new Feet(1.0);
        System.out.println("Test 1.0 ft == 1.0 ft: " + f1.equals(f2));

        Inches i1 = new Inches(1.0);
        Inches i2 = new Inches(1.0);
        System.out.println("Test 1.0 in == 1.0 in: " + i1.equals(i2));

        System.out.println("Test 1.0 ft != 1.0 in: " + !f1.equals(i1));

        System.out.println("UC2 Verification Complete.");
    }
}
