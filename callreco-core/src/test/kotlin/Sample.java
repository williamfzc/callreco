public class Sample {
    public Sample() {
    }

    private void funcC() {

    }

    private void funcA() {
        int[] a = new int[10];
        a[0]++;
        a[9]++;
    }

    public void funcB() {
        funcA();
        for (int i = 0; i < 5000; i++) {
            funcC();
        }
        funcE();
    }

    private void funcD() {

    }

    private void funcE() {

    }

    private void funcF() {

    }

    private void funcG() {

    }
}