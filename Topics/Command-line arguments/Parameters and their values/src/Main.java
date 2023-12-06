class Problem {
    public static void main(String[] args) {
        for (int i = 0; i < args.length; i = i + 2) {
            System.out.print(args[i]);
            System.out.print("=");
            System.out.println(args[i+1]);
        }
    }
}