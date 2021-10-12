public class Sample {
    public Sample() {
    }

    private void funcA() {
        System.out.println("ojbk");
        int[] a = new int[10];
        a[0]++;
        a[9]++;
    }

    public void funcB() {
        funcA();
    }
}